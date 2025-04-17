import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.io.File;

public class Employeee extends JFrame {
    private EmployeeManager manager = new EmployeeManager();
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private boolean isDarkMode = false;

    public Employeee() {
        setTitle("Employee Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set up the main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(isDarkMode ? Color.BLACK : Color.WHITE);
        
        // Modern color scheme
        Color primaryColor = new Color(0, 105, 180);
        Color secondaryColor = new Color(240, 240, 240);

        mainPanel.setBackground(isDarkMode ? Color.DARK_GRAY : secondaryColor);

        // Set up the table model
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Phone", "Type", "Salary"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 5 ? Double.class : String.class;
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        employeeTable.setRowHeight(30);
        employeeTable.setSelectionBackground(new Color(200, 230, 255));

        JTableHeader header = employeeTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setPreferredSize(new Dimension(900, 500));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button setup with icons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);

        String[] buttons = {"Add", "Update", "Delete", "Payslip", "Refresh"};
        String[] iconPaths = { 
            "resources/icons/add.png", 
            "resources/icons/update.png", 
            "resources/icons/delete.png", 
            "resources/icons/payslip.png", 
            "resources/icons/refresh.png"
        };

        for (int i = 0; i < buttons.length; i++) {
            JButton btn = new JButton(buttons[i]);

            // Load and set the icon for each button
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPaths[i]));
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            btn.setIcon(icon);

            // Style the button with icon and text
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBackground(new Color(0, 120, 215));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(this::handleButtonClick);
            addHoverEffect(btn);
            buttonPanel.add(btn);
        }

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);
        loadEmployees();
    }

    private void loadEmployees() {
        tableModel.setRowCount(0);
        for (Employee emp : manager.getAllEmployees()) {
            Object[] row = {
                emp.getId(),
                emp.getName(),
                emp.getEmail(),
                emp.getPhone(),
                emp instanceof FullTimeEmployee ? "Full-Time" : "Part-Time",
                emp.calculateSalary()
            };
            tableModel.addRow(row);
        }
    }

    private void handleButtonClick(ActionEvent e) {
        String command = ((JButton) e.getSource()).getText();
        switch (command) {
            case "Add":
                showAddDialog();
                break;
            case "Update":
                showUpdateDialog();
                break;
            case "Delete":
                deleteEmployee();
                break;
            case "Payslip":
                generatePayslip();
                break;
            case "Refresh":
                loadEmployees();
                break;
        }
    }

    private void showAddDialog() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Full-Time", "Part-Time"});
        JTextField salaryField = new JTextField();
        JTextField hourlyRateField = new JTextField();
        JTextField hoursWorkedField = new JTextField();

        // Style components
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        nameField.setFont(fieldFont);
        emailField.setFont(fieldFont);
        phoneField.setFont(fieldFont);
        typeCombo.setFont(fieldFont);
        salaryField.setFont(fieldFont);
        hourlyRateField.setFont(fieldFont);
        hoursWorkedField.setFont(fieldFont);

        // Set initial visibility
        salaryField.setEnabled(true);
        hourlyRateField.setEnabled(false);
        hoursWorkedField.setEnabled(false);

        typeCombo.addActionListener(e -> {
            boolean isFullTime = typeCombo.getSelectedItem().equals("Full-Time");
            salaryField.setEnabled(isFullTime);
            hourlyRateField.setEnabled(!isFullTime);
            hoursWorkedField.setEnabled(!isFullTime);
        });

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Type:"));
        panel.add(typeCombo);
        panel.add(new JLabel("Monthly Salary:"));
        panel.add(salaryField);
        panel.add(new JLabel("Hourly Rate:"));
        panel.add(hourlyRateField);
        panel.add(new JLabel("Hours Worked:"));
        panel.add(hoursWorkedField);

        int result = JOptionPane.showConfirmDialog(
            this, 
            panel, 
            "Add New Employee", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                boolean isFullTime = typeCombo.getSelectedItem().equals("Full-Time");

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (isFullTime) {
                    double salary = Double.parseDouble(salaryField.getText());
                    FullTimeEmployee emp = new FullTimeEmployee(0, name, email, phone, salary);
                    manager.addEmployee(emp);
                } else {
                    double hourlyRate = Double.parseDouble(hourlyRateField.getText());
                    double hoursWorked = Double.parseDouble(hoursWorkedField.getText());
                    PartTimeEmployee emp = new PartTimeEmployee(0, name, email, phone, hourlyRate, hoursWorked);
                    manager.addEmployee(emp);
                }

                loadEmployees();
                JOptionPane.showMessageDialog(this, "Employee added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showUpdateDialog() {
        int row = employeeTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        Employee existing = manager.getEmployee(id);
        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Employee not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField nameField = new JTextField(existing.getName());
        JTextField emailField = new JTextField(existing.getEmail());
        JTextField phoneField = new JTextField(existing.getPhone());
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Full-Time", "Part-Time"});
        JTextField salaryField = new JTextField();
        JTextField hourlyRateField = new JTextField();
        JTextField hoursWorkedField = new JTextField();

        // Style components
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        nameField.setFont(fieldFont);
        emailField.setFont(fieldFont);
        phoneField.setFont(fieldFont);
        typeCombo.setFont(fieldFont);
        salaryField.setFont(fieldFont);
        hourlyRateField.setFont(fieldFont);
        hoursWorkedField.setFont(fieldFont);

        boolean isFullTime = existing instanceof FullTimeEmployee;
        typeCombo.setSelectedItem(isFullTime ? "Full-Time" : "Part-Time");

        if (isFullTime) {
            salaryField.setText(String.valueOf(((FullTimeEmployee) existing).getSalary()));
            hourlyRateField.setEnabled(false);
            hoursWorkedField.setEnabled(false);
        } else {
            hourlyRateField.setText(String.valueOf(((PartTimeEmployee) existing).getHourlyRate()));
            hoursWorkedField.setText(String.valueOf(((PartTimeEmployee) existing).getHoursWorked()));
            salaryField.setEnabled(false);
        }

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Type:"));
        panel.add(typeCombo);
        panel.add(new JLabel("Monthly Salary:"));
        panel.add(salaryField);
        panel.add(new JLabel("Hourly Rate:"));
        panel.add(hourlyRateField);
        panel.add(new JLabel("Hours Worked:"));
        panel.add(hoursWorkedField);

        typeCombo.addActionListener(e -> {
            boolean ft = typeCombo.getSelectedItem().equals("Full-Time");
            salaryField.setEnabled(ft);
            hourlyRateField.setEnabled(!ft);
            hoursWorkedField.setEnabled(!ft);
        });

        int result = JOptionPane.showConfirmDialog(
            this, 
            panel, 
            "Update Employee", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                boolean updatedFullTime = typeCombo.getSelectedItem().equals("Full-Time");

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                manager.deleteEmployee(id); // Remove old record

                if (updatedFullTime) {
                    double salary = Double.parseDouble(salaryField.getText());
                    FullTimeEmployee updated = new FullTimeEmployee(id, name, email, phone, salary);
                    manager.addEmployee(updated);
                } else {
                    double rate = Double.parseDouble(hourlyRateField.getText());
                    double hours = Double.parseDouble(hoursWorkedField.getText());
                    PartTimeEmployee updated = new PartTimeEmployee(id, name, email, phone, rate, hours);
                    manager.addEmployee(updated);
                }

                loadEmployees();
                JOptionPane.showMessageDialog(this, "Employee updated successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteEmployee() {
        int row = employeeTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to delete this employee?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            manager.deleteEmployee(id);
            loadEmployees();
            JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
        }
    }

    private void generatePayslip() {
        int row = employeeTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(row, 0);
        PaySlipGenerator.generatePaySlip(manager.getEmployee(id));
        JOptionPane.showMessageDialog(this, "Payslip generated successfully!");
    }

    private void addHoverEffect(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 150, 255));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 120, 215));
            }
        });
    }

    // Method to toggle light/dark mode
    public void toggleTheme() {
        isDarkMode = !isDarkMode;
        setLookAndFeel();
        repaint();
    }

    private void setLookAndFeel() {
        Color background = isDarkMode ? Color.DARK_GRAY : Color.WHITE;
        Color textColor = isDarkMode ? Color.WHITE : Color.BLACK;
        
        getContentPane().setBackground(background);
        employeeTable.setBackground(background);
        employeeTable.setForeground(textColor);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            Employeee frame = new Employeee();
            frame.setVisible(true);
        });
    }
}
