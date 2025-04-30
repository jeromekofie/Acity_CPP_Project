import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.*;

public class GpaCalculator extends JFrame {

    private ArrayList<String> courseNames = new ArrayList<>();
    private ArrayList<Double> courseGrades = new ArrayList<>();
    private ArrayList<Integer> courseCredits = new ArrayList<>();

    private JTextField studentIdInput;
    private JTextField courseInput;
    private JTextField gradeInput;
    private JTextField creditInput;
    private JTextArea courseDisplay;
    private JLabel gpaDisplay;
    private JComboBox<String> saveOptionCombo;

    public GpaCalculator() {
        setTitle("GPA Calculator");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createInputPanel(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.CENTER);
        add(createCourseDisplayPanel(), BorderLayout.WEST);
        add(createGpaPanel(), BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Add Course"));

        studentIdInput = new JTextField();
        courseInput = new JTextField();
        gradeInput = new JTextField();
        creditInput = new JTextField();

        panel.add(new JLabel("Student ID:"));
        panel.add(studentIdInput);
        panel.add(new JLabel("Course Name:"));
        panel.add(courseInput);
        panel.add(new JLabel("Grade:"));
        panel.add(gradeInput);
        panel.add(new JLabel("Credit Hours:"));
        panel.add(creditInput);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Add Course");
        JButton calculateButton = new JButton("Calculate GPA");
        JButton clearButton = new JButton("Clear All");
        JButton saveButton = new JButton("Save");
        JButton sendButton = new JButton("Export");

        saveOptionCombo = new JComboBox<>(new String[]{"File", "Database"});

        addButton.addActionListener(e -> handleAddCourse());
        calculateButton.addActionListener(e -> handleCalculateGPA());
        clearButton.addActionListener(e -> handleClearAll());
        saveButton.addActionListener(e -> handleSave());
        sendButton.addActionListener(e -> handleSendData());

        panel.add(addButton);
        panel.add(calculateButton);
        panel.add(clearButton);
        panel.add(saveButton);
        panel.add(saveOptionCombo);
        panel.add(sendButton);

        return panel;
    }

    private JPanel createCourseDisplayPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Courses Added"));

        courseDisplay = new JTextArea(10, 30);
        courseDisplay.setEditable(false);
        courseDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(courseDisplay);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createGpaPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        gpaDisplay = new JLabel("Current GPA: 0.00");
        panel.add(gpaDisplay);
        return panel;
    }

    private void handleAddCourse() {
        String studentId = studentIdInput.getText().trim();
        String course = courseInput.getText().trim();
        String gradeText = gradeInput.getText().trim();
        String creditText = creditInput.getText().trim();

        if (studentId.isEmpty() || course.isEmpty() || gradeText.isEmpty() || creditText.isEmpty()) {
            showMessage("Please fill in all fields.");
            return;
        }

        try {
            double grade = Double.parseDouble(gradeText);
            int credit = Integer.parseInt(creditText);

            if (grade < 0 || grade > 100) {
                showMessage("Grade must be between 0 and 100.");
                return;
            }

            if (credit <= 0) {
                showMessage("Credit hours should exceed 0.");
                return;
            }

            courseNames.add(course);
            courseGrades.add(grade);
            courseCredits.add(credit);

            updateCourseDisplay();
            courseInput.setText("");
            gradeInput.setText("");
            creditInput.setText("");
            courseInput.requestFocus();
        } catch (NumberFormatException ex) {
            showMessage("Please enter valid numbers for grade and credit hours.");
        }
    }

    private void handleCalculateGPA() {
        if (courseGrades.isEmpty()) {
            showMessage("No courses have been added.");
            return;
        }

        double totalWeightedGrade = 0;
        int totalCredits = 0;

        for (int i = 0; i < courseGrades.size(); i++) {
            totalWeightedGrade += courseGrades.get(i) * courseCredits.get(i);
            totalCredits += courseCredits.get(i);
        }

        double gpa = totalWeightedGrade / totalCredits;
        gpaDisplay.setText(String.format("Current GPA: %.2f", gpa));
    }

    private void handleSave() {
        String selectedOption = (String) saveOptionCombo.getSelectedItem();
        if ("Database".equals(selectedOption)) {
            simulateDatabaseSave();
        } else {
            handleSaveToFile();
        }
    }

    private void simulateDatabaseSave() {
        if (courseNames.isEmpty()) {
            showMessage("No data to save to database.");
            return;
        }

        // Simulating DB save
        System.out.println("Saving data to database...");
        System.out.println("Student ID: " + studentIdInput.getText());
        for (int i = 0; i < courseNames.size(); i++) {
            System.out.printf("Course: %s, Grade: %.2f, Credits: %d\n",
                    courseNames.get(i), courseGrades.get(i), courseCredits.get(i));
        }

        showMessage("Data saved to database (simulated).");
    }

    private void handleSaveToFile() {
        try {
            String filePath = "gpa_records.txt";
            if (Files.exists(Paths.get(filePath)) && !Files.isWritable(Paths.get(filePath))) {
                throw new IOException("File exists but is not writable. Check permissions.");
            }

            try (FileWriter writer = new FileWriter(filePath, false)) {
                writer.write("Student ID: " + studentIdInput.getText() + "\n\n");
                writer.write("GPA Calculator Records\n\n");
                for (int i = 0; i < courseNames.size(); i++) {
                    writer.write(String.format("%d. %s - Grade: %.2f, Credits: %d\n",
                            i + 1, courseNames.get(i), courseGrades.get(i), courseCredits.get(i)));
                }

                if (!courseGrades.isEmpty()) {
                    double totalWeightedGrade = 0;
                    int totalCredits = 0;
                    for (int i = 0; i < courseGrades.size(); i++) {
                        totalWeightedGrade += courseGrades.get(i) * courseCredits.get(i);
                        totalCredits += courseCredits.get(i);
                    }
                    double gpa = totalWeightedGrade / totalCredits;
                    writer.write(String.format("\nFinal GPA: %.2f\n", gpa));
                }
            }

            showMessage("Records saved to gpa_records.txt");
        } catch (IOException ex) {
            showMessage("Error saving file: " + ex.getMessage());
        }
    }

    private void handleSendData() {
        if (courseNames.isEmpty()) {
            showMessage("No data to send.");
            return;
        }

        StringBuilder payload = new StringBuilder();
        payload.append("Sending GPA data to server...\n\n");
        payload.append("Student ID: ").append(studentIdInput.getText()).append("\n\n");

        for (int i = 0; i < courseNames.size(); i++) {
            payload.append(String.format("Course: %s, Grade: %.2f, Credits: %d\n",
                    courseNames.get(i), courseGrades.get(i), courseCredits.get(i)));
        }

        double totalWeightedGrade = 0;
        int totalCredits = 0;
        for (int i = 0; i < courseGrades.size(); i++) {
            totalWeightedGrade += courseGrades.get(i) * courseCredits.get(i);
            totalCredits += courseCredits.get(i);
        }

        double gpa = totalWeightedGrade / totalCredits;
        payload.append(String.format("\nCalculated GPA: %.2f", gpa));

        System.out.println(payload.toString());
        showMessage("Data sent successfully ");
    }

    private void handleClearAll() {
        courseNames.clear();
        courseGrades.clear();
        courseCredits.clear();
        updateCourseDisplay();
        gpaDisplay.setText("Current GPA: 0.00");
        studentIdInput.setText("");
        courseInput.setText("");
        gradeInput.setText("");
        creditInput.setText("");
        courseInput.requestFocus();
    }

    private void updateCourseDisplay() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < courseNames.size(); i++) {
            sb.append(String.format("%d. %s | Grade: %.2f | Credits: %d\n",
                    i + 1, courseNames.get(i), courseGrades.get(i), courseCredits.get(i)));
        }
        courseDisplay.setText(sb.toString());
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "GPA Calculator", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GpaCalculator app = new GpaCalculator();
            app.setVisible(true);
        });
    }
}
