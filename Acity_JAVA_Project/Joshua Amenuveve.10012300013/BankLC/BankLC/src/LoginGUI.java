import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;
    private JLabel statusLabel;
    private JCheckBox showPassword;

    public LoginGUI() {
        setTitle("Login");
        setSize(425, 258); // % smaller than 500x350
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        initUI();
        applyDarkTheme();

        setVisible(true);
    }

    private void initUI() {
        Font font = new Font("Roboto", Font.PLAIN, 16);
        Font boldFont = new Font("Serif", Font.BOLD, 16);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(40, 45, 100, 30);
        add(userLabel);

        userField = new JTextField();
        userField.setBounds(160, 45, 200, 30);
        userField.setFont(font);
        add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(40, 95, 100, 30);
        add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(160, 95, 200, 30);
        passField.setFont(font);
        add(passField);

        showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(160, 130, 160, 20);
        showPassword.setFont(new Font("Serif", Font.PLAIN, 13));
        showPassword.setOpaque(false);
        showPassword.setFocusPainted(false);
        showPassword.addActionListener(e -> {
            passField.setEchoChar(showPassword.isSelected() ? (char) 0 : '\u2022');
        });
        add(showPassword);

        loginButton = new JButton("Login");
        loginButton.setBounds(160, 160, 200, 35);
        loginButton.setFont(font);
        loginButton.setFocusPainted(false);
        add(loginButton);

        statusLabel = new JLabel();
        statusLabel.setBounds(40, 210, 340, 30);
        statusLabel.setFont(font);
        add(statusLabel);

        loginButton.addActionListener(e -> authenticate());

        userField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passField.requestFocus();
                }
            }
        });

        passField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    authenticate();
                }
            }
        });
    }

    private void applyDarkTheme() {
    Color bg = darkenColor(new Color(30, 30, 30), 0.2f); // Dark background
    Color fg = darkenColor(Color.WHITE, 0.2f);           // Slightly dimmed white
    Color underline = new Color(150, 150, 150);           // Light grey underline
    Color btnFg = Color.WHITE;                            // White button text
    Color accent = new Color(180, 60, 60);                // Greyish red
    Color errorColor = accent;

    getContentPane().setBackground(bg);

    for (Component comp : getContentPane().getComponents()) {
        if (comp instanceof JLabel) {
            comp.setForeground(fg);
        } else if (comp instanceof JCheckBox cb) {
            cb.setForeground(fg);
        }
    }

    userField.setBackground(bg);
    userField.setForeground(fg);
    userField.setCaretColor(fg);
    userField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, underline));

    passField.setBackground(bg);
    passField.setForeground(fg);
    passField.setCaretColor(fg);
    passField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, underline));

    loginButton.setBackground(bg);              // button background same as main background
    loginButton.setForeground(btnFg);           // white text
    loginButton.setBorder(BorderFactory.createLineBorder(bg));
    statusLabel.setForeground(errorColor);
}

    private void authenticate() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (username.equals("Hans Black") && password.equals("Unity34")) {
            dispose(); // Close login window
            new LoanGUI(); // Replace with your main app GUI
        } else {
            statusLabel.setText("Invalid username or password.");
        }
    }

    private Color darkenColor(Color color, float percent) {
        int r = (int)(color.getRed() * (1 - percent));
        int g = (int)(color.getGreen() * (1 - percent));
        int b = (int)(color.getBlue() * (1 - percent));
        return new Color(Math.max(r, 0), Math.max(g, 0), Math.max(b, 0));
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}