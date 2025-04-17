import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane; // Add this import statement

public class Rental {
    private String customerId;
    private String vehicleId;
    private Date startDate;
    private Date endDate;
    private Double totalCost;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final double LATE_FEE_PERCENTAGE = 1.2; // 20% late fee

    public Rental(String customerId, String vehicleId, Date startDate) {
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.startDate = startDate;
    }
    
    @Override
    public String toString() {
        Customer customer = Customer.findById(customerId);
        String customerName = customer != null ? customer.getName() : "Unknown";
        
        return String.format("Customer: %s (ID: %s), Vehicle ID: %s, Start Date: %s%s%s",
            customerName, customerId, vehicleId, DATE_FORMAT.format(startDate),
            endDate != null ? ", End Date: " + DATE_FORMAT.format(endDate) : "",
            totalCost != null ? ", Total Cost: $" + String.format("%.2f", totalCost) : "");
    }
    
    public void saveToDatabase() {
        String sql = """
            INSERT INTO rentals 
                (customer_id, vehicle_id, start_date, end_date, total_cost)
            VALUES (?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, customerId);
            pstmt.setString(2, vehicleId);
            pstmt.setLong(3, startDate.getTime());
            pstmt.setObject(4, endDate != null ? endDate.getTime() : null);
            pstmt.setObject(5, totalCost);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error saving rental: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static List<Rental> loadAllFromDatabase() {
        List<Rental> rentals = new ArrayList<>();
        String sql = """
            SELECT customer_id, vehicle_id, start_date, end_date, total_cost 
            FROM rentals 
            ORDER BY start_date DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Rental rental = new Rental(
                    rs.getString("customer_id"),
                    rs.getString("vehicle_id"),
                    new Date(rs.getLong("start_date"))
                );
                if (rs.getObject("end_date") != null) {
                    rental.setEndDate(new Date(rs.getLong("end_date")));
                }
                if (rs.getObject("total_cost") != null) {
                    rental.setTotalCost(rs.getDouble("total_cost"));
                }
                rentals.add(rental);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error loading rentals: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return rentals;
    }

    public double calculateCost(double dailyRate) {
        Date actualEndDate = endDate != null ? endDate : new Date();
        long milliseconds = actualEndDate.getTime() - startDate.getTime();
        double daysRented = milliseconds / (1000.0 * 60 * 60 * 24);
        
        // Minimum 1 day charge
        daysRented = Math.max(1, Math.ceil(daysRented));
        
        // Apply late fee if returned after estimated date
        if (endDate == null) {
            daysRented *= LATE_FEE_PERCENTAGE;
        }
        
        totalCost = daysRented * dailyRate;
        return totalCost;
    }

    public void returnVehicle() {
        this.endDate = new Date();
        this.calculateCost(getVehicleRate());
        
        // Add loyalty points (1 point per $10 spent)
        Customer customer = Customer.findById(customerId);
        if (customer != null && totalCost != null) {
            int points = (int) (totalCost / 10);
            customer.addLoyaltyPoints(points);
        }
        
        this.saveToDatabase();
    }

    private double getVehicleRate() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT rate FROM vehicles WHERE id = ?")) {
            pstmt.setString(1, vehicleId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("rate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // Getters and setters
    public String getCustomerId() { return customerId; }
    public String getVehicleId() { return vehicleId; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }
}