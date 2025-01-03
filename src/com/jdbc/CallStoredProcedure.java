package com.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class CallStoredProcedure {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/jdbcdatabase";
        String username = "root";
        String password = "1234";

        Connection con = null;
        CallableStatement cstmt = null;

        Scanner scanner = new Scanner(System.in);

        try {
            // Load the driver and connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            // Prompt user for age limit
            System.out.print("Enter the age limit: ");
            int age = scanner.nextInt();

            // Call the stored procedure
            String sql = "{CALL emp_count(?, ?)}";
            cstmt = con.prepareCall(sql);

            // Set input parameter
            cstmt.setInt(1, 22);

            // Register output parameter
            cstmt.registerOutParameter(2, java.sql.Types.INTEGER);

            // Execute the stored procedure
            cstmt.execute();

            // Get the output value
            int count = cstmt.getInt(2);
            System.out.println("Number of employees older than " + age + ": " + count);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (cstmt != null) cstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            scanner.close();
        }
    }
}
