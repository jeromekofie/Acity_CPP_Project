import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class ReportCardGenerator {
    public static final String URL = "jdbc:mariadb://localhost:3306/reportcard_db";
    public static final String USER = "root";
    public static final String PASSWORD = "najart";

    public static void main(String[] args) {
        if (!checkDatabaseConnection()) return;
        SwingUtilities.invokeLater(LoginFrame::new);
    }

    private static boolean checkDatabaseConnection() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                System.out.println(" Connected!");
                return true;
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Driver not found.");
            return false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Connection failed: " + e.getMessage());
            return false;
        }
    }

    static class LoginFrame extends JFrame {
        public LoginFrame() {
            setTitle("Login");
            setSize(500, 300);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setUndecorated(true);
            setLocationRelativeTo(null);
            setShape(new RoundRectangle2D.Double(0, 0, 500, 300, 30, 30));
            getContentPane().setBackground(new Color(173, 216, 230)); // Soft light blue background

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(12, 12, 12, 12);

            JLabel title = new JLabel("Student Login");
            title.setFont(new Font("Verdana", Font.BOLD, 22));
            title.setForeground(new Color(60, 60, 60)); // Dark Gray for better contrast
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            panel.add(title, gbc);

            JTextField userField = new JTextField(15);
            JPasswordField passField = new JPasswordField(15);

            gbc.gridwidth = 1;
            gbc.gridy++;
            panel.add(createLabel("Username:"), gbc);
            gbc.gridx = 1;
            panel.add(userField, gbc);

            gbc.gridx = 0; gbc.gridy++;
            panel.add(createLabel("Password:"), gbc);
            gbc.gridx = 1;
            panel.add(passField, gbc);

            JButton loginBtn = new JButton("Login");
            styleButton(loginBtn);
            gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
            panel.add(loginBtn, gbc);

            loginBtn.addActionListener(e -> {
                String user = userField.getText();
                String pass = new String(passField.getPassword());
                if (user.equals("najart") && pass.equals("1234")) {
                    dispose();
                    new CourseSelectionFrame();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials.");
                }
            });

            add(panel);
            setVisible(true);
        }
    }

    static class CourseSelectionFrame extends JFrame {
        public CourseSelectionFrame() {
            setTitle("Choose Course");
            setSize(600, 300);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            getContentPane().setBackground(new Color(173, 216, 230)); // Soft light blue background

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(15, 15, 15, 15);

            JLabel header = new JLabel("Select Your Course");
            header.setFont(new Font("Arial", Font.BOLD, 24));
            header.setForeground(new Color(60, 60, 60)); // Dark Gray for better contrast
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            panel.add(header, gbc);

            JComboBox<String> courseDropdown = new JComboBox<>(new String[]{"Select", "CS", "IT"});
            courseDropdown.setFont(new Font("Arial", Font.PLAIN, 16));
            gbc.gridy = 1;
            panel.add(courseDropdown, gbc);

            JButton nextBtn = new JButton("Next");
            styleButton(nextBtn);
            gbc.gridy = 2;
            panel.add(nextBtn, gbc);

            nextBtn.addActionListener(e -> {
                String course = (String) courseDropdown.getSelectedItem();
                if (!course.equals("Select")) {
                    dispose();
                    new StudentReportCardFrame(course);
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a course.");
                }
            });

            add(panel);
            setVisible(true);
        }
    }

    static class StudentReportCardFrame extends JFrame {
        public StudentReportCardFrame(String course) {
            setTitle("Report Card - " + course);
            setSize(700, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            getContentPane().setBackground(new Color(240, 248, 255)); // Soft light blue background

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel header = new JLabel("Report Card Entry - " + course);
            header.setFont(new Font("Arial", Font.BOLD, 22));
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            panel.add(header, gbc);

            String[] infoLabels = {"Name", "Roll Number", "Semester"};
            JTextField[] infoFields = new JTextField[infoLabels.length];

            for (int i = 0; i < infoLabels.length; i++) {
                gbc.gridwidth = 1; gbc.gridy++;
                gbc.gridx = 0;
                panel.add(createLabel(infoLabels[i] + ":"), gbc);
                gbc.gridx = 1;
                infoFields[i] = new JTextField(20);
                panel.add(infoFields[i], gbc);
            }

            String[] subjects = getSubjectsForCourse(course);
            JTextField[] markFields = new JTextField[subjects.length];

            gbc.gridy++;
            gbc.gridx = 0; gbc.gridwidth = 2;
            JLabel marksLabel = new JLabel("Enter Marks (0-100):");
            marksLabel.setFont(new Font("Arial", Font.BOLD, 16));
            panel.add(marksLabel, gbc);

            for (int i = 0; i < subjects.length; i++) {
                gbc.gridwidth = 1; gbc.gridy++;
                gbc.gridx = 0;
                panel.add(createLabel(subjects[i] + ":"), gbc);
                gbc.gridx = 1;
                markFields[i] = new JTextField(5);
                panel.add(markFields[i], gbc);
            }

            JButton generateBtn = new JButton("Generate Report");
            styleButton(generateBtn);
            gbc.gridy++;
            gbc.gridx = 0; gbc.gridwidth = 2;
            panel.add(generateBtn, gbc);

            generateBtn.addActionListener(e -> {
                try {
                    float[] marks = new float[markFields.length];
                    for (int i = 0; i < markFields.length; i++) {
                        marks[i] = Float.parseFloat(markFields[i].getText());
                        if (marks[i] < 0 || marks[i] > 100) throw new NumberFormatException();
                    }

                    showReport(infoFields[0].getText(), infoFields[1].getText(), infoFields[2].getText(),
                               course, subjects, marks);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100.");
                }
            });

            add(panel);
            setVisible(true);
        }

        private String[] getSubjectsForCourse(String course) {
            return course.equals("CS")
                ? new String[]{"Data Structures", "Algorithms", "Database", "OS", "Networking"}
                : new String[]{"Web Tech", "Software Eng", "DBMS", "Cyber Sec", "Cloud"};
        }

        private void showReport(String name, String roll, String sem, String course, String[] subjects, float[] marks) {
            new ReportCardDisplayFrame(name, roll, sem, course, subjects, marks);
        }
    }

    private static String getLetterGrade(float mark) {
        if (mark >= 90) return "A+";
        if (mark >= 80) return "A";
        if (mark >= 70) return "B";
        if (mark >= 60) return "C";
        if (mark >= 50) return "D";
        return "F";
    }

    private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(255, 182, 193)); // Light coral/pinkish color for buttons
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    static class ReportCardDisplayFrame extends JFrame {
        private static DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Name", "Roll", "Semester", "Course", "Average Grade"}, 0
        );

        public ReportCardDisplayFrame(String name, String roll, String sem, String course, String[] subjects, float[] marks) {
            setTitle("Generated Report Card");
            setSize(800, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            getContentPane().setBackground(new Color(240, 248, 255)); // Soft light blue background

            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            mainPanel.setOpaque(false);

            // 📋 Report Display
            JTextArea reportArea = new JTextArea();
            reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            reportArea.setEditable(false);

            float total = 0;
            StringBuilder report = new StringBuilder();
            report.append("REPORT CARD\nName: ").append(name)
                  .append("\nRoll: ").append(roll)
                  .append("\nSemester: ").append(sem)
                  .append("\nCourse: ").append(course).append("\n\nSubject\t\tMarks\tGrade\n");

            for (int i = 0; i < subjects.length; i++) {
                total += marks[i];
                report.append(subjects[i]).append("\t\t").append(marks[i])
                      .append("\t").append(getLetterGrade(marks[i])).append("\n");
            }

            float avg = total / marks.length;
            report.append("\nAverage: ").append(String.format("%.2f", avg))
                  .append("\nGrade: ").append(getLetterGrade(avg));

            reportArea.setText(report.toString());
            mainPanel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

            // ➕ Add to table
            tableModel.addRow(new Object[]{name, roll, sem, course, getLetterGrade(avg)});

            // 📊 Table Panel
            JTable table = new JTable(tableModel);
            JScrollPane tableScrollPane = new JScrollPane(table);

            // 🔘 Buttons Panel
            JPanel buttonPanel = new JPanel();
            buttonPanel.setOpaque(false);

            JButton deleteBtn = new JButton("Delete Selected");
            styleButton(deleteBtn);
            deleteBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) tableModel.removeRow(row);
            });

            buttonPanel.add(deleteBtn);

            JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
            bottomPanel.setOpaque(false);
            bottomPanel.add(new JLabel("Submitted Records:"), BorderLayout.NORTH);
            bottomPanel.add(tableScrollPane, BorderLayout.CENTER);
            bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(reportArea), bottomPanel);
            splitPane.setResizeWeight(0.5);

            mainPanel.add(splitPane, BorderLayout.CENTER);
            add(mainPanel);
            setVisible(true);
        }
    }

    private static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(new Color(60, 60, 60)); // Dark Gray
        return label;
    }
}
