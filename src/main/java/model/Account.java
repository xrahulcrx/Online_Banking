package model;


import java.math.BigDecimal;


//Model class representing a bank account
public class Account {
	
	
	// Unique account ID (cannot change once created)
	private final int accountID;
	// Account holder name (usually fixed)
	private final String name;
	// Account balance (changes after deposit/withdraw)
	private BigDecimal balance;
	
	//constructors for accounts
	public Account(int accountID, String name, BigDecimal balance) {
		this.accountID = accountID;
		this.name = name;
		this.balance = balance;
	}
	
	//getters for account ID, name, balance
	public int getAccountID() {
		return accountID;
	}
	
	public String getName() {
		return name;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	
	//setters for balance after transactions
	
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	
	
	
	

}
