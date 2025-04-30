/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package attendancetracker;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;


public class DbConnection {
      private static final String URL = "jdbc:mysql://localhost:3306/sys"; 
    private static final String USER = "root";  // Your MySQL username
    private static final String PASSWORD = "Mannyzg1z";  // Your MySQL password

    public static Connection getConnection() {
        Connection con = null;
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, "MySQL JDBC Driver not found!", ex);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, "Database connection failed!", ex);
        }
        return con;
    }
}