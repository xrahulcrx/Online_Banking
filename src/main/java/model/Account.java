package model;


import java.math.BigDecimal;



public class Account {
	
	private final int accountID;
	private final String name;
	private BigDecimal balance;
	
	//constructors for accounts
	public Account(int accountID, String name, BigDecimal balance) {
		this.accountID = accountID;
		this.name = name;
		this.balance = balance;
	}
	
	//getters
	public int getAccountID() {
		return accountID;
	}
	
	public String getName() {
		return name;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	
	//setters for balance
	
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	
	
	
	

}
