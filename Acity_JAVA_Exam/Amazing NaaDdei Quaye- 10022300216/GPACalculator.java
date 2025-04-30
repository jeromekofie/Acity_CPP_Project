
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField, courseField, creditHoursField;
    private JComboBox<String> gradeCombo, storageCombo;
    private JTextArea resultArea;
    private ArrayList<Course> courses;
    private static final String FILE_PATH = "gpa_records.txt";

    public GPACalculator() {
        courses = new ArrayList<>();
        setTitle("GPA Calculator");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        nameField = new JTextField(20);
        idField = new JTextField(20);
        courseField = new JTextField(20);
        creditHoursField = new JTextField(5);
        
        String[] grades = {"A", "B+", "B", "C+", "C", "D", "E", "F"};
        gradeCombo = new JComboBox<>(grades);
        
        String[] storage = {"File", "Database"};
        storageCombo = new JComboBox<>(storage);
        
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // panel to allow inputs
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Student ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Course Name:"));
        inputPanel.add(courseField);
        inputPanel.add(new JLabel("Credit Hours:"));
        inputPanel.add(creditHoursField);
        inputPanel.add(new JLabel("Grade:"));
        inputPanel.add(gradeCombo);
        inputPanel.add(new JLabel("Storage Type:"));
        inputPanel.add(storageCombo);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Course");
        JButton calculateButton = new JButton("Calculate GPA");
        JButton clearButton = new JButton("clear all");
        
        buttonPanel.add(addButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);
        
        
        JPanel resultPanel = new JPanel();
        resultPanel.add(new JScrollPane(resultArea));
        
        
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.SOUTH);
        
        
        addButton.addActionListener(e -> addCourse());
        calculateButton.addActionListener(e -> calculateGPA());
        clearButton.addActionListener(e -> clearFields());
    }

    private void addCourse() {
        try {
            String courseName = courseField.getText();
            double creditHours = Double.parseDouble(creditHoursField.getText());
            String grade = (String) gradeCombo.getSelectedItem();
            
            courses.add(new Course(courseName, creditHours, grade));
            courseField.setText("");
            creditHoursField.setText("");
            updateResultArea();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please the required values for credit hours");
        }
    }

    private void calculateGPA() {
        if (courses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add your course");
            return;
        }
        
        double totalPoints = 0;
        double totalHours = 0;
        
        for (Course course : courses) {
            totalPoints += course.getGradePoints() * course.getCreditHours();
            totalHours += course.getCreditHours();
        }
        
        double gpa = totalPoints / totalHours;
        
        String result = String.format("Student Name: %s\nStudent ID: %s\nGPA: %.2f", 
            nameField.getText(), idField.getText(), gpa);
        
        if (storageCombo.getSelectedItem().equals("File")) {
            saveToFile(result);
        } else {
            
            JOptionPane.showMessageDialog(this, "Database is under construction");
        }
        
        resultArea.append("\n" + result);
    }

    private void saveToFile(String data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            writer.println(data);
            writer.println("------------------------");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Saving to file was unsuccessful: " );
        }
    }

    private void clearFields() {
        nameField.setText("");
        idField.setText("");
        courseField.setText("");
        creditHoursField.setText("");
        resultArea.setText("");
        courses.clear();
    }

    private void updateResultArea() {
        StringBuilder sb = new StringBuilder("Current Courses:\n");
        for (Course course : courses) {
            sb.append(course.toString()).append("\n");
        }
        resultArea.setText(sb.toString());
    }

    private class Course {
        private String name;
        private double creditHours;
        private String grade;
        
        public Course(String name, double creditHours, String grade) {
            this.name = name;
            this.creditHours = creditHours;
            this.grade = grade;
        }
        
        public double getCreditHours() {
            return creditHours;
        }
        
        public double getGradePoints() {
            switch (grade) {
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
        
        @Override
        public String toString() {
            return String.format("%s - %s credits - Grade: %s", name, creditHours, grade);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GPACalculator().setVisible(true);
        });
    }
}


