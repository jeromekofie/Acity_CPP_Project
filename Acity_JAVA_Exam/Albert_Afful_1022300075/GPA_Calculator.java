import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;


public class GPA_Calculator extends JFrame {
    
    private JTextField studentNameInput;
    private JTextField studentIdInput;
    
    private JPanel coursesContainer;
    private ArrayList<CourseEntry> allCourses = new ArrayList<>();
    
    private JButton addCourseButton;
    private JButton calculateButton;

    public GPA_Calculator() {

        super("My GPA Calculator");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(10, 10));
        
        setupStudentInfoSection();
        setupCoursesSection();
        setupButtons();
        
        addNewCourseEntry();
    }
    
    private void setupStudentInfoSection() {
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        
        studentNameInput = new JTextField();
        studentIdInput = new JTextField();
        
        infoPanel.add(new JLabel("Your Name:"));
        infoPanel.add(studentNameInput);
        infoPanel.add(new JLabel("Student ID:"));
        infoPanel.add(studentIdInput);
        
        add(infoPanel, BorderLayout.NORTH);
    }
    
    private void setupCoursesSection() {
        coursesContainer = new JPanel();
        coursesContainer.setLayout(new BoxLayout(coursesContainer, BoxLayout.Y_AXIS));
        coursesContainer.setBorder(BorderFactory.createTitledBorder("Your Courses"));
        
        JScrollPane scrollWrapper = new JScrollPane(coursesContainer);
        add(scrollWrapper, BorderLayout.CENTER);
    }
    
    private void setupButtons() {

        JPanel buttonPanel = new JPanel();
        
        addCourseButton = new JButton(" Add Another Course");
        addCourseButton.addActionListener(e -> {
            addNewCourseEntry();
        });
        
        calculateButton = new JButton("Calculate My GPA!");
        calculateButton.setBackground(new Color(70, 130, 180));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.addActionListener(e -> {
            calculateAndSaveResults();
        });
        
        buttonPanel.add(addCourseButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); 
        buttonPanel.add(calculateButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void addNewCourseEntry() {
        CourseEntry newCourse = new CourseEntry();
        allCourses.add(newCourse);
        coursesContainer.add(newCourse);
        coursesContainer.revalidate();
        coursesContainer.repaint(); // Ensure UI updates
    }
    
    private void calculateAndSaveResults() {

        if (studentNameInput.getText().trim().isEmpty() || 
            studentIdInput.getText().trim().isEmpty()) {
            showErrorMessage("Hey! Don't forget to enter your name and student ID!");
            return;
        }
        

        if (allCourses.isEmpty()) {
            showErrorMessage("You need to add at least one course!");
            return;
        }
        

        for (CourseEntry course : allCourses) {
            if (course.getCourseName().isEmpty()) {
                showErrorMessage("Oops! You forgot to name one of your courses.");
                return;
            }
            if (course.getCreditHours() <= 0) {
                showErrorMessage("Please check the credit hours for all courses - they should be positive numbers!");
                return;
            }
        }
        

        double totalGradePoints = 0;
        double totalCreditHours = 0;
        
        for (CourseEntry course : allCourses) {
            totalGradePoints += course.getGradeValue() * course.getCreditHours();
            totalCreditHours += course.getCreditHours();
        }
        
        if (totalCreditHours == 0) {
            showErrorMessage("Something went wrong - total credit hours can't be zero!");
            return;
        }
        
        double finalGPA = totalGradePoints / totalCreditHours;
        

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Your GPA Results");
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File outputFile = fileChooser.getSelectedFile();
            if (!outputFile.getName().endsWith(".txt")) {
                outputFile = new File(outputFile.getAbsolutePath() + ".txt");
            }
            try (PrintWriter writer = new PrintWriter(outputFile)) {

                writer.println("=== GPA CALCULATION REPORT ===");
                writer.println();
                writer.println("Student: " + studentNameInput.getText());
                writer.println("ID: " + studentIdInput.getText());
                writer.println();
                writer.println("YOUR COURSES:");
                writer.println("----------------------------");
                

                for (CourseEntry course : allCourses) {
                    writer.printf("- %s: %s (%.1f credits)%n",
                        course.getCourseName(),
                        course.getGradeLetter(),
                        course.getCreditHours());
                }
                

                writer.println();
                writer.printf("FINAL GPA: %.2f%n", finalGPA);
                writer.println();
                writer.println("Generated by My GPA Calculator");
                
                JOptionPane.showMessageDialog(this,
                    String.format("Success!%nYour GPA is: %.2f%nSaved to: %s",
                        finalGPA,
                        outputFile.getAbsolutePath()),
                    "All Done!",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException e) {
                showErrorMessage("Whoops! Couldn't save the file:\n" + e.getMessage());
            }
        }
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this,
            "<html><body style='width: 300px;'>" + message + "</body></html>",
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    
    class CourseEntry extends JPanel {
        private JTextField courseNameField;
        private JTextField creditHoursField;
        private JComboBox<String> gradeDropdown;
        
        public CourseEntry() {
            setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
            setMaximumSize(new Dimension(500, 35));
            
            courseNameField = new JTextField(15);
            add(new JLabel("Course:"));
            add(courseNameField);
            
            creditHoursField = new JTextField(3);
            add(new JLabel("Credits:"));
            add(creditHoursField);
            
            gradeDropdown = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D", "F"});
            add(new JLabel("Grade:"));
            add(gradeDropdown);
            
            JButton removeButton = new JButton("Remove");
            removeButton.addActionListener(e -> {
                allCourses.remove(this);
                coursesContainer.remove(this);
                coursesContainer.revalidate();
            });
            add(removeButton);
        }
        
        public String getCourseName() {
            return courseNameField.getText().trim();
        }
        
        public double getCreditHours() {
            try {
                double creditHours = Double.parseDouble(creditHoursField.getText());
                if (creditHours <= 0) {
                    throw new NumberFormatException("Credit hours must be positive.");
                }
                return creditHours;
            } catch (NumberFormatException e) {
                showErrorMessage("Invalid credit hours for course: " + getCourseName());
                return -1; // Return -1 to indicate invalid input
            }
        }
        
        public String getGradeLetter() {
            return (String)gradeDropdown.getSelectedItem();
        }
        
        public double getGradeValue() {
            switch (getGradeLetter()) {
                case "A":  return 4.0;
                case "B+": return 3.5;
                case "B":  return 3.0;
                case "C+": return 2.5;
                case "C":  return 2.0;
                case "D":  return 1.5;
                default:   return 0.0; // F
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GPA_Calculator app = new GPA_Calculator();
            app.setVisible(true);
        });
    }
}
