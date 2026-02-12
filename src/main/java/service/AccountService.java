package service;

import db.DatabaseUtility;
import java.sql.*;
import java.math.BigDecimal;
import logs.*;



public class AccountService {
	
	//create account
	
	public void createAccount(String name, BigDecimal initialBalance) {
		
		
		
		// name validation
	    if (!name.matches("^[a-zA-Z][a-zA-Z .-]{1,49}$")) {
	        System.out.println("Invalid name! Use letters, spaces, dots, hyphens only.");
	        return;
	    }

	    if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
	        System.out.println("Initial balance cannot be negative.");
	        return;
	    }
		
		String sqlquery = "INSERT INTO accounts (account_holder_name, balance) VALUES (?, ?)";
		
		try (Connection con = DatabaseUtility.getConnection();
				PreparedStatement ps = con.prepareStatement(sqlquery, PreparedStatement.RETURN_GENERATED_KEYS)){
			
			ps.setString(1, name);
			ps.setBigDecimal(2, initialBalance);
			
			ps.executeUpdate();
			
			// Get generated keys
			
			ResultSet rs = ps.getGeneratedKeys();
			
			if(rs.next()) {
				int accNumber = rs.getInt(1);
				System.out.println("Account created : "+accNumber +" for user : "+name);
			}
			
			
		
		} catch (SQLException e) {
			System.out.println("DB Connection Failed");
			e.printStackTrace();
		}
		
		
	}
	
	
    // DEPOSIT
	public synchronized void deposit(int accountNumber, BigDecimal amount) {
		
		
		//account number validation
		if (accountNumber <= 0) {
	        System.out.println("Invalid account number!");
	        return;
	    }
		
		//amount validation
		if(amount.compareTo(BigDecimal.ZERO) <= 0) {
			System.out.println("Deposit amount must be greater than 0");
			return;
		}
		
				
		Connection con = null;
		
		
		try {
			
			con = DatabaseUtility.getConnection();
			con.setAutoCommit(false);
			
			String updateQuery = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";

			
			
			//update balance
			
			PreparedStatement updatePS = con.prepareStatement(updateQuery);
			
			updatePS.setBigDecimal(1, amount);
			updatePS.setInt(2, accountNumber);
			
			
			int rows = updatePS.executeUpdate();
			
			
			if (rows == 0) {
				System.out.println("Account not found");
				con.rollback();
				return;
			}
			
			
			BigDecimal newBal = fetchBalance(con, accountNumber);
			logTransaction(con, accountNumber, "DEPOSIT", amount, newBal);
			con.commit();
			
			System.out.println("Deposit successful. Balance: " + newBal);
			
						
		} catch (Exception e) {
			rollback(con);
			e.printStackTrace();
		}	
		
	}
	
    // WITHDRAW
	public synchronized void withdraw(int accountNumber, BigDecimal amount) {
		
		
		if(amount.compareTo(BigDecimal.ZERO) <= 0) {
			System.out.println("WithDrawn amount must be greater than 0");
			return;
		}
		
		Connection con = null;
				
				
		try {
			
			con = DatabaseUtility.getConnection();
			con.setAutoCommit(false);
			
			
			
			BigDecimal currentBal = fetchBalance(con, accountNumber);
			
			if (currentBal == null) {
                System.out.println("Account not found");
                con.rollback();
                return;
            }
			
			if (currentBal.compareTo(amount) < 0) {
                System.out.println("Insufficient balance");
                con.rollback();
                return;
            }
			
			
			
			String updateQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
			
						
			PreparedStatement updatePS = con.prepareStatement(updateQuery);
			updatePS.setBigDecimal(1, amount);
			updatePS.setInt(2, accountNumber);
			
			updatePS.executeUpdate();
			
			
			
						
			BigDecimal newBalance = currentBal.subtract(amount);
			
			logTransaction(con, accountNumber, "WITHDRAWAL", amount, newBalance);
			con.commit();
			
			System.out.println("Withdrawal successful. New balance: " + newBalance);			
			
		} catch (Exception e ) {
			rollback(con);
			e.printStackTrace();
		}
		

	}
	
    // VIEW BALANCE
	public void getBalance(int accountNumber) {
				
		try {
			
			Connection con = DatabaseUtility.getConnection();
			
			
			BigDecimal balance = fetchBalance(con, accountNumber);
									
			if(balance == null) {	
				System.out.println("Account not found");
			} else {
				System.out.println("Current Balance : "+ balance);		
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    // HELPER METHODS (REUSABLE)
	
	private BigDecimal fetchBalance(Connection con, int accountNumber) throws SQLException {
		
		String fetchQuery = "SELECT balance FROM accounts WHERE account_number = ?";
		PreparedStatement fetchPS = con.prepareStatement(fetchQuery);
		
		fetchPS.setInt(1, accountNumber);
		ResultSet rs = fetchPS.executeQuery();
		
		return rs.next() ? rs.getBigDecimal("balance") : null;

	}
	
	
	// Log transaction (DB + File)
	private void logTransaction(Connection con, int accountNumber, String Type, 
								BigDecimal amount, BigDecimal balance) throws SQLException {
		
		TransactionLogger.log(con, accountNumber, Type, amount, balance);
		FileLogger.logToFile(accountNumber, Type, amount, balance);
		
	}
	
	
    // Rollback safely
	private void rollback(Connection con) {
		try {
			if (con != null) con.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
