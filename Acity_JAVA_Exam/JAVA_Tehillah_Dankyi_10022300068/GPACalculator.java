import javax.swing.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GPACalculator extends JFrame {

    private JTextField txtName, txtID, txtCourse, txtCredit;
    private JComboBox<String> comboGrade, comboSaveOption;
    private JTextArea txtResult;
    private JButton btnAdd, btnDelete, btnClear, btnCalculate;

    private ArrayList<String> courseList = new ArrayList<>();
    private ArrayList<Double> totalPointsList = new ArrayList<>();
    private ArrayList<Double> creditList = new ArrayList<>();

    public GPACalculator() {
        setTitle("GPA Calculator (Letter Grades)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setLayout(null);

        JLabel lblName = new JLabel("Student Name:");
        JLabel lblID = new JLabel("Student ID:");
        JLabel lblCourse = new JLabel("Course:");
        JLabel lblGrade = new JLabel("Grade (A - F):");
        JLabel lblCredit = new JLabel("Credit Hour:");
        JLabel lblSave = new JLabel("Save To:");
        JLabel lblResult = new JLabel("Course Entries & GPA:");

        lblName.setBounds(30, 20, 120, 25);
        lblID.setBounds(30, 60, 120, 25);
        lblCourse.setBounds(30, 100, 120, 25);
        lblGrade.setBounds(30, 140, 120, 25);
        lblCredit.setBounds(30, 180, 120, 25);
        lblSave.setBounds(30, 220, 120, 25);
        lblResult.setBounds(30, 260, 150, 25);

        txtName = new JTextField();
        txtID = new JTextField();
        txtCourse = new JTextField();
        txtCredit = new JTextField();

        comboGrade = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D", "E", "F"});
        comboSaveOption = new JComboBox<>(new String[]{"File", "Database"});
        txtResult = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(txtResult);

        txtName.setBounds(160, 20, 200, 25);
        txtID.setBounds(160, 60, 200, 25);
        txtCourse.setBounds(160, 100, 200, 25);
        comboGrade.setBounds(160, 140, 200, 25);
        txtCredit.setBounds(160, 180, 200, 25);
        comboSaveOption.setBounds(160, 220, 200, 25);
        scrollPane.setBounds(160, 260, 390, 150);

        btnAdd = new JButton("Add");
        btnDelete = new JButton("Delete Last");
        btnClear = new JButton("Clear All");
        btnCalculate = new JButton("Calculate GPA");

        btnAdd.setBounds(380, 100, 150, 25);
        btnDelete.setBounds(380, 140, 150, 25);
        btnClear.setBounds(380, 180, 150, 25);
        btnCalculate.setBounds(220, 420, 150, 30);

        add(lblName); add(txtName);
        add(lblID); add(txtID);
        add(lblCourse); add(txtCourse);
        add(lblGrade); add(comboGrade);
        add(lblCredit); add(txtCredit);
        add(lblSave); add(comboSaveOption);
        add(lblResult); add(scrollPane);
        add(btnAdd); add(btnDelete); add(btnClear); add(btnCalculate);

        btnAdd.addActionListener(e -> addCourse());
        btnDelete.addActionListener(e -> deleteLastEntry());
        btnClear.addActionListener(e -> clearAllEntries());
        btnCalculate.addActionListener(e -> calculateGPA());

        setVisible(true);
    }

    public double getGradePointFromLetter(String grade) {
        return switch (grade) {
            case "A" -> 4.0;
            case "B+" -> 3.5;
            case "B" -> 3.0;
            case "C+" -> 2.5;
            case "C" -> 2.0;
            case "D" -> 1.5;
            case "E" -> 1.0;
            case "F" -> 0.0;
            default -> -1;
        };
    }

    public void addCourse() {
        String course = txtCourse.getText();
        String creditText = txtCredit.getText();
        String grade = (String) comboGrade.getSelectedItem();

        if (course.isEmpty() || creditText.isEmpty() || grade == null) {
            JOptionPane.showMessageDialog(this, "Please enter course, grade, and credit.");
            return;
        }

        try {
            double credit = Double.parseDouble(creditText);

            if (credit <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid credit value.");
                return;
            }

            double gradePoint = getGradePointFromLetter(grade);
            double totalPoints = gradePoint * credit;

            courseList.add(course + " | Grade: " + grade + " | Credit: " + credit + " | GP: " + gradePoint);
            totalPointsList.add(totalPoints);
            creditList.add(credit);

            updateDisplay();
            txtCourse.setText("");
            txtCredit.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid number for credit.");
        }
    }

    public void deleteLastEntry() {
        if (!courseList.isEmpty()) {
            int lastIndex = courseList.size() - 1;
            courseList.remove(lastIndex);
            totalPointsList.remove(lastIndex);
            creditList.remove(lastIndex);
            updateDisplay();
        } else {
            JOptionPane.showMessageDialog(this, "No entries to delete.");
        }
    }

    public void clearAllEntries() {
        courseList.clear();
        totalPointsList.clear();
        creditList.clear();
        updateDisplay();
    }

    public void updateDisplay() {
        txtResult.setText("");
        for (String entry : courseList) {
            txtResult.append(entry + "\n");
        }
    }

    public void calculateGPA() {
        String name = txtName.getText();
        String id = txtID.getText();
        String option = (String) comboSaveOption.getSelectedItem();

        if (name.isEmpty() || id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter student name and ID.");
            return;
        }

        if (courseList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add at least one course.");
            return;
        }

        double totalPoints = 0;
        double totalCredits = 0;
        for (int i = 0; i < totalPointsList.size(); i++) {
            totalPoints += totalPointsList.get(i);
            totalCredits += creditList.get(i);
        }

        double gpa = totalPoints / totalCredits;

        String result = "Student Name: " + name + "\n"
                      + "ID: " + id + "\n"
                      + "Total Courses: " + courseList.size() + "\n"
                      + "GPA: " + String.format("%.2f", gpa) + "\n";

        txtResult.append("\n--- Final GPA ---\n" + result);

        if (option.equals("File")) {
            try (FileWriter writer = new FileWriter("gpa_records.txt", true)) {
                writer.write(result + "\n");
                JOptionPane.showMessageDialog(this, "Saved to file.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving to file.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Database: Under Construction.");
        }
    }

    public static void main(String[] args) {
        new GPACalculator();
    }
}