/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankaccountmanagement;


 

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UpdateAccount extends JFrame {
    JTextField accountNumberField, emailField;
    JPasswordField passwordField;
    JButton updateButton;

    public UpdateAccount() {
        setTitle("Update Account");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        accountNumberField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();

        panel.add(new JLabel("Enter Account Number:"));
        panel.add(accountNumberField);
        panel.add(new JLabel("New Email:"));
        panel.add(emailField);
        panel.add(new JLabel("New Password:"));
        panel.add(passwordField);

        updateButton = new JButton("Update Account");
        updateButton.addActionListener(e -> updateAccount());
        panel.add(updateButton);

        add(panel);
        setVisible(true);
    }

    void updateAccount() {
        String accNum = accountNumberField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newPassword = new String(passwordField.getPassword());

        if (accNum.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE users SET email=?, password=? WHERE account_number=?");
            stmt.setString(1, newEmail);
            stmt.setString(2, newPassword);
            stmt.setString(3, accNum);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Account updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Account not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating account.");
        }
    }
}
