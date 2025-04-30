import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {
    // CRUD operations for Menu Items
    
    public List<MenuItem> getMenuByHotel(int hotelId) throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM menu_items WHERE hotel_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hotelId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    menuItems.add(new MenuItem(
                        rs.getInt("item_id"),
                        rs.getInt("hotel_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("category")
                    ));
                }
            }
        }
        return menuItems;
    }
    
    public MenuItem getMenuItemById(int id) throws SQLException {
        String sql = "SELECT * FROM menu_items WHERE item_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new MenuItem(
                        rs.getInt("item_id"),
                        rs.getInt("hotel_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("category")
                    );
                }
            }
        }
        return null;
    }
    
    public boolean addMenuItem(MenuItem item) throws SQLException {
        String sql = "INSERT INTO menu_items (hotel_id, name, description, price, category) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, item.getHotelId());
            pstmt.setString(2, item.getName());
            pstmt.setString(3, item.getDescription());
            pstmt.setDouble(4, item.getPrice());
            pstmt.setString(5, item.getCategory());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean updateMenuItem(MenuItem item) throws SQLException {
        String sql = "UPDATE menu_items SET name = ?, description = ?, price = ?, category = ? WHERE item_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getDescription());
            pstmt.setDouble(3, item.getPrice());
            pstmt.setString(4, item.getCategory());
            pstmt.setInt(5, item.getId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteMenuItem(int id) throws SQLException {
        String sql = "DELETE FROM menu_items WHERE item_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
}