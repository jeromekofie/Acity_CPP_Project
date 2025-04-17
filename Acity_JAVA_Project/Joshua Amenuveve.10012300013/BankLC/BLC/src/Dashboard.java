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

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Dashboard - Bank Loan App");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10)); // Adjusted layout for cleaner spacing

        Color bg = new Color(45, 45, 48);  // Dark background
        Color cardColor = new Color(60, 63, 65); // Slightly lighter card background
        Color textColor = Color.WHITE;
        Color buttonHoverColor = new Color(80, 85, 88);  // Button hover effect

        getContentPane().setBackground(bg); // Set background color

        JLabel title = new JLabel("Choose Loan Type", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(textColor);
        add(title);

        // Individual Loan Button
        JButton individualBtn = new JButton("Individual Loan");
        individualBtn.setBackground(cardColor);
        individualBtn.setForeground(Color.BLACK);  // Button text is black
        individualBtn.setFocusPainted(false);
        individualBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        individualBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        individualBtn.setPreferredSize(new Dimension(200, 50)); // Set button size
        individualBtn.addActionListener(e -> {
            dispose();
            new IndividualLoanGUI();
        });
        
        // Hover effect for individual button
        individualBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                individualBtn.setBackground(buttonHoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                individualBtn.setBackground(cardColor);
            }
        });

        add(individualBtn);

        // Business Loan Button
        JButton businessBtn = new JButton("Business Loan");
        businessBtn.setBackground(cardColor);
        businessBtn.setForeground(Color.BLACK);  // Button text is black
        businessBtn.setFocusPainted(false);
        businessBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        businessBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        businessBtn.setPreferredSize(new Dimension(200, 50)); // Set button size
        businessBtn.addActionListener(e -> {
            dispose();
            new BusinessLoanGUI();
        });

        // Hover effect for business button
        businessBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                businessBtn.setBackground(buttonHoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                businessBtn.setBackground(cardColor);
            }
        });

        add(businessBtn);

        // Adding extra padding at the bottom for better layout
        JPanel paddingPanel = new JPanel();
        paddingPanel.setBackground(bg);
        add(paddingPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Dashboard();
    }
}