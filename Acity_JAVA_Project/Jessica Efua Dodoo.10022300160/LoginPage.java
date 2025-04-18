import java.awt.*;
import javax.swing.*;

public class LoginPage {
    private JFrame frame;
    private JTextField userText;
    private JPasswordField passField;
    private JButton loginButton;

    // Default credentials
    private final String USERNAME = "admin";
    private final String PASSWORD = "12345";

    public LoginPage() {
        // Frame setup
        frame = new JFrame("Login");
        frame.setSize(800, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // Center on screen

        // Main panel with background image
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image image = new ImageIcon("C:\\Users\\New User\\Downloads\\R.jpeg").getImage();
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    g.setColor(new Color(240, 240, 240));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Login form panel with glass effect
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formPanel.setBackground(new Color(255, 255, 255, 220));
        formPanel.setMaximumSize(new Dimension(350, 300));

        // Title
        JLabel title = new JLabel("CONTACT MANAGEMENT");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Username field
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        userPanel.setBackground(new Color(255, 255, 255, 0));
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userText = new JTextField(15);
        userText.setFont(new Font("Arial", Font.PLAIN, 14));
        userPanel.add(userLabel);
        userPanel.add(userText);

        // Password field
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        passPanel.setBackground(new Color(255, 255, 255, 0));
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passField = new JPasswordField(15);
        passField.setFont(new Font("Arial", Font.PLAIN, 14));
        passPanel.add(passLabel);
        passPanel.add(passField);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setPreferredSize(new Dimension(100, 30));
        loginButton.setMaximumSize(new Dimension(100, 30));
        loginButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Add components to form panel
        formPanel.add(title);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(userPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(passPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        formPanel.add(loginButton);

        // Add form panel to center of main panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(formPanel, gbc);

        // Add main panel to frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // Action on login button click
        loginButton.addActionListener(e -> checkCredentials());

        // Allow pressing Enter to trigger login
        passField.addActionListener(e -> loginButton.doClick());

        frame.setVisible(true);
    }

    // Credential check logic
    private void checkCredentials() {
        String enteredUser = userText.getText();
        String enteredPass = new String(passField.getPassword());

        if (enteredUser.equals(USERNAME) && enteredPass.equals(PASSWORD)) {
            frame.dispose();
            new Dashboard(); // Load the dashboard
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid username or password. Please try again.");
            passField.setText(""); // Clear password
        }
    }

    /*  Placeholder Dashboard class
    class Dashboard extends JFrame {
        public Dashboard() {
            setTitle("Dashboard");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            JLabel label = new JLabel("Welcome to the Dashboard!", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 18));
            add(label);
            setVisible(true);
        }
    }*/

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}


