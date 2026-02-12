package logs;

import java.math.BigDecimal;
import java.sql.*;


public class TransactionLogger {
	
	// Static method so it can be called without creating object
	// Logs each transaction into DB
	
	public static void log(Connection con, int accountNumber, 
							String type, 
							BigDecimal amount, 
							BigDecimal balanceAfter) throws SQLException{
		
		// SQL insert into transactions table
		String sqlquery = "INSERT INTO transactions" + 
							"(account_number, transaction_type, amount, balance_after)" +
								"VALUES (?, ?, ?, ?)";
		
		// Prepare statement using existing connection
		try(PreparedStatement ps = con.prepareStatement(sqlquery)){
				
			// Set values into placeholders
			ps.setInt(1, accountNumber);
			ps.setString(2, type);
			ps.setBigDecimal(3, amount);
			ps.setBigDecimal(4, balanceAfter);
				
			// Execute insert
			ps.executeUpdate();
		}
			
			
	}
	
	
}
