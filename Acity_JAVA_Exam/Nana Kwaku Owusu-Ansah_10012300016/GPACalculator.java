import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField, courseField, creditField, gradeField;
    private JTextArea outputArea;
    private JComboBox<String> storageOption;

    // Storage for course entries
    private final java.util.List<Double> gradePoints = new ArrayList<>();
    private final java.util.List<Integer> creditHours = new ArrayList<>();
    private final java.util.List<String> courses = new ArrayList<>();

    public GPACalculator() {
        setTitle("GPA Calculator");
        setSize(1600, 853);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Inputs for student details
        add(new JLabel("Student Name:"));
        nameField = new JTextField(15);
        add(nameField);

        add(new JLabel("Student ID:"));
        idField = new JTextField(15);
        add(idField);

        add(new JLabel("Course:"));
        courseField = new JTextField(15);
        add(courseField);

        add(new JLabel("Credit Hours:"));
        creditField = new JTextField(5);
        add(creditField);

        add(new JLabel("Grades (A, B+, B, C+, C, D, E, F):"));
        gradeField = new JTextField(5);
        add(gradeField);

        // Storage options
        add(new JLabel("Save to:"));
        String[] options = { "File", "Database" };
        storageOption = new JComboBox<>(options);
        add(storageOption);

        // Buttons
        JButton addCourseButton = new JButton("➕ Add Course");
        JButton calculateGPAButton = new JButton("💾 Save & Calculate GPA");

        add(addCourseButton);
        add(calculateGPAButton);

        // Output area
        outputArea = new JTextArea(30, 60);
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea));

        // Add Course Button
        addCourseButton.addActionListener(e -> {
            try {
                String course = courseField.getText().trim();
                int credit = Integer.parseInt(creditField.getText().trim());
                String grade = gradeField.getText().trim().toUpperCase();

                double gradePoint = switch (grade) {
                    case "A" -> 4.0;
                    case "B+" -> 3.5;
                    case "B" -> 3.0;
                    case "C+" -> 2.5;
                    case "C" -> 2.0;
                    case "D" -> 1.5;
                    case "E" -> 1.0;
                    case "F" -> 0.0;
                    default -> throw new IllegalArgumentException("Invalid grade entered.");
                };
                gradePoints.add(gradePoint * credit);
                creditHours.add(credit);
                courses.add(String.format("%s (Credits: %d, Grade: %s)", course, credit, grade));

                outputArea.append(String.format("Added Course: %s (Credits: %d, Grade: %s)\n", course, credit, grade));

                // Clear fields for next course
                courseField.setText("");
                creditField.setText("");
                gradeField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Save & Calculate GPA Button
        calculateGPAButton.addActionListener(e -> {
            try {
                if (gradePoints.isEmpty() || creditHours.isEmpty()) {
                    // Check if any course has been added
                    JOptionPane.showMessageDialog(this, "Please add at least one course before calculating the GPA.");
                    return;
                }

                // Calculate total credits and total grade points
                int totalCredits = creditHours.stream().mapToInt(Integer::intValue).sum();
                double totalPoints = gradePoints.stream().mapToDouble(Double::doubleValue).sum();

                // Calculate GPA
                double gpa = totalPoints / totalCredits;

                // Build GPA summary
                StringBuilder GPAsummary = new StringBuilder();
                GPAsummary.append("Student GPA\n");
                GPAsummary.append("-----------------------\n");
                GPAsummary.append("Name: ").append(nameField.getText().trim()).append("\n");
                GPAsummary.append("Student ID: ").append(idField.getText().trim()).append("\n\n");
                GPAsummary.append("Courses Taken:\n");
                for (String entry : courses) {
                    GPAsummary.append(" - ").append(entry).append("\n");
                }
                GPAsummary.append(String.format("\nTotal Credits: %d\nGPA: %.2f\n", totalCredits, gpa));
                GPAsummary.append("-------------------\n\n");

                // Save and  display the GPA summary
                String selected = (String) storageOption.getSelectedItem();
                if ("File".equals(selected)) {
                    writeToFile(GPAsummary.toString());
                    outputArea.append(GPAsummary.toString());
                    JOptionPane.showMessageDialog(this, "GPA Summary saved to file.");
                } else {
                    JOptionPane.showMessageDialog(this, "Database under construction.");
                }

                // Clear all the data for the next student
                gradePoints.clear();
                creditHours.clear();
                courses.clear();
                nameField.setText("");
                idField.setText("");
                outputArea.append("All data cleared for the next student.\n");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Calculation error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }

    private void writeToFile(String content) throws IOException {
        try (FileWriter writer = new FileWriter("gpa_records.txt", true)) {
            writer.write(content + System.lineSeparator());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GPACalculator::new);
    }
}