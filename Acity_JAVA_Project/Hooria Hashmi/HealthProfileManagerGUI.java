import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class HealthProfileManagerGUI extends JFrame {
    private java.util.List<HealthProfile> profiles = new java.util.ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable profileTable;

    public HealthProfileManagerGUI() {
        // Load profiles from database
        profiles = DatabaseConnection.getAllProfiles();
        
        setTitle("Ridge Hospital - Health Profile Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 600);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Ridge Hospital - Health Profile Manager", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0, 102, 102));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Create Profile", createProfilePanel());
        tabs.add("View Profiles", viewProfilesPanel());
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);
        mainPanel.setBackground(Color.WHITE);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(230, 255, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Name:", "Weight (kg):", "Height (m):", "Body Temperature (°C):", "Blood Sugar (mg/dL):", "Systolic BP:", "Diastolic BP:"};
        JTextField[] fields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            fields[i] = new JTextField(15);
            panel.add(fields[i], gbc);
        }

        JButton saveBtn = new JButton("Save Profile");
        gbc.gridy = labels.length;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            try {
                String name = fields[0].getText();
                double weight = Double.parseDouble(fields[1].getText());
                double height = Double.parseDouble(fields[2].getText());
                double temp = Double.parseDouble(fields[3].getText());
                double sugar = Double.parseDouble(fields[4].getText());
                int sys = Integer.parseInt(fields[5].getText());
                int dia = Integer.parseInt(fields[6].getText());

                HealthProfile profile = new HealthProfile(-1, name, weight, height, temp, sugar, sys, dia);
                int newId = DatabaseConnection.createProfile(profile);
                if (newId != -1) {
                    profile.id = newId;
                    profiles.add(profile);
                    JOptionPane.showMessageDialog(this, "Profile saved successfully!");
                    for (JTextField f : fields) f.setText("");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save profile.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please check values.");
                ex.printStackTrace();
            }
        });

        return panel;
    }

    private JPanel viewProfilesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Search Name:");
        JTextField searchField = new JTextField(15);
        JLabel filterLabel = new JLabel("Filter by BMI:");
        String[] bmiOptions = {"All", "Underweight", "Normal weight", "Overweight", "Obese"};
        JComboBox<String> filterBox = new JComboBox<>(bmiOptions);
        JButton applyBtn = new JButton("Apply Filter");

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setBackground(new Color(255, 100, 100));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deleteSelectedProfile());

        JButton editBtn = new JButton("Edit Selected");
        editBtn.setBackground(new Color(100, 100, 255));
        editBtn.setForeground(Color.WHITE);
        editBtn.addActionListener(e -> editSelectedProfile());

        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(filterLabel);
        topPanel.add(filterBox);
        topPanel.add(applyBtn);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(editBtn);
        topPanel.add(deleteBtn);

        String[] columnNames = {"ID", "Name", "Weight", "Height", "Temperature", "Blood Sugar", "BP", "BMI"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        profileTable = new JTable(tableModel);
        profileTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(profileTable);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        applyBtn.addActionListener(e -> {
            String searchText = searchField.getText().trim().toLowerCase();
            String selectedFilter = (String) filterBox.getSelectedItem();
            filterAndSearchTable(searchText, selectedFilter);
        });

        refreshTable();
        return panel;
    }

    private void filterAndSearchTable(String searchText, String bmiFilter) {
        tableModel.setRowCount(0);
        for (HealthProfile p : profiles) {
            boolean nameMatches = p.name.toLowerCase().contains(searchText);
            boolean bmiMatches = bmiFilter.equals("All") || p.bmiCategory().equalsIgnoreCase(bmiFilter);
            if (nameMatches && bmiMatches) {
                tableModel.addRow(p.toTableRow());
            }
        }
    }

    private void refreshTable() {
        profiles = DatabaseConnection.getAllProfiles();
        filterAndSearchTable("", "All");
    }

    private void deleteSelectedProfile() {
        int selectedRow = profileTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a profile to delete.");
            return;
        }

        int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this profile?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (DatabaseConnection.deleteProfile(id)) {
                JOptionPane.showMessageDialog(this, "Profile deleted successfully!");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete profile.");
            }
        }
    }

    private void editSelectedProfile() {
        int selectedRow = profileTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a profile to edit.");
            return;
        }

        int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        HealthProfile profile = DatabaseConnection.getProfileById(id);
        if (profile == null) return;

        JPanel editPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        editPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(profile.name);
        JTextField weightField = new JTextField(String.valueOf(profile.weight));
        JTextField heightField = new JTextField(String.valueOf(profile.height));
        JTextField tempField = new JTextField(String.valueOf(profile.bodyTemperature));
        JTextField sugarField = new JTextField(String.valueOf(profile.bloodSugar));
        JTextField sysField = new JTextField(String.valueOf(profile.systolicBP));
        JTextField diaField = new JTextField(String.valueOf(profile.diastolicBP));

        editPanel.add(new JLabel("Name:"));
        editPanel.add(nameField);
        editPanel.add(new JLabel("Weight (kg):"));
        editPanel.add(weightField);
        editPanel.add(new JLabel("Height (m):"));
        editPanel.add(heightField);
        editPanel.add(new JLabel("Body Temperature (°C):"));
        editPanel.add(tempField);
        editPanel.add(new JLabel("Blood Sugar (mg/dL):"));
        editPanel.add(sugarField);
        editPanel.add(new JLabel("Systolic BP:"));
        editPanel.add(sysField);
        editPanel.add(new JLabel("Diastolic BP:"));
        editPanel.add(diaField);

        int result = JOptionPane.showConfirmDialog(this, editPanel, 
            "Edit Profile", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                profile.name = nameField.getText();
                profile.weight = Double.parseDouble(weightField.getText());
                profile.height = Double.parseDouble(heightField.getText());
                profile.bodyTemperature = Double.parseDouble(tempField.getText());
                profile.bloodSugar = Double.parseDouble(sugarField.getText());
                profile.systolicBP = Integer.parseInt(sysField.getText());
                profile.diastolicBP = Integer.parseInt(diaField.getText());

                if (DatabaseConnection.updateProfile(profile)) {
                    JOptionPane.showMessageDialog(this, "Profile updated successfully!");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update profile.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please check values.");
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // Initialize database before starting the application
        DatabaseConnection.initializeDatabase();
        
        SwingUtilities.invokeLater(() -> {
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.setVisible(true);
        });
    }
}
