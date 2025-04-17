import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Customer {
    private String id;
    private String name;
    private String licenseNumber;
    private String phone;
    private String email;
    private LocalDate registrationDate;
    private int loyaltyPoints;
    
    public Customer(String id, String name, String licenseNumber, String phone, String email) {
        this.id = id;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.phone = phone;
        this.email = email;
        this.registrationDate = LocalDate.now();
        this.loyaltyPoints = 0;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, License: %s, Phone: %s, Email: %s, Member since: %s, Points: %d",
            id, name, licenseNumber, phone, email, registrationDate.toString(), loyaltyPoints);
    }
    
    public void saveToDatabase() {
        String sql = """
            INSERT INTO customers 
                (id, name, license_number, phone, email, registration_date, loyalty_points)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                name = VALUES(name),
                license_number = VALUES(license_number),
                phone = VALUES(phone),
                email = VALUES(email),
                loyalty_points = VALUES(loyalty_points)
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, licenseNumber);
            pstmt.setString(4, phone);
            pstmt.setString(5, email);
            pstmt.setDate(6, Date.valueOf(registrationDate));
            pstmt.setInt(7, loyaltyPoints);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error saving customer: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static List<Customer> loadAllFromDatabase() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT id, name, license_number, phone, email, registration_date, loyalty_points FROM customers ORDER BY name";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("license_number"),
                    rs.getString("phone"),
                    rs.getString("email")
                );
                customer.setRegistrationDate(rs.getDate("registration_date").toLocalDate());
                customer.setLoyaltyPoints(rs.getInt("loyalty_points"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error loading customers: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return customers;
    }
    
    public static boolean deleteFromDatabase(String customerId) {
        if (hasActiveRentals(customerId)) {
            JOptionPane.showMessageDialog(null,
                "Cannot delete customer with active rentals!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        String sql = "DELETE FROM customers WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error deleting customer: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private static boolean hasActiveRentals(String customerId) {
        String sql = "SELECT COUNT(*) FROM rentals WHERE customer_id = ? AND end_date IS NULL";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static Customer findById(String customerId) {
        String sql = "SELECT id, name, license_number, phone, email, registration_date, loyalty_points FROM customers WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Customer customer = new Customer(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("license_number"),
                    rs.getString("phone"),
                    rs.getString("email")
                );
                customer.setRegistrationDate(rs.getDate("registration_date").toLocalDate());
                customer.setLoyaltyPoints(rs.getInt("loyalty_points"));
                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
        this.saveToDatabase();
    }
    
    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getLicenseNumber() { return licenseNumber; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public int getLoyaltyPoints() { return loyaltyPoints; }
    
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }
}