import java.awt.*;
import java.io.*;
import java.sql.*;
import java.time.*;
import java.util.Properties;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EmployeeAttendanceTrackerGUI {
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/attendancetracker";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "K0246626740@n";
    
    private final Vector<AttendanceRecord> attendanceRecords = new Vector<>();
    private Connection conn;
    
    // GUI Components
    private JFrame mainFrame;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JTextField empIdField, dateField, statusField, checkInField, checkOutField;
    private JButton addButton, updateButton, deleteButton, refreshButton, exportButton, importButton;
    private Image backgroundImage;

    static {
        // Load the JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load MySQL JDBC driver");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "MySQL JDBC Driver not found. Please ensure the driver is in your classpath.", 
                "Driver Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new EmployeeAttendanceTrackerGUI().initialize();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, 
                    "Database connection error: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void initialize() throws SQLException {
        // Establish database connection with proper properties
        Properties connectionProps = new Properties();
        connectionProps.put("user", DB_USER);
        connectionProps.put("password", DB_PASSWORD);
        connectionProps.put("useSSL", "false");
        connectionProps.put("autoReconnect", "true");
        connectionProps.put("serverTimezone", "UTC");
        
        conn = DriverManager.getConnection(DB_URL, connectionProps);
        System.out.println("Database connection established successfully");
        
        loadAttendanceData();
        createGUI();
    }

    private void loadAttendanceData() throws SQLException {
        attendanceRecords.clear();
        String sql = "SELECT a.*, p.EMPName FROM attendance a JOIN person p ON a.person_id = p.EMPID";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                AttendanceRecord record = new AttendanceRecord(
                    rs.getString("person_id"),
                    rs.getString("EMPName"),
                    rs.getDate("date").toLocalDate(),
                    rs.getString("status"),
                    rs.getTime("check_in") != null ? rs.getTime("check_in").toLocalTime() : null,
                    rs.getTime("check_out") != null ? rs.getTime("check_out").toLocalTime() : null,
                    rs.getDouble("hours_worked")
                );
                attendanceRecords.add(record);
            }
        }
    }

    private void createGUI() {
        mainFrame = new JFrame("Employee Attendance Tracker");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 650);
        mainFrame.setLayout(new BorderLayout());

        // Table setup
        String[] columnNames = {"Employee ID", "Name", "Date", "Status", "Check-In", "Check-Out", "Hours Worked"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        attendanceTable = new JTable(tableModel);
        attendanceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateTable();
        
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        mainFrame.add(scrollPane, BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Employee ID:"));
        empIdField = new JTextField();
        formPanel.add(empIdField);

        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        formPanel.add(dateField);

        formPanel.add(new JLabel("Status (present/absent/late/on_leave):"));
        statusField = new JTextField();
        formPanel.add(statusField);

        formPanel.add(new JLabel("Check-In (HH:MM):"));
        checkInField = new JTextField();
        formPanel.add(checkInField);

        formPanel.add(new JLabel("Check-Out (HH:MM):"));
        checkOutField = new JTextField();
        formPanel.add(checkOutField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Add Record");
        updateButton = new JButton("Update Record");
        deleteButton = new JButton("Delete Record");
        refreshButton = new JButton("Refresh");
        exportButton = new JButton("Export to CSV");
        importButton = new JButton("Import from CSV");

        // Set button colors
        addButton.setBackground(new Color(76, 175, 80));
        addButton.setForeground(Color.WHITE);
        updateButton.setBackground(new Color(33, 150, 243));
        updateButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(244, 67, 54));
        deleteButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(255, 152, 0));
        refreshButton.setForeground(Color.WHITE);
        exportButton.setBackground(new Color(156, 39, 176));
        exportButton.setForeground(Color.WHITE);
        importButton.setBackground(new Color(0, 150, 136));
        importButton.setForeground(Color.WHITE);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(importButton);

        // Add action listeners
        addButton.addActionListener(e -> addRecord());
        updateButton.addActionListener(e -> updateRecord());
        deleteButton.addActionListener(e -> deleteRecord());
        refreshButton.addActionListener(e -> refreshData());
        exportButton.addActionListener(e -> exportToCSV());
        importButton.addActionListener(e -> importFromCSV());

        // Add selection listener to table
        attendanceTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = attendanceTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateFormFields(selectedRow);
                }
            }
        });

        mainFrame.add(formPanel, BorderLayout.NORTH);
        mainFrame.add(buttonPanel, BorderLayout.SOUTH);
        mainFrame.setVisible(true);
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (AttendanceRecord record : attendanceRecords) {
            Object[] rowData = {
                record.getEmployeeId(),
                record.getEmployeeName(),
                record.getDate(),
                record.getStatus(),
                record.getCheckIn() != null ? record.getCheckIn() : "N/A",
                record.getCheckOut() != null ? record.getCheckOut() : "N/A",
                record.getHoursWorked()
            };
            tableModel.addRow(rowData);
        }
    }

    private void populateFormFields(int rowIndex) {
        AttendanceRecord record = attendanceRecords.get(rowIndex);
        empIdField.setText(record.getEmployeeId());
        dateField.setText(record.getDate().toString());
        statusField.setText(record.getStatus());
        checkInField.setText(record.getCheckIn() != null ? record.getCheckIn().toString() : "");
        checkOutField.setText(record.getCheckOut() != null ? record.getCheckOut().toString() : "");
    }

    private void addRecord() {
        try {
            String empId = empIdField.getText().trim();
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            String status = statusField.getText().trim();
            LocalTime checkIn = checkInField.getText().isEmpty() ? null : LocalTime.parse(checkInField.getText().trim());
            LocalTime checkOut = checkOutField.getText().isEmpty() ? null : LocalTime.parse(checkOutField.getText().trim());
            
            // Validate required fields
            if (empId.isEmpty() || status.isEmpty()) {
                throw new IllegalArgumentException("Employee ID and Status are required fields");
            }
            
            double hoursWorked = 0.0;
            if (checkIn != null && checkOut != null) {
                hoursWorked = Duration.between(checkIn, checkOut).toMinutes() / 60.0;
                if (hoursWorked < 0) {
                    throw new IllegalArgumentException("Check-out time must be after check-in time");
                }
            }

            // Add to database
            String sql = "INSERT INTO attendance (person_id, date, status, check_in, check_out, hours_worked) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, empId);
                pstmt.setDate(2, Date.valueOf(date));
                pstmt.setString(3, status);
                pstmt.setTime(4, checkIn != null ? Time.valueOf(checkIn) : null);
                pstmt.setTime(5, checkOut != null ? Time.valueOf(checkOut) : null);
                pstmt.setDouble(6, hoursWorked);
                pstmt.executeUpdate();
            }

            // Add to vector
            String empName = getEmployeeName(empId);
            AttendanceRecord newRecord = new AttendanceRecord(empId, empName, date, status, checkIn, checkOut, hoursWorked);
            attendanceRecords.add(newRecord);
            updateTable();
            clearFormFields();
            JOptionPane.showMessageDialog(mainFrame, "Record added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, "Error adding record: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRecord() {
        int selectedRow = attendanceTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a record to update", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            AttendanceRecord oldRecord = attendanceRecords.get(selectedRow);
            
            String empId = empIdField.getText().trim();
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            String status = statusField.getText().trim();
            LocalTime checkIn = checkInField.getText().isEmpty() ? null : LocalTime.parse(checkInField.getText().trim());
            LocalTime checkOut = checkOutField.getText().isEmpty() ? null : LocalTime.parse(checkOutField.getText().trim());
            
            // Validate required fields
            if (empId.isEmpty() || status.isEmpty()) {
                throw new IllegalArgumentException("Employee ID and Status are required fields");
            }
            
            double hoursWorked = 0.0;
            if (checkIn != null && checkOut != null) {
                hoursWorked = Duration.between(checkIn, checkOut).toMinutes() / 60.0;
                if (hoursWorked < 0) {
                    throw new IllegalArgumentException("Check-out time must be after check-in time");
                }
            }

            // Update database
            String sql = "UPDATE attendance SET person_id=?, date=?, status=?, check_in=?, check_out=?, hours_worked=? " +
                         "WHERE person_id=? AND date=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, empId);
                pstmt.setDate(2, Date.valueOf(date));
                pstmt.setString(3, status);
                pstmt.setTime(4, checkIn != null ? Time.valueOf(checkIn) : null);
                pstmt.setTime(5, checkOut != null ? Time.valueOf(checkOut) : null);
                pstmt.setDouble(6, hoursWorked);
                pstmt.setString(7, oldRecord.getEmployeeId());
                pstmt.setDate(8, Date.valueOf(oldRecord.getDate()));
                pstmt.executeUpdate();
            }

            // Update vector
            String empName = getEmployeeName(empId);
            AttendanceRecord updatedRecord = new AttendanceRecord(empId, empName, date, status, checkIn, checkOut, hoursWorked);
            attendanceRecords.set(selectedRow, updatedRecord);
            updateTable();
            JOptionPane.showMessageDialog(mainFrame, "Record updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, "Error updating record: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        int selectedRow = attendanceTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a record to delete", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(mainFrame, 
            "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            AttendanceRecord record = attendanceRecords.get(selectedRow);
            
            // Delete from database
            String sql = "DELETE FROM attendance WHERE person_id=? AND date=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, record.getEmployeeId());
                pstmt.setDate(2, Date.valueOf(record.getDate()));
                pstmt.executeUpdate();
            }

            // Remove from vector
            attendanceRecords.remove(selectedRow);
            updateTable();
            clearFormFields();
            JOptionPane.showMessageDialog(mainFrame, "Record deleted successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error deleting record: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshData() {
        try {
            loadAttendanceData();
            updateTable();
            clearFormFields();
            JOptionPane.showMessageDialog(mainFrame, "Data refreshed successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error refreshing data: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Attendance Data as CSV");
        fileChooser.setSelectedFile(new File("attendance_data.csv"));
        
        if (fileChooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }

            try (PrintWriter writer = new PrintWriter(file)) {
                // Write header
                writer.println("EmployeeID,EmployeeName,Date,Status,CheckIn,CheckOut,HoursWorked");
                
                // Write data
                for (AttendanceRecord record : attendanceRecords) {
                    writer.printf("%s,%s,%s,%s,%s,%s,%.2f%n",
                        record.getEmployeeId(),
                        record.getEmployeeName(),
                        record.getDate(),
                        record.getStatus(),
                        record.getCheckIn() != null ? record.getCheckIn() : "",
                        record.getCheckOut() != null ? record.getCheckOut() : "",
                        record.getHoursWorked());
                }
                
                JOptionPane.showMessageDialog(mainFrame, 
                    "Data exported to CSV successfully!\nLocation: " + file.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(mainFrame, "Error exporting to CSV: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importFromCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select CSV File to Import");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        
        if (fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            int confirm = JOptionPane.showConfirmDialog(mainFrame, 
                "Importing will overwrite current data. Continue?", "Warning", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                // Clear existing data
                String deleteSql = "DELETE FROM attendance";
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(deleteSql);
                }
                attendanceRecords.clear();

                // Skip header
                String header = reader.readLine();
                if (header == null || !header.startsWith("EmployeeID")) {
                    throw new IOException("Invalid CSV format. Expected header: EmployeeID,EmployeeName,...");
                }
                
                String line;
                int importedCount = 0;
                while ((line = reader.readLine()) != null) {
                    try {
                        String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                        if (parts.length >= 7) {
                            String empId = parts[0].replace("\"", "");
                            String empName = parts[1].replace("\"", "");
                            LocalDate date = LocalDate.parse(parts[2].replace("\"", ""));
                            String status = parts[3].replace("\"", "");
                            LocalTime checkIn = parts[4].isEmpty() ? null : LocalTime.parse(parts[4].replace("\"", ""));
                            LocalTime checkOut = parts[5].isEmpty() ? null : LocalTime.parse(parts[5].replace("\"", ""));
                            double hoursWorked = Double.parseDouble(parts[6].replace("\"", ""));

                            // Insert into database
                            String sql = "INSERT INTO attendance (person_id, date, status, check_in, check_out, hours_worked) " +
                                         "VALUES (?, ?, ?, ?, ?, ?)";
                            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                                pstmt.setString(1, empId);
                                pstmt.setDate(2, Date.valueOf(date));
                                pstmt.setString(3, status);
                                pstmt.setTime(4, checkIn != null ? Time.valueOf(checkIn) : null);
                                pstmt.setTime(5, checkOut != null ? Time.valueOf(checkOut) : null);
                                pstmt.setDouble(6, hoursWorked);
                                pstmt.executeUpdate();
                            }

                            // Add to vector
                            attendanceRecords.add(new AttendanceRecord(empId, empName, date, status, checkIn, checkOut, hoursWorked));
                            importedCount++;
                        }
                    } catch (Exception e) {
                        System.err.println("Error processing line: " + line);
                        e.printStackTrace();
                    }
                }
                
                updateTable();
                JOptionPane.showMessageDialog(mainFrame, 
                    "Successfully imported " + importedCount + " records from CSV!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Error importing from CSV: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getEmployeeName(String empId) throws SQLException {
        String sql = "SELECT EMPName FROM person WHERE EMPID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, empId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getString("EMPName") : "Unknown";
        }
    }

    private void clearFormFields() {
        empIdField.setText("");
        dateField.setText("");
        statusField.setText("");
        checkInField.setText("");
        checkOutField.setText("");
    }

    // Inner class for attendance records
    private static class AttendanceRecord {
        private final String employeeId;
        private final String employeeName;
        private final LocalDate date;
        private final String status;
        private final LocalTime checkIn;
        private final LocalTime checkOut;
        private final double hoursWorked;

        public AttendanceRecord(String employeeId, String employeeName, LocalDate date, 
                               String status, LocalTime checkIn, LocalTime checkOut, double hoursWorked) {
            this.employeeId = employeeId;
            this.employeeName = employeeName;
            this.date = date;
            this.status = status;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.hoursWorked = hoursWorked;
        }

        // Getters
        public String getEmployeeId() { return employeeId; }
        public String getEmployeeName() { return employeeName; }
        public LocalDate getDate() { return date; }
        public String getStatus() { return status; }
        public LocalTime getCheckIn() { return checkIn; }
        public LocalTime getCheckOut() { return checkOut; }
        public double getHoursWorked() { return hoursWorked; }
    }
}