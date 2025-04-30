import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GPACalculatorFrame().setVisible(true));
    }
}

class GPACalculatorFrame extends JFrame {
    private JTextField studentNameField;
    private JTextField studentIdField;
    private JTextField courseField;
    private JTextField creditHoursField;
    private JTextField gradesField;
    private JComboBox<String> saveOptionComboBox;
    private JTextArea resultArea;

    public GPACalculatorFrame() {
        setTitle("GPA Calculator");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 3, 11, 11));

        // Student Name
        add(new JLabel("Student Name:"));
        studentNameField = new JTextField();
        add(studentNameField);

        // Student ID
        add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        add(studentIdField);

        // Course
        add(new JLabel("Course:"));
        courseField = new JTextField();
        add(courseField);

        // Credit Hours
        add(new JLabel("Credit Hours:"));
        creditHoursField = new JTextField();
        add(creditHoursField);

        // Grades
        add(new JLabel("Grades:"));
        gradesField = new JTextField();
        add(gradesField);

        // Save Option
        add(new JLabel("Save Option:"));
        saveOptionComboBox = new JComboBox<>(new String[]{"File", "Database"});
        add(saveOptionComboBox);

        // Calculate Button
        JButton calculateButton = new JButton("Calculate");
        add(calculateButton);

        // Result Area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea));

        // Empty space for layout alignment
        add(new JLabel());

        // Action Listener for Calculate Button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {String studentName = studentNameField.getText();
                String studentId = studentIdField.getText();
                String course = courseField.getText();
                int creditHours = Integer.parseInt(creditHoursField.getText());
                double grades = Double.parseDouble(gradesField.getText());
                double gpa = grades / creditHours;
    
                String saveOption = (String) saveOptionComboBox.getSelectedItem();
                if ("File".equals(saveOption)) {
                    resultArea.setText("Student Name: " + studentName + "\n"
                            + "Student ID: " + studentId + "\n"
                            + "Course: " + course + "\n"
                            + "Credit Hours: " + creditHours + "\n"
                            + "Grades: " + grades + "\n"
                            + "GPA: " + gpa + "\n"
                            + "File: yet to be implemented");
                } else if ("Database".equals(saveOption)) {
                    resultArea.setText("Student Name: " + studentName + "\n"
                            + "Student ID: " + studentId + "\n"
                            + "Course: " + course + "\n"
                            + "Credit Hours: " + creditHours + "\n"
                            + "Grades: " + grades + "\n"
                            + "GPA: " + gpa + "\n"
                            + "Database: under construction");
                }
                calculateGPA();
            }
        });
    }

    private void calculateGPA() {
        try {
            String studentName = studentNameField.getText();
            String studentId = studentIdField.getText();
            String course = courseField.getText();
            int creditHours = Integer.parseInt(creditHoursField.getText());
            double grades = Double.parseDouble(gradesField.getText());
            double gpa = grades / creditHours;

            String saveOption = (String) saveOptionComboBox.getSelectedItem();
            if ("File".equals(saveOption)) {
                resultArea.setText("Student Name: " + studentName + "\n"
                        + "Student ID: " + studentId + "\n"
                        + "Course: " + course + "\n"
                        + "Credit Hours: " + creditHours + "\n"
                        + "Grades: " + grades + "\n"
                        + "GPA: " + gpa + "\n"
                        + "File: yet to be implemented");
            } else if ("Database".equals(saveOption)) {
                resultArea.setText("Student Name: " + studentName + "\n"
                        + "Student ID: " + studentId + "\n"
                        + "Course: " + course + "\n"
                        + "Credit Hours: " + creditHours + "\n"
                        + "Grades: " + grades + "\n"
                        + "GPA: " + gpa + "\n"
                        + "Database: under construction");
            }
        } catch (NumberFormatException ex) {
            resultArea.setText("Error: Please enter valid numeric values for Credit Hours and Grades.");
        }
    }
}