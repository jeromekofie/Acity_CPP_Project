/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankaccountmanagement;

/**
 *
 * @author Michael
 */


import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DeleteAccount extends JFrame {
    JTextField accountNumberField;
    JButton deleteButton;

    public DeleteAccount() {
        setTitle("Delete Account");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        accountNumberField = new JTextField();
        panel.add(new JLabel("Enter Account Number to Delete:"));
        panel.add(accountNumberField);

        deleteButton = new JButton("Delete Account");
        deleteButton.addActionListener(e -> deleteAccount());
        panel.add(deleteButton);

        add(panel);
        setVisible(true);
    }

    void deleteAccount() {
        String accountNumber = accountNumberField.getText().trim();
        if (accountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Account number cannot be empty.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE account_number = ?");
            stmt.setString(1, accountNumber);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Account successfully deleted.");
            } else {
                JOptionPane.showMessageDialog(this, "Account not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred.");
        }
    }
}

