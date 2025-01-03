package com.jdbc;

import java.io.*;
import java.sql.*;

public class ClobExample {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/jdbcdatabase";
        String username = "root";
        String password = "1234";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Load the driver and connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            // Insert a CLOB into the database
            String insertSQL = "INSERT INTO text_data (content) VALUES (?)";
            pstmt = con.prepareStatement(insertSQL);

            // File containing large text data
            File inputFile = new File("\\JDBC_Practise\\src\\input\\sample_text.txt");
            FileReader fr = new FileReader(inputFile);

            pstmt.setCharacterStream(1, fr, (int) inputFile.length());
            pstmt.executeUpdate();
            System.out.println("Text data inserted successfully.");
            pstmt.close();

            // Retrieve the CLOB from the database
            String selectSQL = "SELECT content FROM text_data WHERE id = 1";
            pstmt = con.prepareStatement(selectSQL);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Reader reader = rs.getCharacterStream("content");
                File outputFile = new File("output/retrieved_text.txt");
                FileWriter fw = new FileWriter(outputFile);

                char[] buffer = new char[1024]; // Buffer size for reading data
                int charsRead;

                // Write the text data to the output file
                while ((charsRead = reader.read(buffer)) != -1) {
                    fw.write(buffer, 0, charsRead);
                }

                fw.close();
                reader.close();
                System.out.println("Text data retrieved successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
