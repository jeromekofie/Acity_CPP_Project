import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField;
    private JComboBox<String> storageComboBox;
    private JButton addCourseBtn, calculateBtn, viewRecordsBtn, clearBtn;
    private JTextArea resultArea, coursesArea;
    private JScrollPane coursesScrollPane, resultScrollPane;
    
    private JPanel courseEntryPanel;
    private JTextField courseNameField, creditHoursField;
    private JComboBox<String> gradeComboBox;
    
    private List<Course> courses = new ArrayList<>();
    private static final String FILE_NAME = "gpa_records.txt";
    private static final DecimalFormat gpaFormat = new DecimalFormat("0.00");
    
    private static final Map<String, Double> GRADE_POINTS = new HashMap<>();
    static {
        GRADE_POINTS.put("A", 4.0);
        GRADE_POINTS.put("B+", 3.5);
        GRADE_POINTS.put("B", 3.0);
        GRADE_POINTS.put("C+", 2.5);
        GRADE_POINTS.put("C", 2.0);
        GRADE_POINTS.put("D", 1.5);
        GRADE_POINTS.put("E", 1.0);
        GRADE_POINTS.put("F", 0.0);
    }

    public GPACalculator() {
        setTitle("GPA Calculator");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        initComponents();
        
        add(createInputPanel(), BorderLayout.NORTH);
        add(createCoursesPanel(), BorderLayout.CENTER);
        add(createResultPanel(), BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        nameField = new JTextField(20);
        idField = new JTextField(20);
        
       
        storageComboBox = new JComboBox<>(new String[]{"File", "Database"});
        storageComboBox.setSelectedIndex(0);
        
        addCourseBtn = new JButton("Add Course");
        calculateBtn = new JButton("Calculate GPA");
        viewRecordsBtn = new JButton("View Records");
        clearBtn = new JButton("Clear All");
        
        
        resultArea = new JTextArea(10, 50);
        resultArea.setEditable(false);
        resultScrollPane = new JScrollPane(resultArea);
        
        coursesArea = new JTextArea(10, 50);
        coursesArea.setEditable(false);
        coursesScrollPane = new JScrollPane(coursesArea);
        
        courseNameField = new JTextField(15);
        creditHoursField = new JTextField(5);
        gradeComboBox = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D", "E", "F"});
        
        addCourseBtn.addActionListener(e -> addCourse());
        calculateBtn.addActionListener(e -> calculateGPA());
        viewRecordsBtn.addActionListener(e -> viewRecords());
        clearBtn.addActionListener(e -> clearAll());
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Student  Full Name:"), gbc);
        
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Student ID:"), gbc);
        
        gbc.gridx = 1;
        panel.add(idField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Save to:"), gbc);
        
        gbc.gridx = 1;
        panel.add(storageComboBox, gbc);
        
        courseEntryPanel = new JPanel();
        courseEntryPanel.add(new JLabel("Course Name:"));
        courseEntryPanel.add(courseNameField);
        courseEntryPanel.add(new JLabel("Credit Hours:"));
        courseEntryPanel.add(creditHoursField);
        courseEntryPanel.add(new JLabel("Grade:"));
        courseEntryPanel.add(gradeComboBox);
        courseEntryPanel.add(addCourseBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(courseEntryPanel, gbc);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(calculateBtn);
        buttonPanel.add(viewRecordsBtn);
        buttonPanel.add(clearBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Added Courses"));
        panel.add(coursesScrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Results"));
        panel.add(resultScrollPane, BorderLayout.CENTER);
        

        JLabel formulaLabel = new JLabel("GPA Formula: GPA = Σ (Grade Points × Credit Hours) / Σ(Credit Hours)");
        panel.add(formulaLabel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private void addCourse() {
        try {
            String courseName = courseNameField.getText().trim();
            String creditHoursStr = creditHoursField.getText().trim();
            String grade = (String) gradeComboBox.getSelectedItem();
            
            if (courseName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a course name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (creditHoursStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter credit hours", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double creditHours = Double.parseDouble(creditHoursStr);
            if (creditHours <= 0) {
                JOptionPane.showMessageDialog(this, "Credit hours must be positive", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Course course = new Course(courseName, creditHours, grade);
            courses.add(course);
            
            updateCoursesDisplay();
            
            courseNameField.setText("");
            creditHoursField.setText("");
            courseNameField.requestFocus();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid credit hours", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateCoursesDisplay() {
        StringBuilder sb = new StringBuilder();
        for (Course course : courses) {
            sb.append(String.format("%s - %.1f credits - Grade: %s\n", 
                course.getName(), course.getCreditHours(), course.getGrade()));
        }
        coursesArea.setText(sb.toString());
    }
    
    private void calculateGPA() {
        if (courses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one course", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String studentName = nameField.getText().trim();
        String studentId = idField.getText().trim();
        
        if (studentName.isEmpty() || studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter student name and ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double totalGradePoints = 0;
        double totalCreditHours = 0;
        
        for (Course course : courses) {
            double gradePoint = GRADE_POINTS.get(course.getGrade());
            totalGradePoints += gradePoint * course.getCreditHours();
            totalCreditHours += course.getCreditHours();
        }
        
        double gpa = totalGradePoints / totalCreditHours;
        
        resultArea.setText("");
        resultArea.append(String.format("Student Name: %s\n", studentName));
        resultArea.append(String.format("Student ID: %s\n", studentId));
        resultArea.append("\nCourse Details:\n");
        
        for (Course course : courses) {
            resultArea.append(String.format("%s - %.1f credits - Grade: %s (%.1f points)\n", 
                course.getName(), course.getCreditHours(), course.getGrade(), 
                GRADE_POINTS.get(course.getGrade())));
        }
        
        resultArea.append(String.format("\nTotal Credit Hours: %.1f\n", totalCreditHours));
        resultArea.append(String.format("GPA: %s\n", gpaFormat.format(gpa)));
        
        String storageOption = (String) storageComboBox.getSelectedItem();
        if (storageOption.equals("File")) {
            saveToFile(studentName, studentId, gpa);
        } else {
            resultArea.append("\nDatabase: Under Construction.\n");
        }
    }
    
    private void saveToFile(String studentName, String studentId, double gpa) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println("\n=== Student Record ===");
            writer.println("Student Name: " + studentName);
            writer.println("Student ID: " + studentId);
            writer.println("---------------------");
            
            for (Course course : courses) {
                writer.println(String.format("Course: %-20s Credits: %-5.1f Grade: %-3s Grade Points: %.1f", 
                    course.getName(), course.getCreditHours(), course.getGrade(),
                    GRADE_POINTS.get(course.getGrade())));
            }
            
            writer.println("---------------------");
            writer.println(String.format("GPA: %s", gpaFormat.format(gpa)));
            writer.println("=====================");
            
            resultArea.append("\nData saved to file: " + FILE_NAME + "\n");
        } catch (IOException e) {
            resultArea.append("\nError saving to file: " + e.getMessage() + "\n");
            JOptionPane.showMessageDialog(this, "Error saving to file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            StringBuilder content = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            
            if (content.length() == 0) {
                JOptionPane.showMessageDialog(this, "No records found", "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JTextArea textArea = new JTextArea(20, 60);
                textArea.setText(content.toString());
                textArea.setEditable(false);
                
                JScrollPane scrollPane = new JScrollPane(textArea);
                JOptionPane.showMessageDialog(this, scrollPane, "Saved Records", JOptionPane.PLAIN_MESSAGE);
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No records file found", "Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearAll() {
        nameField.setText("");
        idField.setText("");
        courseNameField.setText("");
        creditHoursField.setText("");
        coursesArea.setText("");
        resultArea.setText("");
        courses.clear();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GPACalculator calculator = new GPACalculator();
            calculator.setVisible(true);
        });
    }
    
    private static class Course {
        private String name;
        private double creditHours;
        private String grade;
        
        public Course(String name, double creditHours, String grade) {
            this.name = name;
            this.creditHours = creditHours;
            this.grade = grade;
        }
        
        public String getName() { return name; }
        public double getCreditHours() { return creditHours; }
        public String getGrade() { return grade; }
    }
}