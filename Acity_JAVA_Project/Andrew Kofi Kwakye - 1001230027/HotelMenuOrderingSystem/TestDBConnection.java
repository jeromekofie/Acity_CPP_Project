import java.sql.*;

public class TestDBConnection {
    public static void main(String[] args) {
        // Database credentials - update these to match your setup
        String url = "jdbc:mysql://localhost:3306/hotel_orders"; // your database URL
        String user = "root";  // default MySQL username
        String password = "1234";   // your MySQL password (empty if none)

        System.out.println("Testing MySQL database connection...");

        try {
            // 1. Load the JDBC drive
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully!");

            // 2. Attempt to connect to the database
            System.out.println("Attempting to connect to database...");
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                System.out.println("Connection successful!");
                // 3. Test if tables exist
                System.out.println("\nChecking required tables...");
                checkTableExists(connection, "orders");
                checkTableExists(connection, "order_items");
                // 4. Close the connection
            }
            System.out.println("\nConnection closed. All tests passed!");

        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            System.err.println("Download it from: https://dev.mysql.com/downloads/connector/j/");
            System.err.println("Add the JAR file to your project's classpath");
        } catch (SQLException e) {
            System.err.println("Connection failed! Error details:");
            
            // Provide specific troubleshooting based on common errors
            if (e.getMessage().contains("Access denied")) {
                System.err.println("\nTROUBLESHOOTING: Wrong username/password? Try:");
                System.err.println("1. Verify your MySQL credentials");
                System.err.println("2. If you just installed MySQL, the default password might be empty");
            } else if (e.getMessage().contains("Unknown database")) {
                System.err.println("\nTROUBLESHOOTING: Database doesn't exist. Create it with:");
                System.err.println("CREATE DATABASE hotel_orders;");
            } else if (e.getMessage().contains("Communications link failure")) {
                System.err.println("\nTROUBLESHOOTING: Is MySQL server running?");
                System.err.println("On Windows: Check services.msc for MySQL service");
                System.err.println("On Mac/Linux: Try 'sudo service mysql start'");
            }
        }
    }

    private static void checkTableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"TABLE"});
        
        if (resultSet.next()) {
            System.out.println("✔ Table '" + tableName + "' exists");
        } else {
            System.out.println("✖ Table '" + tableName + "' NOT found!");
            System.out.println("Create it with the SQL commands provided in the setup instructions");
        }
    }
}