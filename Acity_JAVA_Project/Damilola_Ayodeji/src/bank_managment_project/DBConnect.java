package bank_managment_project;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;


public class DBConnect {
      private static final String URL = "jdbc:mysql://localhost:3306/mysql_1"; // Make sure this database exists
    private static final String USER = "root";  // Your MySQL username
    private static final String PASSWORD = "1234";  // Your MySQL password

    public static Connection getConnection() {
        Connection con = null;
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, "MySQL JDBC Driver not found!", ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, "Database connection failed!", ex);
        }
        return con;
    }
}

