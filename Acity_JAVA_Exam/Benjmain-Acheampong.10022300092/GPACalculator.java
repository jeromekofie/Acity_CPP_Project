import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.Timer;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField;
    private DefaultTableModel courseTableModel;
    private JTable courseTable;
    private JLabel gpaResult;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel viewPanel;
    private DefaultTableModel viewTableModel;
    private static final String RECORDS_FILE = "gpa_records.txt";
    private static final boolean USE_FILE_STORAGE = true; 
    private boolean isDarkMode = false; 
    private JPanel creditsPanel;

    private static final Map<String, Double> GRADE_POINTS = new HashMap<>();
    static {
        GRADE_POINTS.put("A", 4.0);
        GRADE_POINTS.put("B+", 3.5);
        GRADE_POINTS.put("B", 3.0);
        GRADE_POINTS.put("C+", 2.5);
        GRADE_POINTS.put("C", 2.0);
        GRADE_POINTS.put("D", 1.5);
        GRADE_POINTS.put("E", 1.0);
        GRADE_POINTS.put("F", 0.0);
    }

    public GPACalculator() {
        setTitle("ACity GPA Calculator");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(240, 240, 245));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel titleLabel = new JLabel("GPA");
        titleLabel.setFont(new Font("Sakana", Font.PLAIN, 33));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.RED);
        sidebar.add(titleLabel);
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        ImageIcon logoIcon = new ImageIcon("C:\\Users\\kojob\\Documents\\PROGRAMMING\\Java\\EOD\\EXAM\\ACity Logo.png");
        Image logoImage = logoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(logoLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton calcButton = createSidebarButton("Calculator");
        JButton viewButton = createSidebarButton("View Records");
        
        calcButton.addActionListener(e -> cardLayout.show(mainPanel, "calculator"));
        viewButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "view");
            loadRecordsFromFile();
        });
        
        sidebar.add(calcButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(viewButton);

        JButton settingsButton = createSidebarButton("Settings");
        settingsButton.addActionListener(e -> cardLayout.show(mainPanel, "settings"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(settingsButton);

        JButton creditsButton = createSidebarButton("Credits");
        creditsButton.addActionListener(e -> cardLayout.show(mainPanel, "credits"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(creditsButton);
        
        sidebar.add(Box.createVerticalGlue());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        JPanel calculatorPanel = createCalculatorPanel();
        mainPanel.add(calculatorPanel, "calculator");
        
        viewPanel = createViewPanel();
        mainPanel.add(viewPanel, "view");

        JPanel settingsPanel = createSettingsPanel();
        mainPanel.add(settingsPanel, "settings");

        creditsPanel = createCreditsPanel();
        mainPanel.add(creditsPanel, "credits");
        
        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Montserrat", Font.PLAIN, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(new Color(240, 240, 245));
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(225, 225, 230));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 240, 245));
            }
        });
        
        return button;
    }
    
    private JPanel createCalculatorPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Student Information");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy++;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx++;
        nameField = new JTextField(20);
        nameField.setFont(new Font("Montserrat", Font.PLAIN, 14));
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx++;
        idField = new JTextField(20);
        idField.setFont(new Font("Montserrat", Font.PLAIN, 14));
        formPanel.add(idField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        formPanel.add(new JLabel("Courses:"), gbc);
        
        String[] columnNames = {"Course", "Credit Hours", "Grade"};
        courseTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) return Integer.class;
                return String.class;
            }
        };
        
        courseTable = new JTable(courseTableModel);
        courseTable.setFont(new Font("Montserrat", Font.PLAIN, 14));
        courseTable.setRowHeight(30);
        courseTable.getTableHeader().setFont(new Font("Montserrat", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(courseTable);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(scrollPane, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addButton = new JButton("Add Course");
        styleButton(addButton);
        addButton.addActionListener(e -> courseTableModel.addRow(new Object[]{"", 3, "B"}));
        
        JButton removeButton = new JButton("Remove Selected");
        styleButton(removeButton);
        removeButton.addActionListener(e -> {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow != -1) {
                courseTableModel.removeRow(selectedRow);
            }
        });
        
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);                  
        
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;
        formPanel.add(buttonPanel, gbc);
        
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(Color.WHITE);
        
        JButton calculateButton = new JButton("Calculate & Save GPA");
        styleButton(calculateButton, new Color(0, 122, 255), Color.BLACK);
        calculateButton.setFont(new Font("Montserrat", Font.BOLD, 16));
        calculateButton.addActionListener(e -> calculateGPA());
        
        gpaResult = new JLabel("GPA: -", SwingConstants.CENTER);
        gpaResult.setFont(new Font("Montserrat", Font.BOLD, 24));
        gpaResult.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        resultPanel.add(calculateButton, BorderLayout.NORTH);
        resultPanel.add(gpaResult, BorderLayout.CENTER);
        
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(resultPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Student Records", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Name", "ID", "Courses", "Credit Hours", "Grades", "GPA"};
        viewTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; 
            }
        };

        JTable viewTable = new JTable(viewTableModel);
        viewTable.setFont(new Font("Montserrat", Font.PLAIN, 14));
        viewTable.setRowHeight(30);
        viewTable.getTableHeader().setFont(new Font("Montserrat", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < viewTable.getColumnCount(); i++) {
            viewTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(viewTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton refreshButton = new JButton("Refresh");
        styleButton(refreshButton);
        refreshButton.addActionListener(e -> loadRecordsFromFile());

        JButton deleteButton = new JButton("Delete Selected");
        styleButton(deleteButton, new Color(255, 59, 48), Color.BLACK);
        deleteButton.addActionListener(e -> deleteSelectedRecord());

        JComboBox<String> saveOptions = new JComboBox<>(new String[]{"Save to File", "Save to Database"});
        styleButton(new JButton()); 

        JButton saveButton = new JButton("Save Changes");
        styleButton(saveButton, new Color(0, 122, 255), Color.BLACK);
        saveButton.addActionListener(e -> {
            String selectedOption = (String) saveOptions.getSelectedItem();
            if ("Save to File".equals(selectedOption)) {
                saveAllRecords();
            } else if ("Save to Database".equals(selectedOption)) {
                JOptionPane.showMessageDialog(this, "Database: Under Construction", 
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveOptions);
        buttonPanel.add(saveButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton toggleModeButton = new JButton("Toggle Light/Dark Mode");
        styleButton(toggleModeButton);
        toggleModeButton.addActionListener(e -> toggleLightDarkMode());
        panel.add(toggleModeButton);

        return panel;
    }

    private void toggleLightDarkMode() {
        isDarkMode = !isDarkMode;
        Color bgColor = isDarkMode ? Color.DARK_GRAY : Color.WHITE;
        Color fgColor = isDarkMode ? Color.WHITE : Color.BLACK;

        mainPanel.setBackground(bgColor);
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(bgColor);
            }
        }
        repaint();
    }

    private JPanel createCreditsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        try {
            BufferedImage backdropImage = ImageIO.read(new File("C:\\Users\\kojob\\Documents\\PROGRAMMING\\Java\\EOD\\EXAM\\ACity Campus.jpg"));
            BufferedImage darkenedImage = new BufferedImage(backdropImage.getWidth(), backdropImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = darkenedImage.createGraphics();
            g2d.drawImage(backdropImage, 0, 0, null);
            g2d.setColor(new Color(0, 0, 0, 150)); 
            g2d.fillRect(0, 0, darkenedImage.getWidth(), darkenedImage.getHeight());
            g2d.dispose();

            JLabel backdropLabel = new JLabel(new ImageIcon(darkenedImage));
            backdropLabel.setLayout(new BorderLayout());
            panel.add(backdropLabel, BorderLayout.CENTER);

            // i used a java effect that makes the scroll wheel move automatically in order to do this 

            JLabel creditsText = new JLabel("<html><div style='text-align: center; font-size: 20px; color: white;'>"
                    + "Special Thanks to<br><br>"
                    + "<b>Mr. Kelly and Mr. Kelvin</b><br>"
                    + "For their invaluable guidance and support<br>"
                    + "in mastering Java programming.<br><br>"
                    + "This project is dedicated to you!<br><br>"
                    + "ACity GPA Calculator<br>2025"
                    + "</div></html>", SwingConstants.CENTER);
            creditsText.setFont(new Font("Montserrat", Font.BOLD, 20));
            creditsText.setForeground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(creditsText);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(null);
            backdropLabel.add(scrollPane, BorderLayout.CENTER);

            Timer timer = new Timer(30, new ActionListener() {
                int y = 600;

                @Override
                public void actionPerformed(ActionEvent e) {
                    y -= 2;
                    creditsText.setLocation(creditsText.getX(), y);
                    if (y + creditsText.getHeight() < 0) {
                        y = panel.getHeight();
                    }
                }
            });
            timer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return panel;
    }
    
    private void updateGPA(int row) {
        try {
            String gradesStr = (String) viewTableModel.getValueAt(row, 4);
            String hoursStr = (String) viewTableModel.getValueAt(row, 3);
            
            String[] grades = gradesStr.split(",\\s*");
            String[] hours = hoursStr.split(",\\s*");
            
            if (grades.length != hours.length) {
                JOptionPane.showMessageDialog(this, 
                    "Number of grades doesn't match number of courses", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double totalPoints = 0;
            int totalHours = 0;
            
            for (int i = 0; i < grades.length; i++) {
                String grade = grades[i];
                if (!GRADE_POINTS.containsKey(grade)) {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid grade: " + grade, 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int hour = Integer.parseInt(hours[i]);
                double points = GRADE_POINTS.get(grade);
                totalPoints += points * hour;
                totalHours += hour;
            }
            
            double gpa = totalPoints / totalHours;
            viewTableModel.setValueAt(String.format("%.2f", gpa), row, 5);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error calculating GPA: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveAllRecords() {
        if (!USE_FILE_STORAGE) {
            JOptionPane.showMessageDialog(this, "Database: Under Construction", 
                "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RECORDS_FILE))) {
            for (int i = 0; i < viewTableModel.getRowCount(); i++) {
                String name = viewTableModel.getValueAt(i, 0).toString();
                String id = viewTableModel.getValueAt(i, 1).toString();
                String courses = viewTableModel.getValueAt(i, 2).toString();
                String creditHours = viewTableModel.getValueAt(i, 3).toString();
                String grades = viewTableModel.getValueAt(i, 4).toString();
                String gpa = viewTableModel.getValueAt(i, 5).toString();
                
                writer.write(String.format("%s|%s|%s|%s|%s|%s\n", name, id, courses, creditHours, grades, gpa));
            }
            JOptionPane.showMessageDialog(this, "Records saved successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving records: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void styleButton(JButton button) {
        styleButton(button, new Color(200, 200, 205), Color.BLACK);
    }
    
    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("Montserrat", Font.PLAIN, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 150), 
            1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(
                    Math.max(bgColor.getRed() - 20, 0),
                    Math.max(bgColor.getGreen() - 20, 0),
                    Math.max(bgColor.getBlue() - 20, 0)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }
    
    private void calculateGPA() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter student name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter student ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (courseTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please add at least one course", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double totalPoints = 0;
        int totalHours = 0;
        List<String> courseNames = new ArrayList<>();
        List<Integer> creditHours = new ArrayList<>();
        List<String> grades = new ArrayList<>();

        for (int i = 0; i < courseTableModel.getRowCount(); i++) {
            String course = (String) courseTableModel.getValueAt(i, 0);
            Object hoursObj = courseTableModel.getValueAt(i, 1);
            String grade = (String) courseTableModel.getValueAt(i, 2);

            if (course.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Course name cannot be empty in row " + (i + 1), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!(hoursObj instanceof Integer)) {
                JOptionPane.showMessageDialog(this, "Credit hours must be a number in row " + (i + 1), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int hours = (Integer) hoursObj;
            if (hours <= 0) {
                JOptionPane.showMessageDialog(this, "Credit hours must be positive in row " + (i + 1), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!GRADE_POINTS.containsKey(grade)) {
                JOptionPane.showMessageDialog(this, "Invalid grade in row " + (i + 1), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double points = GRADE_POINTS.get(grade);
            totalPoints += points * hours;
            totalHours += hours;

            courseNames.add(course);
            creditHours.add(hours);
            grades.add(grade);
        }

        double gpa = totalPoints / totalHours;

        if (USE_FILE_STORAGE) {
            saveRecordToFile(
                nameField.getText(), 
                idField.getText(), 
                String.join(", ", courseNames),
                String.join(", ", creditHours.stream().map(String::valueOf).toArray(String[]::new)),
                String.join(", ", grades),
                gpa
            );
        } else {
            JOptionPane.showMessageDialog(this, "Database: Under Construction", 
                "Info", JOptionPane.INFORMATION_MESSAGE);
        }

        gpaResult.setText(String.format("GPA: %.2f", gpa));
    }
    
    private void saveRecordToFile(String name, String id, String courses, String creditHours, String grades, double gpa) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RECORDS_FILE, true))) {
            writer.write(String.format("%s|%s|%s|%s|%s|%.2f\n", name, id, courses, creditHours, grades, gpa));
            JOptionPane.showMessageDialog(this, "Record saved to file successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to file: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadRecordsFromFile() {
        if (!USE_FILE_STORAGE) {
            JOptionPane.showMessageDialog(this, "Database: Under Construction", 
                "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        viewTableModel.setRowCount(0);
        
        File file = new File(RECORDS_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(RECORDS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    viewTableModel.addRow(new Object[]{
                        parts[0], 
                        parts[1], 
                        parts[2], 
                        parts[3], 
                        parts[4],  
                        parts[5]  
                    });
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading records: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteSelectedRecord() {
        JTable viewTable = (JTable) ((JScrollPane) viewPanel.getComponent(1)).getViewport().getView();
        int selectedRow = viewTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!USE_FILE_STORAGE) {
            JOptionPane.showMessageDialog(this, "Database: Under Construction", 
                "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        ArrayList<String> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RECORDS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                records.add(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading records: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (selectedRow < records.size()) {
            records.remove(selectedRow);
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(RECORDS_FILE))) {
                for (String record : records) {
                    writer.write(record + "\n");
                }
                JOptionPane.showMessageDialog(this, "Record deleted successfully", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadRecordsFromFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving records: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
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
}



















































