import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BasicGPACalculator extends JFrame {
    private JTextField nameField, idField, courseField, gradeField, creditField;
    private JTextArea outputArea;
    private JButton addButton, calculateButton, saveButton;

    private double totalPoints = 0;
    private double totalCredits = 0;
    private StringBuilder courseDetails = new StringBuilder();

    private static final Map<String, Double> GRADE_MAP = new HashMap<>();

    static {
        GRADE_MAP.put("A", 4.0);
        GRADE_MAP.put("B+", 3.5);
        GRADE_MAP.put("B", 3.0);
        GRADE_MAP.put("C+", 2.5);
        GRADE_MAP.put("C", 2.0);
        GRADE_MAP.put("D", 1.5);
        GRADE_MAP.put("E", 1.0);
        GRADE_MAP.put("F", 0.0);
    }

    public BasicGPACalculator() {
        setTitle("Simple GPA Calculator");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(10, 2, 5, 5));

        // Fields
        nameField = new JTextField();
        idField = new JTextField();
        courseField = new JTextField();
        gradeField = new JTextField();
        creditField = new JTextField();
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);

        addButton = new JButton("Add Course");
        calculateButton = new JButton("Calculate GPA");
        saveButton = new JButton("Save to File");

        // Add components
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Student ID:"));
        add(idField);
        add(new JLabel("Course Name:"));
        add(courseField);
        add(new JLabel("Grade (A-F):"));
        add(gradeField);
        add(new JLabel("Credit Hours:"));
        add(creditField);
        add(addButton);
        add(calculateButton);
        add(saveButton);
        add(new JLabel("Output:"));
        add(new JScrollPane(outputArea));

        // Button listeners
        addButton.addActionListener(e -> addCourse());
        calculateButton.addActionListener(e -> calculateGPA());
        saveButton.addActionListener(e -> saveToFile());
    }

    private void addCourse() {
        try {
            String course = courseField.getText().trim();
            String grade = gradeField.getText().trim().toUpperCase();
            double credits = Double.parseDouble(creditField.getText().trim());

            if (!GRADE_MAP.containsKey(grade)) {
                outputArea.setText("Invalid grade entered.");
                return;
            }

            double gradePoint = GRADE_MAP.get(grade);
            totalPoints += gradePoint * credits;
            totalCredits += credits;

            courseDetails.append(String.format("Course: %s, Grade: %s, Credits: %.1f\n",
                    course, grade, credits));

            courseField.setText("");
            gradeField.setText("");
            creditField.setText("");
        } catch (NumberFormatException ex) {
            outputArea.setText("Please enter a valid number for credits.");
        }
    }

    private void calculateGPA() {
        if (totalCredits == 0) {
            outputArea.setText("No courses added yet.");
            return;
        }

        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        double gpa = totalPoints / totalCredits;

        String result = "Student Name: " + name + "\n"
                + "Student ID: " + id + "\n\n"
                + courseDetails.toString()
                + "\nGPA: " + String.format("%.2f", gpa);

        outputArea.setText(result);
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("gpa_basic.txt", true))) {
            writer.println(outputArea.getText());
            writer.println("-----");
            JOptionPane.showMessageDialog(this, "Saved to gpa_basic.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BasicGPACalculator().setVisible(true));
    }
}


//Compile: javac BasicGPACalculator.java
//RUN: java BasicGPACalculator