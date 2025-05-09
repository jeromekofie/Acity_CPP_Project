import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField;
    private JComboBox<String> storageComboBox;
    private JPanel coursesPanel;
    private JButton addCourseBtn, removeCourseBtn, calculateBtn, saveBtn, viewRecordsBtn;
    private JLabel gpaLabel, titleLabel;
    private ArrayList<Course> courses;
    private DecimalFormat df = new DecimalFormat("0.00");
    
    // Color scheme codes for later calling
    private final Color PRIMARY_COLOR = new Color(0, 102, 204); 
    private final Color SECONDARY_COLOR = new Color(240, 240, 240); 
    private final Color ACCENT_COLOR = new Color(255, 153, 0); 

    public GPACalculator() {
        setTitle("GPA Calculator BY YOOFIBH");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        courses = new ArrayList<>();
        createUIComponents();
        setLocationRelativeTo(null); // Center window
        setVisible(true);
    }

    private void createUIComponents() {
        // Header 
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        
        titleLabel = new JLabel("YOOFI'S GPA CALCULATOR", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 19));
        titleLabel.setForeground(ACCENT_COLOR);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);

        // Main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        contentPanel.setBackground(Color.WHITE);

        // Student info 
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        infoPanel.setBackground(Color.GRAY);
        
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(nameLabel);
        nameField = new JTextField();
        nameField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        infoPanel.add(nameField);

        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(idLabel);
        idField = new JTextField();
        idField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        infoPanel.add(idField);

        contentPanel.add(infoPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Courses 
        JPanel coursesContainer = new JPanel(new BorderLayout());
        coursesContainer.setBackground(Color.GRAY);
        
        JLabel coursesTitle = new JLabel("Your Courses:");
        coursesTitle.setFont(new Font("Arial", Font.BOLD, 12));
        coursesContainer.add(coursesTitle, BorderLayout.NORTH);
        
        coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursesPanel.setBackground(Color.GRAY);
        
        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        coursesContainer.add(scrollPane, BorderLayout.CENTER);

        // Course buttons
        JPanel courseBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        courseBtnPanel.setBackground(Color.GRAY);
        
        addCourseBtn = createButton("Add Course", PRIMARY_COLOR);
        removeCourseBtn = createButton("Remove Course", new Color(204, 0, 0)); 
        
        addCourseBtn.addActionListener(e -> addCourseRow());
        removeCourseBtn.addActionListener(e -> removeLastCourse());
        
        courseBtnPanel.add(addCourseBtn);
        courseBtnPanel.add(removeCourseBtn);
        coursesContainer.add(courseBtnPanel, BorderLayout.SOUTH);

        contentPanel.add(coursesContainer);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Results 
        JPanel resultsPanel = new JPanel();
        resultsPanel.setBackground(Color.GRAY);
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        
        gpaLabel = new JLabel("GPA: -");
        gpaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gpaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultsPanel.add(gpaLabel);
        
        contentPanel.add(resultsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Action buttons
        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        actionPanel.setBackground(Color.GRAY);
        
        calculateBtn = createButton("Calculate GPA", PRIMARY_COLOR);
        saveBtn = createButton("Save Results", ACCENT_COLOR);
        viewRecordsBtn = createButton("View Records", new Color(0, 153, 76)); 
        
        calculateBtn.addActionListener(e -> calculateGPA());
        saveBtn.addActionListener(e -> saveResults());
        viewRecordsBtn.addActionListener(e -> viewRecords());
        
        actionPanel.add(calculateBtn);
        actionPanel.add(saveBtn);
        actionPanel.add(viewRecordsBtn);
        
        contentPanel.add(actionPanel);

        add(contentPanel, BorderLayout.CENTER);

        // to Add another course row
        addCourseRow();
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return button;
    }

    private void addCourseRow() {
        JPanel courseRow = new JPanel();
        courseRow.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        courseRow.setBackground(SECONDARY_COLOR);
        courseRow.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        courseRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JTextField courseNameField = new JTextField(15);
        courseNameField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        
        JComboBox<Integer> creditHoursCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        creditHoursCombo.setBackground(Color.BLACK);
        
        JComboBox<String> gradeCombo = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D", "E", "F"});
        gradeCombo.setBackground(Color.CYAN);

        courseRow.add(new JLabel("Course:"));
        courseRow.add(courseNameField);
        courseRow.add(new JLabel("Credits:"));
        courseRow.add(creditHoursCombo);
        courseRow.add(new JLabel("Grade:"));
        courseRow.add(gradeCombo);

        coursesPanel.add(courseRow);
        coursesPanel.revalidate();
        coursesPanel.repaint();
    }

    private void removeLastCourse() {
        if (coursesPanel.getComponentCount() > 1) {
            coursesPanel.remove(coursesPanel.getComponentCount() - 1);
            coursesPanel.revalidate();
            coursesPanel.repaint();
        } else {
            showMessage("You need at least one course", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void calculateGPA() {
        if (nameField.getText().trim().isEmpty()) {
            showMessage("Please enter your name", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (idField.getText().trim().isEmpty()) {
            showMessage("Please enter your student ID", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        courses.clear();
        Component[] rows = coursesPanel.getComponents();
        
        for (Component row : rows) {
            if (row instanceof JPanel) {
                JPanel courseRow = (JPanel) row;
                Component[] fields = courseRow.getComponents();
                
                String courseName = ((JTextField) fields[1]).getText().trim();
                if (courseName.isEmpty()) {
                    showMessage("Please enter all course names", "Missing Information", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int creditHours = (int) ((JComboBox<Integer>) fields[3]).getSelectedItem();
                String grade = (String) ((JComboBox<String>) fields[5]).getSelectedItem();
                
                courses.add(new Course(courseName, creditHours, grade));
            }
        }

        double totalPoints = 0;
        double totalHours = 0;
        
        Map<String, Double> gradePoints = new HashMap<>();
        gradePoints.put("A", 4.0);
        gradePoints.put("B+", 3.5);
        gradePoints.put("B", 3.0);
        gradePoints.put("C+", 2.5);
        gradePoints.put("C", 2.0);
        gradePoints.put("D", 1.5);
        gradePoints.put("E", 1.0);
        gradePoints.put("F", 0.0);

        for (Course course : courses) {
            double points = gradePoints.get(course.getGrade());
            int hours = course.getCreditHours();
            totalPoints += points * hours;
            totalHours += hours;
        }

        if (totalHours == 0) {
            showMessage("Cannot calculate GPA with 0 credit hours", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double gpa = totalPoints / totalHours;
        gpaLabel.setText("GPA: " + df.format(gpa));
        
        // Color codefor GPA
        if (gpa >= 3.6) {
            gpaLabel.setForeground(new Color(0, 128, 0)); 
        } else if (gpa >= 2.0) {
            gpaLabel.setForeground(ACCENT_COLOR); 
        } else {
            gpaLabel.setForeground(new Color(204, 0, 0)); 
        }
    }

    private void saveResults() {
        if (gpaLabel.getText().equals("GPA: -")) {
            showMessage("Please calculate GPA first", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (PrintWriter writer = new PrintWriter("student_gpa_records.txt")) {
            writer.println("STUDENT GPA RECORD");
            writer.println("==================");
            writer.println("Name: " + nameField.getText());
            writer.println("ID: " + idField.getText());
            writer.println("------------------");
            
            for (Course course : courses) {
                writer.printf("%s - %d credits - Grade: %s%n", 
                    course.getName(), course.getCreditHours(), course.getGrade());
            }
            
            writer.println("------------------");
            writer.println("GPA: " + gpaLabel.getText().substring(5));
            writer.println("Saved: " + new Date());
            
            showMessage("Results saved to student_gpa_records.txt", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            showMessage("Error saving: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewRecords() {
        try {
            File file = new File("student_gpa_records.txt");
            if (!file.exists()) {
                showMessage("No records found", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            StringBuilder content = new StringBuilder();
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    content.append(scanner.nextLine()).append("\n");
                }
            }
            
            JTextArea textArea = new JTextArea(content.toString(), 15, 40);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(this, scrollPane, "Saved Records", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e) {
            showMessage("Error reading records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GPACalculator());
    }
}

class Course {
    private String name;
    private int creditHours;
    private String grade;

    public Course(String name, int creditHours, String grade) {
        this.name = name;
        this.creditHours = creditHours;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public String getGrade() {
        return grade;
    }
}