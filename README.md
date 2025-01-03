# JDBC Practice Repository

This repository contains various Java examples demonstrating JDBC (Java Database Connectivity) concepts. The project covers all key JDBC functionalities such as CRUD operations, batch processing, stored procedures, working with BLOBs and CLOBs, and more.

## Table of Contents

- [Overview](#overview)
- [JDBC Concepts Covered](#jdbc-concepts-covered)
- [Technologies Used](#technologies-used)
- [Database Setup](#database-setup)
- [Code Examples](#code-examples)
  - [JDBC Introduction](#jdbc-introduction)
  - [Downloading Driver Software](#downloading-driver-software)
  - [CRUD Operations](#crud-operations)
  - [Batch Processing](#batch-processing)
  - [Prepared Statement](#prepared-statement)
  - [ACID Properties](#acid-properties)
  - [Stored Procedure](#stored-procedure)
  - [BLOB](#blob)
  - [CLOB](#clob)
- [How to Run](#how-to-run)
- [License](#license)

## Overview

This repository demonstrates the usage of JDBC for performing database operations in Java. It covers:

- Basic database operations like insert, update, delete, and select (CRUD operations).
- Batch processing to execute multiple SQL commands in a single batch.
- Prepared statements to prevent SQL injection and improve performance.
- The ACID properties of transactions to ensure reliable and consistent database operations.
- Calling stored procedures from Java using CallableStatement.
- Working with BLOB (Binary Large Object) and CLOB (Character Large Object) types for storing large binary and text data in MySQL.

## JDBC Concepts Covered

### JDBC Introduction

This section introduces JDBC, explaining how it allows Java applications to connect and interact with relational databases.

- JDBC provides a standard interface for interacting with databases.
- It allows the execution of SQL queries, updates, and other database interactions.
- The `java.sql` package provides classes and interfaces for database communication.

### Downloading Driver Software

To connect Java to a MySQL database, you need the MySQL JDBC driver. Here are the steps:

1. Download the MySQL Connector/J driver from [MySQL's official site](https://dev.mysql.com/downloads/connector/j/).
2. Add the driver JAR (`mysql-connector-java-x.x.x.jar`) to your project's classpath.

### CRUD Operations

CRUD operations (Create, Read, Update, Delete) are fundamental operations for interacting with a database.

- **Create**: Inserting new records into a table.
- **Read**: Retrieving data from a table using `SELECT` queries.
- **Update**: Modifying existing records in a table.
- **Delete**: Removing records from a table.

Example of basic CRUD operations:

```java
// Insert
String sqlInsert = "INSERT INTO employees (name, age) VALUES (?, ?)";
PreparedStatement pstmtInsert = con.prepareStatement(sqlInsert);
pstmtInsert.setString(1, "John Doe");
pstmtInsert.setInt(2, 30);
pstmtInsert.executeUpdate();

// Select
String sqlSelect = "SELECT * FROM employees";
Statement stmt = con.createStatement();
ResultSet rs = stmt.executeQuery(sqlSelect);
while (rs.next()) {
    System.out.println(rs.getString("name") + " - " + rs.getInt("age"));
}

// Update
String sqlUpdate = "UPDATE employees SET age = ? WHERE name = ?";
PreparedStatement pstmtUpdate = con.prepareStatement(sqlUpdate);
pstmtUpdate.setInt(1, 35);
pstmtUpdate.setString(2, "John Doe");
pstmtUpdate.executeUpdate();

// Delete
String sqlDelete = "DELETE FROM employees WHERE name = ?";
PreparedStatement pstmtDelete = con.prepareStatement(sqlDelete);
pstmtDelete.setString(1, "John Doe");
pstmtDelete.executeUpdate();
```

### Batch Processing

Batch processing allows executing multiple SQL statements in a single batch for efficiency.

```java
// Batch processing
String insertSQL = "INSERT INTO employees (name, age) VALUES (?, ?)";
PreparedStatement pstmt = con.prepareStatement(insertSQL);
for (Employee emp : employees) {
    pstmt.setString(1, emp.getName());
    pstmt.setInt(2, emp.getAge());
    pstmt.addBatch();
}
pstmt.executeBatch();
```

### Prepared Statement

Prepared statements are used to prevent SQL injection and improve performance by reusing SQL queries.

```java
// Using PreparedStatement
String sql = "SELECT * FROM employees WHERE age > ?";
PreparedStatement pstmt = con.prepareStatement(sql);
pstmt.setInt(1, 25);
ResultSet rs = pstmt.executeQuery();
```

### ACID Properties

ACID stands for **Atomicity**, **Consistency**, **Isolation**, and **Durability**. These are the four properties that ensure reliable transactions in a database.

1. **Atomicity**: Ensures that all operations in a transaction are completed successfully or none at all.
2. **Consistency**: Guarantees that the database is left in a valid state after a transaction.
3. **Isolation**: Ensures that concurrent transactions do not interfere with each other.
4. **Durability**: Ensures that once a transaction is committed, it is permanently saved to the database.

Example:

```java
// Transaction with ACID properties
con.setAutoCommit(false);  // Disable auto-commit
try {
    Statement stmt = con.createStatement();
    stmt.executeUpdate("UPDATE employees SET age = 30 WHERE name = 'John Doe'");
    con.commit();  // Commit the transaction
} catch (SQLException e) {
    con.rollback();  // Rollback if an error occurs
}
```

### Stored Procedure

A stored procedure is a precompiled SQL query that can be executed from Java. It helps improve performance and maintainability by encapsulating complex logic in the database.

```java
// Call stored procedure
String sql = "{CALL count_employees(?)}";
CallableStatement cstmt = con.prepareCall(sql);
cstmt.setInt(1, 25);
cstmt.execute();
ResultSet rs = cstmt.getResultSet();
while (rs.next()) {
    System.out.println(rs.getInt(1));
}
```

### BLOB

BLOB (Binary Large Object) is used for storing binary data such as images, videos, and files.

Example code for inserting and retrieving a BLOB:

```java
// Insert BLOB (Image)
String insertSQL = "INSERT INTO images (image_name, image_data) VALUES (?, ?)";
PreparedStatement pstmt = con.prepareStatement(insertSQL);
FileInputStream fis = new FileInputStream(new File("image.jpg"));
pstmt.setString(1, "example_image");
pstmt.setBinaryStream(2, fis, fis.available());
pstmt.executeUpdate();

// Retrieve BLOB (Image)
String selectSQL = "SELECT image_data FROM images WHERE image_name = ?";
PreparedStatement pstmtSelect = con.prepareStatement(selectSQL);
pstmtSelect.setString(1, "example_image");
ResultSet rs = pstmtSelect.executeQuery();
if (rs.next()) {
    InputStream input = rs.getBinaryStream("image_data");
    FileOutputStream output = new FileOutputStream("output_image.jpg");
    byte[] buffer = new byte[1024];
    while (input.read(buffer) > 0) {
        output.write(buffer);
    }
}
```

### CLOB

CLOB (Character Large Object) is used for storing large text data such as documents or long descriptions.

Example code for inserting and retrieving a CLOB:

```java
// Insert CLOB (Text)
String insertSQL = "INSERT INTO employees (name, description) VALUES (?, ?)";
PreparedStatement pstmt = con.prepareStatement(insertSQL);
pstmt.setString(1, "John Doe");
FileReader reader = new FileReader("sample_text.txt");
pstmt.setCharacterStream(2, reader);
pstmt.executeUpdate();

// Retrieve CLOB (Text)
String selectSQL = "SELECT description FROM employees WHERE name = ?";
PreparedStatement pstmtSelect = con.prepareStatement(selectSQL);
pstmtSelect.setString(1, "John Doe");
ResultSet rs = pstmtSelect.executeQuery();
if (rs.next()) {
    Reader reader = rs.getCharacterStream("description");
    BufferedReader br = new BufferedReader(reader);
    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
}
```

## How to Run

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/jdbc-practice.git
   ```
2. **Install MySQL** and set up the database (`jdbcdatabase`) with the required tables.
3. **Update the JDBC connection details** in the Java code (e.g., `BlobExample.java` and `ClobExample.java`).
4. **Compile and run the Java programs** using the following command:
   ```bash
   javac BlobExample.java
   java BlobExample
   ```
5. Follow the prompts to insert and retrieve BLOB and CLOB data.

