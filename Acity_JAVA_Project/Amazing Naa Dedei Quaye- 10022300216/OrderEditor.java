import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List; // ✅ Required for List
import java.util.ArrayList; // ✅ Required for ArrayList

public class OrderEditor extends JFrame {
    private JTextField customerField, itemsField, totalField;
    private Order originalOrder;
    private Runnable refreshCallback;

    public OrderEditor(Order order, Runnable onSaveRefresh) {
        this.originalOrder = order;
        this.refreshCallback = onSaveRefresh;

        setTitle("Edit Order");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Order ID:"));
        add(new JLabel(order.orderId));

        add(new JLabel("Customer:"));
        customerField = new JTextField(order.customerName);
        add(customerField);

        add(new JLabel("Items:"));
        itemsField = new JTextField(order.items);
        add(itemsField);

        add(new JLabel("Total:"));
        totalField = new JTextField(order.total);
        add(totalField);

        JButton saveBtn = new JButton("Save Changes");
        saveBtn.addActionListener(e -> saveChanges());
        add(saveBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        add(cancelBtn);

        setVisible(true);
    }

    private void saveChanges() {
        String updatedCustomer = customerField.getText().trim();
        String updatedItems = itemsField.getText().trim();
        String updatedTotal = calculateTotal(updatedItems); // Calculate total based on items

        if (updatedTotal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Total cannot be empty.");
            return;
        }

        Order updatedOrder = new Order(originalOrder.orderId, updatedCustomer, updatedItems, updatedTotal);

        List<Order> allOrders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Order o = Order.fromFileString(line);
                if (o != null) {
                    if (o.orderId.equals(originalOrder.orderId)) {
                        allOrders.add(updatedOrder); // updated
                    } else {
                        allOrders.add(o); // original
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error reading order file.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("orders.txt", false))) {
            for (Order o : allOrders) {
                bw.write(o.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to order file.");
            return;
        }

        JOptionPane.showMessageDialog(this, "Order updated successfully!");
        dispose();
        refreshCallback.run(); // Refresh order viewer
    }

    private String calculateTotal(String items) {
        // Assuming items are formatted as "item1 x quantity1, item2 x quantity2"
        String[] itemArray = items.split(", ");
        double total = 0.0;
        for (String item : itemArray) {
            String[] parts = item.split(" x ");
            if (parts.length == 2) {
                String itemName = parts[0].trim();
                int quantity = Integer.parseInt(parts[1].trim());
                switch (itemName.toLowerCase()) {
                    case "pizza":
                        total += quantity * 15.99;
                        break;
                    case "salad":
                        total += quantity * 5.50;
                        break;
                    case "burger":
                        total += quantity * 8.99;
                        break;
                    case "water":
                        total += quantity * 2.99;
                        break;
                }
            } else {
                System.out.println("Invalid item format: " + item); // Debugging statement
            }
        }
        System.out.println("Calculated total: " + total); // Debugging statement
        return String.valueOf(total);
    }
}
