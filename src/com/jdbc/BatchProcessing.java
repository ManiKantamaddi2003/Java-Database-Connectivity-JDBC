package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BatchProcessing {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/jdbcdatabase";
        String username = "root";
        String password = "1234";

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            con = DriverManager.getConnection(url, username, password);

            // Create a statement object for batch processing
            stmt = con.createStatement();

            // Add queries to batch
            String query1 = "INSERT INTO employees(id, name, age) VALUES (11, 'Gopal', 25)";
            String query2 = "INSERT INTO employees(id, name, age) VALUES (12, 'Dhanush', 23)";
            stmt.addBatch(query1);
            stmt.addBatch(query2);

            // Execute batch queries
            int[] results = stmt.executeBatch();
            System.out.println("Batch Execution Results:");
            for (int result : results) {
                System.out.println(result == Statement.SUCCESS_NO_INFO ? "Success" : result);
            }

            // Retrieve data from employees table
            String selectQuery = "SELECT * FROM employees";
            rs = stmt.executeQuery(selectQuery);

            // Display table in a structured format
            System.out.println("+----+-------------+-----+");
            System.out.println("| ID | Name        | Age |");
            System.out.println("+----+-------------+-----+");

            while (rs.next()) {
                System.out.printf("| %-2d | %-10s | %-3d |\n", rs.getInt("id"), rs.getString("name"), rs.getInt("age"));
            }

            System.out.println("+----+-------------+-----+");

        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred.");
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error while closing resources.");
                e.printStackTrace();
            }
        }
    }
}
