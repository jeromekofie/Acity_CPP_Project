/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlineshoppingcart;

/**
 *
 * @author jerem
 */
import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/brads_place";
    private static final String USER = "root";         // Change if needed
    private static final String PASS = "Goldenretriever1.";             // Change if needed
    private static Connection conn;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(URL, USER, PASS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}

