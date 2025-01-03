package com.jdbc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BlobExample {
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

            // Insert an image into the database
            String insertSQL = "INSERT INTO images (image) VALUES (?)";
            pstmt = con.prepareStatement(insertSQL);
            File file = new File("C:\\Users\\MADDI MANIKANTA\\OneDrive\\Pictures\\New folder\\berserk-knight-guts-5120x2880-18713.jpg");
            InputStream fis = new java.io.FileInputStream(file);
            pstmt.setBinaryStream(1, fis, (int) file.length());
            pstmt.executeUpdate();
            System.out.println("Image inserted successfully.");
            pstmt.close();

            // Retrieve the image from the database
            String selectSQL = "SELECT image FROM images WHERE id = 1";
            pstmt = con.prepareStatement(selectSQL);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                InputStream is = rs.getBinaryStream("image");
                File outputFile = new File("output/example_image.jpg");
                FileOutputStream fos = new FileOutputStream(outputFile);

                byte[] buffer = new byte[1024]; // Buffer size for reading data
                int bytesRead;

                // Write the image data to the output file
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }

                fos.close();
                is.close();
                System.out.println("Image retrieved successfully.");
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
