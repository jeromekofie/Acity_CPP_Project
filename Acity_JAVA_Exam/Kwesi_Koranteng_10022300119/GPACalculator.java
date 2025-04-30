import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField;
    private JComboBox<String> storageTypeCombo;
    private JButton addCourseBtn, calculateBtn;
    private List<Course> courses;
    private JTable courseTable;
    private DefaultTableModel tableModel;

    // Enhanced color scheme
    private static final Color PRIMARY_COLOR = new Color(52, 73, 94);    // Dark blue-gray
    private static final Color SECONDARY_COLOR = new Color(41, 128, 185); // Bright blue
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);    // Red
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light gray
    private static final Color TEXT_COLOR = new Color(44, 62, 80);       // Dark gray
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);  // Green
    private static final Color WARNING_COLOR = new Color(230, 126, 34);  // Orange

    public GPACalculator() {
        setTitle("GPA Calculator");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        courses = new ArrayList<>();
        initializeComponents();
        layoutComponents();
        applyStyles();
    }

    private void initializeComponents() {
        // Student Information Panel
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        infoPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel nameLabel = new JLabel("Student Name:");
        nameLabel.setForeground(TEXT_COLOR);
        nameField = createStyledTextField();
        nameField.setToolTipText("Enter student's full name");
        
        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setForeground(TEXT_COLOR);
        idField = createStyledTextField();
        idField.setToolTipText("Enter student's ID number");

        infoPanel.add(nameLabel);
        infoPanel.add(nameField);
        infoPanel.add(idLabel);
        infoPanel.add(idField);

        // Storage Type Panel
        JPanel storagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        storagePanel.setBackground(BACKGROUND_COLOR);
        JLabel storageLabel = new JLabel("Save to:");
        storageLabel.setForeground(TEXT_COLOR);
        String[] storageTypes = {"Text File", "Database (Under Construction)"};
        storageTypeCombo = new JComboBox<>(storageTypes);
        storageTypeCombo.setBackground(Color.WHITE);
        storageTypeCombo.setForeground(TEXT_COLOR);
        storageTypeCombo.setToolTipText("Select where to save the GPA record");
        storagePanel.add(storageLabel);
        storagePanel.add(storageTypeCombo);

        // Calculate GPA Button
        calculateBtn = createStyledButton("Calculate GPA", SUCCESS_COLOR);
        calculateBtn.setToolTipText("Calculate GPA and save record");
        storagePanel.add(calculateBtn);

        // Course Table
        String[] columnNames = {"Course Name", "Grade", "Credit Hours"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return String.class;  // Course Name
                    case 1: return String.class;  // Grade
                    case 2: return Integer.class; // Credit Hours
                    default: return Object.class;
                }
            }
        };
        
        courseTable = new JTable(tableModel) {
            @Override
            public Component prepareEditor(TableCellEditor editor, int row, int column) {
                Component component = super.prepareEditor(editor, row, column);
                if (column == 1) { // Grade column
                    JComboBox<String> comboBox = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D+", "D", "F"});
                    comboBox.setBackground(Color.WHITE);
                    comboBox.setForeground(TEXT_COLOR);
                    comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    return comboBox;
                } else if (column == 2) { // Credit Hours column
                    JSpinner spinner = new JSpinner(new SpinnerNumberModel(3, 1, 6, 1));
                    spinner.setBackground(Color.WHITE);
                    spinner.setForeground(TEXT_COLOR);
                    spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    return spinner;
                }
                return component;
            }
        };
        
        // Set up the table
        courseTable.setBackground(Color.WHITE);
        courseTable.setForeground(TEXT_COLOR);
        courseTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        courseTable.setRowHeight(30);
        courseTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        courseTable.getTableHeader().setBackground(PRIMARY_COLOR);
        courseTable.getTableHeader().setForeground(Color.WHITE);
        courseTable.setGridColor(PRIMARY_COLOR);
        courseTable.setShowGrid(true);
        
        // Add Course Button
        addCourseBtn = createStyledButton("Add Course", SECONDARY_COLOR);
        addCourseBtn.setToolTipText("Add a new course");

        // Add action listeners
        addCourseBtn.addActionListener(e -> addCourse());
        calculateBtn.addActionListener(e -> calculateGPA());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Create main container panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top section with student info and storage
        JPanel topSection = new JPanel(new BorderLayout(20, 20));
        topSection.setBackground(BACKGROUND_COLOR);

        // Student info panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(BACKGROUND_COLOR);
        JPanel studentInfoPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        studentInfoPanel.setBackground(BACKGROUND_COLOR);
        studentInfoPanel.add(new JLabel("Student Name:"));
        studentInfoPanel.add(nameField);
        studentInfoPanel.add(new JLabel("Student ID:"));
        studentInfoPanel.add(idField);
        infoPanel.add(studentInfoPanel, BorderLayout.CENTER);
        infoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            "Student Information",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            PRIMARY_COLOR
        ));

        // Storage panel
        JPanel storagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        storagePanel.setBackground(BACKGROUND_COLOR);
        storagePanel.add(new JLabel("Save to:"));
        storagePanel.add(storageTypeCombo);
        storagePanel.add(calculateBtn);
        storagePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            "Storage Type",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            PRIMARY_COLOR
        ));

        topSection.add(infoPanel, BorderLayout.NORTH);
        topSection.add(storagePanel, BorderLayout.SOUTH);

        // Courses section
        JPanel coursesSection = new JPanel(new BorderLayout());
        coursesSection.setBackground(BACKGROUND_COLOR);
        
        // Create a panel for the table and add course button
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);
        tablePanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);
        
        // Add Course button panel
        JPanel addCoursePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addCoursePanel.setBackground(BACKGROUND_COLOR);
        addCoursePanel.add(addCourseBtn);
        tablePanel.add(addCoursePanel, BorderLayout.SOUTH);
        
        coursesSection.add(tablePanel, BorderLayout.CENTER);
        coursesSection.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            "Courses",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            PRIMARY_COLOR
        ));

        // Add all sections to main panel
        mainPanel.add(topSection, BorderLayout.NORTH);
        mainPanel.add(coursesSection, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    private void addCourse() {
        tableModel.addRow(new Object[]{"", "A", 3});
        // Scroll to the new row
        courseTable.scrollRectToVisible(courseTable.getCellRect(tableModel.getRowCount() - 1, 0, true));
    }

    private void calculateGPA() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();

        if (name.isEmpty() || id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter student name and ID", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please add at least one course", "No Courses", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double totalGradePoints = 0;
        int totalCreditHours = 0;
        StringBuilder details = new StringBuilder();

        // Add table header
        details.append(String.format("%-20s %-10s %-15s %-10s\n", 
            "Course", "Grade", "Credit Hours", "Grade Points"));
        details.append(String.format("%-20s %-10s %-15s %-10s\n", 
            "--------------------", "----------", "---------------", "----------"));

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String courseName = (String) tableModel.getValueAt(i, 0);
            if (courseName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter course name for all courses", "Missing Course Name", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String grade = (String) tableModel.getValueAt(i, 1);
            int creditHours = (int) tableModel.getValueAt(i, 2);

            double gradePoint = getGradePoint(grade);
            totalGradePoints += gradePoint * creditHours;
            totalCreditHours += creditHours;

            details.append(String.format("%-20s %-10s %-15d %-10.2f\n",
                courseName, grade, creditHours, gradePoint));
        }

        double gpa = totalCreditHours > 0 ? totalGradePoints / totalCreditHours : 0.0;

        // Format the final result
        String result = String.format(
            "STUDENT INFORMATION\n" +
            "-------------------\n" +
            "Name: %s\n" +
            "ID: %s\n\n" +
            "COURSE DETAILS\n" +
            "--------------\n" +
            "%s\n" +
            "TOTAL CREDIT HOURS: %d\n" +
            "TOTAL GRADE POINTS: %.2f\n" +
            "FINAL GPA: %.2f",
            name, id, details.toString(), totalCreditHours, totalGradePoints, gpa
        );

        // Save to file if text file is selected
        if (storageTypeCombo.getSelectedIndex() == 0) {
            FileHandler.saveToFile(name, id, result);
            JOptionPane.showMessageDialog(this, "Record saved successfully to gpa_records.txt", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Database storage is under construction", "Notice", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private double getGradePoint(String grade) {
        switch (grade) {
            case "A": return 4.0;
            case "B+": return 3.5;
            case "B": return 3.0;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "D+": return 1.5;
            case "D": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }

    private void applyStyles() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GPACalculator().setVisible(true);
        });
    }
} 