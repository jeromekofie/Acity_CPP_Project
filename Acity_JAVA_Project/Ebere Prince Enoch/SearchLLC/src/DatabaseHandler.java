import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    // Update these as per your MySQL configuration
    private final String URL = "jdbc:mysql://localhost:3306/studentdb"; // Make sure this DB exists
    private final String USER = "root"; // Your MySQL username
    private final String PASSWORD = ""; // Your MySQL password (if any)

    /**
     * Saves or updates the keyword count in the database.
     * Assumes a table named `keywords` exists:
     * 
     * CREATE TABLE keywords (
     *     word VARCHAR(255) PRIMARY KEY,
     *     count INT NOT NULL
     * );
     */
    public void saveKeyword(String word, int count) {
        String query = "INSERT INTO keywords (word, count) VALUES (?, ?) " +
                       "ON DUPLICATE KEY UPDATE count = count + ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, word);
            stmt.setInt(2, count);
            stmt.setInt(3, count);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while saving keyword to database:");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
    }

    /**
     * Returns keyword search history from the database.
     * If the input is an empty string, all keyword records will be shown.
     *
     * @param keyword A keyword to filter history (case-insensitive).
     * @return A list of formatted keyword history strings.
     */
    public List<String> searchKeywordHistory(String keyword) {
        ArrayList<String> results = new ArrayList<>();

        String query = "SELECT * FROM keywords WHERE LOWER(word) LIKE LOWER(?) ORDER BY count DESC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + keyword + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String row = "Keyword: " + rs.getString("word") +
                             ", Count: " + rs.getInt("count");
                results.add(row);
            }

        } catch (SQLException e) {
            System.err.println("Error while fetching keyword history:");
            e.printStackTrace();
        }

        return results;
    }
}
