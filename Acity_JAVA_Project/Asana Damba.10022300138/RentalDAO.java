import java.sql.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class RentalDAO {

    public static boolean rentVehicle(int vehicleId, String customerName, Date rentDate, Date returnDate) {
        double ratePerDay = 0;

        try (Connection conn = DBConnection.getConnection()) {
            // Get vehicle rate
            String rateSql = "SELECT rate_per_day FROM vehicles WHERE id = ?";
            try (PreparedStatement rateStmt = conn.prepareStatement(rateSql)) {
                rateStmt.setInt(1, vehicleId);
                ResultSet rs = rateStmt.executeQuery();
                if (rs.next()) {
                    ratePerDay = rs.getDouble("rate_per_day");
                }
            }

            long days = ChronoUnit.DAYS.between(rentDate.toLocalDate(), returnDate.toLocalDate());
            double totalAmount = ratePerDay * days;

            String insertSql = "INSERT INTO rentals (vehicle_id, customer_name, rent_date, return_date, total_amount) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setInt(1, vehicleId);
                stmt.setString(2, customerName);
                stmt.setDate(3, rentDate);
                stmt.setDate(4, returnDate);
                stmt.setDouble(5, totalAmount);
                return stmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<RentalRecord> getAllRentals() {
        ArrayList<RentalRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM rentals";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                RentalRecord record = new RentalRecord(
                    rs.getInt("id"),
                    rs.getInt("vehicle_id"),
                    rs.getString("customer_name"),
                    rs.getDate("rent_date"),
                    rs.getDate("return_date"),
                    rs.getDouble("total_amount")
                );
                list.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
