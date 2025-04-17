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

public class BusinessLoanGUI extends JFrame {
    private JTextField bizNameField, incomeField, amountField, rateField, yearsField;
    private JLabel resultLabel;

    public BusinessLoanGUI() {
        setTitle("Business Loan Calculator");
        setSize(520, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(220, 225, 230));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        JLabel iconLabel = new JLabel(new ImageIcon("business_icon.png"));
        iconLabel.setBounds(200, 10, 100, 100);
        add(iconLabel);

        JLabel nameLabel = new JLabel("Business Name:");
        nameLabel.setBounds(50, 120, 120, 25);
        nameLabel.setFont(labelFont);
        add(nameLabel);

        bizNameField = new JTextField();
        bizNameField.setBounds(180, 120, 250, 25);
        bizNameField.setFont(inputFont);
        add(bizNameField);

        JLabel incomeLabel = new JLabel("Monthly Income:");
        incomeLabel.setBounds(50, 160, 120, 25);
        incomeLabel.setFont(labelFont);
        add(incomeLabel);

        incomeField = new JTextField();
        incomeField.setBounds(180, 160, 250, 25);
        incomeField.setFont(inputFont);
        add(incomeField);

        JLabel loanLabel = new JLabel("Loan Amount:");
        loanLabel.setBounds(50, 200, 120, 25);
        loanLabel.setFont(labelFont);
        add(loanLabel);

        amountField = new JTextField();
        amountField.setBounds(180, 200, 250, 25);
        amountField.setFont(inputFont);
        add(amountField);

        JLabel rateLabel = new JLabel("Interest Rate (%):");
        rateLabel.setBounds(50, 240, 130, 25);
        rateLabel.setFont(labelFont);
        add(rateLabel);

        rateField = new JTextField();
        rateField.setBounds(180, 240, 250, 25);
        rateField.setFont(inputFont);
        add(rateField);

        JLabel timeLabel = new JLabel("Time (Years):");
        timeLabel.setBounds(50, 280, 130, 25);
        timeLabel.setFont(labelFont);
        add(timeLabel);

        yearsField = new JTextField();
        yearsField.setBounds(180, 280, 250, 25);
        yearsField.setFont(inputFont);
        add(yearsField);

        JButton calcBtn = new JButton("Calculate");
        calcBtn.setBounds(180, 320, 150, 35);
        calcBtn.setFont(buttonFont);
        calcBtn.setBackground(new Color(105, 130, 153));
        calcBtn.setForeground(Color.BLACK);
        calcBtn.setFocusPainted(false);
        calcBtn.addActionListener(e -> calculateLoan());
        add(calcBtn);

        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setBounds(50, 360, 400, 25);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultLabel.setForeground(new Color(60, 60, 60));
        add(resultLabel);

        setVisible(true);
    }

    private void calculateLoan() {
        try {
            double income = Double.parseDouble(incomeField.getText());
            double principal = Double.parseDouble(amountField.getText());
            double rate = Double.parseDouble(rateField.getText());
            double time = Double.parseDouble(yearsField.getText());

            if (principal > income * 12) {
                resultLabel.setText("Loan exceeds annual income limit!");
                return;
            }

            double interest = (principal * rate * time) / 100;
            double total = principal + interest;

            DecimalFormat df = new DecimalFormat("#.##");
            resultLabel.setText("Total Payable: $" + df.format(total));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input values.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BusinessLoanGUI::new);
    }
}
