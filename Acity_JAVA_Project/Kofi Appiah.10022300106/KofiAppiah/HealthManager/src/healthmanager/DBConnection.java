package healthmanager;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author User
 */
import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/healthtracker";
    private static final String USER = "root";
    private static final String PASSWORD = "KOFI8415a1"; // update with your MySQL password
    

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

