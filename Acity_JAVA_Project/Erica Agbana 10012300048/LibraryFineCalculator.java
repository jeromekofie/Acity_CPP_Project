import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LibraryFineCalculator {

    // JDBC Setup
    static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
    static final String USER = "root";
    static final String PASS = "Kekeli.10";
    Connection conn;

    // GUI Components
    JFrame frame;
    JTable table;
    DefaultTableModel model;
    JTextField nameField, bookField, dueDateField, returnDateField;

    public static void main(String[] args) {
        LibraryFineCalculator app = new LibraryFineCalculator();
        app.applyModernUI();
        app.setupDatabase();
        app.showLogin();
    }

    // UI Styling
    void applyModernUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("control", new Color(230, 215, 255));
            UIManager.put("info", new Color(180, 140, 255));
            UIManager.put("nimbusBase", new Color(150, 100, 200));
            UIManager.put("nimbusBlueGrey", new Color(170, 140, 200));
            UIManager.put("nimbusLightBackground", new Color(230, 220, 255));
            UIManager.put("text", Color.BLACK);
            UIManager.put("Table.background", new Color(250, 240, 255));
            UIManager.put("Table.alternateRowColor", new Color(240, 230, 255));
            UIManager.put("Table.selectionBackground", new Color(200, 150, 255));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setupDatabase() {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS library_fine_db");
            stmt.execute("USE library_fine_db");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS records (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100)," +
                    "book VARCHAR(100)," +
                    "due_date DATE," +
                    "return_date DATE)");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "username VARCHAR(50) PRIMARY KEY," +
                    "password VARCHAR(50))");
            stmt.executeUpdate("INSERT IGNORE INTO users (username, password) VALUES ('admin', 'admin')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void showLogin() {
        JFrame loginFrame = new JFrame("Library Fine - Login");
        loginFrame.setSize(300, 200);
        loginFrame.setLayout(new GridLayout(4, 1, 10, 10));
        loginFrame.getContentPane().setBackground(new Color(230, 215, 255));
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        JButton exitBtn = new JButton("Exit");

        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
                ps.setString(1, user);
                ps.setString(2, pass);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    loginFrame.dispose();
                    showMainFrame();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid credentials.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        exitBtn.addActionListener(e -> System.exit(0));

        loginFrame.add(new JLabel("Username:"));
        loginFrame.add(userField);
        loginFrame.add(new JLabel("Password:"));
        loginFrame.add(passField);
        loginFrame.add(loginBtn);
        loginFrame.add(exitBtn);

        loginFrame.setVisible(true);
    }

    void showMainFrame() {
        frame = new JFrame("Library Fine Calculator");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(240, 230, 255));
        frame.setLayout(new BorderLayout());

        // Table
        model = new DefaultTableModel(new String[]{"ID", "Name", "Book", "Due Date", "Return Date", "Fine"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        nameField = new JTextField();
        bookField = new JTextField();
        dueDateField = new JTextField("yyyy-mm-dd");
        returnDateField = new JTextField("yyyy-mm-dd");

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Book:"));
        inputPanel.add(bookField);
        inputPanel.add(new JLabel("Due Date:"));
        inputPanel.add(dueDateField);
        inputPanel.add(new JLabel("Return Date:"));
        inputPanel.add(returnDateField);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        JButton addButton = new JButton("Add Record");
        JButton updateButton = new JButton("Update Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton refreshButton = new JButton("Refresh");
        JButton exitButton = new JButton("Exit");

        for (JButton btn : new JButton[]{addButton, updateButton, deleteButton, refreshButton, exitButton}) {
            btn.setBackground(new Color(190, 160, 255));
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setBorder(BorderFactory.createLineBorder(new Color(160, 120, 200), 2, true));
            buttonPanel.add(btn);
        }

        // Button Actions
        addButton.addActionListener(e -> addRecord());
        updateButton.addActionListener(e -> updateRecord());
        deleteButton.addActionListener(e -> deleteRecord());
        refreshButton.addActionListener(e -> loadRecords());
        exitButton.addActionListener(e -> System.exit(0));

        // Add panels
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        loadRecords();
    }

    void addRecord() {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO records (name, book, due_date, return_date) VALUES (?, ?, ?, ?)");
            ps.setString(1, nameField.getText());
            ps.setString(2, bookField.getText());
            ps.setDate(3, java.sql.Date.valueOf(dueDateField.getText()));
            ps.setDate(4, java.sql.Date.valueOf(returnDateField.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Record added!");
            loadRecords();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void updateRecord() {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            int id = (int) model.getValueAt(selected, 0);
            try {
                PreparedStatement ps = conn.prepareStatement("UPDATE records SET name=?, book=?, due_date=?, return_date=? WHERE id=?");
                ps.setString(1, nameField.getText());
                ps.setString(2, bookField.getText());
                ps.setDate(3, java.sql.Date.valueOf(dueDateField.getText()));
                ps.setDate(4, java.sql.Date.valueOf(returnDateField.getText()));
                ps.setInt(5, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Record updated!");
                loadRecords();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    void deleteRecord() {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            int id = (int) model.getValueAt(selected, 0);
            try {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM records WHERE id=?");
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Record deleted!");
                loadRecords();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    void loadRecords() {
        model.setRowCount(0);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM records");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String book = rs.getString("book");
                java.util.Date due = rs.getDate("due_date");
                java.util.Date ret = rs.getDate("return_date");
                long diff = (ret.getTime() - due.getTime()) / (1000 * 60 * 60 * 24);
                double fine = diff > 0 ? diff * 1.0 : 0.0;
                model.addRow(new Object[]{id, name, book, due.toString(), ret.toString(), "GHS " + fine});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
