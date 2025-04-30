import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.util.ArrayList;

public class Java_Exam extends JFrame {
    private JTextField studentname, studentID;
    private JComboBox<String> dropdown;
    private JPanel coursesPanel;
    private ArrayList<JTextField[]> courses = new ArrayList<>();

    public Java_Exam() {
        setTitle("GPA Calculator");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBackground(Color.DARK_GRAY);

        JPanel insert = new JPanel(new GridLayout(3, 2));
        insert.add(new JLabel("Student Name:"));
        studentname = new JTextField();
        insert.add(studentname);

        insert.add(new JLabel("Student ID:"));
        studentID = new JTextField();
        insert.add(studentID);

        insert.add(new JLabel("Storage Option:"));
        dropdown = new JComboBox<>(new String[]{"File", "Database"});
        insert.add(dropdown);

        add(insert, BorderLayout.NORTH);

        coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        add(scrollPane, BorderLayout.CENTER);

        addCourseLabels();
        addCourseInput();

        JPanel bottomPanel = new JPanel();
        JButton addcourseButton = new JButton("ADD COURSE");
        JButton calculateButton = new JButton("CALCULATE GPA");

        addcourseButton.addActionListener(e -> addCourseInput());
        calculateButton.addActionListener(e -> calculateGPA());

        bottomPanel.add(addcourseButton);
        bottomPanel.add(calculateButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addCourseInput() {
        JPanel parameters = new JPanel(new GridLayout(1, 5)); 
        JTextField course = new JTextField();
        JTextField credit = new JTextField();
        JTextField grade = new JTextField();

        JButton updateButton = new JButton("Update");
       JButton deleteButton = new JButton("Delete");

      parameters.add(course);
      parameters.add(credit);
      parameters.add(grade);
    
      coursesPanel.add(parameters);
      JTextField[] courseData = new JTextField[]{course, credit, grade};
      courses.add(courseData);

      updateButton.addActionListener(e -> {
       JOptionPane.showMessageDialog(this, "Update parameters.");
   });

   deleteButton.addActionListener(e -> {
       coursesPanel.remove(parameters);
       courses.remove(courseData);
      
   });
 
     coursesPanel.revalidate();
     coursesPanel.repaint();
    }
    
    private void addCourseLabels() {
    JPanel label = new JPanel(new GridLayout(1, 3));
    label.add(new JLabel("Course"));
    label.add(new JLabel("Credit Hours"));
    label.add(new JLabel("Grade "));
    coursesPanel.add(label);
   }

   private void calculateGPA() {
    double totalPoints = 0;
    double totalCredits = 0;

    StringBuilder dataBuilder = new StringBuilder();
    dataBuilder.append("Name: ").append(studentname.getText()).append ("");
    dataBuilder.append("ID: ").append(studentID.getText()).append ("");
    dataBuilder.append("Courses:\n");

    for (JTextField[] fields : courses) {
        try {
            String course = fields[0].getText();
            double credit = Double.parseDouble(fields[1].getText());
            int grade = Integer.parseInt(fields[2].getText().trim());

            double gradePoint;
            if (grade >= 90) gradePoint = 4.0;
            else if (grade >= 85) gradePoint = 3.5;
            else if (grade >= 80) gradePoint = 3.0;
            else if (grade >= 75) gradePoint = 2.5;
            else if (grade >= 65) gradePoint = 2.0;
            else if (grade >= 55) gradePoint = 1.5;
            else if (grade >= 45) gradePoint = 1.0;
            else gradePoint = 0.0;

            totalCredits += credit;
            totalPoints += gradePoint * credit;

            
            dataBuilder.append(String.format("  %s - %.1f credits - Grade %d -> GPA %.1f", course, credit, grade, gradePoint));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            return;
        }
    }

    double gpa;
        if (totalCredits == 0) {
            gpa = 0;
        } else {
            gpa = totalPoints / totalCredits;
        }
        
        String gradeLetter = getLetterGrade(gpa);
        String gparesult = "GPA: " + String.format("%.2f", gpa) + " \n  Grade: " + gradeLetter;

        dataBuilder.append(gparesult).append("---");

        if ("File".equals(dropdown.getSelectedItem())) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("gpa_records.txt", true))) {
                writer.write(dataBuilder.toString());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to save to file: " + e.getMessage());
            }
            JOptionPane.showMessageDialog(this, gparesult + " Saved to gpa_records.txt");
        } else {
            JOptionPane.showMessageDialog(this, gparesult + "\n(Database: Under Construction. )");
        }
    }

    private String getLetterGrade(double gpa) {
        if (gpa >= 4.0) return "A";
        else if (gpa >= 3.5) return "B+";
        else if (gpa >= 3.0) return "B";
        else if (gpa >= 2.5) return "C+";
        else if (gpa >= 2.0) return "C";
        else if (gpa >= 1.5) return "D";
        else if (gpa >= 1.0) return "E";
        else return "F";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Java_Exam().setVisible(true));
       
    }

}

