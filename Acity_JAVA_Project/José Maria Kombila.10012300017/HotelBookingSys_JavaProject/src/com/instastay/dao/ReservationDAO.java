/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.instastay.dao;
import com.instastay.model.Customer;
import com.instastay.model.Room;
import com.instastay.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */     //Database OPeration
public class ReservationDAO {
    

    // Add a new reservation (Customer + Room)
    public void addReservation(Customer customer, Room room) throws SQLException {
        Connection conn = null;
        PreparedStatement customerStmt = null;
        PreparedStatement roomStmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Insert into customer table
            String customerSQL = "INSERT INTO customer (name, gender, contact) VALUES (?, ?, ?)";
            customerStmt = conn.prepareStatement(customerSQL, Statement.RETURN_GENERATED_KEYS);
            customerStmt.setString(1, customer.getName());
            customerStmt.setString(2, customer.getGender());
            customerStmt.setString(3, customer.getContact());
            customerStmt.executeUpdate();

            // Get the generated customer_id
            generatedKeys = customerStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int customerId = generatedKeys.getInt(1);
                customer.setCustomerId(customerId);
                room.setCustomerId(customerId);
            }

            // Insert into room table
            String roomSQL = "INSERT INTO room (customer_id, room_number, room_type, check_in, check_out, total_cost) VALUES (?, ?, ?, ?, ?, ?)";
            roomStmt = conn.prepareStatement(roomSQL);
            roomStmt.setInt(1, room.getCustomerId());
            roomStmt.setString(2, room.getRoomNumber());
            roomStmt.setString(3, room.getRoomType());
            roomStmt.setDate(4, Date.valueOf(room.getCheckIn()));
            roomStmt.setDate(5, Date.valueOf(room.getCheckOut()));
            roomStmt.setDouble(6, room.getTotalCost());
            roomStmt.executeUpdate();

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                }
            }
            throw e;
        } finally {
            if (generatedKeys != null) generatedKeys.close();
            if (customerStmt != null) customerStmt.close();
            if (roomStmt != null) roomStmt.close();
            if (conn != null) conn.close();
        }
    }

    // Get all reservations (combines Customer and Room data)
    public List<Object[]> getAllReservations() throws SQLException {
        List<Object[]> reservations = new ArrayList<>();
        String sql = "SELECT c.name, c.gender, c.contact, r.room_number, r.room_type, r.check_in, r.check_out, r.total_cost " +
                     "FROM customer c JOIN room r ON c.customer_id = r.customer_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = new Object[8];
                row[0] = rs.getString("name");
                row[1] = rs.getString("gender");
                row[2] = rs.getString("contact");
                row[3] = rs.getString("room_number");
                row[4] = rs.getString("room_type");
                row[5] = rs.getDate("check_in").toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                row[6] = rs.getDate("check_out").toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                row[7] = "GH₵" + String.format("%.2f", rs.getDouble("total_cost"));
                reservations.add(row);
            }
        }
        return reservations;
    }

    // Delete a reservation by room number
    public void deleteReservation(String roomNumber) throws SQLException {
        String sql = "DELETE FROM room WHERE room_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roomNumber);
            stmt.executeUpdate();
        }
    }

    // Update a reservation
    public void updateReservation(Customer customer, Room room) throws SQLException {
        Connection conn = null;
        PreparedStatement customerStmt = null;
        PreparedStatement roomStmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Update customer table
            String customerSQL = "UPDATE customer SET name = ?, gender = ?, contact = ? WHERE customer_id = ?";
            customerStmt = conn.prepareStatement(customerSQL);
            customerStmt.setString(1, customer.getName());
            customerStmt.setString(2, customer.getGender());
            customerStmt.setString(3, customer.getContact());
            customerStmt.setInt(4, customer.getCustomerId());
            customerStmt.executeUpdate();

            // Update room table
            String roomSQL = "UPDATE room SET room_number = ?, room_type = ?, check_in = ?, check_out = ?, total_cost = ? WHERE customer_id = ?";
            roomStmt = conn.prepareStatement(roomSQL);
            roomStmt.setString(1, room.getRoomNumber());
            roomStmt.setString(2, room.getRoomType());
            roomStmt.setDate(3, Date.valueOf(room.getCheckIn()));
            roomStmt.setDate(4, Date.valueOf(room.getCheckOut()));
            roomStmt.setDouble(5, room.getTotalCost());
            roomStmt.setInt(6, room.getCustomerId());
            roomStmt.executeUpdate();

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                }
            }
            throw e;
        } finally {
            if (customerStmt != null) customerStmt.close();
            if (roomStmt != null) roomStmt.close();
            if (conn != null) conn.close();
        }
    }
}

