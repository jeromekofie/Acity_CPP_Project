import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    // CRUD operations for Reservations
    
    public int createReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservations (hotel_id, customer_name, check_in_date, check_out_date, room_type) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, reservation.getHotelId());
            pstmt.setString(2, reservation.getCustomerName());
            pstmt.setDate(3, reservation.getCheckInDate());
            pstmt.setDate(4, reservation.getCheckOutDate());
            pstmt.setString(5, reservation.getRoomType());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating reservation failed, no rows affected.");
            }
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating reservation failed, no ID obtained.");
                }
            }
        }
    }
    
    public List<Reservation> getReservationsByHotel(int hotelId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE hotel_id = ? ORDER BY check_in_date";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hotelId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(new Reservation(
                        rs.getInt("reservation_id"),
                        rs.getInt("hotel_id"),
                        rs.getString("customer_name"),
                        rs.getDate("check_in_date"),
                        rs.getDate("check_out_date"),
                        rs.getString("room_type"),
                        rs.getString("status")
                    ));
                }
            }
        }
        return reservations;
    }
    
    public boolean updateReservation(Reservation reservation) throws SQLException {
        String sql = "UPDATE reservations SET customer_name = ?, check_in_date = ?, " +
                     "check_out_date = ?, room_type = ?, status = ? WHERE reservation_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, reservation.getCustomerName());
            pstmt.setDate(2, reservation.getCheckInDate());
            pstmt.setDate(3, reservation.getCheckOutDate());
            pstmt.setString(4, reservation.getRoomType());
            pstmt.setString(5, reservation.getStatus());
            pstmt.setInt(6, reservation.getId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean cancelReservation(int reservationId) throws SQLException {
        String sql = "UPDATE reservations SET status = 'Cancelled' WHERE reservation_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reservationId);
            return pstmt.executeUpdate() > 0;
        }
    }
}