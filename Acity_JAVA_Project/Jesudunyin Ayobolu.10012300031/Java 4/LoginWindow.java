import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private static final Color TEXT_COLOR = new Color(30, 30, 30);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color TRANSLUCENT_WHITE = new Color(255, 255, 255, 220);
    private static final Color TITLE_COLOR = new Color(0, 82, 147); // Aesthetic blue color

    public LoginWindow() {
        initializeWindow();
        setupUI();
    }

    private void initializeWindow() {
        setTitle("Employee Management System - Login");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void setupUI() {
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        JPanel mainPanel = createMainPanel();
        JPanel wrapperPanel = createWrapperPanel(mainPanel);
        
        backgroundPanel.add(wrapperPanel);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setBackground(TRANSLUCENT_WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        // Logo and Title Panel
        panel.add(createLogoTitlePanel(), gbc);

        // Username Field
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(createLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        usernameField = createTextField();
        panel.add(usernameField, gbc);

        // Password Field
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(createLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        passwordField = createPasswordField();
        panel.add(passwordField, gbc);

        // Login Button
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createLoginButton(), gbc);

        // Signup Link
        gbc.gridy++;
        panel.add(createSignupLink(), gbc);

        return panel;
    }

    private JPanel createLogoTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setOpaque(false);
        
        // Load and resize the logo image
        ImageIcon originalIcon = new ImageIcon("images.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(300, 60, Image.SCALE_SMOOTH);
        ImageIcon logoIcon = new ImageIcon(scaledImage);
        
        JLabel logoLabel = new JLabel(logoIcon);
        JLabel titleLabel = new JLabel("EMPLOYEE\n MANAGEMENT\n SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 10));
        titleLabel.setForeground(TITLE_COLOR);
        
        panel.add(logoLabel);
        panel.add(titleLabel);
        
        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(400, 45));
        field.setMargin(new Insets(10, 15, 10, 15));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(400, 45));
        field.setMargin(new Insets(10, 15, 10, 15));
        return field;
    }

    private JButton createLoginButton() {
        JButton button = new JButton("Login");
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(220, 50));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> authenticate());
        return button;
    }

    private JPanel createSignupLink() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setOpaque(false);
        
        JLabel prompt = new JLabel("Don't have an account?");
        prompt.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JButton signupBtn = new JButton("Sign up");
        signupBtn.setFont(new Font("Arial", Font.BOLD, 16));
        signupBtn.setForeground(Color.BLUE.darker());
        signupBtn.setContentAreaFilled(false);
        signupBtn.setBorderPainted(false);
        signupBtn.addActionListener(e -> {
            dispose();
            new SignupWindow().setVisible(true);
        });
        
        panel.add(prompt);
        panel.add(signupBtn);
        return panel;
    }

    private JPanel createWrapperPanel(JPanel mainPanel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.add(mainPanel, BorderLayout.CENTER);
        return panel;
    }

    private void authenticate() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both username and password", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT * FROM users WHERE username = ? AND password = ?")) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    dispose();
                    new MainWindow().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid username or password", 
                        "Login Failed", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Database error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}