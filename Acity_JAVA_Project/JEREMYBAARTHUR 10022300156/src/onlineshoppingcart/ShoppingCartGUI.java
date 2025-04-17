package onlineshoppingcart;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import javax.sound.sampled.*;
import java.io.*;

public class ShoppingCartGUI {
    private CartManager manager = new CartManager();
    private JPanel productPanel;
    private JTextArea cartArea;
    private JLabel totalLabel;
    private JTextField nameField, priceField, quantityField;
    private JComboBox<Item> itemDropdown;
    private DefaultComboBoxModel<Item> dropdownModel;

    public ShoppingCartGUI() {
        JFrame frame = new JFrame("Brad's Place - Online Shopping");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.BLACK);

        productPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        productPanel.setBackground(Color.BLACK);
        loadProductCards();

        JScrollPane scrollPane = new JScrollPane(productPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        cartArea = new JTextArea(15, 30);
        cartArea.setEditable(false);
        cartArea.setBackground(Color.DARK_GRAY);
        cartArea.setForeground(Color.LIGHT_GRAY);
        cartArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane cartScroll = new JScrollPane(cartArea);

        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(Color.LIGHT_GRAY);

        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        cartPanel.setBackground(Color.BLACK);
        cartPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Cart", 0, 0, null, Color.LIGHT_GRAY));
        cartPanel.add(cartScroll);
        cartPanel.add(totalLabel);

        JButton checkoutButton = createStyledButton("Checkout");
        checkoutButton.addActionListener(e -> createReceipt());
        cartPanel.add(checkoutButton);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(Color.BLACK);
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Manage Products", 0, 0, null, Color.LIGHT_GRAY));

        nameField = new JTextField();
        priceField = new JTextField();
        JButton addProductButton = createStyledButton("Add Product");
        JButton removeProductButton = createStyledButton("Remove Product");
        itemDropdown = new JComboBox<>();
        dropdownModel = new DefaultComboBoxModel<>(manager.getItemsFromDatabase().toArray(new Item[0]));
        itemDropdown.setModel(dropdownModel);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.LIGHT_GRAY);
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setForeground(Color.LIGHT_GRAY);

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(priceLabel);
        inputPanel.add(priceField);
        inputPanel.add(addProductButton);
        inputPanel.add(removeProductButton);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(Color.BLACK);
        sidePanel.add(cartPanel);
        sidePanel.add(Box.createVerticalStrut(20));
        sidePanel.add(inputPanel);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.EAST);

        frame.add(mainPanel);

        addProductButton.addActionListener(e -> addProduct());
        removeProductButton.addActionListener(e -> removeProduct());

        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.LIGHT_GRAY);
        button.setPreferredSize(new Dimension(140, 35));
        button.setFocusPainted(false);
        return button;
    }

    private void loadProductCards() {
        productPanel.removeAll();
        List<Item> items = manager.getItemsFromDatabase();
        for (Item item : items) {
            JPanel card = createProductCard(item);
            productPanel.add(card);
        }
        productPanel.revalidate();
        productPanel.repaint();
    }

    private JPanel createProductCard(Item item) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(Color.DARK_GRAY);

        JLabel name = new JLabel(item.getName());
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));
        name.setForeground(Color.WHITE);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel price = new JLabel("$" + item.getPrice());
        price.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        price.setForeground(Color.LIGHT_GRAY);
        price.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField qtyField = new JTextField("1");
        qtyField.setMaximumSize(new Dimension(50, 30));
        qtyField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addToCartBtn = createStyledButton("Add to Cart");
        addToCartBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addToCartBtn.addActionListener(e -> {
            try {
                int qty = Integer.parseInt(qtyField.getText());
                if (qty > 0) {
                    manager.addToCart(item.getId(), qty);
                    playSound("resources/add.wav");
                    updateCartDisplay();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid quantity.");
            }
        });

        JButton removeFromCartBtn = createStyledButton("Remove");
        removeFromCartBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeFromCartBtn.addActionListener(e -> {
            manager.removeFromCart(item.getId());
            playSound("resources/remove.wav");
            updateCartDisplay();
        });

        card.add(Box.createVerticalStrut(10));
        card.add(name);
        card.add(price);
        card.add(Box.createVerticalStrut(5));
        card.add(qtyField);
        card.add(Box.createVerticalStrut(5));
        card.add(addToCartBtn);
        card.add(removeFromCartBtn);

        return card;
    }

    private void updateCartDisplay() {
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#0.00");

        Map<Integer, Integer> cart = manager.getCart();
        List<Item> items = manager.getItemsFromDatabase();

        for (Item item : items) {
            int quantity = cart.getOrDefault(item.getId(), 0);
            if (quantity > 0) {
                double lineTotal = item.getPrice() * quantity;
                sb.append(item.getName())
                  .append(" x ")
                  .append(quantity)
                  .append(" = $")
                  .append(df.format(lineTotal))
                  .append("\n");
            }
        }

        cartArea.setText(sb.toString());
        totalLabel.setText("Total: $" + df.format(manager.calculateTotal()));
    }

    private void addProduct() {
        String name = nameField.getText();
        String priceText = priceField.getText();
        try {
            double price = Double.parseDouble(priceText);
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO items (name, price) VALUES (?, ?)");
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.executeUpdate();
            refreshDropdown();
            loadProductCards();
            nameField.setText("");
            priceField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid product info.");
        }
    }

    private void removeProduct() {
        Item selected = (Item) itemDropdown.getSelectedItem();
        if (selected != null) {
            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM items WHERE id = ?");
                stmt.setInt(1, selected.getId());
                stmt.executeUpdate();
                manager.removeFromCart(selected.getId());
                refreshDropdown();
                loadProductCards();
                updateCartDisplay();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Failed to remove product.");
            }
        }
    }

    private void refreshDropdown() {
        dropdownModel.removeAllElements();
        for (Item item : manager.getItemsFromDatabase()) {
            dropdownModel.addElement(item);
        }
    }

    private void playSound(String soundPath) {
        try {
            File soundFile = new File(soundPath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Sound error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ShoppingCartGUI();
    }

    // ✅ Updated createReceipt() method
    private void createReceipt() {
        StringBuilder receipt = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#0.00");

        receipt.append("======== Brad's Place Receipt ========\n");
        receipt.append("Date: ").append(new java.util.Date()).append("\n\n");

        Map<Integer, Integer> cart = manager.getCart();
        List<Item> items = manager.getItemsFromDatabase();
        double total = 0;

        for (Item item : items) {
            int quantity = cart.getOrDefault(item.getId(), 0);
            if (quantity > 0) {
                double lineTotal = item.getPrice() * quantity;
                receipt.append(item.getName())
                       .append(" x ")
                       .append(quantity)
                       .append(" = $")
                       .append(df.format(lineTotal))
                       .append("\n");
                total += lineTotal;
            }
        }

        receipt.append("\nTotal: $").append(df.format(total)).append("\n");
        receipt.append("Thank you for shopping at Brad's Place! Bigger is Better \n");
        receipt.append("=====================================\n");

        JTextArea receiptArea = new JTextArea(receipt.toString());
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        receiptArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(null, scrollPane, "Receipt", JOptionPane.INFORMATION_MESSAGE);

        manager.clearCart();
        updateCartDisplay();
    }
}
