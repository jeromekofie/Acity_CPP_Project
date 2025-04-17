import java.sql.*;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db2";
    private static final String USER = "root";
    private static final String PASS = "benittaking";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found");
        }
    }
    
    public static void initializeDatabase() {
        String[] createTables = {
            "CREATE TABLE IF NOT EXISTS users (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "username VARCHAR(50) UNIQUE NOT NULL, " +
            "password VARCHAR(255) NOT NULL, " +
            "role VARCHAR(20) DEFAULT 'librarian')",
            
            "CREATE TABLE IF NOT EXISTS books (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "title VARCHAR(255) NOT NULL, " +
            "author VARCHAR(255) NOT NULL, " +
            "isbn VARCHAR(20) UNIQUE, " +
            "available INT DEFAULT 1)",
            
            "CREATE TABLE IF NOT EXISTS members (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "name VARCHAR(255) NOT NULL, " +
            "email VARCHAR(255) UNIQUE, " +
            "phone VARCHAR(20))",
            
            "CREATE TABLE IF NOT EXISTS loans (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "book_id INT NOT NULL, " +
            "member_id INT NOT NULL, " +
            "loan_date DATE NOT NULL, " +
            "due_date DATE NOT NULL, " +
            "return_date DATE, " +
            "FOREIGN KEY (book_id) REFERENCES books(id), " +
            "FOREIGN KEY (member_id) REFERENCES members(id))",
            
            "CREATE TABLE IF NOT EXISTS fines (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "loan_id INT NOT NULL, " +
            "amount DECIMAL(10,2) NOT NULL, " +
            "issued_date DATE NOT NULL, " +
            "paid BOOLEAN DEFAULT FALSE, " +
            "FOREIGN KEY (loan_id) REFERENCES loans(id))"
        };
        
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            for (String sql : createTables) {
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}