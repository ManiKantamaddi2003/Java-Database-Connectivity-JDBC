package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TypeScrollScensitivity {
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
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,0);

            // Select query
            String selectQuery = "SELECT * FROM employees";
            rs = stmt.executeQuery(selectQuery);

            // Display result
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " " + rs.getString("name") + " " + rs.getInt("age"));
            }
            System.out.println("using `first`");
            rs.first();
            System.out.println(rs.getInt("id") + " " + rs.getString("name") + " " + rs.getInt("age"));
            System.out.println("using `last`");
            rs.last();
            System.out.println(rs.getInt("id") + " " + rs.getString("name") + " " + rs.getInt("age"));
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


