import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class Car extends Vehicle {
    private int doors;
    private String transmission;
    private String fuelType;
    
    public Car(String id, String brand, String model, double rate, String color, int year,
               int doors, String transmission, String fuelType) {
        super(id, "CAR", brand, model, rate, color, year);
        this.doors = doors;
        this.transmission = transmission;
        this.fuelType = fuelType;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(", Doors: %d, Transmission: %s, Fuel: %s",
            doors, transmission, fuelType);
    }
    
    @Override
    public void saveToDatabase() {
        String sql = """
            INSERT INTO vehicles 
                (id, type, brand, model, rate, color, year, doors, transmission, fuel_type, is_rented)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                type = VALUES(type),
                brand = VALUES(brand),
                model = VALUES(model),
                rate = VALUES(rate),
                color = VALUES(color),
                year = VALUES(year),
                doors = VALUES(doors),
                transmission = VALUES(transmission),
                fuel_type = VALUES(fuel_type),
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
            pstmt.setInt(8, doors);
            pstmt.setString(9, transmission);
            pstmt.setString(10, fuelType);
            pstmt.setBoolean(11, isRented);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error saving car: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Getters
    public int getDoors() { return doors; }
    public String getTransmission() { return transmission; }
    public String getFuelType() { return fuelType; }
}