/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Aude-Ivanne
 */
public class DatabaseConnector {
    import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/attendance_db";
    private static final String DB_USER = "root"; // change if you use a different username
    private static final String DB_PASSWORD = ""; // your MySQL password

    public static Connection connect() throws SQLException {
        // Connect to the MySQL database
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Method to create the necessary tables (just like in C++)
    public static void createTables() {
        String createStudentTable = "CREATE TABLE IF NOT EXISTS students ("
            + "roll_number INT PRIMARY KEY, "
            + "name VARCHAR(100) NOT NULL);";
        
        String createAttendanceTable = "CREATE TABLE IF NOT EXISTS attendance ("
            + "roll_number INT, "
            + "date DATE, "
            + "status BOOLEAN, "
            + "FOREIGN KEY (roll_number) REFERENCES students(roll_number));";
        
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createStudentTable);
            stmt.execute(createAttendanceTable);
            System.out.println("✅ Tables created or already exist.");
        } catch (SQLException e) {
            System.out.println("❌ Error creating tables: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        createTables(); // Create the tables when the app starts
    }
}
    
}
