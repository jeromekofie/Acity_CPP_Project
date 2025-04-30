import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GPACalculatorFrame());
    }
}

class GPACalculatorFrame extends JFrame {
    private JTextField nameField, idField;
    private ArrayList<JTextField> courseFields = new ArrayList<>();
    private ArrayList<JTextField> creditFields = new ArrayList<>();
    private ArrayList<JTextField> gradeFields = new ArrayList<>();
    private JComboBox<String> saveOptionBox;
    private JPanel coursesPanel;
    private int courseCount = 3; // Default number of courses

    public GPACalculatorFrame() {
        setTitle("CYRIL GPA Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        // Courses panel
        coursesPanel = new JPanel(new GridLayout(courseCount + 1, 3, 5, 5));
        coursesPanel.add(new JLabel("Course Name"));
        coursesPanel.add(new JLabel("Credit Hours"));
        coursesPanel.add(new JLabel("Grade (A-F)"));
        for (int i = 0; i < courseCount; i++) {
            JTextField course = new JTextField();
            JTextField credit = new JTextField();
            JTextField grade = new JTextField();
            courseFields.add(course);
            creditFields.add(credit);
            gradeFields.add(grade);
            coursesPanel.add(course);
            coursesPanel.add(credit);
            coursesPanel.add(grade);
        }
        JButton addCourseBtn = new JButton("Add Course");
        addCourseBtn.addActionListener(e -> addCourseRow());

        // Save option
        inputPanel.add(new JLabel("Save to:"));
        saveOptionBox = new JComboBox<>(new String[]{"File", "Database"});
        inputPanel.add(saveOptionBox);

        // Calculate button
        JButton calcBtn = new JButton("Calculate GPA");
        calcBtn.addActionListener(e -> calculateAndSave());

        // Layout
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(coursesPanel, BorderLayout.CENTER);
        centerPanel.add(addCourseBtn, BorderLayout.SOUTH);

        add(inputPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(calcBtn, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addCourseRow() {
        JTextField course = new JTextField();
        JTextField credit = new JTextField();
        JTextField grade = new JTextField();
        courseFields.add(course);
        creditFields.add(credit);
        gradeFields.add(grade);
        coursesPanel.setLayout(new GridLayout(courseFields.size() + 1, 3, 5, 5));
        coursesPanel.add(course);
        coursesPanel.add(credit);
        coursesPanel.add(grade);
        coursesPanel.revalidate();
        coursesPanel.repaint();
        pack();
    }

    private void calculateAndSave() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        ArrayList<String> courses = new ArrayList<>();
        ArrayList<Integer> credits = new ArrayList<>();
        ArrayList<Double> grades = new ArrayList<>();
        for (int i = 0; i < courseFields.size(); i++) {
            String course = courseFields.get(i).getText().trim();
            String creditStr = creditFields.get(i).getText().trim();
            String gradeStr = gradeFields.get(i).getText().trim().toUpperCase().replace(" ", "");
            if (course.isEmpty() || creditStr.isEmpty() || gradeStr.isEmpty()) continue;
            try {
                int credit = Integer.parseInt(creditStr);
                double gradePoint = letterToPoint(gradeStr);
                courses.add(course);
                credits.add(credit);
                grades.add(gradePoint);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input in course row " + (i+1) + 
                    "\nValid grades are: A (4.0), B+ (3.5), B (3.0), C+ (2.5), C (2.0), D (1.5), E (1.0), F (0.0)");
                return;
            }
        }
        if (courses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter at least one valid course.");
            return;
        }
        double totalPoints = 0, totalCredits = 0;
        for (int i = 0; i < courses.size(); i++) {
            totalPoints += grades.get(i) * credits.get(i);
            totalCredits += credits.get(i);
        }
        double gpa = totalPoints / totalCredits;
        String saveOption = (String) saveOptionBox.getSelectedItem();
        if (saveOption.equals("File")) {
            saveToFile(name, id, courses, credits, grades, gpa);
        } else {
            saveToDatabase(name, id, courses, credits, grades, gpa);
        }
    }

    private double letterToPoint(String grade) {
        switch (grade) {
            case "A": return 4.0;
            case "B+": return 3.5;
            case "B": return 3.0;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "D": return 1.5;
            case "E": return 1.0;
            case "F": return 0.0;
            default: throw new IllegalArgumentException("Invalid grade: " + grade);
        }
    }

    private void saveToFile(String name, String id, ArrayList<String> courses, ArrayList<Integer> credits, ArrayList<Double> grades, double gpa) {
        try (PrintWriter out = new PrintWriter(new FileWriter("gpa_records.txt", true))) {
            out.println("Name: " + name);
            out.println("ID: " + id);
            for (int i = 0; i < courses.size(); i++) {
                out.println("Course: " + courses.get(i) + ", Credit: " + credits.get(i) + ", Grade: " + grades.get(i));
            }
            out.println("GPA: " + String.format("%.2f", gpa));
            out.println("----------------------");
            JOptionPane.showMessageDialog(this, "GPA calculated: " + String.format("%.2f", gpa) + "\nSaved to file.\nDatabase: under construction");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to file.");
        }
    }

    private void saveToDatabase(String name, String id, ArrayList<String> courses, ArrayList<Integer> credits, ArrayList<Double> grades, double gpa) {
        // Placeholder for database code
        JOptionPane.showMessageDialog(this, "GPA calculated: " + String.format("%.2f", gpa) + "\nfile : yet to be implemented");
    }
}
