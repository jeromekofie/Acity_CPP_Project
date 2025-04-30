import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class HotelMenuOrderingSystem {
    public static void main(String[] args) {
        // Initialize database connection
    
        
        SwingUtilities.invokeLater(() -> {
            MenuOrderGUI gui = new MenuOrderGUI();
            gui.setVisible(true);
        });
    }
}

class MenuOrderGUI extends JFrame {
    private OrderManager orderManager = new OrderManager();
    private JTextArea orderTextArea;
    private JLabel totalLabel;
    private DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");

    public MenuOrderGUI() {
        setTitle("Hotel Menu Ordering System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Menu items panel
        JPanel menuPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        String[] menuItems = {
            "Burger - $5.99", "Pizza - $8.99", 
            "Pasta - $7.49", "Salad - $4.99",
            "Soup - $3.99", "Steak - $12.99",
            "Soda - $1.99", "Water - $0.99"
        };

        for (String item : menuItems) {
            JButton itemButton = new JButton(item);
            itemButton.addActionListener(new MenuItemListener());
            menuPanel.add(itemButton);
        }

        // Order display panel
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderTextArea = new JTextArea(10, 30);
        orderTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderTextArea);
        orderPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with total and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Total: $0.00");
        bottomPanel.add(totalLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton clearButton = new JButton("Clear Order");
        clearButton.addActionListener(e -> clearOrder());
        buttonPanel.add(clearButton);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> checkout());
        buttonPanel.add(checkoutButton);

        // Add view orders button
        JButton viewOrdersButton = new JButton("View Orders");
        viewOrdersButton.addActionListener(e -> viewOrders());
        buttonPanel.add(viewOrdersButton);

        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        // Add components to main panel
        mainPanel.add(new JLabel("Menu Items:"), BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.add(orderPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private class MenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String itemText = ((JButton) e.getSource()).getText();
            // Extract item name and price
            String[] parts = itemText.split(" - \\$");
            String itemName = parts[0];
            double price = Double.parseDouble(parts[1]);
            
            orderManager.addItem(itemName, price);
            updateOrderDisplay();
        }
    }

    private void updateOrderDisplay() {
        orderTextArea.setText("");
        for (OrderItem item : orderManager.getItems()) {
            orderTextArea.append(String.format("%s - %s\n", 
                item.getName(), currencyFormat.format(item.getPrice())));
        }
        totalLabel.setText("Total: " + currencyFormat.format(orderManager.getTotal()));
    }

    private void clearOrder() {
        orderManager.clear();
        orderTextArea.setText("");
        totalLabel.setText("Total: $0.00");
    }

    private void checkout() {
        if (orderManager.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your order is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Generate bill
        StringBuilder bill = new StringBuilder();
        bill.append("=== Hotel Menu Order ===\n");
        bill.append("Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n\n");
        bill.append("Items Ordered:\n");
        
        for (OrderItem item : orderManager.getItems()) {
            bill.append(String.format("- %-15s %10s\n", 
                item.getName(), currencyFormat.format(item.getPrice())));
        }
        
        bill.append("\n");
        bill.append(String.format("%-15s %10s\n", "Subtotal:", currencyFormat.format(orderManager.getTotal())));
        double tax = orderManager.getTotal() * 0.08; // 8% tax
        bill.append(String.format("%-15s %10s\n", "Tax (8%):", currencyFormat.format(tax)));
        double total = orderManager.getTotal() + tax;
        bill.append(String.format("%-15s %10s\n", "Total:", currencyFormat.format(total)));
        
        // Save to database
        boolean success = DatabaseManager.saveOrder(
            orderManager.getItems(), 
            orderManager.getTotal(),
            tax,
            total
        );
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Order saved successfully!\n\n" + bill.toString(), "Order Complete", JOptionPane.INFORMATION_MESSAGE);
            clearOrder();
        } else {
            JOptionPane.showMessageDialog(this, "Error saving order to database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewOrders() {
        String orders = DatabaseManager.getAllOrders();
        JTextArea textArea = new JTextArea(20, 50);
        textArea.setText(orders);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(this, scrollPane, "Order History", JOptionPane.INFORMATION_MESSAGE);
    }
}