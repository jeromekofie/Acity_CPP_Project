package bankaccountmanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;
    GradientPanel background;

    public Login() {
        setTitle("Login");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set custom background
        background = new GradientPanel();
        background.setLayout(new GridBagLayout());

        // Timer to cycle through gradient themes every 5 seconds
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                background.nextTheme();
                background.repaint();
            }
        });
        timer.start();

        // Login form panel
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 250));
        panel.setBackground(new Color(255, 255, 255, 180));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        // Show Password checkbox
        JCheckBox showPassword = new JCheckBox("Show password");
        showPassword.setOpaque(false);
        showPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        JCheckBox rememberMe = new JCheckBox("Remember me");
        rememberMe.setOpaque(false);
        rememberMe.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginButton = new JButton("LOGIN");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setFocusPainted(false);
        loginButton.setBackground(new Color(255, 128, 128));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));

        loginButton.addActionListener(e -> login());

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(showPassword); // Show Password checkbox
        panel.add(Box.createVerticalStrut(10));
        panel.add(rememberMe);
        panel.add(Box.createVerticalStrut(10));
        panel.add(loginButton);

        background.add(panel);
        setContentPane(background);
        setVisible(true);
    }

    void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getDouble("balance"),
                        rs.getString("account_number")
                );
                new Dashboard(user);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}

// Custom JPanel with rotating gradient themes and 3 diagonal watermarks
class GradientPanel extends JPanel {
    private int themeIndex = 0;
    private final Color[][] themes = new Color[][] {
        {new Color(255, 170, 102), new Color(255, 102, 204)}, // Sunset Warmth
        {new Color(173, 216, 230), new Color(135, 206, 250)}, // Calm Sky Blue
        {new Color(168, 230, 207), new Color(220, 237, 193)}  // Minty Fresh
    };

    public void nextTheme() {
        themeIndex = (themeIndex + 1) % themes.length;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color color1 = themes[themeIndex][0];
        Color color2 = themes[themeIndex][1];
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(255, 255, 255, 40));
        int width = getWidth();
        int height = getHeight();
        g2d.drawLine(0, height / 4, width, height);
        g2d.drawLine(0, height / 2, width, height + height / 4);
        g2d.drawLine(0, (3 * height) / 4, width, height + height / 2);

        g2d.setFont(new Font("Segoe UI", Font.BOLD, 48));
        g2d.setColor(new Color(255, 255, 255, 40));
        g2d.rotate(Math.toRadians(-45));
        g2d.drawString("Michaelson", -100, 200);
        g2d.drawString("Michaelson", 100, 400);
        g2d.drawString("Michaelson", 300, 600);

        g2d.dispose();
    }
}
