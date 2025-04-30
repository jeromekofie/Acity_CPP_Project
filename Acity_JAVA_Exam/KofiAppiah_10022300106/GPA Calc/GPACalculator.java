import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.util.HashMap;

public class GPACalculator {

    public static void main(String[] args) {
        JFrame frame = new JFrame("GPA Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new GridLayout(9, 2));

        
        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();
        JTextField coursesField = new JTextField();
        JTextField creditHoursField = new JTextField();
        JTextField gradesField = new JTextField();
        JButton calculateBtn = new JButton("Calculate GPA");
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        
        frame.add(new JLabel("Student Name:"));
        frame.add(nameField);
        frame.add(new JLabel("Student ID:"));
        frame.add(idField);
        frame.add(new JLabel("Courses :"));
        frame.add(coursesField);
        frame.add(new JLabel("Credit Hours :"));
        frame.add(creditHoursField);
        frame.add(new JLabel("Grades :"));
        frame.add(gradesField);
        frame.add(calculateBtn);
        frame.add(new JLabel());
        frame.add(new JLabel("Result:"));
        frame.add(resultArea);

        
        HashMap<String, Double> gradePoints = new HashMap<>();
        gradePoints.put("A", 4.0);
        gradePoints.put("B+", 3.5);
        gradePoints.put("B", 3.0);
        gradePoints.put("C+", 2.5);
        gradePoints.put("C", 2.0);
        gradePoints.put("D", 1.5);
        gradePoints.put("E", 1.0);
        gradePoints.put("F", 0.0);

        calculateBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String id = idField.getText().trim();
                String[] courses = coursesField.getText().split(",");
                String[] credits = creditHoursField.getText().split(",");
                String[] grades = gradesField.getText().split(",");

                if (courses.length != credits.length || credits.length != grades.length) {
                    resultArea.setText("Error: Courses, credits, and grades must match in number.");
                    return;
                }

                double totalPoints = 0;
                int totalCredits = 0;

                for (int i = 0; i < grades.length; i++) {
                    int credit = Integer.parseInt(credits[i].trim());
                    String grade = grades[i].trim().toUpperCase();
                    if (!gradePoints.containsKey(grade)) {
                        resultArea.setText("Error: Invalid grade entered - " + grade);
                        return;
                    }
                    totalPoints += gradePoints.get(grade) * credit;
                    totalCredits += credit;
                }

                double gpa = totalPoints / totalCredits;

                String record = "Name: " + name + "\n"
                        + "ID: " + id + "\n"
                        + "Courses: " + String.join(", ", courses) + "\n"
                        + "Credits: " + String.join(", ", credits) + "\n"
                        + "Grades: " + String.join(", ", grades) + "\n"
                        + "GPA: " + String.format("%.2f", gpa) + "\n"
                        + "Database: Under Construction.";

                FileWriter writer = new FileWriter("gpa_r@cords.txt", true);
                writer.write(record);
                writer.close();

                resultArea.setText(record);

            } catch (Exception ex) {
                resultArea.setText("Error: " + ex.getMessage());
            }
        });

        frame.setVisible(true);
    }
}