/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author joshua_veve
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;
    private JLabel statusLabel;
    private JCheckBox showPassword;

    public LoginGUI() {
        setTitle("Login - Bank Loan App");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        Color bgColor = new Color(40, 40, 40);
        Color inputColor = new Color(60, 63, 65);
        Color textColor = Color.WHITE;
        Color buttonColor = new Color(169, 169, 169);

        getContentPane().setBackground(bgColor);
        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        Font boldFont = new Font("Segoe UI", Font.BOLD, 14);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(40, 40, 80, 25);
        userLabel.setForeground(textColor);
        add(userLabel);

        userField = new JTextField();
        userField.setBounds(140, 40, 200, 25);
        userField.setBackground(inputColor);
        userField.setForeground(textColor);
        userField.setCaretColor(textColor);
        add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(40, 80, 80, 25);
        passLabel.setForeground(textColor);
        add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(140, 80, 200, 25);
        passField.setBackground(inputColor);
        passField.setForeground(textColor);
        passField.setCaretColor(textColor);
        add(passField);

        showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(140, 110, 200, 20);
        showPassword.setForeground(textColor);
        showPassword.setBackground(bgColor);
        showPassword.addActionListener(e -> {
            passField.setEchoChar(showPassword.isSelected() ? (char) 0 : '•');
        });
        add(showPassword);

        loginButton = new JButton("Login");
        loginButton.setBounds(140, 140, 200, 30);
        loginButton.setBackground(buttonColor);
        loginButton.setForeground(Color.BLACK);
        loginButton.setFont(boldFont);
        loginButton.addActionListener(e -> authenticate());
        add(loginButton);

        statusLabel = new JLabel();
        statusLabel.setBounds(40, 180, 300, 25);
        statusLabel.setForeground(Color.RED);
        add(statusLabel);

        setVisible(true);
    }

    private void authenticate() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (username.equals("Hans Black") && password.equals("Unity34")) {
            dispose(); // Close login window
            new Dashboard(); // Open dashboard
        } else {
            statusLabel.setText("Invalid username or password.");
        }
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}