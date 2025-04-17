import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;
import java.sql.*;

public class SignupWindow extends JFrame {
    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    
    private static final Color TEXT_COLOR = new Color(30, 30, 30);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color TRANSLUCENT_WHITE = new Color(255, 255, 255, 220);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static ImageIcon backgroundImage;
    private static ImageIcon logoImage;

    static {
        try {
            backgroundImage = new ImageIcon("bg1.jpg");
            logoImage = new ImageIcon("images.png");
            Image img = logoImage.getImage().getScaledInstance(200, 40, Image.SCALE_SMOOTH);
            logoImage = new ImageIcon(img);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Image not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public SignupWindow() {
        setTitle("Employee Management System - Sign Up");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create background panel with image
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Add logo to top right corner
        if (logoImage != null) {
            JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            logoPanel.setOpaque(false);
            logoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 15));
            logoPanel.add(new JLabel(logoImage));
            backgroundPanel.add(logoPanel, BorderLayout.NORTH);
        }

        // Create main content panel with reduced vertical space
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(TRANSLUCENT_WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 35, 20, 35));
        mainPanel.setMaximumSize(new Dimension(350, 450));

        // Title with smaller top margin
        JLabel titleLabel = new JLabel("CREATE ACCOUNT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Form fields with tighter spacing
        mainPanel.add(createFormField("Full Name:", fullNameField = createMediumTextField()));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(createFormField("Username:", usernameField = createMediumTextField()));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(createFormField("Email:", emailField = createMediumTextField()));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(createFormField("Password:", passwordField = createMediumPasswordField()));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(createFormField("Confirm Password:", confirmPasswordField = createMediumPasswordField()));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Sign Up button with less bottom margin
        JButton signupButton = new JButton("Sign Up");
        signupButton.setFont(new Font("Arial", Font.BOLD, 16));
        signupButton.setBackground(BUTTON_COLOR);
        signupButton.setForeground(Color.WHITE);
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.setPreferredSize(new Dimension(180, 40));
        signupButton.setMaximumSize(new Dimension(180, 40));
        signupButton.addActionListener(e -> signup());
        mainPanel.add(signupButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Login link - now definitely visible
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginPanel.setOpaque(false);
        JLabel loginLabel = new JLabel("Already have an account?");
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setForeground(Color.BLUE.darker());
        loginButton.addActionListener(e -> {
            dispose();
            new LoginWindow().setVisible(true);
        });
        loginPanel.add(loginLabel);
        loginPanel.add(loginButton);
        loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(loginPanel);

        // Center the main panel higher up
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.2; // Push content upward
        centerWrapper.add(Box.createGlue(), gbc);
        
        gbc.gridy = 1;
        gbc.weighty = 0;
        centerWrapper.add(mainPanel, gbc);
        
        gbc.gridy = 2;
        gbc.weighty = 0.8; // Allow space at bottom
        centerWrapper.add(Box.createGlue(), gbc);
        
        backgroundPanel.add(centerWrapper, BorderLayout.CENTER);
    }

    static class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                Image img = backgroundImage.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(new Color(240, 240, 240));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    private JTextField createMediumTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(300, 35));
        field.setPreferredSize(new Dimension(300, 35));
        field.setMargin(new Insets(8, 12, 8, 12));
        return field;
    }

    private JPasswordField createMediumPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(300, 35));
        field.setPreferredSize(new Dimension(300, 35));
        field.setMargin(new Insets(8, 12, 8, 12));
        return field;
    }

    private JPanel createFormField(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(TRANSLUCENT_WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(field);

        return panel;
    }

    private void signup() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
                                            
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (userExists(conn, username, email)) {
                JOptionPane.showMessageDialog(this, "Username or email already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "INSERT INTO users (username, password, full_name, email) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, fullName);
                stmt.setString(4, email);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginWindow().setVisible(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error creating account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean userExists(Connection conn, String username, String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ? OR email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}