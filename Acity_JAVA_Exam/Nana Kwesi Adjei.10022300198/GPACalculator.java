import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField;
    private JComboBox<String> storageComboBox;
    private JButton addCourseBtn, calculateBtn;
    private JPanel coursesPanel;
    private ArrayList<CoursePanel> coursePanels;
    private Map<String, Double> gradePoints;

    public GPACalculator() {
        setTitle("GPA Calculator");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeGradePoints();
        createUI();

        setVisible(true);
    }

    private void initializeGradePoints() {
        gradePoints = new HashMap<>();
        gradePoints.put("A", 4.0);
        gradePoints.put("B+", 3.5);
        gradePoints.put("B", 3.0);
        gradePoints.put("C+", 2.5);
        gradePoints.put("C", 2.0);
        gradePoints.put("D", 1.5);
        gradePoints.put("E", 1.0);
        gradePoints.put("F", 0.0);
    }

    private void createUI() {
        // Top panel for student info
        JPanel topPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        topPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        topPanel.add(nameField);
        topPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        topPanel.add(idField);
        topPanel.add(new JLabel("Save to:"));
        storageComboBox = new JComboBox<>(new String[]{"File", "Database"});
        topPanel.add(storageComboBox);
        add(topPanel, BorderLayout.NORTH);

        // Courses panel
        coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursePanels = new ArrayList<>();

        // Add initial course
        addCoursePanel();

        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addCourseBtn = new JButton("Add Course");
        addCourseBtn.addActionListener(e -> addCoursePanel());
        bottomPanel.add(addCourseBtn);

        calculateBtn = new JButton("Calculate GPA");
        calculateBtn.addActionListener(new CalculateListener());
        bottomPanel.add(calculateBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addCoursePanel() {
        CoursePanel coursePanel = new CoursePanel();
        coursePanels.add(coursePanel);
        coursesPanel.add(coursePanel);
        revalidate();
        repaint();
    }

    private class CoursePanel extends JPanel {
        private JTextField courseNameField;
        private JSpinner creditHoursSpinner;
        private JComboBox<String> gradeComboBox;

        public CoursePanel() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setBorder(BorderFactory.createEtchedBorder());

            add(new JLabel("Course Name:"));
            courseNameField = new JTextField(10);
            add(courseNameField);

            add(new JLabel("Credit Hours:"));
            creditHoursSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
            add(creditHoursSpinner);

            add(new JLabel("Grade:"));
            gradeComboBox = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D", "E", "F"});
            add(gradeComboBox);

            JButton removeBtn = new JButton("Remove");
            removeBtn.addActionListener(e -> {
                coursePanels.remove(this);
                coursesPanel.remove(this);
                revalidate();
                repaint();
            });
            add(removeBtn);
        }

        public String getCourseName() {
            return courseNameField.getText();
        }

        public int getCreditHours() {
            return (int) creditHoursSpinner.getValue();
        }

        public String getGrade() {
            return (String) gradeComboBox.getSelectedItem();
        }
    }

    private class CalculateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String name = nameField.getText();
                String id = idField.getText();

                if (name.isEmpty() || id.isEmpty()) {
                    JOptionPane.showMessageDialog(GPACalculator.this, 
                        "Please enter student name and ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (coursePanels.isEmpty()) {
                    JOptionPane.showMessageDialog(GPACalculator.this, 
                        "Please add at least one course", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double totalPoints = 0;
                int totalCreditHours = 0;
                StringBuilder coursesDetails = new StringBuilder();

                for (CoursePanel cp : coursePanels) {
                    String courseName = cp.getCourseName();
                    if (courseName.isEmpty()) {
                        JOptionPane.showMessageDialog(GPACalculator.this, 
                            "Please enter course name for all courses", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int creditHours = cp.getCreditHours();
                    String grade = cp.getGrade();
                    double gradePoint = gradePoints.get(grade);

                    totalPoints += gradePoint * creditHours;
                    totalCreditHours += creditHours;

                    coursesDetails.append(String.format("%s: %s (%d credits)\n", 
                        courseName, grade, creditHours));
                }

                double gpa = totalPoints / totalCreditHours;

                String result = String.format(
                    "Student Name: %s\nStudent ID: %s\n\nCourses:\n%s\nGPA: %.2f",
                    name, id, coursesDetails.toString(), gpa);

                JOptionPane.showMessageDialog(GPACalculator.this, result, "GPA Result", JOptionPane.INFORMATION_MESSAGE);

                // Save data
                String selectedStorage = (String) storageComboBox.getSelectedItem();
                if (selectedStorage.equals("File")) {
                    saveToFile(name, id, coursesDetails.toString(), gpa);
                } else {
                    saveToDatabase(name, id, coursesDetails.toString(), gpa);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(GPACalculator.this, 
                    "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void saveToFile(String name, String id, String courses, double gpa) {
            try (FileWriter writer = new FileWriter("gpa_records.txt", true)) {
                writer.write(String.format(
                    "Student Name: %s\nStudent ID: %s\nCourses:\n%sGPA: %.2f\n\n",
                    name, id, courses, gpa));
                JOptionPane.showMessageDialog(GPACalculator.this, 
                    "Data saved to file successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(GPACalculator.this, 
                    "Error saving to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void saveToDatabase(String name, String id, String courses, double gpa) {
            JOptionPane.showMessageDialog(GPACalculator.this, 
                "Database: Under Construction.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new GPACalculator().setVisible(true);
        });
    }
}