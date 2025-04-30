import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField;
    private JComboBox<String> storageCombo;
    private JButton addCourseBtn, calculateBtn, viewRecordsBtn;
    private JPanel coursesPanel, buttonPanel;
    private List<CourseEntry> courseEntries;
    private JLabel gpaResultLabel;
    private DefaultTableModel tableModel;
    private JTable recordsTable;
    private JScrollPane tableScrollPane;
    private JPanel recordsPanel;

    
    private final Color PRIMARY_COLOR = new Color(70, 130, 180);  
    private final Color SECONDARY_COLOR = new Color(245, 245, 245);
    private final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    private final Color BORDER_COLOR = new Color(200, 200, 210);
    private final Font SYSTEM_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
    private final Font SYSTEM_FONT_BOLD = new Font(Font.SANS_SERIF, Font.BOLD, 13);
    private final Font SYSTEM_FONT_TITLE = new Font(Font.SANS_SERIF, Font.BOLD, 14);

    public GPACalculator() {
        setTitle("GPA Calculator");
        setSize(1000, 650);
        setMinimumSize(new Dimension(850, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(BACKGROUND_COLOR);

        courseEntries = new ArrayList<>();

        
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBackground(BACKGROUND_COLOR);
        inputPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        
        JPanel studentInfoPanel = createStyledPanel("Student Information");
        studentInfoPanel.setLayout(new GridLayout(2, 2, 8, 8));

        JLabel nameLabel = new JLabel("Student Name:");
        nameLabel.setFont(SYSTEM_FONT);
        nameField = createStyledTextField(20);
        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(SYSTEM_FONT);
        idField = createStyledTextField(20);

        studentInfoPanel.add(nameLabel);
        studentInfoPanel.add(nameField);
        studentInfoPanel.add(idLabel);
        studentInfoPanel.add(idField);

        
        coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursesPanel.setBorder(createStyledBorder("Course Details"));
        coursesPanel.setBackground(Color.WHITE);

        
        JPanel storagePanel = createStyledPanel("Save To");
        storagePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
        storageCombo = new JComboBox<>(new String[]{"File", "Database"});
        styleComboBox(storageCombo);
        JLabel saveLabel = new JLabel("Save to:");
        saveLabel.setFont(SYSTEM_FONT);
        storagePanel.add(saveLabel);
        storagePanel.add(storageCombo);

        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        addCourseBtn = createStyledButton("Add Course", 130, 35);
        calculateBtn = createStyledButton("Calculate GPA", 130, 35);
        viewRecordsBtn = createStyledButton("View Records", 130, 35);

        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(addCourseBtn);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(calculateBtn);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(viewRecordsBtn);
        buttonPanel.add(Box.createVerticalGlue());

        
        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resultPanel.setBackground(BACKGROUND_COLOR);
        gpaResultLabel = new JLabel("GPA: ");
        gpaResultLabel.setFont(SYSTEM_FONT_TITLE);
        gpaResultLabel.setForeground(PRIMARY_COLOR);
        resultPanel.add(gpaResultLabel);

        
        recordsPanel = new JPanel(new BorderLayout());
        recordsPanel.setBackground(BACKGROUND_COLOR);
        recordsPanel.setVisible(false);

        String[] columnNames = {"Name", "ID", "Course", "Credits", "Grade", "GPA"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        recordsTable = new JTable(tableModel);
        styleTable(recordsTable);
        
        tableScrollPane = new JScrollPane(recordsTable);
        tableScrollPane.setBorder(createStyledBorder("Student Records"));
        recordsPanel.add(tableScrollPane, BorderLayout.CENTER);

        
        JPanel recordActionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        recordActionPanel.setBackground(BACKGROUND_COLOR);
        JButton editButton = createStyledButton("Edit", 90, 30);
        JButton deleteButton = createStyledButton("Delete", 90, 30);
        recordActionPanel.add(editButton);
        recordActionPanel.add(deleteButton);
        recordsPanel.add(recordActionPanel, BorderLayout.SOUTH);

        
        inputPanel.add(studentInfoPanel, BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(coursesPanel), BorderLayout.CENTER);
        inputPanel.add(storagePanel, BorderLayout.SOUTH);

        
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.add(inputPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.EAST);

        
        add(contentPanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.SOUTH);
        add(recordsPanel, BorderLayout.WEST);

        
        addCourse();

    
        addCourseBtn.addActionListener(e -> addCourse());
        calculateBtn.addActionListener(e -> calculateGPA());
        viewRecordsBtn.addActionListener(e -> toggleRecordsPanel());
        editButton.addActionListener(e -> editSelectedRecord());
        deleteButton.addActionListener(e -> deleteSelectedRecord());
    }

    private void toggleRecordsPanel() {
        if (recordsPanel.isVisible()) {
            recordsPanel.setVisible(false);
            viewRecordsBtn.setText("View Records");
        } else {
            loadRecords();
            recordsPanel.setVisible(true);
            viewRecordsBtn.setText("Hide Records");
        }
        revalidate();
        repaint();
    }

    private JPanel createStyledPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBorder(createStyledBorder(title));
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private Border createStyledBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            title,
            TitledBorder.LEFT, TitledBorder.TOP, 
            SYSTEM_FONT_TITLE, PRIMARY_COLOR);
    }

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(SYSTEM_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        return field;
    }

    private JButton createStyledButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(SYSTEM_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.BLACK); // Changed to black text
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR.darker(), 1, true),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR.brighter());
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
                button.setCursor(Cursor.getDefaultCursor());
            }
        });
        
        return button;
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(SYSTEM_FONT);
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
    }

    private void styleTable(JTable table) {
        table.setFont(SYSTEM_FONT);
        table.getTableHeader().setFont(SYSTEM_FONT_BOLD);
        table.setRowHeight(28);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(true);
        table.setSelectionBackground(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 150));
        table.setSelectionForeground(Color.WHITE);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);
    }

    private void addCourse() {
        CourseEntry entry = new CourseEntry();
        courseEntries.add(entry);
        coursesPanel.add(entry);
        coursesPanel.revalidate();
        coursesPanel.repaint();
    }

    private void calculateGPA() {
        try {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();

            if (name.isEmpty() || id.isEmpty()) {
                showError("Please enter student name and ID");
                return;
            }

            if (courseEntries.isEmpty()) {
                showError("Please add at least one course");
                return;
            }

            double totalGradePoints = 0;
            int totalCreditHours = 0;
            StringBuilder record = new StringBuilder();
            record.append(name).append(";").append(id).append(";");

            for (CourseEntry entry : courseEntries) {
                String courseName = entry.getCourseName();
                String creditHoursStr = entry.getCreditHours();
                String grade = entry.getGrade();

                if (courseName.isEmpty() || creditHoursStr.isEmpty() || grade.isEmpty()) {
                    showError("Please fill in all course details");
                    return;
                }

                int creditHours;
                try {
                    creditHours = Integer.parseInt(creditHoursStr);
                    if (creditHours <= 0) {
                        showError("Credit hours must be positive");
                        return;
                    }
                } catch (NumberFormatException e) {
                    showError("Credit hours must be a number");
                    return;
                }

                double gradePoint = getGradePoint(grade);
                totalGradePoints += gradePoint * creditHours;
                totalCreditHours += creditHours;

                record.append(courseName).append(",")
                      .append(creditHours).append(",")
                      .append(grade).append(";");
            }

            if (totalCreditHours == 0) {
                showError("Total credit hours cannot be zero");
                return;
            }

            double gpa = totalGradePoints / totalCreditHours;
            gpaResultLabel.setText(String.format("GPA: %.2f", gpa));
            record.append(String.format("%.2f", gpa));

            
            String selectedOption = (String) storageCombo.getSelectedItem();
            if ("File".equals(selectedOption)) {
                saveToFile(record.toString());
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Database: Under Construction", 
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            showError("An error occurred: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, 
            "Error", JOptionPane.ERROR_MESSAGE);
    }

    private double getGradePoint(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "B+": return 3.5;
            case "B": return 3.0;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "D": return 1.5;
            case "E": return 1.0;
            case "F": return 0.0;
            default:
                throw new IllegalArgumentException("Invalid grade: " + grade);
        }
    }

    private void saveToFile(String record) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("gpa_records.txt", true))) {
            writer.write(record);
            writer.newLine();
            JOptionPane.showMessageDialog(this, 
                "Record saved to file successfully", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            showError("Failed to save to file: " + e.getMessage());
        }
    }

    private void loadRecords() {
        tableModel.setRowCount(0);
        try (BufferedReader reader = new BufferedReader(new FileReader("gpa_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    String name = parts[0];
                    String id = parts[1];
                    String gpa = parts[parts.length - 1];
                    
                    for (int i = 2; i < parts.length - 1; i++) {
                        String[] courseInfo = parts[i].split(",");
                        if (courseInfo.length == 3) {
                            tableModel.addRow(new Object[]{
                                name, id, courseInfo[0], 
                                courseInfo[1], courseInfo[2], gpa
                            });
                        }
                    }
                }
            }
        } catch (IOException e) {
            
        }
    }

    private void editSelectedRecord() {
        int selectedRow = recordsTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a record to edit");
            return;
        }

        String name = (String) tableModel.getValueAt(selectedRow, 0);
        String id = (String) tableModel.getValueAt(selectedRow, 1);

        
        nameField.setText(name);
        idField.setText(id);
        coursesPanel.removeAll();
        courseEntries.clear();

        
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            if (tableModel.getValueAt(row, 0).equals(name) && 
                tableModel.getValueAt(row, 1).equals(id)) {
                
                String courseName = (String) tableModel.getValueAt(row, 2);
                String creditHours = tableModel.getValueAt(row, 3).toString();
                String grade = (String) tableModel.getValueAt(row, 4);

                CourseEntry entry = new CourseEntry();
                entry.setCourseName(courseName);
                entry.setCreditHours(creditHours);
                entry.setGrade(grade);
                
                courseEntries.add(entry);
                coursesPanel.add(entry);
            }
        }

        coursesPanel.revalidate();
        coursesPanel.repaint();
        toggleRecordsPanel();
        
        JOptionPane.showMessageDialog(this, 
            "Record loaded for editing. Make changes and click 'Calculate GPA' to update.", 
            "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteSelectedRecord() {
        int selectedRow = recordsTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a record to delete");
            return;
        }

        String name = (String) tableModel.getValueAt(selectedRow, 0);
        String id = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete all records for " + name + "?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            
            List<String> recordsToKeep = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("gpa_records.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length >= 2 && !(parts[0].equals(name) && parts[1].equals(id))) {
                        recordsToKeep.add(line);
                    }
                }
            } catch (IOException e) {
                showError("Error reading records: " + e.getMessage());
                return;
            }

            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("gpa_records.txt"))) {
                for (String record : recordsToKeep) {
                    writer.write(record);
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(this, 
                    "Record deleted successfully", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadRecords();
            } catch (IOException e) {
                showError("Error saving records: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            GPACalculator calculator = new GPACalculator();
            calculator.setVisible(true);
        });
    }

    private class CourseEntry extends JPanel {
        private JTextField courseNameField, creditHoursField;
        private JComboBox<String> gradeCombo;

        public CourseEntry() {
            setLayout(new FlowLayout(FlowLayout.LEFT, 8, 5));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

            
            courseNameField = createStyledTextField(15);
            JLabel courseLabel = new JLabel("Course:");
            courseLabel.setFont(SYSTEM_FONT);
            add(courseLabel);
            add(courseNameField);

            creditHoursField = createStyledTextField(3);
            JLabel creditLabel = new JLabel("Credits:");
            creditLabel.setFont(SYSTEM_FONT);
            add(creditLabel);
            add(creditHoursField);

            
            gradeCombo = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D", "E", "F"});
            styleComboBox(gradeCombo);
            JLabel gradeLabel = new JLabel("Grade:");
            gradeLabel.setFont(SYSTEM_FONT);
            add(gradeLabel);
            add(gradeCombo);

            
            JButton removeBtn = new JButton("✕");
            removeBtn.setFont(SYSTEM_FONT_BOLD);
            removeBtn.setForeground(Color.BLACK); // Changed to black text
            removeBtn.setContentAreaFilled(false);
            removeBtn.setBorderPainted(false);
            removeBtn.setFocusPainted(false);
            removeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            removeBtn.addActionListener(e -> {
                courseEntries.remove(this);
                coursesPanel.remove(this);
                coursesPanel.revalidate();
                coursesPanel.repaint();
            });
            add(removeBtn);
        }

        public String getCourseName() {
            return courseNameField.getText().trim();
        }

        public String getCreditHours() {
            return creditHoursField.getText().trim();
        }

        public String getGrade() {
            return (String) gradeCombo.getSelectedItem();
        }

        public void setCourseName(String name) {
            courseNameField.setText(name);
        }

        public void setCreditHours(String hours) {
            creditHoursField.setText(hours);
        }

        public void setGrade(String grade) {
            gradeCombo.setSelectedItem(grade);
        }
    }
}