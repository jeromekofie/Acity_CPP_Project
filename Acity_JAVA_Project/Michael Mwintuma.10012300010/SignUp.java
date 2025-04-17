package bankaccountmanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.util.Random;

public class SignUp extends JFrame {
    JTextField usernameField, emailField, balanceField;
    JPasswordField passwordField;
    JButton signUpButton, signInButton;

    public SignUp() {
        setTitle("Create Your Account");
        setSize(400, 600);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 400, 600, 40, 40));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Create Your Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setFocusable(false);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));

        // Input Fields
        usernameField = createInputField();
        emailField = createInputField();
        passwordField = new JPasswordField();
        customizeField(passwordField);
        balanceField = createInputField();

        mainPanel.add(createLabeledField("Full Name", usernameField));
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createLabeledField("Email", emailField));
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createLabeledField("Password", passwordField));
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createLabeledField("Initial Balance", balanceField));
        mainPanel.add(Box.createVerticalStrut(25));

        // Buttons
        signUpButton = new JButton("SIGN UP");
        signInButton = new JButton("Already have an account? Login");

        styleButton(signUpButton, true);
        styleButton(signInButton, false);

        signUpButton.addActionListener(e -> signUp());
        signInButton.addActionListener(e -> {
            new Login();
            this.dispose();
        });

        mainPanel.add(signUpButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(signInButton);

        add(mainPanel);
        setVisible(true);
    }

    private JTextField createInputField() {
        JTextField field = new JTextField();
        customizeField(field);
        return field;
    }

    private void customizeField(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setMargin(new Insets(5, 10, 5, 10));
        field.setForeground(Color.DARK_GRAY);
        field.setBackground(Color.WHITE);
        field.setCaretColor(Color.BLACK);
        field.setSelectionColor(new Color(255, 255, 255)); // Invisible highlight
        field.setSelectedTextColor(Color.BLACK);
        field.setHighlighter(null); // Prevents selection flicker
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JPanel createLabeledField(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setFocusable(false); // Prevent click highlight

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        return panel;
    }

    private void styleButton(JButton button, boolean isPrimary) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setBackground(isPrimary ? new Color(220, 20, 60) : new Color(255, 255, 255, 80));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            // Gradient background
            GradientPaint gp = new GradientPaint(0, 0, new Color(102, 0, 102), 0, getHeight(), new Color(204, 0, 0));
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Diagonal "Michaelson" watermark (3 times)
            g2d.setFont(new Font("Arial", Font.BOLD, 60));
            g2d.setColor(new Color(255, 255, 255, 30));
            AffineTransform originalTransform = g2d.getTransform();
            g2d.rotate(-Math.PI / 4, getWidth() / 2.0, getHeight() / 2.0);

            int spacing = 200;
            for (int i = -1; i <= 1; i++) {
                g2d.drawString("Michaelson", getWidth() / 2 - 150, getHeight() / 2 + (i * spacing));
            }

            g2d.setTransform(originalTransform);
            g2d.dispose();
        }
    }

    String generateUniqueAccountNumber(Connection conn) throws SQLException {
        Random rand = new Random();
        String accountNumber;
        boolean isUnique;

        do {
            accountNumber = "AC" + (100000 + rand.nextInt(900000));
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE account_number = ?");
            checkStmt.setString(1, accountNumber);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            isUnique = rs.getInt(1) == 0;
        } while (!isUnique);

        return accountNumber;
    }

    void signUp() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        double balance = Double.parseDouble(balanceField.getText());

        try (Connection conn = DatabaseConnection.getConnection()) {
            String accountNumber = generateUniqueAccountNumber(conn);

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, email, password, balance, account_number) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setDouble(4, balance);
            stmt.setString(5, accountNumber);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Account Created! Please login.");
            new Login();
            this.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
