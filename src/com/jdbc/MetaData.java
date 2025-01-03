package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class MetaData {
    public static void main(String args[]) {
        String url = "jdbc:mysql://localhost:3306/jdbcdatabase";
        String username = "root";
        String password = "1234";

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            // Select query
            String selectQuery = "SELECT * FROM employees";
            rs = stmt.executeQuery(selectQuery);

            // Display table header
            System.out.println("+----+-------------+-----+");
            System.out.println("| ID | Name       | Age |");
            System.out.println("+----+-------------+-----+");

            // Display result rows
            while (rs.next()) {
                System.out.printf("| %-2d | %-10s | %-3d |\n", rs.getInt("id"), rs.getString("name"), rs.getInt("age"));
            }

            // Display footer line
            System.out.println("+----+------------+-----+");
            
            ResultSetMetaData metadata=rs.getMetaData();
            int ColumnCount=metadata.getColumnCount();
            for(int i=1;i<=ColumnCount;i++)
            {
            	System.out.println("Column Name: "+metadata.getColumnName(i));
            	System.out.println("Column Type: "+metadata.getColumnType(i));
            	
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
