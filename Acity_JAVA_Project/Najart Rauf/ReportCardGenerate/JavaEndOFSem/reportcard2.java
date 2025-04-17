import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
// import java.text.SimpleDateFormat; // Added import


public class reportcard2 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }

    // === LOGIN SCREEN ===
    static class LoginFrame extends JFrame {
        public LoginFrame() {
            setTitle("Login");
            setSize(800, 500);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setUndecorated(true);
            setShape(new RoundRectangle2D.Double(0, 0, 500, 300, 30, 30));
            getContentPane().setBackground(new Color(255, 235, 205));

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setOpaque(false);
            panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel titleLabel = new JLabel("STUDENT REPORT CARD LOGIN", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setForeground(new Color(70, 70, 70));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(titleLabel, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            panel.add(new JLabel("Username:"), gbc);

            JTextField usernameField = new JTextField(20);
            gbc.gridx = 1;
            panel.add(usernameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(new JLabel("Password:"), gbc);

            JPasswordField passwordField = new JPasswordField(20);
            gbc.gridx = 1;
            panel.add(passwordField, gbc);

            JButton loginButton = new JButton("Login");
            styleButton(loginButton);
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.CENTER;
            panel.add(loginButton, gbc);

            add(panel);

            loginButton.addActionListener(e -> {
                String user = usernameField.getText().trim();
                String pass = new String(passwordField.getPassword());
                if (user.equals("najart") && pass.equals("1234")) {
                    dispose();
                    new CourseSelectionFrame();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials.");
                }
            });

            setVisible(true);
        }
    }

    // === COURSE SELECTION WINDOW ===
    static class CourseSelectionFrame extends JFrame {
        public CourseSelectionFrame() {
            setTitle("Select Course");
            setSize(900, 900);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            getContentPane().setBackground(new Color(255, 235, 205));

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setOpaque(false);
            panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(15, 15, 15, 15);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel titleLabel = new JLabel("SELECT COURSE", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setForeground(new Color(70, 70, 70));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(titleLabel, gbc);

            JComboBox<String> courseDropdown = new JComboBox<>(new String[]{"Select", "CS", "IT"});
            courseDropdown.setFont(new Font("Arial", Font.PLAIN, 16));
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            panel.add(courseDropdown, gbc);

            JButton nextButton = new JButton("Next");
            styleButton(nextButton);
            gbc.gridy = 4;
            panel.add(nextButton, gbc);

            add(panel);

            nextButton.addActionListener(e -> {
                String selected = (String) courseDropdown.getSelectedItem();
                if (selected.equals("CS") || selected.equals("IT")) {
                    dispose();
                    new StudentReportCardFrame(selected);
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a valid course.");
                }
            });

            setVisible(true);
        }
    }

    

    private String getLetterGrade(float mark) {
        if (mark >= 90) return "A+";
        if (mark >= 80) return "A";
        if (mark >= 70) return "B";
        if (mark >= 60) return "C";
        if (mark >= 50) return "D";
        return "F";
    }

    // === STYLE HELPER ===
    private static void styleButton(JButton button) {
        button.setBackground(new Color(255, 140, 0));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
    }
}