import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField;
    private JTextArea coursesArea, creditsArea, gradesArea;
    private JCheckBox saveToFileCheck;
    private JTextArea displayArea;

    public GPACalculator() {
        setTitle("GPA Calculator");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        topPanel.add(new JLabel("Student Name:")); 
        nameField = new JTextField();
        topPanel.add(nameField);

        topPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        topPanel.add(idField);
        add(topPanel, BorderLayout.NORTH);

        
        JPanel middlePanel = new JPanel(new GridLayout(4, 2, 5, 5));
        middlePanel.add(new JLabel("Courses (one per line):"));
        coursesArea = new JTextArea(5, 20);
        middlePanel.add(new JScrollPane(coursesArea));

        middlePanel.add(new JLabel("Credit Hours (match lines):"));
        creditsArea = new JTextArea(5, 20);
        middlePanel.add(new JScrollPane(creditsArea));

        middlePanel.add(new JLabel("Grades (match lines):"));
        gradesArea = new JTextArea(5, 20);
        middlePanel.add(new JScrollPane(gradesArea));

        saveToFileCheck = new JCheckBox("Save to File");
        middlePanel.add(saveToFileCheck);

        JButton calcButton = new JButton("Calculate & Save");
        middlePanel.add(calcButton);
        add(middlePanel, BorderLayout.CENTER);

        
        displayArea = new JTextArea(10, 40);
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.SOUTH);

        
        calcButton.addActionListener(e -> calculateAndSave());

        setVisible(true); 
    }
    private void calculateAndSave() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        String[] courses = coursesArea.getText().trim().split("\n");
        String[] credits = creditsArea.getText().trim().split("\n");
        String[] grades = gradesArea.getText().trim().split("\n");


        if (name.isEmpty() || id.isEmpty() || courses.length == 0) {
            JOptionPane.showMessageDialog(this, "Please fill all fields else kelly will vex.");
            return;

        }

        if (courses.length != credits.length || courses.length != grades.length) {
            JOptionPane.showMessageDialog(this, "Mismatch in number of lines for courses, credits, or grades. masa do it well");
            return;
        }

        double totalPoints = 0.0;
        int totalCredits = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append("\n");
        sb.append("ID: ").append(id).append("\n");

        for (int i = 0; i < courses.length; i++) {
            String course = courses[i].trim();
            int credit;
            double gradePoint;

            try {
                credit = Integer.parseInt(credits[i].trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid credit hour at line " + (i + 1));
                return;
            }

            gradePoint = getGradePoint(grades[i].trim().toUpperCase());
            if (gradePoint < 0) {
                JOptionPane.showMessageDialog(this, "Invalid grade at line " + (i + 1));
                return;
            }

            totalPoints += gradePoint * credit;
            totalCredits += credit;

            sb.append("Course: ").append(course)
              .append(", Credit Hours: ").append(credit)
              .append(", Grade: ").append(grades[i].trim().toUpperCase())
              .append("\n");
        }

        double gpa = totalPoints / totalCredits;
        sb.append(String.format("Cumulative GPA: %.2f\n", gpa));
        sb.append("Database: under construction\n");
        if (gpa > 3.5){
            JOptionPane.showMessageDialog(this, "you be shark wai");
        }

        displayArea.setText(sb.toString());

        if (saveToFileCheck.isSelected()) {
            try (FileWriter writer = new FileWriter("gpa_records.txt", true)) {
                writer.write(sb.toString());
                writer.write("------------------------------\n");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving to file.");
            }
        }
    }

    private double getGradePoint(String grade) {
        Map<String, Double> map = new HashMap<>();
        map.put("A", 4.0);
        map.put("B+", 3.5);
        map.put("B", 3.0);
        map.put("C+", 2.5);
        map.put("C", 2.0);
        map.put("D", 1.5);
        map.put("E", 1.0);
        map.put("F", 0.0);
        return map.getOrDefault(grade, -1.0); 

      
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GPACalculator());
    }
}
