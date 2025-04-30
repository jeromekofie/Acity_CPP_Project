import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {
    // CRUD operations for Hotels
    
    public List<Hotel> getAllHotels() throws SQLException {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotels";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                hotels.add(new Hotel(
                    rs.getInt("hotel_id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getDouble("rating")
                ));
            }
        }
        return hotels;
    }
    
    public Hotel getHotelById(int id) throws SQLException {
        String sql = "SELECT * FROM hotels WHERE hotel_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Hotel(
                        rs.getInt("hotel_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getDouble("rating")
                    );
                }
            }
        }
        return null;
    }
    
    public boolean addHotel(Hotel hotel) throws SQLException {
        String sql = "INSERT INTO hotels (name, address, phone, rating) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, hotel.getName());
            pstmt.setString(2, hotel.getAddress());
            pstmt.setString(3, hotel.getPhone());
            pstmt.setDouble(4, hotel.getRating());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean updateHotel(Hotel hotel) throws SQLException {
        String sql = "UPDATE hotels SET name = ?, address = ?, phone = ?, rating = ? WHERE hotel_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, hotel.getName());
            pstmt.setString(2, hotel.getAddress());
            pstmt.setString(3, hotel.getPhone());
            pstmt.setDouble(4, hotel.getRating());
            pstmt.setInt(5, hotel.getId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteHotel(int id) throws SQLException {
        String sql = "DELETE FROM hotels WHERE hotel_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
}