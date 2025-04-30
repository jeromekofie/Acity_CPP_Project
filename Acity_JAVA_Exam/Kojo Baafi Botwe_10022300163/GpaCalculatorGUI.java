import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

public class GpaCalculatorGUI extends JFrame {
    private JTextField nameField, idField, courseField, creditHoursField, gradeField;
    private JComboBox<String> saveOptionCombo;
    private JButton addCourseButton, calculateButton, saveButton, newStudentButton;
    private JLabel resultLabel;
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private Student student;

    public GpaCalculatorGUI() {
        setTitle("GPA Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Student Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(20);
        inputPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Course Name:"), gbc);
        gbc.gridx = 1;
        courseField = new JTextField(20);
        inputPanel.add(courseField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Credit Hours:"), gbc);
        gbc.gridx = 1;
        creditHoursField = new JTextField(20);
        inputPanel.add(creditHoursField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Grade:"), gbc);
        gbc.gridx = 1;
        gradeField = new JTextField(20);
        inputPanel.add(gradeField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        addCourseButton = new JButton("Add Course");
        inputPanel.add(addCourseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(new JLabel("Save Option:"), gbc);
        gbc.gridx = 1;
        String[] options = {"File", "Database"};
        saveOptionCombo = new JComboBox<>(options);
        inputPanel.add(saveOptionCombo, gbc);

        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        String[] columnNames = {"Course Name", "Grade", "Credit Hours"};
        tableModel = new DefaultTableModel(columnNames, 0);
        courseTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(courseTable);
        tableScrollPane.setPreferredSize(new Dimension(380, 100));
        mainPanel.add(tableScrollPane);
        mainPanel.add(Box.createVerticalStrut(20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        calculateButton = new JButton("Calculate GPA");
        buttonPanel.add(calculateButton);
        saveButton = new JButton("Save");
        buttonPanel.add(saveButton);
        newStudentButton = new JButton("New Student");
        buttonPanel.add(newStudentButton);
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resultLabel = new JLabel("GPA: ");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultPanel.add(resultLabel);
        mainPanel.add(resultPanel);

        add(mainPanel, BorderLayout.CENTER);

        addCourseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCourseToStudent();
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateGpaForStudent();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });

        newStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetForNewStudent();
            }
        });

        setSize(450, 500);
        setLocationRelativeTo(null);
    }

    private void addCourseToStudent() {
        String courseName = courseField.getText();
        String grade = gradeField.getText();
        String creditHoursText = creditHoursField.getText();
        if (courseName.isEmpty() || grade.isEmpty() || creditHoursText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all course fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double creditHours;
        try {
            creditHours = Double.parseDouble(creditHoursText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Credit hours must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (student == null) {
            String name = nameField.getText();
            String id = idField.getText();
            if (name.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter student name and ID first.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            student = new Student(name, id);
        }
        Course course = new Course(courseName, grade, creditHours);
        student.addCourse(course);
        tableModel.addRow(new Object[]{courseName, grade, creditHours});
        courseField.setText("");
        gradeField.setText("");
        creditHoursField.setText("");
    }

    private void calculateGpaForStudent() {
        if (student == null || student.getCourses().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one course.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double gpa = GpaCalculator.calculateGpa(student);
        resultLabel.setText("GPA: " + String.format("%.2f", gpa));
    }

    private void saveData() {
        if (student == null || student.getCourses().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one course before saving.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double gpa = GpaCalculator.calculateGpa(student);
        String selectedOption = (String)saveOptionCombo.getSelectedItem();
        RecordSaver.SaveOption saveOption = RecordSaver.getSaveOption(selectedOption);
        saveOption.save(student, gpa);
        
        if ("File".equals(selectedOption)) {
            JOptionPane.showMessageDialog(this, "Data saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void resetForNewStudent() {
        nameField.setText("");
        idField.setText("");
        courseField.setText("");
        gradeField.setText("");
        creditHoursField.setText("");
        resultLabel.setText("GPA: ");
        tableModel.setRowCount(0);
        student = null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GpaCalculatorGUI().setVisible(true);
            }
        });
    }
} 