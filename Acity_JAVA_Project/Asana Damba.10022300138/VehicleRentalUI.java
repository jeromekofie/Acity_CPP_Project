import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VehicleRentalUI extends JFrame {

    private JTable vehicleTable;
    private DefaultTableModel vehicleModel;

    public VehicleRentalUI() {
        setTitle("Vehicle Rental System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Vehicle Table
        vehicleModel = new DefaultTableModel(new String[]{"ID", "Type", "Model", "Plate", "Rate"}, 0);
        vehicleTable = new JTable(vehicleModel);
        JScrollPane scrollPane = new JScrollPane(vehicleTable);

        // Load data
        loadVehicles();

        // Top Panel - Buttons
        JPanel topPanel = new JPanel();
        JButton addBtn = new JButton("Add Vehicle");
        JButton deleteBtn = new JButton("Delete Vehicle");
        JButton rentBtn = new JButton("Rent Vehicle");
        topPanel.add(addBtn);
        topPanel.add(deleteBtn);
        topPanel.add(rentBtn);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button Actions
        addBtn.addActionListener(e -> openAddVehicleDialog());
        deleteBtn.addActionListener(e -> deleteSelectedVehicle());
        rentBtn.addActionListener(e -> openRentDialog());

        setVisible(true);
    }

    private void loadVehicles() {
        vehicleModel.setRowCount(0); // clear table
        ArrayList<Vehicle> vehicles = VehicleDAO.getAllVehicles();
        for (Vehicle v : vehicles) {
            vehicleModel.addRow(new Object[]{
                v.getId(), v.getType(), v.getModel(), v.getNumberPlate(), v.getRatePerDay()
            });
        }
    }

    private void openAddVehicleDialog() {
        JTextField modelField = new JTextField(10);
        JTextField plateField = new JTextField(10);
        JTextField rateField = new JTextField(10);
        String[] types = {"Car", "Bike", "Truck"};
        JComboBox<String> typeBox = new JComboBox<>(types);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Type:")); panel.add(typeBox);
        panel.add(new JLabel("Model:")); panel.add(modelField);
        panel.add(new JLabel("Plate:")); panel.add(plateField);
        panel.add(new JLabel("Rate/Day:")); panel.add(rateField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Vehicle", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String type = (String) typeBox.getSelectedItem();
            String model = modelField.getText();
            String plate = plateField.getText();
            double rate = Double.parseDouble(rateField.getText());

            Vehicle v = switch (type) {
                case "Car" -> new Car(0, model, plate, rate);
                case "Bike" -> new Bike(0, model, plate, rate);
                case "Truck" -> new Truck(0, model, plate, rate);
                default -> null;
            };

            if (v != null && VehicleDAO.insertVehicle(v)) {
                loadVehicles();
                JOptionPane.showMessageDialog(this, "Vehicle added!");
            }
        }
    }

    private void deleteSelectedVehicle() {
        int selected = vehicleTable.getSelectedRow();
        if (selected >= 0) {
            int id = (int) vehicleModel.getValueAt(selected, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this vehicle?");
            if (confirm == JOptionPane.YES_OPTION) {
                if (VehicleDAO.deleteVehicle(id)) {
                    loadVehicles();
                    JOptionPane.showMessageDialog(this, "Vehicle deleted.");
                }
            }
        }
    }

    private void openRentDialog() {
        int selected = vehicleTable.getSelectedRow();
        if (selected >= 0) {
            int vehicleId = (int) vehicleModel.getValueAt(selected, 0);
            JTextField customerField = new JTextField(10);
            JTextField rentDateField = new JTextField("YYYY-MM-DD");
            JTextField returnDateField = new JTextField("YYYY-MM-DD");

            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Customer:")); panel.add(customerField);
            panel.add(new JLabel("Rent Date:")); panel.add(rentDateField);
            panel.add(new JLabel("Return Date:")); panel.add(returnDateField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Rent Vehicle", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = customerField.getText();
                Date rentDate = Date.valueOf(LocalDate.parse(rentDateField.getText()));
                Date returnDate = Date.valueOf(LocalDate.parse(returnDateField.getText()));

                if (RentalDAO.rentVehicle(vehicleId, name, rentDate, returnDate)) {
                    JOptionPane.showMessageDialog(this, "Rental recorded successfully.");
                }
            }
        }
    }
}
