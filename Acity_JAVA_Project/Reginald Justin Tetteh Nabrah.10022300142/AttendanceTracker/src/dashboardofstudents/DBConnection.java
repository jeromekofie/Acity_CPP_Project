/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dashboardofstudents;

/**
 *
 * @author palac
 */
import java.sql.*;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/teachers";
        String user = "root";
        String password = ""; // change this if your DB has a password
        return DriverManager.getConnection(url, user, password);
    }
}
