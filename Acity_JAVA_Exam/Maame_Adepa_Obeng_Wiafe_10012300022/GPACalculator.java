import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField;
    private JComboBox<String> storageTypeCombo;
    private JTable courseTable;
    private JButton calculateButton, addCourseButton;
    private DefaultTableModel tableModel;
    private ArrayList<Course> courses;

    public GPACalculator() {
        courses = new ArrayList<>();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        // Initialize text fields
        nameField = new JTextField(20);
        idField = new JTextField(20);


        String[] storageTypes = {"File", "Database"};
        storageTypeCombo = new JComboBox<>(storageTypes);

        String[] columnNames = {"Course", "Credit Hours", "Grade"};
        tableModel = new DefaultTableModel(columnNames, 0);
        courseTable = new JTable(tableModel);

        calculateButton = new JButton("Calculate GPA");
        addCourseButton = new JButton("Add Course");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Student ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Storage Type:"));
        inputPanel.add(storageTypeCombo);


        JScrollPane tableScrollPane = new JScrollPane(courseTable);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addCourseButton);
        buttonPanel.add(calculateButton);

        add(inputPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("GPA Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void setupEventHandlers() {
        addCourseButton.addActionListener(e -> addCourse());
        calculateButton.addActionListener(e -> calculateGPA());
    }

    private void addCourse() {
        String course = JOptionPane.showInputDialog(this, "Enter Course Name:");
        if (course != null && !course.trim().isEmpty()) {
            String creditHours = JOptionPane.showInputDialog(this, "Enter Credit Hours:");
            String grade = JOptionPane.showInputDialog(this, "Enter Grade (A, B, C, D, F):");
            
            if (creditHours != null && grade != null) {
                tableModel.addRow(new Object[]{course, creditHours, grade});
                courses.add(new Course(course, Double.parseDouble(creditHours), grade));
            }
        }
    }

    private void calculateGPA() {
        if (courses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one course!");
            return;
        }

        double totalPoints = 0;
        double totalCreditHours = 0;

        for (Course course : courses) {
            double gradePoints = getGradePoints(course.getGrade());
            totalPoints += gradePoints * course.getCreditHours();
            totalCreditHours += course.getCreditHours();
        }

        double gpa = totalPoints / totalCreditHours;
        
        saveResults(gpa);
        
        JOptionPane.showMessageDialog(this, 
            String.format("GPA: %.2f", gpa),
            "GPA Calculation Result",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private double getGradePoints(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "B+": return 3.5;
            case "B": return 3.0;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "D": return 1.5;
            case "E": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }

    private void saveResults(double gpa) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("gpa_records.txt"))) {
            writer.println("Student Name: " + nameField.getText());
            writer.println("Student ID: " + idField.getText());
            writer.println("Storage Type: " + storageTypeCombo.getSelectedItem());
            writer.println("\nCourses:");
            
            for (Course course : courses) {
                writer.println(course.toString());
            }
            
            writer.println("\nGPA: " + String.format("%.2f", gpa));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error saving results: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GPACalculator().setVisible(true);
        });
    }
}

class Course {
    private String name;
    private double creditHours;
    private String grade;

    public Course(String name, double creditHours, String grade) {
        this.name = name;
        this.creditHours = creditHours;
        this.grade = grade;
    }

    public String getName() { return name; }
    public double getCreditHours() { return creditHours; }
    public String getGrade() { return grade; }

    @Override
    public String toString() {
        return String.format("%s - %d credits - Grade: %s", name, (int)creditHours, grade);
    }
}
