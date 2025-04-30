import java.awt.*;
import java.io.*;
import javax.swing.*;

public class GPACalc extends JFrame {
    private JTextField nameField, idField, coursesField, creditsField, gradesField;
    private JTextArea outputArea;
    private JComboBox<String> saveMethodBox;

    public GPACalc() {
        setTitle("Kekeli's GPA Calculator");
        setSize(600, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color bgColor = new Color(245, 245, 255); 
        Color inputColor = new Color(255, 255, 255);
        Color buttonColor = new Color(70, 130, 180);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 20);

        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        inputPanel.setBackground(bgColor);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel title = new JLabel("GPA Calculator");
        title.setFont(titleFont);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(title, BorderLayout.NORTH);
       
        inputPanel.add(makeLabel("Student Name:", labelFont));
        nameField = makeTextField(inputColor);
        inputPanel.add(nameField);

        inputPanel.add(makeLabel("Student ID:", labelFont));
        idField = makeTextField(inputColor);
        inputPanel.add(idField);

        inputPanel.add(makeLabel("Courses:", labelFont));
        coursesField = makeTextField(inputColor);
        inputPanel.add(coursesField);

        inputPanel.add(makeLabel("Credit Hours:", labelFont));
        creditsField = makeTextField(inputColor);
        inputPanel.add(creditsField);

        inputPanel.add(makeLabel("Grades:", labelFont));
        gradesField = makeTextField(inputColor);
        inputPanel.add(gradesField);

        inputPanel.add(makeLabel("Save To:", labelFont));
        saveMethodBox = new JComboBox<>(new String[]{"File", "Database"});
        inputPanel.add(saveMethodBox);

        add(inputPanel, BorderLayout.CENTER);
        JButton calcButton = new JButton("Calculate GPA");
        calcButton.setBackground(buttonColor);
        calcButton.setForeground(Color.WHITE);
        calcButton.setFocusPainted(false);
        calcButton.setFont(labelFont);
        calcButton.addActionListener(e -> calculateGPA());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(bgColor);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        bottomPanel.add(calcButton, BorderLayout.NORTH);

        outputArea = new JTextArea(6, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setBackground(Color.WHITE);
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        getContentPane().setBackground(bgColor);
        setVisible(true);
    }

    private JLabel makeLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private JTextField makeTextField(Color bg) {
        JTextField field = new JTextField();
        field.setBackground(bg);
        return field;
    }

    private void calculateGPA() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        String[] courses = coursesField.getText().split(",");
        String[] credits = creditsField.getText().split(",");
        String[] grades = gradesField.getText().split(",");

        if (courses.length != credits.length || credits.length != grades.length) {
            outputArea.setText("Error!! The courses, credits, grades aren't the same length.");
            return;
        }

        try{
            double totalPoints = 0;
            int totalCredits = 0;

            for (int i = 0; i < grades.length; i++) {
                int credit = Integer.parseInt(credits[i].trim());
                double point = getGradePoint(grades[i].trim());
                totalPoints += point * credit;
                totalCredits += credit;
            }

            double gpa = totalPoints / totalCredits;

            String result = "Name: " + name + "\n" +
                            "Student ID: " + id + "\n" +
                            "GPA: " + String.format("%.2f", gpa) + "\n";

            if (saveMethodBox.getSelectedItem().equals("File")) {
                saveToFile(result);
            } else {
                outputArea.setText("Database: Under construction");
            }

        } catch (NumberFormatException e) {
            outputArea.setText("Error!! Please enter valid numbers for credit hours.");
        }
    }

    private double getGradePoint(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "B+": return 3.5;
            case "B": return 3.0;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "D": return 1.5;
            case "E": return 1.0;
            default: return 0.0;
        }
    }

    private void saveToFile(String data) {
        try (FileWriter fw = new FileWriter("gpa_records.txt", true)) {
            fw.write(data + "\n---------------------\n");
            outputArea.setText("Saved to file successfully!\n\n" + data);
        } catch (IOException e) {
            outputArea.setText("Error saving to file.");
        }
    }

    public static void main(String[] args) {
        new GPACalc();
    }
}
