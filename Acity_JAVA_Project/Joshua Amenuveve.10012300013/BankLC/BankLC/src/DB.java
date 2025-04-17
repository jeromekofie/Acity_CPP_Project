/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */import java.sql.*;

/**
 *
 * @author joshua_veve
 */
public class DB {
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/loan_db", "root", ""
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
