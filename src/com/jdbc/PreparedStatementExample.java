package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PreparedStatementExample {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String url = "jdbc:mysql://localhost:3306/jdbcdatabase";
        String username = "root";
        String password = "1234";

        Connection con = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            con = DriverManager.getConnection(url, username, password);

            System.out.println("Enter the number of employees you want to add:");
            int n = scan.nextInt();

            // Prepare the SQL query
            String sql = "INSERT INTO employees(id, name, age) VALUES (?, ?, ?)";
            pstmt = con.prepareStatement(sql);

            for (int i = 0; i < n; i++) {
                System.out.println("Enter details for Employee " + (i + 1) + " (ID Name Age):");
                int id = scan.nextInt();
                String name = scan.next();
                int age = scan.nextInt();

                pstmt.setInt(1, id);
                pstmt.setString(2, name);
                pstmt.setInt(3, age);

                // Add to batch
                pstmt.addBatch();
            }

            // Execute batch
            int[] results = pstmt.executeBatch();
            System.out.println(results.length + " row(s) inserted successfully!");

            // Retrieve and display the table data
            stmt = con.createStatement();
            String selectQuery = "SELECT * FROM employees";
            rs = stmt.executeQuery(selectQuery);

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
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error while closing resources.");
                e.printStackTrace();
            }
        }

        scan.close();
    }
}
