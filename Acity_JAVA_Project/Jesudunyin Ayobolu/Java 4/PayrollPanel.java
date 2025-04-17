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
import java.io.*;

public class PayrollPanel extends JPanel {
    private JTable payrollTable;
    private DefaultTableModel tableModel;
    private JButton generateButton;
    private JButton downloadButton;
    private static final Color TEXT_COLOR = new Color(30, 30, 30);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color TRANSLUCENT_WHITE = new Color(255, 255, 255, 220);

    public PayrollPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Create top panel with logo and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Add logo to top right
        try {
            ImageIcon logoIcon = new ImageIcon("images.png");
            Image logoImage = logoIcon.getImage().getScaledInstance(200, 20, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(logoImage);
            JLabel logoLabel = new JLabel(logoIcon);
            logoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            topPanel.add(logoLabel, BorderLayout.EAST);
        } catch (Exception e) {
            System.err.println("Error loading logo image: " + e.getMessage());
        }

        // Generate button
        generateButton = createStyledButton("Generate Payroll");
        generateButton.addActionListener(e -> generatePayroll());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.add(generateButton);
        topPanel.add(buttonPanel, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Employee Type", "Gross Salary", "Deductions", "Net Salary"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        payrollTable = new JTable(tableModel);
        payrollTable.setFont(new Font("Arial", Font.PLAIN, 12));
        payrollTable.setRowHeight(30);
        payrollTable.setForeground(TEXT_COLOR);
        payrollTable.setBackground(TRANSLUCENT_WHITE);
        payrollTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(payrollTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);

        // Download button
        downloadButton = createStyledButton("Download Report");
        downloadButton.addActionListener(e -> showDownloadDialog());
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        bottomPanel.setOpaque(false);
        bottomPanel.add(downloadButton);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshPayrollTable();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(180, 35));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    private void showDownloadDialog() {
        // Create custom dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Save Payroll Report");
        dialog.setModal(true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Set background
        try {
            ImageIcon bgIcon = new ImageIcon("bg1.jpg");
            JLabel bgLabel = new JLabel(new ImageIcon(bgIcon.getImage().getScaledInstance(
                dialog.getWidth(), dialog.getHeight(), Image.SCALE_SMOOTH)));
            bgLabel.setLayout(new BorderLayout());
            dialog.setContentPane(bgLabel);
        } catch (Exception e) {
            dialog.setContentPane(new JPanel());
            System.err.println("Error loading background: " + e.getMessage());
        }

        // Create options panel
        JPanel optionsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        optionsPanel.setOpaque(false);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JButton htmlButton = createStyledButton("Save as HTML");
        htmlButton.addActionListener(e -> {
            exportToHTML();
            dialog.dispose();
        });

        JButton textButton = createStyledButton("Save as Text File");
        textButton.addActionListener(e -> {
            exportToText();
            dialog.dispose();
        });

        JButton cancelButton = createStyledButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());

        optionsPanel.add(htmlButton);
        optionsPanel.add(textButton);
        optionsPanel.add(cancelButton);

        dialog.getContentPane().add(optionsPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void exportToHTML() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save as HTML");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("HTML Files", "html"));
        fileChooser.setSelectedFile(new File("payroll_report.html"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".html")) {
                file = new File(file.getParentFile(), file.getName() + ".html");
            }

            try (PrintWriter out = new PrintWriter(file)) {
                out.println("<!DOCTYPE html>");
                out.println("<html><head><title>Payroll Report</title>");
                out.println("<style>");
                out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
                out.println("h1 { color: #4682B4; }");
                out.println("table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
                out.println("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
                out.println("th { background-color: #4682B4; color: white; }");
                out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
                out.println(".footer { margin-top: 20px; font-size: 0.8em; color: #666; }");
                out.println("</style></head><body>");
                
                out.println("<h1>Payroll Report</h1>");
                out.println("<table>");
                out.println("<thead><tr>");
                
                // Headers
                for (int i = 0; i < payrollTable.getColumnCount(); i++) {
                    out.printf("<th>%s</th>%n", payrollTable.getColumnName(i));
                }
                out.println("</tr></thead><tbody>");
                
                // Data
                for (int i = 0; i < payrollTable.getRowCount(); i++) {
                    out.println("<tr>");
                    for (int j = 0; j < payrollTable.getColumnCount(); j++) {
                        out.printf("<td>%s</td>%n", payrollTable.getValueAt(i, j));
                    }
                    out.println("</tr>");
                }
                
                out.println("</tbody></table>");
                out.printf("<div class=\"footer\">Generated on: %s</div>%n", new java.util.Date());
                out.println("</body></html>");
                
                JOptionPane.showMessageDialog(this, 
                    "HTML report saved successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error saving HTML file: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportToText() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save as Text");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
        fileChooser.setSelectedFile(new File("payroll_report.txt"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getParentFile(), file.getName() + ".txt");
            }

            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("PAYROLL REPORT");
                writer.println("==============");
                writer.println();
                writer.println("Generated on: " + new java.util.Date());
                writer.println();
                
                // Calculate column widths
                int[] colWidths = new int[payrollTable.getColumnCount()];
                for (int i = 0; i < payrollTable.getColumnCount(); i++) {
                    colWidths[i] = payrollTable.getColumnName(i).length();
                    for (int j = 0; j < payrollTable.getRowCount(); j++) {
                        String val = payrollTable.getValueAt(j, i).toString();
                        if (val.length() > colWidths[i]) {
                            colWidths[i] = val.length();
                        }
                    }
                    colWidths[i] += 2; // Add padding
                }
                
                // Write headers
                for (int i = 0; i < payrollTable.getColumnCount(); i++) {
                    writer.printf("%-" + colWidths[i] + "s", payrollTable.getColumnName(i));
                }
                writer.println();
                
                // Write separator
                for (int width : colWidths) {
                    writer.print(String.format("%" + width + "s", "").replace(' ', '-'));
                }
                writer.println();
                
                // Write data
                for (int i = 0; i < payrollTable.getRowCount(); i++) {
                    for (int j = 0; j < payrollTable.getColumnCount(); j++) {
                        writer.printf("%-" + colWidths[j] + "s", payrollTable.getValueAt(i, j));
                    }
                    writer.println();
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Text report saved successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error saving text file: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshPayrollTable() {
        tableModel.setRowCount(0);
        List<Employee> employees = getAllEmployees();
        for (Employee emp : employees) {
            Object[] row = {
                emp.getId(),
                emp.getFirstName() + " " + emp.getLastName(),
                emp.getEmployeeType(),
                formatCurrency(emp.getGrossSalary()),
                formatCurrency(emp.getGrossSalary().subtract(emp.getNetSalary())),
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

    private void generatePayroll() {
        refreshPayrollTable();
        JOptionPane.showMessageDialog(this, 
            "Payroll generated successfully!", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this, 
                "Error loading employees: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        return employees;
    }

    private Employee extractEmployeeFromResultSet(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setId(rs.getInt("id"));
        emp.setFirstName(rs.getString("first_name"));
        emp.setLastName(rs.getString("last_name"));
        emp.setEmployeeType(rs.getString("employee_type"));
        emp.setGrossSalary(rs.getBigDecimal("gross_salary"));
        emp.setNetSalary(rs.getBigDecimal("net_salary"));
        return emp;
    }
}