package logs;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;


public class FileLogger {
	
	private static final String TRANS_LOGS = "transactions.txt";
	
	
	public static void logToFile(int accountNumber, String type, BigDecimal amount, BigDecimal balanceAfter) {
		
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String time = LocalDateTime.now().format(formatter);
		
		String message = String.format("%-20s | Acccount Number:%-6d | Type:%-10s | Amount:%-8s | Balance:%-10s%n", 
								time,
								accountNumber,
								type,
								amount,
								balanceAfter);
		
		try(FileWriter fw = new FileWriter(TRANS_LOGS, true)) {
			fw.write(message);
			
			//to get the path of file stored
			//System.out.println(new File(TRANS_LOGS).getAbsolutePath());

		} catch (IOException e) {
			
			System.out.println("File logging failed!");
			e.printStackTrace();
		}
		
		
	}
	
	
	public static void viewLogs() {
		
		System.out.println("\n======= TRANSACTION LOGS =======");
		
		try (BufferedReader br = new BufferedReader(new FileReader(TRANS_LOGS))){
			
			String line;
			
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			
			
		}catch (IOException e) {
			System.out.println("No logs found.");
		}
		
		
	}

}
