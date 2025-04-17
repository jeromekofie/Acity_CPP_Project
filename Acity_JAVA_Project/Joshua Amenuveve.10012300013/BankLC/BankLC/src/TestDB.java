import java.sql.*;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author joshua_veve
 */
public class TestDB {public static void main(String[] args) {
        Connection conn = DB.getConnection();
        if (conn != null) {
            System.out.println("✅ Connected to database successfully!");
        } else {
            System.out.println("❌ Connection failed.");
        }
    }
    
}
