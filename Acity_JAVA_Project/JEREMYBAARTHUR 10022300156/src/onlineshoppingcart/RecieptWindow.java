package onlineshoppingcart;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class RecieptWindow {
    public RecieptWindow() {
        JFrame frame = new JFrame("Receipt");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        JLabel receiptLabel = new JLabel("Receipt");
        receiptLabel.setForeground(Color.LIGHT_GRAY);
        receiptLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(receiptLabel);

        JTextArea receiptArea = new JTextArea(10, 30);
        receiptArea.setEditable(false);
        receiptArea.setBackground(Color.DARK_GRAY);
        receiptArea.setForeground(Color.LIGHT_GRAY);
        receiptArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Here you can generate the receipt dynamically with cart details
        StringBuilder receiptContent = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#0.00");

        // Add some static data or dynamic cart data here
        receiptContent.append("Items purchased:\n");
        receiptContent.append("Item 1 - $20\n");
        receiptContent.append("Item 2 - $15\n");
        receiptContent.append("Total: $35.00");

        receiptArea.setText(receiptContent.toString());
        panel.add(new JScrollPane(receiptArea));

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> frame.dispose());
        panel.add(closeButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}
