package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AcidExampleWithCompleteValidation {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/jdbcdatabase";
        String username = "root";
        String password = "1234";

        Connection con = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmtCheckSourceBalance = null;
        PreparedStatement pstmtCheckDestination = null;
        ResultSet rsSource = null;
        ResultSet rsDestination = null;

        Scanner scanner = new Scanner(System.in);

        try {
            // Load the driver and connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            // Disable auto-commit for transaction management
            con.setAutoCommit(false);

            // Prompt user for transfer details
            System.out.print("Enter Source Account ID: ");
            int fromAccountId = scanner.nextInt();

            System.out.print("Enter Destination Account ID: ");
            int toAccountId = scanner.nextInt();

            System.out.print("Enter Transfer Amount: ");
            double transferAmount = scanner.nextDouble();

            // Step 1: Validate the source account balance
            String checkSourceBalanceSql = "SELECT balance FROM accounts WHERE account_id = ?";
            pstmtCheckSourceBalance = con.prepareStatement(checkSourceBalanceSql);
            pstmtCheckSourceBalance.setInt(1, fromAccountId);
            rsSource = pstmtCheckSourceBalance.executeQuery();

            if (rsSource.next()) {
                double currentBalance = rsSource.getDouble("balance");
                if (currentBalance < transferAmount) {
                    throw new SQLException("Insufficient balance in the source account.");
                }
            } else {
                throw new SQLException("Source account ID: " + fromAccountId + " not found.");
            }

            // Step 2: Validate the destination account existence
            String checkDestinationSql = "SELECT account_id FROM accounts WHERE account_id = ?";
            pstmtCheckDestination = con.prepareStatement(checkDestinationSql);
            pstmtCheckDestination.setInt(1, toAccountId);
            rsDestination = pstmtCheckDestination.executeQuery();

            if (!rsDestination.next()) {
                throw new SQLException("Destination account ID: " + toAccountId + " not found.");
            }

            // Step 3: Deduct amount from source account
            String deductSql = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
            pstmt1 = con.prepareStatement(deductSql);
            pstmt1.setDouble(1, transferAmount);
            pstmt1.setInt(2, fromAccountId);
            int rowsUpdated1 = pstmt1.executeUpdate();

            if (rowsUpdated1 == 0) {
                throw new SQLException("Failed to deduct amount from source account.");
            }

            // Step 4: Add amount to destination account
            String addSql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
            pstmt2 = con.prepareStatement(addSql);
            pstmt2.setDouble(1, transferAmount);
            pstmt2.setInt(2, toAccountId);
            int rowsUpdated2 = pstmt2.executeUpdate();

            if (rowsUpdated2 == 0) {
                throw new SQLException("Failed to add amount to destination account.");
            }

            // Commit the transaction
            con.commit();
            System.out.println("Transaction successful! Transferred $" + transferAmount);

        } catch (Exception e) {
            // Rollback the transaction in case of an error
            if (con != null) {
                try {
                    con.rollback();
                    System.out.println("Transaction rolled back due to an error.");
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rsSource != null) rsSource.close();
                if (rsDestination != null) rsDestination.close();
                if (pstmtCheckSourceBalance != null) pstmtCheckSourceBalance.close();
                if (pstmtCheckDestination != null) pstmtCheckDestination.close();
                if (pstmt1 != null) pstmt1.close();
                if (pstmt2 != null) pstmt2.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            scanner.close();
        }
    }
}
