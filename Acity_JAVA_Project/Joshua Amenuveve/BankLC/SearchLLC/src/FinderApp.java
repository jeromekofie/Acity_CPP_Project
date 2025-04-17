import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class FinderApp extends JFrame {
    private Connection conn;
    private JTable table;
    private DefaultTableModel model;
    private JTextField nameField, ageField, programField, emailField;
    private JButton searchBtn, addBtn, updateBtn, deleteBtn;

    public FinderApp() {
        setTitle("Student Finder");
        setSize(850, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        connectToDatabase();
        setupUI();
        loadTable();
    }

    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentsdb", "root", "");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }
    }

    private void setupUI() {
        // Create main panels
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(new Color(245, 245, 245)); // Light gray background

        nameField = new JTextField();
        ageField = new JTextField();
        programField = new JTextField();
        emailField = new JTextField();
        
        // Add labels and input fields
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Age:"));
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("Program:"));
        inputPanel.add(programField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(34, 45, 50)); // Dark background for the button panel

        addBtn = new JButton("Add");
        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");
        searchBtn = new JButton("Search");

        // Style buttons with custom background and hover effect
        addBtn.setBackground(new Color(46, 139, 87)); // Green color
        updateBtn.setBackground(new Color(255, 165, 0)); // Orange color
        deleteBtn.setBackground(new Color(255, 69, 0)); // Red color
        searchBtn.setBackground(new Color(70, 130, 180)); // Steel blue color

        // Set button text color
        addBtn.setForeground(Color.WHITE);
        updateBtn.setForeground(Color.WHITE);
        deleteBtn.setForeground(Color.WHITE);
        searchBtn.setForeground(Color.WHITE);

        // Add buttons to buttonPanel
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(searchBtn);

        // Table setup
        model = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Program", "Email"}, 0);
        table = new JTable(model);
        JScrollPane tablePane = new JScrollPane(table);
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(173, 216, 230)); // Light blue selection color
        table.setSelectionForeground(Color.BLACK);
        
        // Add components to main layout
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tablePane, BorderLayout.SOUTH);

        // Action listeners for buttons
        addBtn.addActionListener(e -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        searchBtn.addActionListener(e -> searchStudents());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                nameField.setText(model.getValueAt(row, 1).toString());
                ageField.setText(model.getValueAt(row, 2).toString());
                programField.setText(model.getValueAt(row, 3).toString());
                emailField.setText(model.getValueAt(row, 4).toString());
            }
        });
    }

    private void loadTable() {
        model.setRowCount(0);
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("program"),
                        rs.getString("email")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addStudent() {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO students (name, age, program, email) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, nameField.getText());
            ps.setInt(2, Integer.parseInt(ageField.getText()));
            ps.setString(3, programField.getText());
            ps.setString(4, emailField.getText());
            ps.executeUpdate();
            loadTable();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Add Error: " + e.getMessage());
        }
    }

    private void updateStudent() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        try (PreparedStatement ps = conn.prepareStatement("UPDATE students SET name=?, age=?, program=?, email=? WHERE id=?")) {
            ps.setString(1, nameField.getText());
            ps.setInt(2, Integer.parseInt(ageField.getText()));
            ps.setString(3, programField.getText());
            ps.setString(4, emailField.getText());
            ps.setInt(5, (int) model.getValueAt(row, 0));
            ps.executeUpdate();
            loadTable();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Update Error: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM students WHERE id=?")) {
            ps.setInt(1, (int) model.getValueAt(row, 0));
            ps.executeUpdate();
            loadTable();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Delete Error: " + e.getMessage());
        }
    }

    private void searchStudents() {
        model.setRowCount(0);
        String keyword = nameField.getText();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM students WHERE name LIKE ? OR program LIKE ?")) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("program"),
                        rs.getString("email")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Search Error: " + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        programField.setText("");
        emailField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FinderApp().setVisible(true));
    }
}