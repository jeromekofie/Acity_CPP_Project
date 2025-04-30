import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class MyGPACalculator extends JFrame implements ActionListener {
    
    // Text fields for input
    private JTextField nameInput, idInput, courseInput, creditInput, gradeInput;
    // Dropdown for save option
    private JComboBox<String> saveOption;
    // Area to show results
    private JTextArea outputArea;
    // Buttons
    private JButton addBtn, calcBtn, saveBtn;
    
    // Lists to store grades and credits
    private ArrayList<Double> grades = new ArrayList<>();
    private ArrayList<Integer> credits = new ArrayList<>();
    // String to build output
    private StringBuilder outputText = new StringBuilder();
    
    public MyGPACalculator() {
        // Basic window setup
        super("My GPA Calculator");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 2, 5, 5));
        
        // Add components
        add(new JLabel("Name:"));
        nameInput = new JTextField();
        add(nameInput);
        
        add(new JLabel("ID:"));
        idInput = new JTextField();
        add(idInput);
        
        add(new JLabel("Course:"));
        courseInput = new JTextField();
        add(courseInput);
        
        add(new JLabel("Credits:"));
        creditInput = new JTextField();
        add(creditInput);
        
        add(new JLabel("Grade (A-F):"));
        gradeInput = new JTextField();
        add(gradeInput);
        
        add(new JLabel("Save to:"));
        saveOption = new JComboBox<>(new String[]{"Text File", "Database"});
        add(saveOption);
        
        // Buttons
        addBtn = new JButton("Add Course");
        calcBtn = new JButton("Calculate GPA");
        saveBtn = new JButton("Save Data");
        
        addBtn.addActionListener(this);
        calcBtn.addActionListener(this);
        saveBtn.addActionListener(this);
        
        add(addBtn);
        add(calcBtn);
        add(new JLabel(""));
        add(saveBtn);
        
        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JLabel("Results:"));
        add(outputArea);
        
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            addCourse();
        } else if (e.getSource() == calcBtn) {
            calculateGPA();
        } else if (e.getSource() == saveBtn) {
            saveData();
        }
    }
    
    private void addCourse() {
        try {
            String grade = gradeInput.getText().trim();
            double gradeValue = convertGrade(grade);
            
            if (gradeValue == -1) {
                showMessage("Invalid grade! Use A, B+, B, C+, C, D, E, or F");
                return;
            }
            
            int credit = Integer.parseInt(creditInput.getText().trim());
            
            grades.add(gradeValue);
            credits.add(credit);
            
            outputText.append(courseInput.getText())
                     .append(": Grade=").append(grade)
                     .append(", Credits=").append(credit)
                     .append("\n");
            
            // Clear input fields
            courseInput.setText("");
            creditInput.setText("");
            gradeInput.setText("");
            
        } catch (Exception ex) {
            showMessage("Please check your inputs - credits must be a number!");
        }
    }
    
    private void calculateGPA() {
        double totalPoints = 0;
        int totalCredits = 0;
        
        for (int i = 0; i < grades.size(); i++) {
            totalPoints += grades.get(i) * credits.get(i);
            totalCredits += credits.get(i);
        }
        
        double gpa = totalCredits > 0 ? totalPoints / totalCredits : 0;
        outputText.append("Overall GPA: ").append(String.format("%.2f", gpa)).append("\n");
        outputArea.setText(outputText.toString());
    }
    
    private double convertGrade(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "B+": return 3.5;
            case "B": return 3.0;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "D": return 1.5;
            case "E": return 1.0;
            case "F": return 0.0;
            default: return -1;
        }
    }
    
    private void saveData() {
        String option = saveOption.getSelectedItem().toString();
        
        if (option.equals("Text File")) {
            try (FileWriter fw = new FileWriter("student_gpa.txt", true)) {
                fw.write("Student: " + nameInput.getText() + " (ID: " + idInput.getText() + ")\n");
                fw.write(outputText.toString() + "\n");
                outputArea.setText("Data saved to student_gpa.txt");
            } catch (IOException e) {
                showMessage("Error saving to file!");
            }
        } else {
            outputArea.setText("Database option not implemented yet");
        }
    }
    
    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
    
    public static void main(String[] args) {
        // Start the program
        new MyGPACalculator();
    }
}