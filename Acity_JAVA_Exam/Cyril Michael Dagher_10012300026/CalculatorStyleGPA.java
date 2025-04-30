import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class CalculatorStyleGPA {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StyledGPACalculatorFrame());
    }
}

class StyledGPACalculatorFrame extends JFrame {
    // Input fields
    private JTextField nameField, idField, ageField;
    private JComboBox<String> genderBox;
    private ArrayList<JTextField> courseFields = new ArrayList<>();
    private ArrayList<JTextField> creditFields = new ArrayList<>();
    private ArrayList<JTextField> gradeFields = new ArrayList<>();
    private JComboBox<String> saveOptionBox;
    private JPanel coursesPanel;
    private JLabel gpaLabel, summaryLabel;
    private int courseCount = 3;

    public StyledGPACalculatorFrame() {
        setTitle("GPA Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // Left panel: GPA result and summary
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(32, 30, 80));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        gpaLabel = new JLabel("0.00");
        gpaLabel.setFont(new Font("SansSerif", Font.BOLD, 64));
        gpaLabel.setForeground(Color.WHITE);
        gpaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(gpaLabel);

        JLabel estLabel = new JLabel("ESTIMATED GPA");
        estLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        estLabel.setForeground(Color.WHITE);
        estLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(estLabel);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        summaryLabel = new JLabel("Enter your details to calculate GPA");
        summaryLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        summaryLabel.setForeground(Color.WHITE);
        summaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(summaryLabel);

        // Right panel: Inputs
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("GPA CALCULATOR");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(title);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Name, ID, Gender, Age
        JPanel infoPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        infoPanel.setMaximumSize(new Dimension(700, 50));
        infoPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        infoPanel.add(nameField);
        infoPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        infoPanel.add(idField);
        infoPanel.add(new JLabel("Gender:"));
        genderBox = new JComboBox<>(new String[]{"Female", "Male"});
        infoPanel.add(genderBox);
        infoPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        infoPanel.add(ageField);
        rightPanel.add(infoPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Courses
        coursesPanel = new JPanel(new GridLayout(courseCount + 1, 3, 5, 5));
        coursesPanel.add(new JLabel("Course Name"));
        coursesPanel.add(new JLabel("Credit Hours"));
        coursesPanel.add(new JLabel("Grade"));
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
        rightPanel.add(coursesPanel);
        JButton addCourseBtn = new JButton("Add Course");
        addCourseBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addCourseBtn.addActionListener(e -> addCourseRow());
        rightPanel.add(addCourseBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Save option
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        savePanel.add(new JLabel("Save to:"));
        saveOptionBox = new JComboBox<>(new String[]{"File", "Database"});
        savePanel.add(saveOptionBox);
        rightPanel.add(savePanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Calculate button
        JButton calcBtn = new JButton("CALCULATE GPA");
        calcBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        calcBtn.setBackground(new Color(110, 81, 160));
        calcBtn.setForeground(Color.WHITE);
        calcBtn.setFocusPainted(false);
        calcBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        calcBtn.addActionListener(e -> calculateAndSave());
        rightPanel.add(calcBtn);

        add(leftPanel);
        add(rightPanel);
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
        String gender = (String) genderBox.getSelectedItem();
        String age = ageField.getText().trim();
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
            summaryLabel.setText("Please enter at least one valid course.");
            gpaLabel.setText("0.00");
            return;
        }
        double totalPoints = 0, totalCredits = 0;
        for (int i = 0; i < courses.size(); i++) {
            totalPoints += grades.get(i) * credits.get(i);
            totalCredits += credits.get(i);
        }
        double gpa = totalPoints / totalCredits;
        gpaLabel.setText(String.format("%.2f", gpa));
        summaryLabel.setText("GPA calculated for " + name + " (ID: " + id + ")");
        String saveOption = (String) saveOptionBox.getSelectedItem();
        if (saveOption.equals("File")) {
            saveToFile(name, id, gender, age, courses, credits, grades, gpa);
        } else {
            saveToDatabase(name, id, gender, age, courses, credits, grades, gpa);
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

    private void saveToFile(String name, String id, String gender, String age, ArrayList<String> courses, ArrayList<Integer> credits, ArrayList<Double> grades, double gpa) {
        try (PrintWriter out = new PrintWriter(new FileWriter("gpa_recordss.txt", true))) {
            out.println("Name: " + name);
            out.println("ID: " + id);
            out.println("Gender: " + gender);
            out.println("Age: " + age);
            for (int i = 0; i < courses.size(); i++) {
                out.println("\nCourse: " + courses.get(i) + ", \nCredit: " + credits.get(i) + ", \nGrade: " + grades.get(i));
            }
            out.println("GPA: " + String.format("%.2f", gpa));
            out.println("----------------------\n");
            JOptionPane.showMessageDialog(this, "GPA calculated: " + String.format("%.2f", gpa) + "\nSuccessfully saved to file.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to file.");
        }
    }

    private void saveToDatabase(String name, String id, String gender, String age, ArrayList<String> courses, ArrayList<Integer> credits, ArrayList<Double> grades, double gpa) {
        // Placeholder for database code
        JOptionPane.showMessageDialog(this, "GPA calculated: " + String.format("%.2f", gpa) + "\nDATABASE UNDER CONSTRUCTION ");
    }
} 