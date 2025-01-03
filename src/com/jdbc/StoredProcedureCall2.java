package com.jdbc;

import java.sql.*;

public class StoredProcedureCall2 {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/jdbcdatabase";
        String username = "root";
        String password = "1234";

        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            // Load the driver and connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            // Prepare the stored procedure call
            String sql = "{CALL new_procedure(?)}";
            cstmt = con.prepareCall(sql);

            // Set input parameter (e.g., age)
            int age = 19; // Example input parameter
            cstmt.setInt(1, age);

            // Execute the stored procedure
            boolean hasResults = cstmt.execute();

            // Process the result set
            if (hasResults) {
                rs = cstmt.getResultSet();
                while (rs.next()) {
                    System.out.println("Employee Name: " + rs.getString(1));
                }
            } else {
                System.out.println("No results returned.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (cstmt != null) cstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
