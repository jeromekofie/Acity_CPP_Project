import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/employee_db";
    private static final String USER = "root";
    private static final String PASSWORD = "qwerty";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                         "id INT AUTO_INCREMENT PRIMARY KEY, " +
                         "username VARCHAR(50) NOT NULL UNIQUE, " +
                         "password VARCHAR(255) NOT NULL)");
            
            // Create employees table
            stmt.execute("CREATE TABLE IF NOT EXISTS employees (" +
                         "id INT AUTO_INCREMENT PRIMARY KEY, " +
                         "name VARCHAR(100) NOT NULL, " +
                         "email VARCHAR(100), " +
                         "phone VARCHAR(20), " +
                         "type VARCHAR(20) NOT NULL, " +
                         "salary DOUBLE, " +
                         "hourly_rate DOUBLE, " +
                         "hours_worked DOUBLE)");
            
            // Add default admin if no users exist
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            rs.next();
            if (rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO users (username, password) VALUES ('admin', 'admin123')");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}