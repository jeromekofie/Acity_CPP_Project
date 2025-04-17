import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public abstract class Vehicle {
    protected String id;
    protected String type;
    protected String brand;
    protected String model;
    protected double rate;
    protected String color;
    protected int year;
    protected boolean isRented;
    protected static final DecimalFormat RATE_FORMAT = new DecimalFormat("$#,##0.00");

    public Vehicle(String id, String type, String brand, String model, double rate, String color, int year) {
        this.id = id;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.rate = rate;
        this.color = color;
        this.year = year;
        this.isRented = false;
    }

    public void display() {
        JOptionPane.showMessageDialog(null, this.toString(), "Vehicle Details", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public String toString() {
        return String.format("ID: %s, Type: %s, Brand: %s, Model: %s, Year: %d, Color: %s, Rate: %s/day%s",
            id, type, brand, model, year, color, RATE_FORMAT.format(rate), isRented ? " [RENTED]" : "");
    }

    public void saveToDatabase() {
        String sql = """
            INSERT INTO vehicles (id, type, brand, model, rate, color, year, is_rented)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                type = VALUES(type),
                brand = VALUES(brand),
                model = VALUES(model),
                rate = VALUES(rate),
                color = VALUES(color),
                year = VALUES(year),
                is_rented = VALUES(is_rented)
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.setString(2, type);
            pstmt.setString(3, brand);
            pstmt.setString(4, model);
            pstmt.setDouble(5, rate);
            pstmt.setString(6, color);
            pstmt.setInt(7, year);
            pstmt.setBoolean(8, isRented);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error saving vehicle: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static List<Vehicle> loadAllFromDatabase() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT id, type, brand, model, rate, color, year, is_rented FROM vehicles";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String type = rs.getString("type");
                Vehicle vehicle;
                
                if ("CAR".equalsIgnoreCase(type)) {
                    vehicle = new Car(
                        rs.getString("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getDouble("rate"),
                        rs.getString("color"),
                        rs.getInt("year"),
                        rs.getInt("doors"),
                        rs.getString("transmission"),
                        rs.getString("fuel_type")
                    );
                } else {
                    // Default to basic vehicle if type not recognized
                    vehicle = new Car(
                        rs.getString("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getDouble("rate"),
                        rs.getString("color"),
                        rs.getInt("year"),
                        4, "Automatic", "Gasoline"
                    );
                }
                
                vehicle.setIsRented(rs.getBoolean("is_rented"));
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error loading vehicles: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return vehicles;
    }

    public static boolean deleteFromDatabase(String vehicleId) {
        if (isVehicleRented(vehicleId)) {
            JOptionPane.showMessageDialog(null,
                "Cannot delete a vehicle that is currently rented!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        String sql = "DELETE FROM vehicles WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, vehicleId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error deleting vehicle: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private static boolean isVehicleRented(String vehicleId) {
        String sql = "SELECT is_rented FROM vehicles WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, vehicleId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("is_rented");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getType() { return type; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public double getRate() { return rate; }
    public String getColor() { return color; }
    public int getYear() { return year; }
    public boolean getIsRented() { return isRented; }
    public void setIsRented(boolean isRented) { 
        this.isRented = isRented;
        this.saveToDatabase();
    }
}