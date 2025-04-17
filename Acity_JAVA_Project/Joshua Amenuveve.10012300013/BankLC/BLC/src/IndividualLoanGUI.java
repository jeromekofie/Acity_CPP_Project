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
import java.text.DecimalFormat;

public class IndividualLoanGUI extends JFrame {
    private JTextField nameField, amountField, rateField, timeField;
    private JLabel resultLabel;

    public IndividualLoanGUI() {
        setTitle("Individual Loan Calculator");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(230, 232, 235));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        JLabel iconLabel = new JLabel(new ImageIcon("individual_icon.png"));
        iconLabel.setBounds(200, 10, 100, 100);
        add(iconLabel);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 120, 100, 25);
        nameLabel.setFont(labelFont);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(160, 120, 250, 25);
        nameField.setFont(inputFont);
        add(nameField);

        JLabel amountLabel = new JLabel("Loan Amount:");
        amountLabel.setBounds(50, 160, 100, 25);
        amountLabel.setFont(labelFont);
        add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(160, 160, 250, 25);
        amountField.setFont(inputFont);
        add(amountField);

        JLabel rateLabel = new JLabel("Interest Rate (%):");
        rateLabel.setBounds(50, 200, 150, 25);
        rateLabel.setFont(labelFont);
        add(rateLabel);

        rateField = new JTextField();
        rateField.setBounds(200, 200, 210, 25);
        rateField.setFont(inputFont);
        add(rateField);

        JLabel timeLabel = new JLabel("Time (Years):");
        timeLabel.setBounds(50, 240, 150, 25);
        timeLabel.setFont(labelFont);
        add(timeLabel);

        timeField = new JTextField();
        timeField.setBounds(200, 240, 210, 25);
        timeField.setFont(inputFont);
        add(timeField);

        JButton calculateBtn = new JButton("Calculate");
        calculateBtn.setBounds(160, 280, 150, 35);
        calculateBtn.setFont(buttonFont);
        calculateBtn.setBackground(new Color(119, 136, 153));
        calculateBtn.setForeground(Color.BLACK);
        calculateBtn.setFocusPainted(false);
        calculateBtn.addActionListener(e -> calculateLoan());
        add(calculateBtn);

        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setBounds(50, 320, 380, 25);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultLabel.setForeground(new Color(60, 60, 60));
        add(resultLabel);

        setVisible(true);
    }

    private void calculateLoan() {
        try {
            double principal = Double.parseDouble(amountField.getText());
            double rate = Double.parseDouble(rateField.getText());
            double time = Double.parseDouble(timeField.getText());
            double interest = (principal * rate * time) / 100;
            double total = principal + interest;

            DecimalFormat df = new DecimalFormat("#.##");
            resultLabel.setText("Total Payable Amount: $" + df.format(total));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric inputs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(IndividualLoanGUI::new);
    }
}

