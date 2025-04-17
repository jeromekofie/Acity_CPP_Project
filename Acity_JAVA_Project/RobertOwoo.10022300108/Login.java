import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public Login() {
        setTitle("Employee Management System - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Set modern font
        Font font = new Font("Segoe UI", Font.PLAIN, 16);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Login to Continue", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(40, 40, 100));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(font);
        usernameField = new JTextField();
        usernameField.setFont(font);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(font);
        passwordField = new JPasswordField();
        passwordField.setFont(font);

        formPanel.add(userLabel);
        formPanel.add(usernameField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(font);
        loginButton.setBackground(new Color(0, 120, 215));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        loginButton.addActionListener(this::performLogin);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loginButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Add KeyListener to trigger login on Enter key press
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin(null);
                }
            }
        });
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin(null);
                }
            }
        });
    }

    private void performLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Basic validation for non-empty fields
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Successful login
                SwingUtilities.invokeLater(() -> {
                    new Employeee().setVisible(true); // Show Employeee window
                    dispose(); // Close the login window
                });
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Run the login screen
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
