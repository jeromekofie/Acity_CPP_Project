import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class Dashboard {
    JFrame frame;
    JTable table;
    DefaultTableModel model;
    JButton addBtn, editBtn, deleteBtn, searchBtn;
    JTextField searchField;

    public Dashboard() {
        frame = new JFrame("Dashboard");
        frame.setSize(800, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE); // or any other color


        model = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Phone"}, 0);
        table = new JTable(model);

        table.setBackground(new Color(240, 248, 255)); // light blue
        table.setForeground(Color.DARK_GRAY);          // font color
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);

        table.getTableHeader().setBackground(new Color(70, 130, 180)); // steel blue
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));

        loadContacts();  // Initial load of all contacts

        JPanel topPanel = new JPanel();
        addBtn = new JButton("Add");
        editBtn = new JButton("Edit");
        deleteBtn = new JButton("Delete");
        searchBtn = new JButton("Search");
        searchField = new JTextField(20);

        Color buttonColor = new Color(100, 149, 237); // cornflower blue
        Color fontColor = Color.WHITE;

        JButton[] buttons = {addBtn, editBtn, deleteBtn, searchBtn};
        for (JButton btn : buttons) {
            btn.setBackground(buttonColor);
            btn.setForeground(fontColor);
            btn.setFocusPainted(false);
            btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        }

        searchField.setBackground(Color.WHITE);
        searchField.setForeground(Color.BLACK);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13)); 

        topPanel.add(addBtn);
        topPanel.add(editBtn);
        topPanel.add(deleteBtn);
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchBtn);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        addBtn.addActionListener(e -> openForm(null));
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Contact c = new Contact(
                    Integer.parseInt(model.getValueAt(row, 0).toString()),
                    model.getValueAt(row, 1).toString(),
                    model.getValueAt(row, 2).toString(),
                    model.getValueAt(row, 3).toString()
                );
                openForm(c);
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = Integer.parseInt(model.getValueAt(row, 0).toString());
                try (Connection conn = DBConnection.getConnection()) {
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM contacts WHERE id=?");
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    loadContacts();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        searchBtn.addActionListener(e -> {
            System.out.println("Search button clicked!");
            searchContacts(searchField.getText());
        });

        frame.setVisible(true);
    }

    void loadContacts() {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM contacts");
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("phone")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void searchContacts(String searchTerm) {
        model.setRowCount(0); // clear the table
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            loadContacts(); // load all if empty
            return;
        }
    
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM contacts WHERE name LIKE ? OR email LIKE ? OR phone LIKE ?";
            PreparedStatement ps = conn.prepareStatement(query);
            String pattern = "%" + searchTerm.trim() + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
    
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone")
                });
            }
    
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error occurred while searching.");
        }
    }

    void openForm(Contact contact) {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();

        if (contact != null) {
            nameField.setText(contact.getName());
            emailField.setText(contact.getEmail());
            phoneField.setText(contact.getPhone());
        }

        Object[] formFields = {
            "Name:", nameField,
            "Email:", emailField,
            "Phone:", phoneField
        };

        int option = JOptionPane.showConfirmDialog(null, formFields, contact == null ? "Add Contact" : "Edit Contact", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                if (contact == null) {
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO contacts (name, email, phone) VALUES (?, ?, ?)");
                    ps.setString(1, nameField.getText());
                    ps.setString(2, emailField.getText());
                    ps.setString(3, phoneField.getText());
                    ps.executeUpdate();
                } else {
                    PreparedStatement ps = conn.prepareStatement("UPDATE contacts SET name=?, email=?, phone=? WHERE id=?");
                    ps.setString(1, nameField.getText());
                    ps.setString(2, emailField.getText());
                    ps.setString(3, phoneField.getText());
                    ps.setInt(4, contact.getId());
                    ps.executeUpdate();
                }
                loadContacts();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}