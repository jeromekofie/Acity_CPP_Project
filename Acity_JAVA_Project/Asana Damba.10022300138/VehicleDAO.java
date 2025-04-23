import java.sql.*;
import java.util.ArrayList;

public class VehicleDAO {

    public static boolean insertVehicle(Vehicle v) {
        String sql = "INSERT INTO vehicles (type, model, number_plate, rate_per_day) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, v.getType());
            stmt.setString(2, v.getModel());
            stmt.setString(3, v.getNumberPlate());
            stmt.setDouble(4, v.getRatePerDay());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Vehicle> getAllVehicles() {
        ArrayList<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                String model = rs.getString("model");
                String number = rs.getString("number_plate");
                double rate = rs.getDouble("rate_per_day");

                Vehicle v;
                switch (type) {
                    case "Car": v = new Car(id, model, number, rate); break;
                    case "Bike": v = new Bike(id, model, number, rate); break;
                    case "Truck": v = new Truck(id, model, number, rate); break;
                    default: continue;
                }

                list.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean deleteVehicle(int id) {
        String sql = "DELETE FROM vehicles WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
