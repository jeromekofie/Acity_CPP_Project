package healthmanager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * A panel for managing health records with CRUD operations.
 * 
 * @author [Your Name]
 */
public class RecordFormPanel extends JPanel {
    private JTextField nameField, ageField, weightField, heightField, bloodTypeField, allergiesField;
    private JButton saveBtn, updateBtn, deleteBtn;
    private final JTable table;
    private final DefaultTableModel tableModel;

    /**
     * Constructs a RecordFormPanel with the specified table and model.
     * 
     * @param table the JTable to display records
     * @param tableModel the table model containing record data
     */
    public RecordFormPanel(JTable table, DefaultTableModel tableModel) {
        this.table = table;
        this.tableModel = tableModel;
        initializeUI();
        setupEventHandlers();
    }

    private void initializeUI() {
        setLayout(new GridLayout(10, 2, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialize fields
        nameField = createTextField();
        ageField = createTextField();
        weightField = createTextField();
        heightField = createTextField();
        bloodTypeField = createTextField();
        allergiesField = createTextField();

        // Initialize buttons
        saveBtn = createButton("Save Record", new Color(76, 175, 80));
        updateBtn = createButton("Update Record", new Color(33, 150, 243));
        deleteBtn = createButton("Delete Record", new Color(244, 67, 54));

        // Add components to panel
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Age:"));
        add(ageField);
        add(new JLabel("Weight (kg):"));
        add(weightField);
        add(new JLabel("Height (m):"));
        add(heightField);
        add(new JLabel("Blood Type:"));
        add(bloodTypeField);
        add(new JLabel("Allergies:"));
        add(allergiesField);
        add(saveBtn);
        add(updateBtn);
        add(deleteBtn);
        add(new JLabel()); // Empty cell for layout
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void setupEventHandlers() {
        saveBtn.addActionListener(e -> saveRecord());
        updateBtn.addActionListener(e -> updateRecord());
        deleteBtn.addActionListener(e -> deleteRecord());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadSelectedRecord();
            }
        });
    }

    private void loadSelectedRecord() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            nameField.setText(tableModel.getValueAt(row, 1).toString());
            ageField.setText(tableModel.getValueAt(row, 2).toString());
            weightField.setText(tableModel.getValueAt(row, 3).toString());
            heightField.setText(tableModel.getValueAt(row, 4).toString());
            bloodTypeField.setText(tableModel.getValueAt(row, 6).toString());
            allergiesField.setText(tableModel.getValueAt(row, 7).toString());
        }
    }

    private void saveRecord() {
        try {
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            double weight = Double.parseDouble(weightField.getText().trim());
            double height = Double.parseDouble(heightField.getText().trim());
            String bloodType = bloodTypeField.getText().trim();
            String allergies = allergiesField.getText().trim();

            // Validate input
            if (name.isEmpty() || bloodType.isEmpty()) {
                throw new IllegalArgumentException("Required fields are missing");
            }

            HealthRecord record = new HealthRecord(name, age, weight, height, bloodType, allergies);
            
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO health_records (name, age, weight, height, bmi, blood_type, allergies) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                
                stmt.setString(1, record.name);
                stmt.setInt(2, record.age);
                stmt.setDouble(3, record.weight);
                stmt.setDouble(4, record.height);
                stmt.setDouble(5, record.bmi);
                stmt.setString(6, record.bloodType);
                stmt.setString(7, record.allergies);
                stmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Record saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            MainFrame.reloadTable(tableModel);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for age, weight, and height", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (HeadlessException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRecord() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a record to update", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(row, 0);
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            double weight = Double.parseDouble(weightField.getText().trim());
            double height = Double.parseDouble(heightField.getText().trim());
            double bmi = BMIUtils.calculateBMI(weight, height);
            String bloodType = bloodTypeField.getText().trim();
            String allergies = allergiesField.getText().trim();

            // Validate input
            if (name.isEmpty() || bloodType.isEmpty()) {
                throw new IllegalArgumentException("Required fields are missing");
            }

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE health_records SET name=?, age=?, weight=?, height=?, bmi=?, blood_type=?, allergies=? WHERE id=?")) {
                
                stmt.setString(1, name);
                stmt.setInt(2, age);
                stmt.setDouble(3, weight);
                stmt.setDouble(4, height);
                stmt.setDouble(5, bmi);
                stmt.setString(6, bloodType);
                stmt.setString(7, allergies);
                stmt.setInt(8, id);
                stmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Record updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            MainFrame.reloadTable(tableModel);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for age, weight, and height", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to delete this record?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(row, 0);
            
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM health_records WHERE id=?")) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Record deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            MainFrame.reloadTable(tableModel);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        weightField.setText("");
        heightField.setText("");
        bloodTypeField.setText("");
        allergiesField.setText("");
    }
}