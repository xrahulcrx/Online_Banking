package logs;

import java.math.BigDecimal;
import java.sql.*;


public class TransactionLogger {
	
	public static void log(Connection con, int accountNumber, 
							String type, 
							BigDecimal amount, 
							BigDecimal balanceAfter) throws SQLException{
		
		String sqlquery = "INSERT INTO transactions" + 
							"(account_number, transaction_type, amount, balance_after)" +
								"VALUES (?, ?, ?, ?)";
		

		PreparedStatement ps = con.prepareStatement(sqlquery);
			
		ps.setInt(1, accountNumber);
		ps.setString(2, type);
		ps.setBigDecimal(3, amount);
		ps.setBigDecimal(4, balanceAfter);
			
		ps.executeUpdate();
			
			
	}
	
	
}
