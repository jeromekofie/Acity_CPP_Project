import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class EmployeeManagementPanel extends JPanel {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> searchComboBox;
    private JTextField searchField;
    private static final Color TEXT_COLOR = new Color(30, 30, 30);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color TRANSLUCENT_WHITE = new Color(255, 255, 255, 220);
    private static final Color RED_BUTTON_COLOR = new Color(220, 80, 60);

    public EmployeeManagementPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Create left panel for action buttons
        JPanel leftButtonPanel = new JPanel();
        leftButtonPanel.setOpaque(false);
        leftButtonPanel.setLayout(new BoxLayout(leftButtonPanel, BoxLayout.Y_AXIS));
        leftButtonPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        // Main action buttons
        JButton addButton = createStyledButton("Add Employee");
        JButton editButton = createStyledButton("Edit Employee");
        JButton deleteButton = createStyledButton("Delete Employee");
        JButton refreshButton = createStyledButton("Refresh");

        addButton.addActionListener(e -> showEmployeeDialog(null));
        editButton.addActionListener(e -> editSelectedEmployee());
        deleteButton.addActionListener(e -> deleteSelectedEmployee());
        refreshButton.addActionListener(e -> refreshEmployeeTable());

        // Add vertical spacing between buttons
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        editButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        deleteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        refreshButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftButtonPanel.add(addButton);
        leftButtonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftButtonPanel.add(editButton);
        leftButtonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftButtonPanel.add(deleteButton);
        leftButtonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftButtonPanel.add(refreshButton);

        // Add filler to push logout/exit buttons to bottom
        leftButtonPanel.add(Box.createVerticalGlue());

        // Logout and Exit buttons (in red)
        JButton logoutButton = createRedButton("Logout");
        JButton exitButton = createRedButton("Exit");

        logoutButton.addActionListener(e -> returnToLogin());
        exitButton.addActionListener(e -> confirmExit());

        logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        exitButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftButtonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        leftButtonPanel.add(logoutButton);
        leftButtonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftButtonPanel.add(exitButton);

        add(leftButtonPanel, BorderLayout.WEST);

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        // Create top panel for logo
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(0, 0, 20, 0)); // Add some space below logo
        
        // Add logo to top right
        try {
            ImageIcon logoIcon = new ImageIcon("images.png");
            // Scale the image if needed
            Image logoImage = logoIcon.getImage().getScaledInstance(200, 30, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(logoImage);
            JLabel logoLabel = new JLabel(logoIcon);
            logoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            topPanel.add(logoLabel, BorderLayout.EAST);
        } catch (Exception e) {
            System.err.println("Error loading logo image: " + e.getMessage());
        }
        
        contentPanel.add(topPanel, BorderLayout.NORTH);

        // Search panel at bottom
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        searchPanel.setOpaque(false);
        
        JLabel searchLabel = new JLabel("Search by:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchLabel.setForeground(TEXT_COLOR);
        
        searchComboBox = new JComboBox<>(new String[]{"ID","First Name", "Last Name", "Employee Type"});
        searchComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        searchComboBox.setPreferredSize(new Dimension(150, 35));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(250, 35));
        
        JButton searchButton = createStyledButton("Search");
        searchButton.addActionListener(e -> searchEmployees());

        searchPanel.add(searchLabel);
        searchPanel.add(searchComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        contentPanel.add(searchPanel, BorderLayout.SOUTH);

        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"ID", "First Name", "Last Name", "Address", "Next of Kin", 
            "Type", "Salary/Hourly Rate", "Gross Salary", "Net Salary"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 12));
        employeeTable.setRowHeight(30);
        employeeTable.setForeground(TEXT_COLOR);
        employeeTable.setBackground(TRANSLUCENT_WHITE);
        
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        refreshEmployeeTable();
    }

    private JButton createRedButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 35));
        button.setMinimumSize(new Dimension(150, 35));
        button.setMaximumSize(new Dimension(150, 35));
        button.setBackground(RED_BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void returnToLogin() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            // Show login window - replace with your actual login class
            new LoginWindow().setVisible(true);
        }
    }

    private void confirmExit() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit the application?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 35));
        button.setMinimumSize(new Dimension(150, 35));
        button.setMaximumSize(new Dimension(150, 35));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void refreshEmployeeTable() {
        tableModel.setRowCount(0);
        List<Employee> employees = getAllEmployees();
        for (Employee emp : employees) {
            Object[] row = {
                emp.getId(),
                emp.getFirstName(),
                emp.getLastName(),
                emp.getAddress(),
                emp.getNextOfKin(),
                emp.getEmployeeType(),
                emp.getEmployeeType().equals("FULL_TIME") ? formatCurrency(emp.getSalary()) : formatCurrency(emp.getHourlyRate()),
                formatCurrency(emp.getGrossSalary()),
                formatCurrency(emp.getNetSalary())
            };
            tableModel.addRow(row);
        }
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) return "$0.00";
        DecimalFormat df = new DecimalFormat("'$'#,###.00");
        return df.format(amount);
    }

    private void editSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to edit", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Employee employee = getEmployeeById(id);
        if (employee != null) {
            showEmployeeDialog(employee);
        }
    }

    private void deleteSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this employee?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            deleteEmployee(id);
            refreshEmployeeTable();
        }
    }

    private void searchEmployees() {
        String searchBy = (String) searchComboBox.getSelectedItem();
        String searchValue = searchField.getText().trim();

        if (searchValue.isEmpty()) {
            refreshEmployeeTable();
            return;
        }

        tableModel.setRowCount(0);
        List<Employee> employees = searchEmployees(searchBy, searchValue);
        for (Employee emp : employees) {
            Object[] row = {
                emp.getId(),
                emp.getFirstName(),
                emp.getLastName(),
                emp.getAddress(),
                emp.getNextOfKin(),
                emp.getEmployeeType(),
                emp.getEmployeeType().equals("FULL_TIME") ? formatCurrency(emp.getSalary()) : formatCurrency(emp.getHourlyRate()),
                formatCurrency(emp.getGrossSalary()),
                formatCurrency(emp.getNetSalary())
            };
            tableModel.addRow(row);
        }
    }

    private void showEmployeeDialog(Employee employee) {
        EmployeeDialog dialog = new EmployeeDialog(this, employee);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            refreshEmployeeTable();
        }
    }

    private List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM employees";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                employees.add(extractEmployeeFromResultSet(rs));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return employees;
    }

    private List<Employee> searchEmployees(String searchBy, String searchValue) {
        List<Employee> employees = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql;
            PreparedStatement stmt;
            
            if (searchBy.equals("Employee Type")) {
                sql = "SELECT * FROM employees WHERE employee_type = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, searchValue.toUpperCase());
            } else {
                String column = searchBy.equals("First Name") ? "first_name" : "last_name";
                sql = "SELECT * FROM employees WHERE " + column + " LIKE ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, "%" + searchValue + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                employees.add(extractEmployeeFromResultSet(rs));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error searching employees: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return employees;
    }

    private Employee getEmployeeById(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM employees WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractEmployeeFromResultSet(rs);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private void deleteEmployee(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM employees WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Employee extractEmployeeFromResultSet(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setId(rs.getInt("id"));
        emp.setFirstName(rs.getString("first_name"));
        emp.setLastName(rs.getString("last_name"));
        emp.setAddress(rs.getString("address"));
        emp.setNextOfKin(rs.getString("next_of_kin"));
        emp.setEmployeeType(rs.getString("employee_type"));
        emp.setSalary(rs.getBigDecimal("salary"));
        emp.setHourlyRate(rs.getBigDecimal("hourly_rate"));
        emp.setGrossSalary(rs.getBigDecimal("gross_salary"));
        emp.setNetSalary(rs.getBigDecimal("net_salary"));
        return emp;
    }
}