import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class VehicleRentalGUI extends JFrame {
    private JTextField tfMake, tfModel, tfColor, tfDays;
    private JComboBox<String> cbType;
    private JTable table;
    private DefaultTableModel model;
    private String currentUserRole = "";
    private JButton saveBtn, viewBtn, updateBtn, deleteBtn, statsBtn, purchaseBtn, historyBtn;
    private int selectedVehicleId = -1;

    private final String url = "jdbc:mariadb://localhost:3306/vehiclerental";
    private final String dbUser = "root";
    private final String dbPass = "Mariadb1";

    // Instance variables for the slideshow
    private JLabel slideshowLabel; // Label to display slideshow images
    private Timer slideshowTimer; // Timer to cycle through images
    private String[] imagePaths = { // Paths to slideshow images
        "src/images.jpg",
        "src/images (2).jpg",
        "src/images (1).jpg",
        "src/download.jpg",
        "src/download (3).jpg",
        "src/download (2).jpg"
    };
    private int currentImageIndex = 0; // Index of the current image

    public VehicleRentalGUI(String username) {
        this.currentUserRole = getUserRole(username); // Fetch the role for the logged-in user
        initComponents();
    }

    private String getUserRole(String username) {
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            String sql = "SELECT role FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching user role: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return "guest"; // Default role if not found
    }

    // Method to initialize the slideshow
    private void initSlideshow() {
        // Create a JLabel to display images
        slideshowLabel = new JLabel();
        slideshowLabel.setHorizontalAlignment(JLabel.CENTER);
        slideshowLabel.setVerticalAlignment(JLabel.CENTER);
        slideshowLabel.setPreferredSize(new Dimension(800, 300));
        slideshowLabel.setBackground(Color.BLACK);
        slideshowLabel.setOpaque(true);

        // Load the first image
        updateSlideshowImage();

        // Create a Timer to cycle through images every 3 seconds
        slideshowTimer = new Timer(3000, e -> {
            currentImageIndex = (currentImageIndex + 1) % imagePaths.length; // Move to the next image
            updateSlideshowImage();
        });
        slideshowTimer.start(); // Start the slideshow
    }

    // Method to update the image in the slideshow
    private void updateSlideshowImage() {
        try {
            ImageIcon icon = new ImageIcon(imagePaths[currentImageIndex]);
            Image scaledImage = icon.getImage().getScaledInstance(slideshowLabel.getWidth(), slideshowLabel.getHeight(), Image.SCALE_SMOOTH);
            slideshowLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            slideshowLabel.setText("Image not found: " + imagePaths[currentImageIndex]);
            slideshowLabel.setForeground(Color.RED);
        }
    }

    private void initComponents() {
        setTitle("Vehicle Rental System | Role: " + currentUserRole.toUpperCase());
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(30, 30, 30));

        // Initialize the slideshow
        initSlideshow();

        // Add the slideshow panel to the top of the GUI
        add(slideshowLabel, BorderLayout.NORTH);

        // Input panel for vehicle details
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(45, 45, 45));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        tfMake = new JTextField(10);
        tfModel = new JTextField(10);
        tfColor = new JTextField(10);
        tfDays = new JTextField(5);
        cbType = new JComboBox<>(new String[]{"Car", "Truck", "Motorbike"});

        String[] labelNames = {"Make:", "Model:", "Color:", "Type:", "Days:"};
        JLabel[] labels = new JLabel[labelNames.length];
        for (int i = 0; i < labelNames.length; i++) {
            labels[i] = new JLabel(labelNames[i]);
            labels[i].setForeground(Color.WHITE);
        }

        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(labels[0], gbc);
        gbc.gridx = 1; inputPanel.add(tfMake, gbc);
        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(labels[1], gbc);
        gbc.gridx = 1; inputPanel.add(tfModel, gbc);
        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(labels[2], gbc);
        gbc.gridx = 1; inputPanel.add(tfColor, gbc);
        gbc.gridx = 0; gbc.gridy = 3; inputPanel.add(labels[3], gbc);
        gbc.gridx = 1; inputPanel.add(cbType, gbc);
        gbc.gridx = 0; gbc.gridy = 4; inputPanel.add(labels[4], gbc);
        gbc.gridx = 1; inputPanel.add(tfDays, gbc);

        saveBtn = new JButton("Save");
        viewBtn = new JButton("Refresh");
        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");
        statsBtn = new JButton("Dashboard");
        purchaseBtn = new JButton("Purchase");
        historyBtn = new JButton("Purchase History");

        JButton[] buttons = {saveBtn, viewBtn, updateBtn, deleteBtn, statsBtn, purchaseBtn, historyBtn};
        Font btnFont = new Font("SansSerif", Font.BOLD, 14);
        Color btnColor = new Color(50, 50, 50);
        int row = 5;
        for (JButton btn : buttons) {
            btn.setBackground(btnColor);
            btn.setForeground(Color.WHITE);
            btn.setFont(btnFont);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
            inputPanel.add(btn, gbc);
        }

        model = new DefaultTableModel(new Object[]{"ID", "Make", "Model", "Color", "Type"}, 0);
        table = new JTable(model);
        table.setBackground(new Color(20, 20, 20));
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.DARK_GRAY);
        JScrollPane scrollPane = new JScrollPane(table);

        saveBtn.addActionListener(e -> saveVehicle());
        viewBtn.addActionListener(e -> loadVehicles());
        updateBtn.addActionListener(e -> updateVehicle());
        deleteBtn.addActionListener(e -> deleteVehicle());
        statsBtn.addActionListener(e -> showDashboard());
        purchaseBtn.addActionListener(e -> purchaseVehicle());
        historyBtn.addActionListener(e -> showPurchaseHistory());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                selectedVehicleId = (int) model.getValueAt(table.getSelectedRow(), 0);
                tfMake.setText(model.getValueAt(table.getSelectedRow(), 1).toString());
                tfModel.setText(model.getValueAt(table.getSelectedRow(), 2).toString());
                tfColor.setText(model.getValueAt(table.getSelectedRow(), 3).toString());
                cbType.setSelectedItem(model.getValueAt(table.getSelectedRow(), 4));
            }
        });

        setRoleAccess();

        JScrollPane inputScroll = new JScrollPane(inputPanel);
        inputScroll.setPreferredSize(new Dimension(300, 500));
        inputScroll.setBorder(null);
        inputScroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.DARK_GRAY);
        leftPanel.add(inputScroll, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        loadVehicles();
        setVisible(true);
    }

    private void saveVehicle() {
        String make = tfMake.getText();
        String model = tfModel.getText();
        String color = tfColor.getText();
        String type = cbType.getSelectedItem().toString();
        String days = tfDays.getText();

        if (make.isEmpty() || model.isEmpty() || color.isEmpty() || type.isEmpty() || days.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            String sql = "INSERT INTO vehicles (make, model, color, type, days) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, make);
            stmt.setString(2, model);
            stmt.setString(3, color);
            stmt.setString(4, type);
            stmt.setInt(5, Integer.parseInt(days));

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Vehicle saved successfully!");
                loadVehicles();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving vehicle: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadVehicles() {
        model.setRowCount(0); // Clear the table
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            String sql = "SELECT * FROM vehicles";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[5]; // Initialize the row array
                row[0] = rs.getInt("id");
                row[1] = rs.getString("make");
                row[2] = rs.getString("model");
                row[3] = rs.getString("color");
                row[4] = rs.getString("type");
                model.addRow(row); // Add the row to the table model
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading vehicles: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateVehicle() {
        if (selectedVehicleId == -1) {
            JOptionPane.showMessageDialog(this, "No vehicle selected for update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String make = tfMake.getText();
        String model = tfModel.getText();
        String color = tfColor.getText();
        String type = cbType.getSelectedItem().toString();
        String days = tfDays.getText();

        if (make.isEmpty() || model.isEmpty() || color.isEmpty() || type.isEmpty() || days.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            String sql = "UPDATE vehicles SET make = ?, model = ?, color = ?, type = ?, days = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, make);
            stmt.setString(2, model);
            stmt.setString(3, color);
            stmt.setString(4, type);
            stmt.setInt(5, Integer.parseInt(days));
            stmt.setInt(6, selectedVehicleId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Vehicle updated successfully!");
                loadVehicles();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating vehicle: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteVehicle() {
        if (selectedVehicleId == -1) {
            JOptionPane.showMessageDialog(this, "No vehicle selected for deletion.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            String sql = "DELETE FROM vehicles WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, selectedVehicleId);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Vehicle deleted successfully!");
                loadVehicles();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting vehicle: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showDashboard() {
        if (!"admin".equalsIgnoreCase(currentUserRole)) {
            JOptionPane.showMessageDialog(this, "Access denied. Only admins can view the dashboard.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder dashboard = new StringBuilder("===== Statistics Dashboard =====\n");

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            // Total number of vehicles
            String vehicleCountSql = "SELECT COUNT(*) AS total_vehicles FROM vehicles";
            PreparedStatement vehicleStmt = conn.prepareStatement(vehicleCountSql);
            ResultSet vehicleRs = vehicleStmt.executeQuery();
            if (vehicleRs.next()) {
                dashboard.append("Total Vehicles: ").append(vehicleRs.getInt("total_vehicles")).append("\n");
            }

            // Total number of purchases
            String purchaseCountSql = "SELECT COUNT(*) AS total_purchases FROM purchase_history";
            PreparedStatement purchaseStmt = conn.prepareStatement(purchaseCountSql);
            ResultSet purchaseRs = purchaseStmt.executeQuery();
            if (purchaseRs.next()) {
                dashboard.append("Total Purchases: ").append(purchaseRs.getInt("total_purchases")).append("\n");
            }

            // Total revenue
            String revenueSql = "SELECT SUM(total_cost) AS total_revenue FROM purchase_history";
            PreparedStatement revenueStmt = conn.prepareStatement(revenueSql);
            ResultSet revenueRs = revenueStmt.executeQuery();
            if (revenueRs.next()) {
                dashboard.append("Total Revenue: $").append(revenueRs.getDouble("total_revenue")).append("\n");
            }

            dashboard.append("==================================");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading dashboard: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, dashboard.toString(), "Statistics Dashboard", JOptionPane.INFORMATION_MESSAGE);
    }

    private void purchaseVehicle() {
        if (selectedVehicleId == -1) {
            JOptionPane.showMessageDialog(this, "No vehicle selected for purchase.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String username = JOptionPane.showInputDialog(this, "Enter your username for the receipt:");
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username is required for the receipt.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String daysInput = JOptionPane.showInputDialog(this, "Enter the number of days for the rental:");
        if (daysInput == null || daysInput.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Number of days is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int days;
        try {
            days = Integer.parseInt(daysInput);
            if (days <= 0) {
                JOptionPane.showMessageDialog(this, "Number of days must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number of days. Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            // Fetch vehicle details
            String sql = "SELECT make, model, color, type FROM vehicles WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, selectedVehicleId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String vehicleDetails = rs.getString("make") + " " + rs.getString("model") + " (" + rs.getString("color") + ", " + rs.getString("type") + ")";
                double costPerDay = 50.0; // Example cost per day
                double totalCost = days * costPerDay;

                // Generate receipt
                generateReceipt(username, vehicleDetails, days, totalCost);

                // Save purchase to history
                String insertSql = "INSERT INTO purchase_history (username, vehicle_id, total_cost) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, username);
                insertStmt.setInt(2, selectedVehicleId);
                insertStmt.setDouble(3, totalCost);
                insertStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Purchase completed successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Vehicle not found in the database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error processing purchase: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to generate a receipt for a purchase
    private void generateReceipt(String username, String vehicleDetails, int days, double totalCost) {
        String receipt = "===== Vehicle Rental Receipt =====\n" +
                         "Customer: " + username + "\n" +
                         "Vehicle: " + vehicleDetails + "\n" +
                         "Days: " + days + "\n" +
                         "Total Cost: $" + totalCost + "\n" +
                         "==================================";
        JOptionPane.showMessageDialog(this, receipt, "Receipt", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showPurchaseHistory() {
        StringBuilder history = new StringBuilder("===== Purchase History =====\n");

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            String sql = "SELECT ph.username, v.make, v.model, v.color, v.type, ph.total_cost " +
                         "FROM purchase_history ph " +
                         "JOIN vehicles v ON ph.vehicle_id = v.id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                history.append("Customer: ").append(rs.getString("username")).append("\n")
                       .append("Vehicle: ").append(rs.getString("make")).append(" ").append(rs.getString("model"))
                       .append(" (").append(rs.getString("color")).append(", ").append(rs.getString("type")).append(")\n")
                       .append("Total Cost: $").append(rs.getDouble("total_cost")).append("\n")
                       .append("----------------------------------\n");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading purchase history: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, history.toString(), "Purchase History", JOptionPane.INFORMATION_MESSAGE);
    }

    private void setRoleAccess() {
        saveBtn.setEnabled("admin".equalsIgnoreCase(currentUserRole));
        updateBtn.setEnabled("admin".equalsIgnoreCase(currentUserRole) || "staff".equalsIgnoreCase(currentUserRole));
        deleteBtn.setEnabled("admin".equalsIgnoreCase(currentUserRole));
        purchaseBtn.setEnabled(true);
        historyBtn.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}

class LoginFrame extends JFrame {
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private final String url = "jdbc:mariadb://localhost:3306/vehiclerental";
    private final String dbUser = "root";
    private final String dbPass = "Mariadb1";

    public LoginFrame() {
        setTitle("Login");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblUsername = new JLabel("Username:");
        tfUsername = new JTextField(15);
        JLabel lblPassword = new JLabel("Password:");
        pfPassword = new JPasswordField(15);
        JButton btnLogin = new JButton("Login");

        gbc.gridx = 0; gbc.gridy = 0; add(lblUsername, gbc);
        gbc.gridx = 1; add(tfUsername, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(lblPassword, gbc);
        gbc.gridx = 1; add(pfPassword, gbc);
        gbc.gridx = 1; gbc.gridy = 2; add(btnLogin, gbc);

        btnLogin.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {
        String username = tfUsername.getText();
        String password = new String(pfPassword.getPassword());

        if (authenticateUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the login frame
            SwingUtilities.invokeLater(() -> new VehicleRentalGUI(username)); // Launch the main GUI
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Login error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
