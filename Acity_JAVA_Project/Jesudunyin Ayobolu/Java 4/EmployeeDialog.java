import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class EmployeeDialog extends JDialog {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextArea addressArea;
    private JTextField nextOfKinField;
    private JComboBox<String> typeComboBox;
    private JFormattedTextField salaryField;
    private JFormattedTextField hourlyRateField;
    private JLabel grossSalaryLabel;
    private JLabel netSalaryLabel;
    private JButton saveButton;
    private JButton cancelButton;
    private boolean saved = false;
    private Employee employee;
    private static final Color TEXT_COLOR = new Color(30, 30, 30);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);

    public EmployeeDialog(JPanel parent, Employee employee) {
        super((Frame)SwingUtilities.getWindowAncestor(parent), employee == null ? "Add Employee" : "Edit Employee", true);
        this.employee = employee;
        setSize(400, 700);
        setLocationRelativeTo(null); // This centers the dialog on screen
        setLayout(new BorderLayout());
        setResizable(true);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255, 240));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;

        // First Name
        addFormField(formPanel, gbc, "First Name:", firstNameField = createLargeTextField());
        
        // Last Name
        addFormField(formPanel, gbc, "Last Name:", lastNameField = createLargeTextField());
        
        // Address
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Arial", Font.BOLD, 18));
        formPanel.add(addressLabel, gbc);

        gbc.gridx = 1;
        addressArea = new JTextArea(5, 30);
        addressArea.setFont(new Font("Arial", Font.PLAIN, 16));
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        addressScroll.setPreferredSize(new Dimension(400, 150));
        formPanel.add(addressScroll, gbc);

        // Next of Kin
        addFormField(formPanel, gbc, "Next of Kin:", nextOfKinField = createLargeTextField());

        // Employee Type
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel typeLabel = new JLabel("Employee Type:");
        typeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        formPanel.add(typeLabel, gbc);

        gbc.gridx = 1;
        typeComboBox = new JComboBox<>(new String[]{"FULL_TIME", "PART_TIME"});
        typeComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        typeComboBox.setPreferredSize(new Dimension(400, 50));
        typeComboBox.addActionListener(e -> updateSalaryFieldsVisibility());
        formPanel.add(typeComboBox, gbc);

        // Salary/Hourly Rate fields
        addFormField(formPanel, gbc, "Salary:", salaryField = createCurrencyField());
        addFormField(formPanel, gbc, "Hourly Rate:", hourlyRateField = createCurrencyField());

        // Calculated fields (display only)
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel grossLabel = new JLabel("Gross Salary:");
        grossLabel.setFont(new Font("Arial", Font.BOLD, 18));
        formPanel.add(grossLabel, gbc);

        gbc.gridx = 1;
        grossSalaryLabel = new JLabel("$0.00");
        grossSalaryLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(grossSalaryLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel netLabel = new JLabel("Net Salary:");
        netLabel.setFont(new Font("Arial", Font.BOLD, 18));
        formPanel.add(netLabel, gbc);

        gbc.gridx = 1;
        netSalaryLabel = new JLabel("$0.00");
        netSalaryLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(netSalaryLabel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setOpaque(false);
        
        saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 18));
        saveButton.setPreferredSize(new Dimension(200, 60));
        saveButton.setBackground(BUTTON_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveEmployee());
        
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 18));
        cancelButton.setPreferredSize(new Dimension(200, 60));
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add components to dialog
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        wrapperPanel.add(formPanel, BorderLayout.CENTER);
        wrapperPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        backgroundPanel.add(wrapperPanel, BorderLayout.CENTER);

        // Add property change listeners for formatted fields
        salaryField.addPropertyChangeListener("value", e -> calculateSalaries());
        hourlyRateField.addPropertyChangeListener("value", e -> calculateSalaries());

        if (employee != null) {
            loadEmployeeData();
        }

        updateSalaryFieldsVisibility();
        SwingUtilities.invokeLater(() -> firstNameField.requestFocusInWindow());
    }

    private JTextField createLargeTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setMargin(new Insets(12, 12, 12, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        field.setPreferredSize(new Dimension(400, 50));
        return field;
    }

    private JFormattedTextField createCurrencyField() {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        JFormattedTextField field = new JFormattedTextField(format);
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setMargin(new Insets(12, 12, 12, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        field.setPreferredSize(new Dimension(400, 50));
        field.setValue(BigDecimal.ZERO);
        return field;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field) {
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(TEXT_COLOR);
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void updateSalaryFieldsVisibility() {
        String type = (String) typeComboBox.getSelectedItem();
        salaryField.setVisible(type.equals("FULL_TIME"));
        hourlyRateField.setVisible(type.equals("PART_TIME"));
        calculateSalaries();
        revalidate();
        repaint();
    }

    private void calculateSalaries() {
        try {
            BigDecimal baseAmount;
            String type = (String) typeComboBox.getSelectedItem();
            
            if (type.equals("FULL_TIME")) {
                baseAmount = salaryField.getValue() != null ? 
                    new BigDecimal(salaryField.getValue().toString().replaceAll("[^\\d.]", "")) : 
                    BigDecimal.ZERO;
            } else {
                BigDecimal hourlyRate = hourlyRateField.getValue() != null ? 
                    new BigDecimal(hourlyRateField.getValue().toString().replaceAll("[^\\d.]", "")) : 
                    BigDecimal.ZERO;
                // Assume 160 hours per month (20 days * 8 hours)
                baseAmount = hourlyRate.multiply(new BigDecimal("160"));
            }
            
            // Calculate gross salary
            BigDecimal grossSalary = baseAmount;
            
            // Calculate deductions (20% tax + 5% other deductions)
            BigDecimal tax = grossSalary.multiply(new BigDecimal("0.20"));
            BigDecimal otherDeductions = grossSalary.multiply(new BigDecimal("0.05"));
            BigDecimal netSalary = grossSalary.subtract(tax).subtract(otherDeductions);

            // Update labels
            DecimalFormat df = new DecimalFormat("'$'#,###.00");
            grossSalaryLabel.setText(df.format(grossSalary));
            netSalaryLabel.setText(df.format(netSalary));
        } catch (Exception e) {
            grossSalaryLabel.setText("$0.00");
            netSalaryLabel.setText("$0.00");
        }
    }

    private void loadEmployeeData() {
        firstNameField.setText(employee.getFirstName());
        lastNameField.setText(employee.getLastName());
        addressArea.setText(employee.getAddress());
        nextOfKinField.setText(employee.getNextOfKin());
        typeComboBox.setSelectedItem(employee.getEmployeeType());
        
        if (employee.getEmployeeType().equals("FULL_TIME")) {
            salaryField.setValue(employee.getSalary() != null ? employee.getSalary() : BigDecimal.ZERO);
        } else {
            hourlyRateField.setValue(employee.getHourlyRate() != null ? employee.getHourlyRate() : BigDecimal.ZERO);
        }
        
        calculateSalaries();
    }

    private void saveEmployee() {
        if (firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty() ||
            addressArea.getText().trim().isEmpty() || nextOfKinField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String type = (String) typeComboBox.getSelectedItem();
        try {
            BigDecimal salary = type.equals("FULL_TIME") ? 
                new BigDecimal(salaryField.getValue().toString().replaceAll("[^\\d.]", "")) : null;
            BigDecimal hourlyRate = type.equals("PART_TIME") ? 
                new BigDecimal(hourlyRateField.getValue().toString().replaceAll("[^\\d.]", "")) : null;
            BigDecimal grossSalary = new BigDecimal(grossSalaryLabel.getText().replaceAll("[^\\d.]", ""));
            BigDecimal netSalary = new BigDecimal(netSalaryLabel.getText().replaceAll("[^\\d.]", ""));

            if (employee == null) {
                employee = new Employee();
                employee.setFirstName(firstNameField.getText());
                employee.setLastName(lastNameField.getText());
                employee.setAddress(addressArea.getText());
                employee.setNextOfKin(nextOfKinField.getText());
                employee.setEmployeeType(type);
                employee.setSalary(salary);
                employee.setHourlyRate(hourlyRate);
                employee.setGrossSalary(grossSalary);
                employee.setNetSalary(netSalary);

                addEmployee(employee);
            } else {
                employee.setFirstName(firstNameField.getText());
                employee.setLastName(lastNameField.getText());
                employee.setAddress(addressArea.getText());
                employee.setNextOfKin(nextOfKinField.getText());
                employee.setEmployeeType(type);
                employee.setSalary(salary);
                employee.setHourlyRate(hourlyRate);
                employee.setGrossSalary(grossSalary);
                employee.setNetSalary(netSalary);

                updateEmployee(employee);
            }

            saved = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addEmployee(Employee emp) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO employees (first_name, last_name, address, next_of_kin, employee_type, " +
                        "salary, hourly_rate, gross_salary, net_salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, emp.getFirstName());
            stmt.setString(2, emp.getLastName());
            stmt.setString(3, emp.getAddress());
            stmt.setString(4, emp.getNextOfKin());
            stmt.setString(5, emp.getEmployeeType());
            stmt.setBigDecimal(6, emp.getSalary());
            stmt.setBigDecimal(7, emp.getHourlyRate());
            stmt.setBigDecimal(8, emp.getGrossSalary());
            stmt.setBigDecimal(9, emp.getNetSalary());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                emp.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateEmployee(Employee emp) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE employees SET first_name = ?, last_name = ?, address = ?, next_of_kin = ?, " +
                        "employee_type = ?, salary = ?, hourly_rate = ?, gross_salary = ?, net_salary = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, emp.getFirstName());
            stmt.setString(2, emp.getLastName());
            stmt.setString(3, emp.getAddress());
            stmt.setString(4, emp.getNextOfKin());
            stmt.setString(5, emp.getEmployeeType());
            stmt.setBigDecimal(6, emp.getSalary());
            stmt.setBigDecimal(7, emp.getHourlyRate());
            stmt.setBigDecimal(8, emp.getGrossSalary());
            stmt.setBigDecimal(9, emp.getNetSalary());
            stmt.setInt(10, emp.getId());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }
}