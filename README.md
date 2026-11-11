# Supermarket Billing System

A Java-based desktop application developed using **Java Swing, JDBC, and MySQL** to simplify supermarket billing and product management.

## Features

- **Admin Login:** Secure administrator login.
- **Product Management:** Add, update, delete, search, and view products.
- **Billing System:** Add products to a shopping cart and calculate bills.
- **Bill Generation:** Generate invoices with subtotal, 5% GST, and total amount.
- **Stock Management:** Automatically update product stock after successful billing.
- **Sales History:** View previous bills, search by bill ID, and view bill details.

## Technologies Used

- Java (JDK 24)
- Java Swing
- JDBC
- MySQL 8
- MySQL Connector/J
- Visual Studio Code

## Project Structure

```text
Supermarket-Billing-System/
│
├── database/
│   └── DBConnection.java
├── model/
│   ├── User.java
│   ├── Product.java
│   ├── Bill.java
│   └── BillItem.java
├── dao/
│   ├── UserDAO.java
│   ├── ProductDAO.java
│   ├── BillDAO.java
│   └── SalesHistoryDAO.java
├── ui/
│   ├── LoginFrame.java
│   ├── DashboardFrame.java
│   ├── ProductFrame.java
│   ├── BillingFrame.java
│   └── SalesHistoryFrame.java
├── lib/
│   └── mysql-connector-j-9.7.0.jar
├── Main.java
├── database.sql
├── db.properties.example
├── .gitignore
└── README.md
```

## Setup Instructions

1. Install JDK 24 and MySQL 8.
2. Clone or download this repository.
3. Create the database by executing `database.sql` in MySQL.
4. Copy `db.properties.example` and rename the copy to `db.properties`.
5. Update the database username and password in `db.properties`.
6. Open the project folder in VS Code.

## Compile and Run

Compile:

```powershell
javac -cp ".;lib\mysql-connector-j-9.7.0.jar" Main.java database\*.java model\*.java dao\*.java ui\*.java
```

Run:

```powershell
java -cp ".;lib\mysql-connector-j-9.7.0.jar" Main
```

## Default Admin Login

- Username: `admin`
- Password: `admin123`

Change the default password before using the application in a real environment.

## Author

Developed as an Advanced Java Academic Project.