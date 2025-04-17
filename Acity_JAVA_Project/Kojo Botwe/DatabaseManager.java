import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/taskmanagementsystem";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "cyrilpp";
    private Connection conn;

    public DatabaseManager() {
        try {
            // Load MariaDB driver
            Class.forName("org.mariadb.jdbc.Driver");

            // Connect to DB with username and password
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            // Table creation removed, since you'll create it manually
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addTask(String description, String deadline, Priority priority) {
        String sql = "INSERT INTO tasks (description, deadline, priority, completed) VALUES (?, ?, ?, 0)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, description);
            pstmt.setString(2, deadline);
            pstmt.setString(3, priority.name());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Task task = new Task(
                    rs.getInt("id"),
                    rs.getString("description"),
                    rs.getString("deadline"),
                    Priority.valueOf(rs.getString("priority")),
                    rs.getBoolean("completed")
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    

    public void markTaskAsCompleted(int id) {
        String sql = "UPDATE tasks SET completed = 1 WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {  // Use the class-level conn instance
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
}
