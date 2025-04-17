import java.sql.*;
import javax.swing.JOptionPane;

public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/rental_system?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "Numelio1810";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            showErrorAndExit("MySQL JDBC Driver not found!", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Successfully connected to database");
            return conn;
        } catch (SQLException e) {
            System.err.println("Connection failed:");
            e.printStackTrace();
            throw e;
        }
    }
    
    public static void initializeDatabase() {
        String adminUrl = "jdbc:mysql://127.0.0.1:3306/?user=root&password=Numelio1810";
        
        try (Connection conn = DriverManager.getConnection(adminUrl);
             Statement stmt = conn.createStatement()) {
            
            // 1. Create fresh database
            stmt.execute("DROP DATABASE IF EXISTS rental_system");
            stmt.execute("CREATE DATABASE rental_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            stmt.execute("USE rental_system");
            System.out.println("Database created successfully");
            
            // 2. Create vehicles table (no dependencies)
            stmt.execute("CREATE TABLE vehicles ("
                + "id VARCHAR(20) PRIMARY KEY,"
                + "type VARCHAR(20) NOT NULL,"
                + "brand VARCHAR(50) NOT NULL,"
                + "model VARCHAR(50) NOT NULL,"
                + "rate DECIMAL(10,2) NOT NULL,"
                + "color VARCHAR(30),"
                + "year INT,"
                + "doors INT,"
                + "transmission VARCHAR(20),"
                + "fuel_type VARCHAR(20),"
                + "is_rented BOOLEAN NOT NULL DEFAULT FALSE,"
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
                + ") ENGINE=InnoDB");
            System.out.println("Vehicles table created");
            
            // 3. Create customers table
            stmt.execute("CREATE TABLE customers ("
                + "id VARCHAR(20) PRIMARY KEY,"
                + "name VARCHAR(100) NOT NULL,"
                + "license_number VARCHAR(50) NOT NULL UNIQUE,"
                + "phone VARCHAR(20),"
                + "email VARCHAR(100),"
                + "registration_date DATE,"
                + "loyalty_points INT DEFAULT 0,"
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
                + ") ENGINE=InnoDB");
            System.out.println("Customers table created");
            
            // 4. Create rentals table (with foreign keys)
            stmt.execute("CREATE TABLE rentals ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "customer_id VARCHAR(20) NOT NULL,"
                + "vehicle_id VARCHAR(20) NOT NULL,"
                + "start_date BIGINT NOT NULL,"
                + "end_date BIGINT DEFAULT NULL,"
                + "total_cost DECIMAL(10,2) DEFAULT NULL,"
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,"
                + "FOREIGN KEY (customer_id) REFERENCES customers(id),"
                + "INDEX idx_vehicle_id (vehicle_id),"
                + "INDEX idx_customer_id (customer_id)"
                + ") ENGINE=InnoDB");
            System.out.println("Rentals table created");
            
            JOptionPane.showMessageDialog(null, 
                "Database initialized successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            String errorDetails = "Failed to initialize database:\n\n"
                + "Error: " + e.getMessage() + "\n"
                + "SQL State: " + e.getSQLState() + "\n"
                + "Error Code: " + e.getErrorCode() + "\n\n"
                + "Please ensure:\n"
                + "1. MySQL server is running\n"
                + "2. User 'root' has full privileges\n"
                + "3. No other application is using the database";
            
            JOptionPane.showMessageDialog(null,
                errorDetails,
                "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    private static void showErrorAndExit(String message, Exception e) {
        String errorMsg = message + ":\n" + e.getMessage() 
            + "\n\nApplication will now exit.";
        JOptionPane.showMessageDialog(null, 
            errorMsg, 
            "Critical Error", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}