# Online Banking Transaction System

### (Core Java + JDBC + MySQL + Maven)

A console-based banking application built using Core Java, JDBC, and MySQL, simulating real-world banking operations like account creation, deposits, withdrawals, balance enquiry, and transaction logging.

This project demonstrates JDBC transactions, multithreading safety, input validation, and financial accuracy using BigDecimal.

## Features

* Create new bank accounts
* Deposit money
* Withdraw money with balance validation
* View account balance
* Transaction logging (Database + File)
* Thread-safe operations using synchronized
* Safe numeric input handling
* Menu-driven console interface
* JDBC transaction management with commit/rollback

## Technologies Used

* Java (JDK 21)
* JDBC
* MySQL
* Maven
* File Handling
* Multithreading & Synchronization
* BigDecimal (for money precision)



## Project Structure

```
src/main/java
│
├── db
│   └── DatabaseUtility.java
│
├── logs
│   ├── FileLogger.java
│   └── TransactionLogger.java
│
├── model
│   └── Account.java
│
├── service
│   └── AccountService.java
│
└── MainApp.java

```

## Package Explanation

### db package
#### DatabaseUtility.java - Central place for database connectivity.

```
Handles:
JDBC connection setup
DB credentials configuration
Returning reusable DB connection
```

### model package
#### Account.java - Acts as data model for accounts.
```
Represents account entity:
accountId
accountHolderName
balance
```

### service package - This is the heart of the application.
#### AccountService.java
```
Core business logic:
createAccount()
deposit()
withdraw()
getBalance()
transaction safety
validation rules
commit/rollback handling
```

### logs package
#### TransactionLogger.java - Provides audit trail in DB.
```
Stores transactions in database
Uses PreparedStatements
Ensures reliable logging
```
#### FileLogger.java - Provides audit trail in file.
```
Logs transactions to text file
Timestamped entries
Append mode logging
```

### MainApp.java - Acts as UI layer.
```
Handles:
Menu display
User input
Input validation
Calling service methods
```

## Database Setup (SQL)

### Create Database
```
CREATE DATABASE bankdb;
USE bankdb;
```

### Create Accounts Table
```
CREATE TABLE accounts (
    account_number INT AUTO_INCREMENT PRIMARY KEY,
    account_holder_name VARCHAR(100) NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00 NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)AUTO_INCREMENT = 1000;
```

### Create Transactions Table
```
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_number INT,
    transaction_type VARCHAR(20),
    amount DECIMAL(15,2),
    balance_after DECIMAL(15,2),
    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_number)
        REFERENCES accounts(account_number)
);
```

## Maven Setup
### pom.xml Dependency
#### Add MySQL connector:
```
<dependencies>
	<dependency>
	    <groupId>com.mysql</groupId>
	    <artifactId>mysql-connector-j</artifactId>
	    <version>9.6.0</version>
	    <scope>compile</scope>
	</dependency>
</dependencies>
```
#### Compiler Plugin (Java 21)
```
<build>
  <plugins>
    <plugin>
      <artifactId>maven-compiler-plugin</artifactId>
      <version>3.13.0</version>
      <configuration>
        <release>21</release>
      </configuration>
    </plugin>
  </plugins>
</build>

```


## How to Run
### Clone repository
```
https://github.com/xrahulcrx/Online_Banking
```

### Configure DB credentials in: DatabaseUtility.java
```
String URL= "jdbc:mysql://localhost:3306/bankdb";
String USER= "root";
String PASSWORD= "yourpassword";
```

### Run
```
MainApp.java
```

### Sample Menu
```
===== BANK MENU =====
1. Create Account
2. Deposit
3. Withdraw
4. View Balance
5. View Logs
6. Exit
```