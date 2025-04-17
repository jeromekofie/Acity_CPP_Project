import javax.swing.SwingUtilities;

public class LibraryMain {
    public static void main(String[] args) {
        // Initialize database
        DatabaseHelper.initializeDatabase();
        
        // Start with login screen
        SwingUtilities.invokeLater(() -> {
            new SignUpUI().setVisible(true);
        });
    }
}