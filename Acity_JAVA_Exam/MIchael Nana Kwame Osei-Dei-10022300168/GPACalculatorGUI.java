import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class GPACalculatorGUI extends JFrame {
    private JTextField nameField, idField, courseField, creditHoursField;
    private JComboBox<String> gradeBox;
    private JButton addButton, calculateButton;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private List<Double> gradePoints = new ArrayList<>();
    private List<Double> creditHoursList = new ArrayList<>();

    public GPACalculatorGUI() {
        setTitle("GPA Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);

        
        BackgroundPanel backgroundPanel = new BackgroundPanel("c:/Users/Dell Users/Downloads/download_2.jpg");
        backgroundPanel.setLayout(new BorderLayout(10, 10));

        //MMMM//
        JPanel inputPanel = createInputPanel();

        
        JPanel buttonPanel = createButtonPanel();

        
        JPanel tablePanel = createTablePanel();

        
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setOpaque(false);
        topSection.add(inputPanel);
        topSection.add(buttonPanel);

        backgroundPanel.add(topSection, BorderLayout.NORTH);
        backgroundPanel.add(tablePanel, BorderLayout.CENTER);

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Enter Course Details", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));

        nameField = new JTextField(10);
        idField = new JTextField(10);
        courseField = new JTextField(10);
        creditHoursField = new JTextField(5);
        gradeBox = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D", "E", "F"});

        inputPanel.add(createLabeledField("Name:", nameField));
        inputPanel.add(createLabeledField("ID:", idField));
        inputPanel.add(createLabeledField("Course:", courseField));
        inputPanel.add(createLabeledField("Credit Hours:", creditHoursField));
        inputPanel.add(createLabeledField("Grade:", gradeBox));

        return inputPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);

        addButton = new JButton("Add Course");
        calculateButton = new JButton("Calculate GPA");

        addButton.addActionListener(e -> addCourse());
        calculateButton.addActionListener(e -> calculateGPA());

        buttonPanel.add(addButton);
        buttonPanel.add(calculateButton);

        return buttonPanel;
    }

    private JPanel createTablePanel() {
        tableModel = new DefaultTableModel(new String[]{"Name", "ID", "Course", "Credit Hours", "Grade"}, 0);
        resultTable = new JTable(tableModel);
        resultTable.setFont(new Font("Arial", Font.PLAIN, 14));
        resultTable.setRowHeight(25);

        
        resultTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Results", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createLabeledField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label);
        panel.add(field);
        return panel;
    }

    private void addCourse() {
        try {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            String course = courseField.getText().trim();
            double creditHours = Double.parseDouble(creditHoursField.getText().trim());
            String grade = (String) gradeBox.getSelectedItem();

            if (name.isEmpty() || id.isEmpty() || course.isEmpty() || grade == null) {
                JOptionPane.showMessageDialog(this, "All fields must be filled.");
                return;
            }

            double gradePoint = getGradePoint(grade);
            gradePoints.add(gradePoint * creditHours);
            creditHoursList.add(creditHours);

            tableModel.addRow(new Object[]{name, id, course, creditHours, grade});
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter valid numbers for credit hours.");
        }
    }

    private void calculateGPA() {
        if (creditHoursList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No courses added to calculate GPA.");
            return;
        }

        double totalGradePoints = gradePoints.stream().mapToDouble(Double::doubleValue).sum();
        double totalCreditHours = creditHoursList.stream().mapToDouble(Double::doubleValue).sum();
        double gpa = totalGradePoints / totalCreditHours;

        JOptionPane.showMessageDialog(this, "Your GPA is: " + String.format("%.2f", gpa));
        saveToFile("GPA: " + String.format("%.2f", gpa));
    }

    private double getGradePoint(String grade) {
        return switch (grade) {
            case "A" -> 4.0;
            case "B+" -> 3.5;
            case "B" -> 3.0;
            case "C+" -> 2.5;
            case "C" -> 2.0;
            case "D" -> 1.5;
            case "E" -> 1.0;
            case "F" -> 0.0;
            default -> 0.0;
        };
    }

    private void saveToFile(String line) {
        try (FileWriter writer = new FileWriter("gpa_results.txt", true)) {
            writer.write(line + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save to file.");
        }
    }

    private void clearFields() {
        nameField.setText("");
        idField.setText("");
        courseField.setText("");
        creditHoursField.setText("");
        gradeBox.setSelectedIndex(0);
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(imagePath).getImage();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Failed to load background image: " + imagePath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GPACalculatorGUI::new);
    }
}