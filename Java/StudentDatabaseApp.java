import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class StudentDatabaseApp extends JFrame {
    private JTextField nameField, ageField, courseField;
    private JTextArea outputArea;

    public StudentDatabaseApp() {
        setTitle("Student Database");
        setSize(400, 300);

        
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField(10);
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField(5);
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("Course:"));
        courseField = new JTextField(10);
        inputPanel.add(courseField);

        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(e -> addStudent());
        inputPanel.add(addButton);

       
        outputArea = new JTextArea(10, 20);

        
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        setVisible(true);
    }

    private void addStudent() {
        try {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String course = courseField.getText();

            FileWriter writer = new FileWriter("students.txt", true);
            writer.write(name + "," + age + "," + course + "\n");
            writer.close();

            outputArea.append("Student added!\n");
        } catch (Exception e) {
            outputArea.append("Error: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        new StudentDatabaseApp();
    }
}
