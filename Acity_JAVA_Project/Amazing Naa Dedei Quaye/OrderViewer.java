import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class OrderViewer extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public OrderViewer() {
        setTitle("Order History");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // -------- Custom Background Panel --------
        JPanel contentPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("images\\512021655bf62f3a08b7ca12cdc05f64.jpg"); // Change path if needed
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPanel.setLayout(new BorderLayout());
        setContentPane(contentPanel);

        // -------- Table Setup --------
        String[] cols = {"Order ID", "Customer", "Items", "Total"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // disable direct editing
            }
        };

        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(Color.BLACK);

        // Transparent styling
        table.setOpaque(false);
        ((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(true);
        table.setBackground(new Color(255, 255, 255, 200));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(255, 255, 255, 200));
        header.setOpaque(true);

        // Panel wrapper for table
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(new Color(255, 255, 255, 180));
        tableWrapper.setOpaque(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        tableWrapper.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(tableWrapper, BorderLayout.CENTER);

        // -------- Buttons Panel --------
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setLayout(new FlowLayout());

        JButton editBtn = createStyledButton("Edit Selected", new Color(86, 179, 255));     // Blue
        JButton deleteBtn = createStyledButton("Delete Selected", new Color(255, 87, 87));  // Red

        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        contentPanel.add(btnPanel, BorderLayout.SOUTH);

        // -------- Button Logic --------
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = model.getValueAt(row, 0).toString();
                Order order = getOrderById(id);
                if (order != null) {
                    new OrderEditor(order, this::loadOrders);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an order to edit.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = model.getValueAt(row, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this order?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteOrderById(id);
                    loadOrders();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an order to delete.");
            }
        });

        loadOrders();
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK);
            }
        });
        return button;
    }

    private void loadOrders() {
        model.setRowCount(0); // Clear existing rows
        try (BufferedReader br = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Order o = Order.fromFileString(line);
                if (o != null) {
                    model.addRow(new Object[]{o.orderId, o.customerName, o.items, o.total});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Order getOrderById(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Order o = Order.fromFileString(line);
                if (o != null && o.orderId.equals(id)) return o;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void deleteOrderById(String id) {
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("orders.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                Order o = Order.fromFileString(line);
                if (o != null && !o.orderId.equals(id)) {
                    lines.add(line);
                }
            }
            reader.close();

            FileWriter writer = new FileWriter("orders.txt", false);
            for (String l : lines) writer.write(l + "\n");
            writer.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting order.");
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderViewer());
    }
}
