import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class ZanettaCars {
    private static JFrame frame;
    private static JTextField makeField, modelField, colorField, ratePerHourField;
    private static DefaultTableModel tableModel;
    private static JTable carTable;
    private static int selectedCarIndex = -1;
    private static ArrayList<Car> carList = new ArrayList<>();
    private static final String ADMIN_PASSWORD = "1234";
    private static JComboBox<Car> carDropdown;
    private static JLabel countLabel; 

    // Color Scheme
    private static final Color PRIMARY = new Color(0x0D47A1);
    private static final Color SECONDARY = new Color(0x00ACC1);
    private static final Color ACCENT = new Color(0xFF7043);
    private static final Color BACKGROUND = new Color(0xECEFF1);
    private static final Color CARD = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(0x263238);
    private static final Color TEXT_SECONDARY = new Color(0x607D8B);
    private static final Color ERROR = new Color(0xD32F2F);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ZanettaCars::showMainScreen);
    }

    private static void showMainScreen() {
        frame = new JFrame("Zanetta Cars");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Welcome", getWelcomePanel(tabs));
        tabs.addTab("Client", getClientPanel());
        tabs.addTab("Admin", getAdminPanel());

        frame.add(tabs);
        frame.setVisible(true);

        loadCarsFromDatabase();
    }

    private static JPanel getWelcomePanel(JTabbedPane tabs) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel label = new JLabel("Welcome to Zanetta Cars", SwingConstants.CENTER);
        label.setForeground(TEXT_PRIMARY);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));

        countLabel = new JLabel("Total Cars Available: " + getCarCount(), SwingConstants.CENTER);
        countLabel.setForeground(TEXT_SECONDARY);
        countLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        JButton clientBtn = new JButton("Client Portal");
        JButton adminBtn = new JButton("Admin Login");
        styleButton(clientBtn, PRIMARY);
        styleButton(adminBtn, SECONDARY);

        clientBtn.addActionListener(e -> tabs.setSelectedIndex(1));
        adminBtn.addActionListener(e -> {
            JPasswordField pwdField = new JPasswordField();
            int option = JOptionPane.showConfirmDialog(
                    frame,
                    pwdField,
                    "Enter Admin Password:",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (option == JOptionPane.OK_OPTION) {
                String password = new String(pwdField.getPassword());
                if (ADMIN_PASSWORD.equals(password)) {
                    tabs.setSelectedIndex(2);
                } else {
                    JOptionPane.showMessageDialog(frame, "Incorrect password!", "Access Denied", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel buttons = new JPanel();
        buttons.setBackground(BACKGROUND);
        buttons.add(clientBtn);
        buttons.add(adminBtn);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(label, gbc);

        gbc.gridy = 1;
        panel.add(countLabel, gbc);

        gbc.gridy = 2;
        panel.add(buttons, gbc);

        return panel;
    }

    private static JPanel getClientPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        carDropdown = new JComboBox<>();
        refreshCarDropdown();

        JLabel nameLabel = label("Client Name:");
        JTextField nameField = new JTextField(15);
        JLabel phoneLabel = label("Phone:");
        JTextField phoneField = new JTextField(15);
        JLabel hoursLabel = label("Rental Hours:");
        JTextField hoursField = new JTextField(5);
        JButton rentBtn = new JButton("Rent Car");
        styleButton(rentBtn, PRIMARY);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(label("Select Car:"), gbc);
        gbc.gridx = 1; panel.add(carDropdown, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(nameLabel, gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(phoneLabel, gbc);
        gbc.gridx = 1; panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(hoursLabel, gbc);
        gbc.gridx = 1; panel.add(hoursField, gbc);

        gbc.gridx = 1; gbc.gridy = 4; panel.add(rentBtn, gbc);

        rentBtn.addActionListener(e -> {
            Car selectedCar = (Car) carDropdown.getSelectedItem();
            String name = nameField.getText();
            String phone = phoneField.getText();
            String hoursStr = hoursField.getText();

            if (selectedCar == null || name.isEmpty() || phone.isEmpty() || hoursStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Missing Info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int hours = Integer.parseInt(hoursStr);
                double total = selectedCar.ratePerHour * hours;

                String receipt = "------ Rental Receipt ------\n"
                        + "Client: " + name + "\n"
                        + "Phone: " + phone + "\n"
                        + "Car: " + selectedCar.make + " " + selectedCar.model + " (" + selectedCar.color + ")\n"
                        + "Hours: " + hours + "\n"
                        + "Rate/hour: $" + selectedCar.ratePerHour + "\n"
                        + "Total: $" + total + "\n"
                        + "----------------------------";

                JOptionPane.showMessageDialog(frame, receipt, "Receipt", JOptionPane.INFORMATION_MESSAGE);

                nameField.setText("");
                phoneField.setText("");
                hoursField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Enter a valid number of hours.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private static JPanel getAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);

        tableModel = new DefaultTableModel(new String[]{"Make", "Model", "Color", "Rate/Hour", "Rate/Day"}, 0);
        carTable = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(carTable);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(CARD);

        makeField = new JTextField(); modelField = new JTextField();
        colorField = new JTextField(); ratePerHourField = new JTextField();

        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        styleButton(add, PRIMARY); styleButton(update, SECONDARY); styleButton(delete, ERROR);

        formPanel.add(label("Make:")); formPanel.add(makeField);
        formPanel.add(label("Model:")); formPanel.add(modelField);
        formPanel.add(label("Color:")); formPanel.add(colorField);
        formPanel.add(label("Rate/Hour:")); formPanel.add(ratePerHourField);

        JPanel btnPanel = new JPanel(); btnPanel.setBackground(BACKGROUND);
        btnPanel.add(add); btnPanel.add(update); btnPanel.add(delete);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(btnPanel, BorderLayout.SOUTH);

        add.addActionListener(e -> addCar());
        update.addActionListener(e -> updateCar());
        delete.addActionListener(e -> deleteCar());

        carTable.getSelectionModel().addListSelectionListener(e -> {
            int i = carTable.getSelectedRow();
            if (i >= 0) {
                selectedCarIndex = i;
                makeField.setText((String) tableModel.getValueAt(i, 0));
                modelField.setText((String) tableModel.getValueAt(i, 1));
                colorField.setText((String) tableModel.getValueAt(i, 2));
                ratePerHourField.setText(String.valueOf(tableModel.getValueAt(i, 3)));
            }
        });

        return panel;
    }

    private static void addCar() {
        String make = makeField.getText();
        String model = modelField.getText();
        String color = colorField.getText();
        String rateText = ratePerHourField.getText();

        if (make.isEmpty() || model.isEmpty() || color.isEmpty() || rateText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double rate = Double.parseDouble(rateText);

            try (Connection conn = DbConnection.getConnection()) {
                String sql = "INSERT INTO cars (make, model, color, rate_per_hour) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, make);
                    stmt.setString(2, model);
                    stmt.setString(3, color);
                    stmt.setDouble(4, rate);
                    stmt.executeUpdate();
                }
            }

            loadCarsFromDatabase();
            clearFormFields();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid rate format.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to add car to the database.", "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void updateCar() {
        if (selectedCarIndex < 0) {
            JOptionPane.showMessageDialog(frame, "No car selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String make = makeField.getText();
        String model = modelField.getText();
        String color = colorField.getText();
        String rateText = ratePerHourField.getText();

        if (make.isEmpty() || model.isEmpty() || color.isEmpty() || rateText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double rate = Double.parseDouble(rateText);
            Car carToUpdate = carList.get(selectedCarIndex);

            try (Connection conn = DbConnection.getConnection()) {
                String sql = "UPDATE cars SET make = ?, model = ?, color = ?, rate_per_hour = ? " +
                        "WHERE make = ? AND model = ? AND color = ? AND rate_per_hour = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, make);
                    stmt.setString(2, model);
                    stmt.setString(3, color);
                    stmt.setDouble(4, rate);

                    stmt.setString(5, carToUpdate.make);
                    stmt.setString(6, carToUpdate.model);
                    stmt.setString(7, carToUpdate.color);
                    stmt.setDouble(8, carToUpdate.ratePerHour);

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(frame, "Car updated successfully.");
                        loadCarsFromDatabase();
                        clearFormFields();
                        selectedCarIndex = -1;
                    } else {
                        JOptionPane.showMessageDialog(frame, "Car update failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid rate format.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error during update.", "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void deleteCar() {
        if (selectedCarIndex < 0) {
            JOptionPane.showMessageDialog(frame, "No car selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Car carToDelete = carList.get(selectedCarIndex);

        try (Connection conn = DbConnection.getConnection()) {
            String sql = "DELETE FROM cars WHERE make = ? AND model = ? AND color = ? AND rate_per_hour = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, carToDelete.make);
                stmt.setString(2, carToDelete.model);
                stmt.setString(3, carToDelete.color);
                stmt.setDouble(4, carToDelete.ratePerHour);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "Car deleted successfully.");
                    loadCarsFromDatabase();
                    clearFormFields();
                    selectedCarIndex = -1;
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to delete car.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error during deletion.", "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void loadCarsFromDatabase() {
        carList.clear();
        if (tableModel != null) tableModel.setRowCount(0);
        try (Connection conn = DbConnection.getConnection()) {
            String sql = "SELECT * FROM cars";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String make = rs.getString("make");
                    String model = rs.getString("model");
                    String color = rs.getString("color");
                    double rate = rs.getDouble("rate_per_hour");
                    double ratePerDay = rate * 24;

                    Car car = new Car(make, model, color, rate);
                    carList.add(car);
                    if (tableModel != null) {
                        tableModel.addRow(new Object[]{make, model, color, rate, ratePerDay});
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        refreshCarDropdown();
        if (countLabel != null) {
            countLabel.setText("Total Cars Available: " + getCarCount());
        }
    }

    private static int getCarCount() {
        return carList.size();
    }

    private static void refreshCarDropdown() {
        carDropdown.removeAllItems();
        for (Car car : carList) {
            carDropdown.addItem(car);
        }
    }

    private static void clearFormFields() {
        makeField.setText("");
        modelField.setText("");
        colorField.setText("");
        ratePerHourField.setText("");
    }

    private static JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_PRIMARY);
        return lbl;
    }

    private static void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
    }

    static class Car {
        String make, model, color;
        double ratePerHour;

        public Car(String make, String model, String color, double ratePerHour) {
            this.make = make;
            this.model = model;
            this.color = color;
            this.ratePerHour = ratePerHour;
        }

        @Override
        public String toString() {
            return make + " " + model + " (" + color + ")";
        }
    }

    static class DbConnection {
        public static Connection getConnection() {
            try {
                return DriverManager.getConnection("jdbc:mysql://localhost:3306/zanettacars", "root", "p@rty123Z");
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
