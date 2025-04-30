import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GPACalculator extends JFrame {
    private JTextField studentNameField, studentIDField;
    private JComboBox<String> storageComboBox;
    private JPanel coursesPanel;
    private ArrayList<JTextField[]> courseFields = new ArrayList<>();

    public GPACalculator() {
        setTitle("GPA Calculator");
        setSize(650, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.GREEN);

        JLabel title = new JLabel("GPA CALCULATOR", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(0, 102, 204));
        add(title, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        inputPanel.setBackground(Color.WHITE);

        inputPanel.add(new JLabel("Student Name:"));
        studentNameField = new JTextField();
        inputPanel.add(studentNameField);

        inputPanel.add(new JLabel("Student ID:"));
        studentIDField = new JTextField();
        inputPanel.add(studentIDField);

        inputPanel.add(new JLabel("Storage Option:"));
        storageComboBox = new JComboBox<>(new String[]{"File", "Database"});
        inputPanel.add(storageComboBox);

        add(inputPanel, BorderLayout.WEST);

        coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Courses"));
        add(scrollPane, BorderLayout.CENTER);

        addCourseLabels();
        addCourseInput();

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addCourseButton = new JButton("Add Course");
        JButton calculateButton = new JButton("Calculate GPA");

        addCourseButton.setFocusPainted(false);
        calculateButton.setFocusPainted(false);

        addCourseButton.setBackground(new Color(220, 220, 250));
        calculateButton.setBackground(new Color(200, 255, 200));

        addCourseButton.addActionListener(e -> addCourseInput());
        calculateButton.addActionListener(e -> calculateGPA());

        bottomPanel.add(addCourseButton);
        bottomPanel.add(calculateButton);
        bottomPanel.setBackground(Color.WHITE);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addCourseInput() {
        JPanel courseRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        courseRow.setBackground(Color.WHITE);

        JTextField courseField = new JTextField(10);
        JTextField creditField = new JTextField(5);
        JTextField gradeField = new JTextField(5);

        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 10));
        deleteButton.setPreferredSize(new Dimension(80, 25));
        deleteButton.setForeground(Color.RED);

        updateButton.setFocusPainted(false);
        deleteButton.setFocusPainted(false);

        courseRow.add(new JLabel("Course:"));
        courseRow.add(courseField);
        courseRow.add(new JLabel("Credits:"));
        courseRow.add(creditField);
        courseRow.add(new JLabel("Grade:"));
        courseRow.add(gradeField);
        courseRow.add(updateButton);
        courseRow.add(deleteButton);

        JTextField[] courseData = new JTextField[]{courseField, creditField, gradeField};
        courseFields.add(courseData);
        coursesPanel.add(courseRow);

        updateButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "You can edit the fields directly to update.");
        });

        deleteButton.addActionListener(e -> {
            coursesPanel.remove(courseRow);
            courseFields.remove(courseData);
            coursesPanel.revalidate();
            coursesPanel.repaint();
        });

        coursesPanel.revalidate();
        coursesPanel.repaint();
    }

    private void addCourseLabels() {
        JPanel labelRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelRow.setBackground(new Color(240, 240, 240));
        labelRow.add(new JLabel("Course"));
        labelRow.add(Box.createHorizontalStrut(80));
        labelRow.add(new JLabel("Credit Hours"));
        labelRow.add(Box.createHorizontalStrut(40));
        labelRow.add(new JLabel("Grade"));
        coursesPanel.add(labelRow);
    }

    private void calculateGPA() {
        double totalPoints = 0;
        double totalCredits = 0;

        StringBuilder dataBuilder = new StringBuilder();
        dataBuilder.append("Name: ").append(studentNameField.getText()).append("\n");
        dataBuilder.append("ID: ").append(studentIDField.getText()).append("\n");
        dataBuilder.append("Courses:\n");

        for (JTextField[] fields : courseFields) {
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

                dataBuilder.append(String.format("  %s - %.1f credits - Grade %d -> GPA %.1f\n", course, credit, grade, gradePoint));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
                return;
            }
        }

        double gpa = totalCredits == 0 ? 0 : totalPoints / totalCredits;
        String gradeLetter = getLetterGrade(gpa);
        String gpaResult = "GPA: " + String.format("%.2f", gpa) + " -> Grade: " + gradeLetter;
        dataBuilder.append(gpaResult).append("\n---\n");

        if ("File".equals(storageComboBox.getSelectedItem())) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("gpa_records.txt", true))) {
                writer.write(dataBuilder.toString());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to save to file: " + e.getMessage());
            }
            JOptionPane.showMessageDialog(this, gpaResult + "\nData saved to gpa_records.txt");
        } else {
            JOptionPane.showMessageDialog(this, gpaResult + "\n(Database saving under construction)");
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
        SwingUtilities.invokeLater(() -> new GPACalculator().setVisible(true));
    }
}
