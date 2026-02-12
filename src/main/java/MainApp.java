import service.AccountService;


import java.util.Scanner;
import java.math.BigDecimal;

public class MainApp {
	
	// Safe Integer Input Method
	
	static int readInt(Scanner sc, String msg) {
        while (true) {
            System.out.print(msg);
            if (sc.hasNextInt()) return sc.nextInt();
            System.out.println("Numbers only.");
            sc.next();
        }
    }
	
	
    // Safe BigDecimal Input Method
	
	static BigDecimal readBigDecimal(Scanner sc, String msg) {
        while (true) {
            System.out.print(msg);
            try {
                // amount cannot be negative
                BigDecimal val = new BigDecimal(sc.next());
                if (val.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Amount must be positive");
                    continue;
                }
                return val;
            } catch (Exception e) {
                System.out.println("Numbers only.");
            }
        }
    }
	
	
	
    // MAIN METHOD (Program)

    public static void main(String[] args) throws Exception {
    	
    	
    	Scanner sc = new Scanner(System.in);
 
        // Service class handles  logic
    	AccountService service = new AccountService();
    	
    	
        // Infinite loop for menu
    	while (true) {
    		
            // Display menu
    		System.out.println("\n===== BANK MENU =====");
    		System.out.println("1. Create Account");
    		System.out.println("2. Deposit");
    		System.out.println("3. Withdraw");
    		System.out.println("4. View Balance");
    		System.out.println("5. View Logs");
    		System.out.println("6. Exit");
    		
    		
            // Read user menu choice safely
    		int choice = readInt(sc, "\nEnter the choice: ");
    		
    		    		
    		switch (choice) {
			
    		//CREATE ACCOUNT
    		case 1: 
				
				sc.nextLine();
								
				System.out.print("Enter Name: ");
				String name = sc.nextLine();
				
				
				BigDecimal initAmt = readBigDecimal(sc, "Enter the Initial Deposit: ");
				
				service.createAccount(name, initAmt);
				break;
			
			//DEPOSIT
			case 2: 
				
				
				int accDeposit = readInt(sc,"Enter the Account Number: ");

				BigDecimal depAmt = readBigDecimal(sc, "Enter the Deposit Amount: ");

				service.deposit(accDeposit, depAmt);
				break;
				
			//WITHDRAW 
			case 3: 
				
				int accWithdraw = readInt(sc, "Enter the Account Number: ");
				
				BigDecimal withAmt = readBigDecimal(sc, "Enter the Withdrawal Amount: ");
				
				service.withdraw(accWithdraw, withAmt);
				break;
				
			//BALANCE 
			case 4:
				
				int accBal = readInt(sc, "Enter the account number to check the balance: ");
				
				service.getBalance(accBal);
				break;
				
			//VIEW LOGS
			case 5:
				
				logs.FileLogger.viewLogs();
				break;
					
			//EXIT 
			case 6: 
				
				System.out.println("Thank you for using the service. Exiting..");
				sc.close();
				return;
			
			//INVALID 	
			default:
				System.out.println("Invalid choice!");
				
			}
    		
    	}
    	  	
    	
    	
    }
}
