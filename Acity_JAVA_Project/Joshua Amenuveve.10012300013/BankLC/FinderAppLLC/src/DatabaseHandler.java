import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private final String URL = "jdbc:mysql://localhost:3306/studentdb";
    private final String USER = "root"; // or your actual MySQL username
    private final String PASSWORD = "";

    // Method for searching with optional case sensitivity
    public ArrayList<String> searchDatabase(String keyword, boolean caseSensitive) {
        ArrayList<String> results = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Statement stmt = conn.createStatement();
            String query;

            if (caseSensitive) {
                query = "SELECT * FROM students WHERE BINARY name LIKE '%" + keyword + "%'";
            } else {
                query = "SELECT * FROM students WHERE LOWER(name) LIKE LOWER('%" + keyword + "%')";
            }

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String studentInfo = "ID: " + rs.getInt("id") +
                                     ", Name: " + rs.getString("name") +
                                     ", Address: " + rs.getString("address");
                results.add(studentInfo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    // Simplified version of searchWord using searchDatabase
    public List<String> searchWord(String keyword) {
        return searchDatabase(keyword, false);  // case insensitive search
    }
}