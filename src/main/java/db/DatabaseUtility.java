package db;

import java.sql.*;

//Utility class for handling database connection
public class DatabaseUtility {
	
	
	// Database connection details
	private static final String URL = "jdbc:mysql://localhost:3306/bankdb";
	private static final String USER = "root";
	private static final String PASSWORD = "admin";
	
    // Method to establish and return DB connection
	public static Connection getConnection() {
		Connection dbconnect = null;
		
		try {
			
			//db connect
			
			dbconnect = DriverManager.getConnection(URL, USER, PASSWORD);
			//System.out.println("Database Connection Successful");
				
		} catch (SQLException e) {
			System.out.println("Databse Connection Failed");
			e.printStackTrace();	
		}
		
		return dbconnect;
	}

}
