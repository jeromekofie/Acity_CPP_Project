import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;

public class ReportCardGenerator {
    // Database configuration
    public static final String URL = "jdbc:mariadb://localhost:3306/reportcard_db";
    public static final String USER = "root";
    public static final String PASSWORD = "najart";

    public static void main(String[] args) {
        // Verify database connection first
        if (!checkDatabaseConnection()) {
            return;
        }

        // Start the GUI
        SwingUtilities.invokeLater(LoginFrame::new);
    }

    private static boolean checkDatabaseConnection() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                System.out.println("✅ Connected to the database successfully!");
                return true;
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "❌ MariaDB JDBC Driver not found.");
            return false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Database connection failed:\n" + e.getMessage());
            return false;
        }
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

    // === REPORT CARD FRAME ===
    static class StudentReportCardFrame extends JFrame {
        public StudentReportCardFrame(String course) {
            setTitle("Student Report Card - " + course);
            setSize(900, 700);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            getContentPane().setBackground(new Color(255, 235, 205));

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setOpaque(false);
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Header
            JLabel titleLabel = new JLabel("STUDENT REPORT CARD - " + course, SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setForeground(new Color(70, 70, 70));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(titleLabel, gbc);

            // Student info fields
            String[] labels = {"Student Name:", "Roll Number:", "Semester:"};
            JTextField[] fields = new JTextField[labels.length];
            
            for (int i = 0; i < labels.length; i++) {
                gbc.gridy = i + 1;
                gbc.gridx = 0;
                gbc.gridwidth = 1;
                panel.add(new JLabel(labels[i]), gbc);
                
                fields[i] = new JTextField(20);
                gbc.gridx = 1;
                panel.add(fields[i], gbc);
            }

            // Subjects and marks (would be different for CS/IT)
            String[] subjects = getSubjectsForCourse(course);
            JTextField[] marksFields = new JTextField[subjects.length];
            
            gbc.gridy += 1;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            panel.add(new JLabel("Marks:", SwingConstants.LEFT), gbc);
            
            for (int i = 0; i < subjects.length; i++) {
                gbc.gridy += 1;
                gbc.gridx = 0;
                gbc.gridwidth = 1;
                panel.add(new JLabel(subjects[i] + ":"), gbc);
                
                marksFields[i] = new JTextField(5);
                gbc.gridx = 1;
                panel.add(marksFields[i], gbc);
            }

            // Generate button
            JButton generateButton = new JButton("Generate Report Card");
            styleButton(generateButton);
            gbc.gridy += 1;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.CENTER;
            panel.add(generateButton, gbc);

            generateButton.addActionListener(e -> {
                // Validate and process marks
                try {
                    float[] marks = new float[subjects.length];
                    for (int i = 0; i < marksFields.length; i++) {
                        marks[i] = Float.parseFloat(marksFields[i].getText());
                        if (marks[i] < 0 || marks[i] > 100) {
                            throw new NumberFormatException();
                        }
                    }
                    
                    // Show report card (in a real app, you'd save to database)
                    showReportCard(fields[0].getText(), fields[1].getText(), 
                                  fields[2].getText(), course, subjects, marks);
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter valid marks (0-100) for all subjects.");
                }
            });

            add(panel);
            setVisible(true);
        }

        private String[] getSubjectsForCourse(String course) {
            if (course.equals("CS")) {
                return new String[]{"Data Structures", "Algorithms", "Database", "OS", "Networking"};
            } else {
                return new String[]{"Web Tech", "Software Eng", "DBMS", "Cyber Security", "Cloud Computing"};
            }
        }

        private void showReportCard(String name, String roll, String semester, 
                                  String course, String[] subjects, float[] marks) {
            StringBuilder report = new StringBuilder();
            report.append("REPORT CARD\n");
            report.append("Name: ").append(name).append("\n");
            report.append("Roll: ").append(roll).append("\n");
            report.append("Semester: ").append(semester).append("\n");
            report.append("Course: ").append(course).append("\n\n");
            report.append("SUBJECTS\tMARKS\tGRADE\n");
            
            float total = 0;
            for (int i = 0; i < subjects.length; i++) {
                String grade = getLetterGrade(marks[i]);
                report.append(subjects[i]).append("\t")
                      .append(marks[i]).append("\t")
                      .append(grade).append("\n");
                total += marks[i];
            }
            
            float percentage = total / subjects.length;
            String overallGrade = getLetterGrade(percentage);
            
            report.append("\nPercentage: ").append(String.format("%.2f", percentage)).append("%\n");
            report.append("Overall Grade: ").append(overallGrade).append("\n");
            
            JOptionPane.showMessageDialog(this, report.toString(), "Report Card", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // === UTILITY METHODS ===
    private static String getLetterGrade(float mark) {
        if (mark >= 90) return "A+";
        if (mark >= 80) return "A";
        if (mark >= 70) return "B";
        if (mark >= 60) return "C";
        if (mark >= 50) return "D";
        return "F";
    }

    private static void styleButton(JButton button) {
        button.setBackground(new Color(255, 140, 0));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
    }
}