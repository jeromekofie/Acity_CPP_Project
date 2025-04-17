import java.sql.*;
import java.util.*;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/health_profile";
    private static final String USER = "ridge_user";
    private static final String PASSWORD = "ridge_pass";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to MySQL Database!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("❌ Failed to connect to the database.");
            e.printStackTrace();
        }
        return conn;
    }

    // Get all profiles from the database
    public static List<HealthProfile> getAllProfiles() {
        List<HealthProfile> profiles = new ArrayList<>();
        String query = "SELECT * FROM profiles";
        
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double weight = rs.getDouble("weight");
                double height = rs.getDouble("height");
                double bodyTemperature = rs.getDouble("body_temperature");
                double bloodSugar = rs.getDouble("blood_sugar");
                int systolicBP = rs.getInt("systolic_bp");
                int diastolicBP = rs.getInt("diastolic_bp");

                HealthProfile profile = new HealthProfile(id, name, weight, height, bodyTemperature, bloodSugar, systolicBP, diastolicBP);
                profiles.add(profile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profiles;
    }

    // Create a new profile in the database
    public static int createProfile(HealthProfile profile) {
        String query = "INSERT INTO profiles (name, weight, height, body_temperature, blood_sugar, systolic_bp, diastolic_bp) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, profile.name);
            stmt.setDouble(2, profile.weight);
            stmt.setDouble(3, profile.height);
            stmt.setDouble(4, profile.bodyTemperature);
            stmt.setDouble(5, profile.bloodSugar);
            stmt.setInt(6, profile.systolicBP);
            stmt.setInt(7, profile.diastolicBP);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);  // Return the generated ID
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Delete a profile by its ID
    public static boolean deleteProfile(int id) {
        String query = "DELETE FROM profiles WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get a profile by its ID
    public static HealthProfile getProfileById(int id) {
        String query = "SELECT * FROM profiles WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                double weight = rs.getDouble("weight");
                double height = rs.getDouble("height");
                double bodyTemperature = rs.getDouble("body_temperature");
                double bloodSugar = rs.getDouble("blood_sugar");
                int systolicBP = rs.getInt("systolic_bp");
                int diastolicBP = rs.getInt("diastolic_bp");

                return new HealthProfile(id, name, weight, height, bodyTemperature, bloodSugar, systolicBP, diastolicBP);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update an existing profile in the database
    public static boolean updateProfile(HealthProfile profile) {
        String query = "UPDATE profiles SET name = ?, weight = ?, height = ?, body_temperature = ?, blood_sugar = ?, systolic_bp = ?, diastolic_bp = ? WHERE id = ?";
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, profile.name);
            stmt.setDouble(2, profile.weight);
            stmt.setDouble(3, profile.height);
            stmt.setDouble(4, profile.bodyTemperature);
            stmt.setDouble(5, profile.bloodSugar);
            stmt.setInt(6, profile.systolicBP);
            stmt.setInt(7, profile.diastolicBP);
            stmt.setInt(8, profile.id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Initialize the database (e.g., create tables, etc.)
    public static void initializeDatabase() {
        String query = "CREATE TABLE IF NOT EXISTS profiles (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "weight DOUBLE, " +
                "height DOUBLE, " +
                "body_temperature DOUBLE, " +
                "blood_sugar DOUBLE, " +
                "systolic_bp INT, " +
                "diastolic_bp INT)";
        
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
            System.out.println("✅ Database initialized.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
