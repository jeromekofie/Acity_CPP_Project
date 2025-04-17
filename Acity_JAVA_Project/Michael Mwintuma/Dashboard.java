package bankaccountmanagement;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Dashboard extends JFrame {
    User user;
    JComboBox<String> actionBox;
    JTextField amountField, recipientField;
    JButton submitButton, updateButton, deleteButton;
    JLabel welcomeLabel, accountLabel, balanceLabel, selectedActionLabel;

    public Dashboard(User user) {
        this.user = user;
        setTitle("Dashboard");
        setSize(600, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GradientPanel background = new GradientPanel();
        background.setLayout(new GridBagLayout());

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.SrcOver.derive(1f));
                g2d.setColor(getBackground());
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        panel.setPreferredSize(new Dimension(400, 450));
        panel.setBackground(new Color(255, 255, 255, 230));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        welcomeLabel = new JLabel("Welcome, " + user.username);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        accountLabel = new JLabel("Account Number: " + user.accountNumber);
        accountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        balanceLabel = new JLabel("Account Balance: GHS " + String.format("%.2f", user.balance));
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        actionBox = new JComboBox<>(new String[]{"Deposit", "Withdraw", "Transfer"});
        actionBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        actionBox.setBorder(BorderFactory.createTitledBorder("Choose Action"));

        selectedActionLabel = new JLabel("Selected Action: Deposit");
        selectedActionLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        selectedActionLabel.setForeground(Color.DARK_GRAY);
        selectedActionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        actionBox.addActionListener(e -> {
            String selected = (String) actionBox.getSelectedItem();
            selectedActionLabel.setText("Selected Action: " + selected);
        });

        amountField = new JTextField();
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        amountField.setBorder(BorderFactory.createTitledBorder("Enter Amount"));

        recipientField = new JTextField();
        recipientField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        recipientField.setFont(new Font("Arial", Font.PLAIN, 14));
        recipientField.setBorder(BorderFactory.createTitledBorder("Recipient Account Number"));

        submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setFocusPainted(false);
        submitButton.setBackground(new Color(255, 128, 128));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.addActionListener(e -> performAction());

        updateButton = new JButton("Update Account");
        updateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateButton.setFocusPainted(false);
        updateButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.addActionListener(e -> new UpdateAccount());

        deleteButton = new JButton("Delete Account");
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.setFocusPainted(false);
        deleteButton.setBackground(new Color(220, 20, 60)); // Crimson
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.addActionListener(e -> new DeleteAccount());

        // Add components
        panel.add(welcomeLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(accountLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(balanceLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(actionBox);
        panel.add(Box.createVerticalStrut(5));
        panel.add(selectedActionLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(amountField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(recipientField);
        panel.add(Box.createVerticalStrut(15));
        panel.add(submitButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(updateButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(deleteButton);

        background.add(panel);
        setContentPane(background);
        setVisible(true);
    }

    void performAction() {
        try {
            String action = (String) actionBox.getSelectedItem();
            double amount = Double.parseDouble(amountField.getText());

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than zero.");
                return;
            }

            if (action.equals("Deposit")) {
                user.balance += amount;

            } else if (action.equals("Withdraw")) {
                if (amount > user.balance) {
                    JOptionPane.showMessageDialog(this, "Insufficient funds");
                    return;
                }
                user.balance -= amount;

            } else if (action.equals("Transfer")) {
                String recipientAcc = recipientField.getText().trim();
                if (recipientAcc.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter recipient account number.");
                    return;
                }
                if (amount > user.balance) {
                    JOptionPane.showMessageDialog(this, "Insufficient funds");
                    return;
                }

                try (Connection conn = DatabaseConnection.getConnection()) {
                    conn.setAutoCommit(false);

                    PreparedStatement findRecipient = conn.prepareStatement("SELECT * FROM users WHERE account_number=?");
                    findRecipient.setString(1, recipientAcc);
                    ResultSet rs = findRecipient.executeQuery();

                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this, "Recipient not found.");
                        conn.rollback();
                        return;
                    }

                    double recipientBalance = rs.getDouble("balance");
                    int recipientId = rs.getInt("id");

                    user.balance -= amount;
                    PreparedStatement deduct = conn.prepareStatement("UPDATE users SET balance=? WHERE id=?");
                    deduct.setDouble(1, user.balance);
                    deduct.setInt(2, user.id);
                    deduct.executeUpdate();

                    PreparedStatement credit = conn.prepareStatement("UPDATE users SET balance=? WHERE id=?");
                    credit.setDouble(1, recipientBalance + amount);
                    credit.setInt(2, recipientId);
                    credit.executeUpdate();

                    conn.commit();

                    balanceLabel.setText("Account Balance: GHS " + String.format("%.2f", user.balance));
                    JOptionPane.showMessageDialog(this, "Transfer successful! GHS " + amount + " sent to " + recipientAcc);
                    return;
                }
            }

            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("UPDATE users SET balance=? WHERE id=?");
                stmt.setDouble(1, user.balance);
                stmt.setInt(2, user.id);
                stmt.executeUpdate();

                balanceLabel.setText("Account Balance: GHS " + String.format("%.2f", user.balance));
                JOptionPane.showMessageDialog(this, action + " successful! New balance: GHS " + user.balance);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while performing the action.");
        }
    }

    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color color1 = new Color(255, 170, 102);
            Color color2 = new Color(255, 102, 204);
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setFont(new Font("Segoe UI", Font.BOLD, 48));
            g2d.setColor(new Color(255, 255, 255, 40));
            g2d.rotate(Math.toRadians(-45));

            g2d.drawString("Michaelson", -100, 200);
            g2d.drawString("Michaelson", 100, 400);
            g2d.drawString("Michaelson", 300, 600);

            g2d.dispose();
        }
    }
}
