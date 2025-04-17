import java.sql.*;

public class DBConnect {
    private Connection connection;

    public DBConnect() {
        String url = "jdbc:mysql://localhost:3306/report_cards";
        String username = "root";
        String password = "grizi777";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Connection to database established successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Failed to connect to database.");
            e.printStackTrace();
        }
    }

    public void insertData(String name, int rollnum, float average, String course, String remarks) {
        if (connection == null) return;

        String query = "INSERT INTO report_card (name, rollnum, average, course, remarks) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, rollnum);
            stmt.setFloat(3, average);
            stmt.setString(4, course);
            stmt.setString(5, remarks);
            stmt.executeUpdate();
            System.out.println("✅ Data inserted into report_card table.");
        } catch (SQLException e) {
            System.err.println("❌ Failed to insert data.");
            e.printStackTrace();
        }
    }

    public ResultSet fetchAllRecords() {
        if (connection == null) return null;
        String query = "SELECT * FROM report_card";
        try {
            return connection.createStatement().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteRecordByRollNum(int rollnum) {
        if (connection == null) return;

        String query = "DELETE FROM report_card WHERE rollnum = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, rollnum);
            stmt.executeUpdate();
            System.out.println("✅ Record deleted successfully.");
        } catch (SQLException e) {
            System.err.println("❌ Failed to delete record.");
            e.printStackTrace();
        }
    }

    public void updateRecord(int newRollnum, String name, String course, String remarks) {
        if (connection == null) return;

        // Update query now updates the roll number, keeping the average, course, and remarks same
        String query = "UPDATE report_card SET name = ?, course = ?, remarks = ? WHERE rollnum = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, course);
            stmt.setString(3, remarks);
            stmt.setInt(4, newRollnum);  // Updating the ID
            stmt.executeUpdate();
            System.out.println("✅ Record updated successfully.");
        } catch (SQLException e) {
            System.err.println("❌ Failed to update record.");
            e.printStackTrace();
        }
    }
}
