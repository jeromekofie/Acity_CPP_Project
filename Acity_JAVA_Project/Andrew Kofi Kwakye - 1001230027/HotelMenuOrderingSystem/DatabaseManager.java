import java.sql.*;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel_orders";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234"; // Use your MySQL password here

    // Static block to load the MySQL JDBC driver first
    static {
        try {
            // Try to load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // This will be thrown if the MySQL JDBC driver is not found
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    // Method to initialize the database (create tables if they don't exist)
    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            // Create orders table if it doesn't exist
            String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders (" +
                "order_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "subtotal DECIMAL(10,2), " +
                "tax DECIMAL(10,2), " +
                "total DECIMAL(10,2)" +
                ")";
            
            // Create order_items table if it doesn't exist
            String createOrderItemsTable = "CREATE TABLE IF NOT EXISTS order_items (" +
                "item_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "order_id INT, " +
                "item_name VARCHAR(100), " +
                "item_price DECIMAL(10,2), " +
                "FOREIGN KEY (order_id) REFERENCES orders(order_id)" +
                ")";
            
            stmt.execute(createOrdersTable);
            stmt.execute(createOrderItemsTable);
            
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    // Method to save an order to the database
    public static boolean saveOrder(List<OrderItem> items, double subtotal, double tax, double total) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false); // Start transaction
            
            // Insert order header
            String insertOrderSQL = "INSERT INTO orders (subtotal, tax, total) VALUES (?, ?, ?)";
            PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setDouble(1, subtotal);
            orderStmt.setDouble(2, tax);
            orderStmt.setDouble(3, total);
            orderStmt.executeUpdate();
            
            // Get the generated order ID
            ResultSet rs = orderStmt.getGeneratedKeys();
            int orderId = -1;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }
            
            // Insert order items
            String insertItemSQL = "INSERT INTO order_items (order_id, item_name, item_price) VALUES (?, ?, ?)";
            PreparedStatement itemStmt = conn.prepareStatement(insertItemSQL);
            
            for (OrderItem item : items) {
                itemStmt.setInt(1, orderId);
                itemStmt.setString(2, item.getName());
                itemStmt.setDouble(3, item.getPrice());
                itemStmt.addBatch();
            }
            
            itemStmt.executeBatch();
            conn.commit(); // Commit transaction
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error saving order: " + e.getMessage());
            return false;
        }
    }

    // Method to get all orders from the database
    public static String getAllOrders() {
        StringBuilder sb = new StringBuilder();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            String query = "SELECT o.order_id, o.order_date, o.total, " +
                "GROUP_CONCAT(CONCAT(i.item_name, ' ($', i.item_price, ')') SEPARATOR ', ') AS items " +
                "FROM orders o JOIN order_items i ON o.order_id = i.order_id " +
                "GROUP BY o.order_id, o.order_date, o.total " +
                "ORDER BY o.order_date DESC";
            
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                sb.append("Order ID: ").append(rs.getInt("order_id")).append("\n");
                sb.append("Date: ").append(rs.getTimestamp("order_date")).append("\n");
                sb.append("Items: ").append(rs.getString("items")).append("\n");
                sb.append("Total: $").append(rs.getDouble("total")).append("\n\n");
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving orders: " + e.getMessage());
            return "Error retrieving orders from database";
        }
        
        return sb.toString().isEmpty() ? "No orders found in database" : sb.toString();
    }
}
