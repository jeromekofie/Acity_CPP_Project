import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    // CRUD operations for Orders
    
    public int createOrder(int hotelId, String customerName, List<OrderItem> items) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmtOrder = null;
        PreparedStatement pstmtDetails = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // 1. Insert order header
            String sqlOrder = "INSERT INTO orders (hotel_id, customer_name, order_date, total_amount) VALUES (?, ?, NOW(), ?)";
            pstmtOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            
            // Calculate total amount
            double total = items.stream().mapToDouble(OrderItem::getSubtotal).sum();
            
            pstmtOrder.setInt(1, hotelId);
            pstmtOrder.setString(2, customerName);
            pstmtOrder.setDouble(3, total);
            pstmtOrder.executeUpdate();
            
            // Get generated order ID
            rs = pstmtOrder.getGeneratedKeys();
            int orderId = -1;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }
            
            if (orderId == -1) {
                throw new SQLException("Failed to get order ID");
            }
            
            // 2. Insert order details
            String sqlDetails = "INSERT INTO order_details (order_id, item_id, quantity, price) VALUES (?, ?, ?, ?)";
            pstmtDetails = conn.prepareStatement(sqlDetails);
            
            for (OrderItem item : items) {
                pstmtDetails.setInt(1, orderId);
                pstmtDetails.setInt(2, item.getMenuItemId());
                pstmtDetails.setInt(3, item.getQuantity());
                pstmtDetails.setDouble(4, item.getPrice());
                pstmtDetails.addBatch();
            }
            
            pstmtDetails.executeBatch();
            
            conn.commit(); // Commit transaction
            return orderId;
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                    System.err.println("Error during rollback: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (pstmtOrder != null) pstmtOrder.close();
            if (pstmtDetails != null) pstmtDetails.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    public List<Order> getOrdersByHotel(int hotelId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE hotel_id = ? ORDER BY order_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hotelId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("hotel_id"),
                        rs.getString("customer_name"),
                        rs.getTimestamp("order_date"),
                        rs.getDouble("total_amount"),
                        rs.getString("status"),
                        getOrderItems(rs.getInt("order_id"))
                    );
                    orders.add(order);
                }
            }
        }
        return orders;
    }
    
    private List<OrderItem> getOrderItems(int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT od.*, mi.name FROM order_details od " + "JOIN menu_items mi ON od.item_id = mi.item_id " +
                     "WHERE od.order_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(new OrderItem(
                        rs.getInt("item_id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                    ));
                }
            }
        }
        return items;
    }
    
    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean cancelOrder(int orderId) throws SQLException {
        return updateOrderStatus(orderId, "Cancelled");
    }
}