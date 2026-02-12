package service;

import db.DatabaseUtility;
import java.sql.*;
import java.math.BigDecimal;
import logs.*;
import model.Account;



public class AccountService {
	
	//create account
	
	public void createAccount(String name, BigDecimal initialBalance) {
		
		
		
		// name validation
	    if (!name.matches("^[a-zA-Z][a-zA-Z .-]{1,49}$")) {
	        System.out.println("Invalid name! Use letters, spaces, dots, hyphens only.");
	        return;
	    }
	    
	    // Prevent negative balance
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
			
			// Retrieve auto-generated account number
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
	
	
	//FETCH ACCOUNT
	private Account getAccount(Connection con, int accountNumber) throws SQLException {

        String accQuery = "SELECT * FROM accounts WHERE account_number=?";
        
        try(PreparedStatement ps = con.prepareStatement(accQuery)){

	        ps.setInt(1, accountNumber);
	
	        ResultSet rs = ps.executeQuery();
	
	        if (rs.next()) {
	            return new Account(
	                rs.getInt("account_number"),
	                rs.getString("account_holder_name"),
	                rs.getBigDecimal("balance"));
	        }
        }

        return null;
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
			
			Account acc = getAccount(con, accountNumber);
			
			if (acc == null) {
				System.out.println("Account not found");
				con.rollback();
				return;
			}
			
			BigDecimal newBalance = acc.getBalance().add(amount);
			
			updateBalance(con, accountNumber, newBalance);
			
			logTransaction(con, accountNumber, "DEPOSIT", amount, newBalance);
			con.commit();
			
			System.out.println("Deposit successful. Balance: " + newBalance);
			
						
		} catch (Exception e) {
			rollback(con);
			e.printStackTrace();
		}	
		
	}
	
    // WITHDRAW
	public synchronized void withdraw(int accountNumber, BigDecimal amount) {
		
		
		if(amount.compareTo(BigDecimal.ZERO) <= 0) {
			System.out.println("Withdrawal amount must be greater than 0");
			return;
		}
		
		Connection con = null;
				
				
		try {
			
			con = DatabaseUtility.getConnection();
			con.setAutoCommit(false);
			
			
			
			Account acc = getAccount(con, accountNumber);
			
			
			//account number validation
			if (acc == null) {
                System.out.println("Account not found");
                con.rollback();
                return;
            }
			
			//amount validation
			if (acc.getBalance().compareTo(amount) < 0) {
                System.out.println("Insufficient balance");
                con.rollback();
                return;
            }
						
			
			BigDecimal newBalance = acc.getBalance().subtract(amount);
			
			updateBalance(con, accountNumber, newBalance);

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

	    try (Connection con = DatabaseUtility.getConnection()) {

	        Account acc = getAccount(con, accountNumber);

	        if (acc == null) {
	            System.out.println("Account not found");
	            return;
	        }

	        System.out.println("Account Holder Name: " + acc.getName() + " | Balance: " + acc.getBalance());

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	
    // HELPER METHODS (REUSABLE)
	
	private void updateBalance(Connection con,int accountNumber, BigDecimal balance) throws SQLException {

        String updateQuery = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        
        try(PreparedStatement ps = con.prepareStatement(updateQuery)){

	        ps.setBigDecimal(1, balance);
	        ps.setInt(2, accountNumber);
	        ps.executeUpdate();
        }
    }

	
	
	// Log transaction (DB + File)
	private void logTransaction(Connection con, int accountNumber, String type, 
								BigDecimal amount, BigDecimal balance) throws SQLException {
		
		TransactionLogger.log(con, accountNumber, type, amount, balance);
		FileLogger.logToFile(accountNumber, type, amount, balance);
		
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
