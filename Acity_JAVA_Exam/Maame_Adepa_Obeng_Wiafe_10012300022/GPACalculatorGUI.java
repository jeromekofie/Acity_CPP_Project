import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GPACalculatorGUI extends JFrame {
    private JTextField nameField;
    private JTextField idField;
    private JComboBox<String> storageTypeCombo;
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JButton addCourseButton;
    private JButton calculateButton;
    private JButton clearButton;

    public GPACalculatorGUI() {
        setTitle("GPA Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        initializeComponents();
        setupLayout();
        setupEventListeners();
    }

    private void initializeComponents() {
        nameField = new JTextField(20);
        idField = new JTextField(20);

        String[] storageTypes = {"File", "Database"};
        storageTypeCombo = new JComboBox<>(storageTypes);


        String[] columnNames = {"Course", "Credit Hours", "Grade"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        courseTable = new JTable(tableModel);
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addCourseButton = new JButton("Add Course");
        calculateButton = new JButton("Calculate GPA");
        clearButton = new JButton("Clear All");
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Student ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Storage Type:"));
        inputPanel.add(storageTypeCombo);

        JScrollPane tableScrollPane = new JScrollPane(courseTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addCourseButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);

        add(inputPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {

        addCourseButton.addActionListener(e -> addCourse());


        calculateButton.addActionListener(e -> calculateGPA());


        clearButton.addActionListener(e -> clearAll());
    }

    private void addCourse() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField courseField = new JTextField();
        JTextField creditHoursField = new JTextField();
        JComboBox<String> gradeCombo = new JComboBox<>(new String[]{"A","B+", "B", "C+", "C", "D", "E", "F"});

        panel.add(new JLabel("Course Name:"));
        panel.add(courseField);
        panel.add(new JLabel("Credit Hours:"));
        panel.add(creditHoursField);
        panel.add(new JLabel("Grade:"));
        panel.add(gradeCombo);

        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Add Course", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String course = courseField.getText().trim();
            String creditHours = creditHoursField.getText().trim();
            String grade = (String) gradeCombo.getSelectedItem();

            if (course.isEmpty() || creditHours.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please fill in all fields!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double hours = Double.parseDouble(creditHours);
                if (hours <= 0) {
                    throw new NumberFormatException();
                }
                tableModel.addRow(new Object[]{course, creditHours, grade});
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid number for credit hours!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void calculateGPA() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Please add at least one course!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nameField.getText().trim().isEmpty() || idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter student name and ID!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        double totalPoints = 0;
        double totalCreditHours = 0;

        // Calculate GPA
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            double creditHours = Double.parseDouble(tableModel.getValueAt(i, 1).toString());
            String grade = tableModel.getValueAt(i, 2).toString();
            
            double gradePoints = getGradePoints(grade);
            totalPoints += gradePoints * creditHours;
            totalCreditHours += creditHours;
        }

        double gpa = totalPoints / totalCreditHours;
        
        // Save results
        saveResults(gpa);
        
        // Show result
        JOptionPane.showMessageDialog(this, 
            String.format("GPA: %.2f", gpa),
            "GPA Calculation Result",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private double getGradePoints(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "B+": return 3.5;
            case "B": return 3.0;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "D": return 1.5;
            case "E": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }

    private void saveResults(double gpa) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("gpa-RecordedStackTrace.txt"))) {
            writer.println("Student Name: " + nameField.getText());
            writer.println("Student ID: " + idField.getText());
            writer.println("Storage Type: " + storageTypeCombo.getSelectedItem());
            writer.println("\nCourses:");
            
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String course = tableModel.getValueAt(i, 0).toString();
                String creditHours = tableModel.getValueAt(i, 1).toString();
                String grade = tableModel.getValueAt(i, 2).toString();
                writer.println(course + " - " + creditHours + " credits - Grade: " + grade);
            }
            
            writer.println("\nGPA: " + String.format("%.2f", gpa));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error saving results: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearAll() {
        nameField.setText("");
        idField.setText("");
        storageTypeCombo.setSelectedIndex(0);
        tableModel.setRowCount(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GPACalculatorGUI().setVisible(true);
        });
    }
} 