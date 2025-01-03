package com.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FirstProgram {

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
            stmt = con.createStatement();

            // Insert query
            String insertQuery = "INSERT INTO employees(id, name, age) VALUES (4, 'ManiKanta', 22)";
            int rowsUpdated = stmt.executeUpdate(insertQuery);
            System.out.println(rowsUpdated + " Row(s) Updated");
            
            
            //Update Query
            String updateQuery="UPDATE `employees` SET `age` = '21' WHERE (`id` = '2')";
            int i=stmt.executeUpdate(updateQuery);
            System.out.println(i +"Rows(s) Updated");
            
            //Delete query
            String deleteQuery="Delete from `employees` where age='28' ";
            int j=stmt.executeUpdate(deleteQuery);
            System.out.println(j+" rows Deleted");
            
            // Select query
            String selectQuery = "SELECT * FROM employees";
            rs = stmt.executeQuery(selectQuery);

            // Display result
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " " + rs.getString("name") + " " + rs.getInt("age"));
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
