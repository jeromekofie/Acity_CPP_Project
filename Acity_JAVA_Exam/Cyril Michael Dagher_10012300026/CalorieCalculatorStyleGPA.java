import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class StyledGPACalculatorFrame extends JFrame {
    private JLabel gpaLabel;
    private JLabel summaryLabel;
    private JTextField nameField;
    private JTextField idField;
    private ArrayList<JTextField> courseFields;
    private ArrayList<JTextField> creditFields;
    private ArrayList<JTextField> gradeFields;
    private JComboBox<String> saveOptionBox;

    public StyledGPACalculatorFrame() {
        setTitle("GPA Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // Left panel: GPA result and summary
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(110, 81, 160));
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

        // Name, ID
        JPanel infoPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        infoPanel.setMaximumSize(new Dimension(700, 40));
        infoPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        infoPanel.add(nameField);
        infoPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        infoPanel.add(idField);
        rightPanel.add(infoPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Course inputs
        JPanel coursePanel = new JPanel(new GridLayout(5, 2, 10, 10));
        courseFields = new ArrayList<>();
        creditFields = new ArrayList<>();
        gradeFields = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            courseFields.add(new JTextField());
            courseFields.get(i).setMaximumSize(new Dimension(200, 20));
            coursePanel.add(new JLabel("Course " + (i+1) + ":"));
            coursePanel.add(courseFields.get(i));

            creditFields.add(new JTextField());
            creditFields.get(i).setMaximumSize(new Dimension(50, 20));
            coursePanel.add(new JLabel("Credit " + (i+1) + ":"));
            coursePanel.add(creditFields.get(i));

            gradeFields.add(new JTextField());
            gradeFields.get(i).setMaximumSize(new Dimension(50, 20));
            coursePanel.add(new JLabel("Grade " + (i+1) + ":"));
            coursePanel.add(gradeFields.get(i));
        }
        rightPanel.add(coursePanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Save option
        saveOptionBox = new JComboBox<>(new String[]{"File", "Database"});
        rightPanel.add(new JLabel("Save to:"));
        rightPanel.add(saveOptionBox);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Calculate button
        JButton calculateButton = new JButton("Calculate GPA");
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(calculateButton);

        // Add components to frame
        add(leftPanel);
        add(rightPanel);

        // Add event listeners
        calculateButton.addActionListener(this::calculateAndSave);
    }

    private void calculateAndSave(ActionEvent e) {
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
            saveToFile(name, id, courses, credits, grades, gpa);
        } else {
            saveToDatabase(name, id, courses, credits, grades, gpa);
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

    private double letterToPoint(String grade) {
        switch (grade) {
            case "A":
                return 4.0;
            case "B+":
                return 3.5;
            case "B":
                return 3.0;
            case "C+":
                return 2.5;
            case "C":
                return 2.0;
            case "D":
                return 1.5;
            case "E":
                return 1.0;
            case "F":
                return 0.0;
            default:
                throw new IllegalArgumentException("Invalid grade format");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StyledGPACalculatorFrame().setVisible(true));
    }
} 