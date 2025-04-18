import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/zanettacars"; // Ensure the schema is correct
    private static final String DB_USER = "root"; // Update with your MySQL username
    private static final String DB_PASSWORD = "p@rty123Z"; // Update with your MySQL password

    public static Connection getConnection() {
        Connection connection = null;

        try {
            // Attempt to connect to the database
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connection to database established successfully.");
        } catch (SQLException e) {
            System.out.println("Error while connecting to the database.");
            e.printStackTrace();
        }

        return connection;
    }
}
