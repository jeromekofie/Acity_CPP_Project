import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReportCard {
    private JFrame frame;
    private JTextField nameField, rollField;
    private JComboBox<String> courseComboBox;
    private JTextArea resultArea;
    private JButton submitButton;
    private static final int MAX_SUBJECTS = 8;

    public ReportCard() {
        frame = new JFrame("Report Card System");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(0, 100, 0)); // Deep Green

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(null);
        inputPanel.setBackground(new Color(34, 139, 34)); // Darker Green
        inputPanel.setBorder(BorderFactory.createTitledBorder("DETAILS:"));
        inputPanel.setPreferredSize(new Dimension(350, 500));

        JLabel nameLabel = new JLabel("NAME:");
        nameLabel.setBounds(20, 30, 100, 25);
        nameField = new JTextField(20);
        nameField.setBounds(20, 55, 300, 25);

        JLabel rollLabel = new JLabel("ID:");
        rollLabel.setBounds(20, 90, 100, 25);
        rollField = new JTextField(20);
        rollField.setBounds(20, 115, 300, 25);

        JLabel courseLabel = new JLabel("COURSE:");
        courseLabel.setBounds(20, 150, 100, 25);
        String[] courses = {"Computer Science", "Information Technology", "Business Administration"};
        courseComboBox = new JComboBox<>(courses);
        courseComboBox.setBounds(20, 175, 300, 25);

        submitButton = new JButton("Submit");
        submitButton.setBounds(20, 220, 300, 30);

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(rollLabel);
        inputPanel.add(rollField);
        inputPanel.add(courseLabel);
        inputPanel.add(courseComboBox);
        inputPanel.add(submitButton);

        frame.add(inputPanel, BorderLayout.WEST);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("STUDENTS:"));

        // Result area setup
        resultArea = new JTextArea(20, 30);  // Increased row count to make the result area longer
        resultArea.setBackground(Color.LIGHT_GRAY);
        resultArea.setEditable(false);
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.NORTH);

        frame.add(resultPanel, BorderLayout.CENTER);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });

        frame.setVisible(true);
    }

    private void generateReport() {
        String name = nameField.getText();
        int roll = Integer.parseInt(rollField.getText());
        String course = (String) courseComboBox.getSelectedItem();
        float total = 0;
        String[] subjects = new String[MAX_SUBJECTS];
        String[] remarks = new String[MAX_SUBJECTS];

        if (course.equals("Computer Science")) {
            subjects[0] = "Programming in C++ ";
            subjects[1] = "Data Structures    ";
            subjects[2] = "Database Systems   ";
            subjects[3] = "Operations Research";
            subjects[4] = "Calculus & Matlab  ";
            subjects[5] = "Leadership Seminar ";
            subjects[6] = "Logic and Design   ";
            subjects[7] = "F. I .E            ";
        } else if (course.equals("Information Technology")) {
            subjects[0] = "Business in Africa   ";
            subjects[1] = "Web Development      ";
            subjects[2] = "System Administration";
            subjects[3] = "Database Systems     ";
            subjects[4] = "Cybersecurity        ";
            subjects[5] = "Leadership Seminar   ";
            subjects[6] = "Data Structures      ";
            subjects[7] = "F. I .E              ";
        } else if (course.equals("Business Administration")) {
            subjects[0] = "Management Principles";
            subjects[1] = "Marketing            ";
            subjects[2] = "Accounting           ";
            subjects[3] = "Business Law         ";
            subjects[4] = "Economics            ";
            subjects[5] = "Leadership Seminar   ";
            subjects[6] = "I. P. Law            ";
            subjects[7] = "F. I .E              ";
        }

        StringBuilder report = new StringBuilder();
        report.append("\n--------- Report Card ---------\n");
        report.append("Name       : ").append(name).append("\n");
        report.append("Roll Number: ").append(roll).append("\n");
        report.append("Course     : ").append(course).append("\n\n");

        for (int i = 0; i < MAX_SUBJECTS; i++) {
            float classScore = Float.parseFloat(JOptionPane.showInputDialog("Enter class score for " + subjects[i] + " (out of 40): "));
            while (classScore < 0 || classScore > 40) {
                classScore = Float.parseFloat(JOptionPane.showInputDialog("Invalid score! Please enter a value between 0 and 40: "));
            }

            float examScore = Float.parseFloat(JOptionPane.showInputDialog("Enter exam score for " + subjects[i] + " (out of 60): "));
            while (examScore < 0 || examScore > 60) {
                examScore = Float.parseFloat(JOptionPane.showInputDialog("Invalid score! Please enter a value between 0 and 60: "));
            }

            float subjectTotal = classScore + examScore;
            total += subjectTotal;

            char grade = calculateGrade(subjectTotal);

            report.append(subjects[i]).append("\t: ").append(subjectTotal).append(" | Grade: \t\t ").append(grade).append("\n");
        }

        float average = total / MAX_SUBJECTS;

        // Generate remarks based on the average
        String remarks = getRemarks(average);

        report.append("\nTotal Marks: ").append(total).append("\n");
        report.append("CWA        : ").append(average).append("\n");
        report.append("Remarks    : ").append(remarks).append("\n");

        resultArea.setText(report.toString());

        // Save report to file
        try (FileWriter outToFile = new FileWriter(new File("reportcard.txt"), true)) {
            outToFile.write(report.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(frame, "Report Card Generated Successfully!");
    }

    private char calculateGrade(float subjectTotal) {
        if (subjectTotal >= 90) {
            return 'A';
        } else if (subjectTotal >= 75) {
            return 'B';
        } else if (subjectTotal >= 50) {
            return 'C';
        } else {
            return 'D';
        }
    }

    private String getRemarks(float average) {
        if (average >= 90) {
            return "Excellent performance. More vim.";
        } else if (average >= 75) {
            return "Good job. You force.";
        } else if (average >= 50) {
            return "You try. There's room for improvement.";
        } else {
            return "Needs improvement. Don't give up.";
        }
    }

    public static void main(String[] args) {
        new ReportCard();
    }
}
