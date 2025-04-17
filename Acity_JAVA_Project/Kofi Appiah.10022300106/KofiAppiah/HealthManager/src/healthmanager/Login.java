package healthmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

public class Login extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn;

    public Login() {
        setTitle("Kwabs Hospital Management System - Login");
        setSize(500, 400); // Slightly larger for better spacing
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        // Main panel with gradient background
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color1 = new Color(230, 240, 255); // Light blue
                Color color2 = new Color(200, 220, 255); // Slightly darker blue
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Header with icon
        JLabel headerLabel = new JLabel("Kwabs Hospital Login", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerLabel.setForeground(new Color(0, 82, 123)); // Darker blue
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(headerLabel, gbc);

        // Add a subtle separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(180, 200, 220));
        gbc.gridy = 1;
        panel.add(separator, gbc);

        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setForeground(new Color(60, 60, 60));

        usernameField = new JTextField();
        styleTextField(usernameField);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(60, 60, 60));

        passwordField = new JPasswordField();
        stylePasswordField(passwordField);

        // Show Password checkbox
        JCheckBox showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPasswordCheckbox.setForeground(new Color(60, 60, 60));
        showPasswordCheckbox.setOpaque(false);
        showPasswordCheckbox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                passwordField.setEchoChar((char) 0); // Show password
            } else {
                passwordField.setEchoChar('•'); // Hide password
            }
        });

        // Login button
        loginBtn = new JButton("LOGIN");
        styleButton(loginBtn);

        // Add components to panel
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Add Show Password checkbox
        gbc.gridy = 4;
        gbc.gridx = 1;
        panel.add(showPasswordCheckbox, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(Box.createVerticalStrut(20), gbc);

        gbc.gridy = 6;
        panel.add(loginBtn, gbc);

        // Add the panel to the frame
        add(panel);

        // Button action listener
        loginBtn.addActionListener((ActionEvent e) -> {
            loginUser();
        });
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(250, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(150, 180, 220)), // Bottom border only
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(new Color(250, 253, 255));
        field.setForeground(new Color(50, 50, 50));
    }

    private void stylePasswordField(JPasswordField field) {
        styleTextField(field); // Reuse the same styling
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(0, 120, 180)); // Vibrant blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(300, 45));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Modern button styling with shadow and hover effects
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                ButtonModel model = b.getModel();

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow effect
                if (!model.isPressed()) {
                    g2.setColor(new Color(0, 120, 180, 100));
                    g2.fillRoundRect(3, 3, c.getWidth(), c.getHeight(), 8, 8);
                }

                // Button color changes on hover/press
                if (model.isPressed()) {
                    g2.setColor(new Color(0, 90, 150));
                } else if (model.isRollover()) {
                    g2.setColor(new Color(0, 140, 210));
                } else {
                    g2.setColor(b.getBackground());
                }

                g2.fillRoundRect(0, 0, c.getWidth()-1, c.getHeight()-1, 8, 8);
                super.paint(g, c);
            }
        });
    }

    private void loginUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both username and password",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Hardcoded credentials (replace with proper authentication)
        if (username.equals("Kofi") && password.equals("appiah")) {
            JOptionPane.showMessageDialog(this,
                "Login successful. Welcome, " + username + "!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new MainFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid credentials. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }
}
