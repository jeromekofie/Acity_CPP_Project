import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Random;

public class HotelMenuSystem extends JFrame {
    private JTextField nameField;
    private JSpinner pizzaSpinner, saladSpinner, burgerSpinner, waterSpinner;
    private DecimalFormat df = new DecimalFormat("#.00");

    public HotelMenuSystem() {
        setTitle("Hotel Menu Ordering System");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 🌄 Background panel with image
        JPanel contentPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("images\\fb85bc12d09b0cd3e5c193b665dcb92c.jpg");
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        setContentPane(contentPanel);

        // 🧾 Transparent container panel
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60)); // Padding

        JLabel title = new JLabel("Hotel Menu Ordering System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(title);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        // 🧩 Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setOpaque(false);

        nameField = new JTextField();
        pizzaSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        saladSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        burgerSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        waterSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));

        formPanel.add(createLabel("Customer Name:")); formPanel.add(nameField);
        formPanel.add(createLabel("Pizza ($15.99):")); formPanel.add(pizzaSpinner);
        formPanel.add(createLabel("Salad ($5.50):")); formPanel.add(saladSpinner);
        formPanel.add(createLabel("Burger ($8.99):")); formPanel.add(burgerSpinner);
        formPanel.add(createLabel("Water ($2.99):")); formPanel.add(waterSpinner);

        container.add(formPanel);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        // 🧃 Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        JButton orderBtn = createButton("Place Order", new Color(76, 175, 80));
        JButton viewBtn = createButton("View Orders", new Color(33, 150, 243));

        buttonPanel.add(orderBtn);
        buttonPanel.add(viewBtn);

        container.add(buttonPanel);
        contentPanel.add(container);

        orderBtn.addActionListener(e -> placeOrder());
        viewBtn.addActionListener(e -> new OrderViewer());

        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JButton createButton(String text, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 35));

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

    // private void placeOrder() {
    //     String name = nameField.getText();
    //     if (name.trim().isEmpty()) {
    //         JOptionPane.showMessageDialog(this, "Please enter customer name.");
    //         return;
    //     }

    //     int pizza = (Integer) pizzaSpinner.getValue();
    //     int salad = (Integer) saladSpinner.getValue();
    //     int burger = (Integer) burgerSpinner.getValue();
    //     int water = (Integer) waterSpinner.getValue();

    //     double total = pizza * 15.99 + salad * 5.50 + burger * 8.99 + water * 2.99;
    //     if (total == 0) {
    //         JOptionPane.showMessageDialog(this, "Please select at least one item.");
    //         return;
    //     }

    //     String orderId = "ORD" + new Random().nextInt(100000);
    //     StringBuilder itemSummary = new StringBuilder();
    //     if (pizza > 0) itemSummary.append(pizza + "x Pizza=$" + df.format(pizza * 15.99) + ", ");
    //     if (salad > 0) itemSummary.append(salad + "x Salad=$" + df.format(salad * 5.50) + ", ");
    //     if (burger > 0) itemSummary.append(burger + "x Burger=$" + df.format(burger * 8.99) + ", ");
    //     if (water > 0) itemSummary.append(water + "x Water=$" + df.format(water * 2.99) + ", ");

    //     if (itemSummary.length() > 0) itemSummary.setLength(itemSummary.length() - 2); // remove last comma

    //     try (FileWriter writer = new FileWriter("orders.txt", true)) {
    //         writer.write(orderId + "|" + name + "|" + itemSummary + "|" + df.format(total) + "\n");
    //         JOptionPane.showMessageDialog(this, "Order placed successfully!");
    //     } catch (Exception e) {
    //         JOptionPane.showMessageDialog(this, "Error saving order.");
    //     }
    // }

    private void placeOrder() {
        String name = nameField.getText();
        if (name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name.");
            return;
        }
    
        int pizza = (Integer) pizzaSpinner.getValue();
        int salad = (Integer) saladSpinner.getValue();
        int burger = (Integer) burgerSpinner.getValue();
        int water = (Integer) waterSpinner.getValue();
    
        double total = pizza * 15.99 + salad * 5.50 + burger * 8.99 + water * 2.99;
        if (total == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one item.");
            return;
        }
    
        String orderId = "ORD" + new Random().nextInt(100000);
        StringBuilder itemSummary = new StringBuilder();
        if (pizza > 0) itemSummary.append(pizza + "x Pizza=$" + df.format(pizza * 15.99) + ", ");
        if (salad > 0) itemSummary.append(salad + "x Salad=$" + df.format(salad * 5.50) + ", ");
        if (burger > 0) itemSummary.append(burger + "x Burger=$" + df.format(burger * 8.99) + ", ");
        if (water > 0) itemSummary.append(water + "x Water=$" + df.format(water * 2.99) + ", ");
        if (itemSummary.length() > 0) itemSummary.setLength(itemSummary.length() - 2); // remove last comma
    
        String totalFormatted = df.format(total);
        Order order = new Order(orderId, name, itemSummary.toString(), totalFormatted);
    
        // 🧠 Insert into database
        try {
            DatabaseManager db = new DatabaseManager();
            db.addOrder(orderId, name, itemSummary.toString(), total);
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save to database.");
        }
    
        // 🧾 Save to text file
        try (FileWriter writer = new FileWriter("orders.txt", true)) {
            writer.write(order.toFileString() + "\n");
            JOptionPane.showMessageDialog(this, "Order placed successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving order.");
        }
    }
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelMenuSystem());
    }
}
