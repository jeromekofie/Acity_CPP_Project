import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel_orders";
    private static final String USER = "root";
    private static final String PASSWORD = "$Post1303"; // Update with your password

    private Connection connection;

    // Constructor to establish a database connection
    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }

    // Method to load all orders from the database
    public ArrayList<Order> loadOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String orderId = rs.getString("order_id");  // ✅ FIXED: Get order_id from DB
                String customerName = rs.getString("customer_name");
                String items = rs.getString("items");
                String total = String.valueOf(rs.getDouble("total")); // ✅ Convert double to String if needed
                orders.add(new Order(orderId, customerName, items, total));
            }
        } catch (SQLException e) {
            System.err.println("Error loading orders: " + e.getMessage());
        }
        return orders;
    }
    
    // Method to insert a new order into the database
    public void addOrder(String orderId, String customerName, String items, double total) {
        String query = "INSERT INTO orders (order_id, customer_name, items, total) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, orderId);
            stmt.setString(2, customerName);
            stmt.setString(3, items);
            stmt.setDouble(4, total);
            stmt.executeUpdate();
            System.out.println("Order added to database.");
        } catch (SQLException e) {
            System.err.println("Error adding order: " + e.getMessage());
        }
    }
    

    // Method to update an existing order
    public void updateOrder(int orderId, String customerName, String items, double total) {
        String query = "UPDATE orders SET customer_name = ?, items = ?, total = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, customerName);
            stmt.setString(2, items);
            stmt.setDouble(3, total);
            stmt.setInt(4, orderId);
            stmt.executeUpdate();
            System.out.println("Order updated in database.");
        } catch (SQLException e) {
            System.err.println("Error updating order: " + e.getMessage());
        }
    }

    // Method to delete an order from the database
    public void deleteOrder(int orderId) {
        String query = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
            System.out.println("Order deleted from database.");
        } catch (SQLException e) {
            System.err.println("Error deleting order: " + e.getMessage());
        }
    }

    // Close the database connection
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    
}


