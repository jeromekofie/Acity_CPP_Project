import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

class Course {
    private final String code;
    private final String name;
    private final String grade;
    private final int credits;

    public Course(String code, String name, String grade, int credits) {
        this.code = code;
        this.name = name;
        this.grade = grade;
        this.credits = credits;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getGrade() { return grade; }
    public int getCredits() { return credits; }
}

public class GPACalculator extends JFrame {
    private final List<Course> courses;
    private final JPanel mainPanel;
    private final JButton addCourseButton;
    private final JButton calculateButton;
    private final JButton saveButton;
    private final JLabel gpaLabel;
    private final JScrollPane scrollPane;
    private final JTextField studentNameField;
    private final JTextField studentIdField;
    private final JComboBox<String> storageTypeComboBox;
    private final JTextArea gradesDisplayArea;

    public GPACalculator() {
        setTitle("GPA Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        courses = new ArrayList<>();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel studentInfoPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        studentInfoPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        
        studentNameField = new JTextField(20);
        studentIdField = new JTextField(20);
        storageTypeComboBox = new JComboBox<>(new String[]{"File Storage", "Database Storage"});
        
        studentInfoPanel.add(new JLabel("Student Name:"));
        studentInfoPanel.add(studentNameField);
        studentInfoPanel.add(new JLabel("Student ID:"));
        studentInfoPanel.add(studentIdField);
        studentInfoPanel.add(new JLabel("Storage Type:"));
        studentInfoPanel.add(storageTypeComboBox);

        JPanel buttonPanel = new JPanel();
        addCourseButton = new JButton("Add Course");
        calculateButton = new JButton("Calculate GPA");
        saveButton = new JButton("Save Data");
        buttonPanel.add(addCourseButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(saveButton);

        gpaLabel = new JLabel("GPA: 0.00");
        gpaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gpaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        gradesDisplayArea = new JTextArea(10, 50);
        gradesDisplayArea.setEditable(false);
        JScrollPane gradesScrollPane = new JScrollPane(gradesDisplayArea);
        gradesScrollPane.setBorder(BorderFactory.createTitledBorder("Grades Summary"));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(studentInfoPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        scrollPane = new JScrollPane(mainPanel);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(gradesScrollPane, BorderLayout.CENTER);
        bottomPanel.add(gpaLabel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        addCourseButton.addActionListener(e -> addCoursePanel());
        calculateButton.addActionListener(e -> calculateGPA());
        saveButton.addActionListener(e -> saveData());
    }

    private void addCoursePanel() {
        JPanel coursePanel = new JPanel();
        coursePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        coursePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        coursePanel.setBorder(BorderFactory.createEtchedBorder());

        JTextField courseNameField = new JTextField(15);
        JTextField courseCodeField = new JTextField(10);
        JComboBox<String> gradeComboBox = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D", "E", "FF"});
        JSpinner creditSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        JButton removeButton = new JButton("Remove");

        coursePanel.add(new JLabel("Course Code:"));
        coursePanel.add(courseCodeField);
        coursePanel.add(new JLabel("Course Name:"));
        coursePanel.add(courseNameField);
        coursePanel.add(new JLabel("Grade:"));
        coursePanel.add(gradeComboBox);
        coursePanel.add(new JLabel("Credits:"));
        coursePanel.add(creditSpinner);
        coursePanel.add(removeButton);

        mainPanel.add(coursePanel);
        mainPanel.revalidate();
        mainPanel.repaint();

        removeButton.addActionListener(e -> {
            mainPanel.remove(coursePanel);
            mainPanel.revalidate();
            mainPanel.repaint();
        });
    }

    private void calculateGPA() {
        if (studentNameField.getText().trim().isEmpty() || studentIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter student name and ID", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double totalPoints = 0;
        int totalCredits = 0;
        StringBuilder gradesSummary = new StringBuilder();
        gradesSummary.append("Course Code\tCourse Name\tGrade\tCredits\n");
        gradesSummary.append("------------------------------------------------\n");

        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof JPanel coursePanel) {
                Component[] components = coursePanel.getComponents();
                
                String courseCode = ((JTextField) components[1]).getText();
                String courseName = ((JTextField) components[3]).getText();
                String grade = ((JComboBox<String>) components[5]).getSelectedItem().toString();
                int credits = (int) ((JSpinner) components[7]).getValue();
                
                if (courseCode.trim().isEmpty() || courseName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all course details", "Missing Information", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                double gradePoints = getGradePoints(grade);
                totalPoints += gradePoints * credits;
                totalCredits += credits;
                
                gradesSummary.append(String.format("%s\t%s\t%s\t%d\n", courseCode, courseName, grade, credits));
            }
        }

        if (totalCredits == 0) {
            JOptionPane.showMessageDialog(this, "Please add at least one course", "No Courses", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double gpa = totalPoints / totalCredits;
        gpaLabel.setText(String.format("Student: %s (ID: %s) - GPA: %.2f", 
            studentNameField.getText(), 
            studentIdField.getText(), 
            gpa));
        
        gradesDisplayArea.setText(gradesSummary.toString());
    }

    private void saveData() {
        String storageType = (String) storageTypeComboBox.getSelectedItem();
        
        if (storageType.equals("File Storage")) {
            try {
                try (FileWriter writer = new FileWriter("gpa_records.txt")) {
                    writer.write("Student Name: " + studentNameField.getText() + "\n");
                    writer.write("Student ID: " + studentIdField.getText() + "\n");
                    writer.write(gradesDisplayArea.getText());
                    writer.write("\nGPA: " + gpaLabel.getText());
                }
                JOptionPane.showMessageDialog(this, "Data saved to file successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Database: Yet to be Implemented", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private double getGradePoints(String grade) {
        return switch (grade) {
            case "A" -> 4.0;
            case "B+" -> 3.5;
            case "B" -> 3.0;
            case "C+" -> 2.5;
            case "C" -> 2.0;
            case "D" -> 1.5;
            case "E" -> 1.0;
            case "FF" -> 0.0;
            default -> 0.0;
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GPACalculator().setVisible(true);
        });
    }
} 