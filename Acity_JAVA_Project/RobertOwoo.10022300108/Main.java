import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));

       
        // Add this test to your main method
        try {
            Connection testConn = DatabaseConnection.getConnection();
            System.out.println("Database connection successful!");
            testConn.close();
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
}