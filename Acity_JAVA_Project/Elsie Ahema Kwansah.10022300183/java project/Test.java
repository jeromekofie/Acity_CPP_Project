package BankingManagementSystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.Map;


public class Test extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Map<String, Account> accounts = new HashMap<>();
    private Account currentAccount = null;

    private JFrame loginFrame, mainFrame;
    private JTextField loginNameField, registerNameField;
    private JPasswordField loginPasswordField, registerPasswordField, registerPinField;

    // Constructor
    public Test() {
        showLoginRegisterWindow();
    }

    // Account class
    static class Account {
        String name;
        String password;
        String pin;
        double balance = 0.0;
        List<String> transactions = new ArrayList<>();

        Account(String name, String password, String pin) {
            this.name = name;
            this.password = password;
            this.pin = pin;
        }
    }

    private void showLoginRegisterWindow() {
        loginFrame = new JFrame("KAE Bank - Login/Register");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(500, 400);
        loginFrame.setLayout(new BorderLayout());

        ImageIcon backgroundIcon = new ImageIcon("background.jpg"); // Add your image here
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        loginFrame.add(backgroundLabel, BorderLayout.CENTER);
       
        JLabel title = new JLabel("KAE Bank", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(Color.RED);
        loginFrame.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        loginFrame.add(centerPanel, BorderLayout.CENTER);

        // Register Panel
        JPanel registerPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        registerPanel.setBorder(BorderFactory.createTitledBorder("Register"));
      

        registerPanel.add(new JLabel("Register Name:"));
        registerNameField = new JTextField();
        registerPanel.add(registerNameField);

        registerPanel.add(new JLabel("Register Password:"));
        registerPasswordField = new JPasswordField();
        registerPanel.add(registerPasswordField);

        registerPanel.add(new JLabel("Security Pin:"));
        registerPinField = new JPasswordField();
        registerPanel.add(registerPinField);

        JButton registerBtn = new JButton("Register");
        registerBtn.addActionListener(e -> register());
        registerPanel.add(registerBtn);

        centerPanel.add(registerPanel);

        // Login Panel
        JPanel loginPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        loginPanel.setBorder(BorderFactory.createTitledBorder("Login"));
     

        loginPanel.add(new JLabel("Login Name:"));
        loginNameField = new JTextField();
        loginPanel.add(loginNameField);

        loginPanel.add(new JLabel("Login Password:"));
        loginPasswordField = new JPasswordField();
        loginPanel.add(loginPasswordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> login());
        loginPanel.add(loginBtn);

        centerPanel.add(loginPanel);

        loginFrame.setVisible(true);
    }

    private void showMainWindow() {
        mainFrame = new JFrame("KAE Bank - Dashboard");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(500, 500);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(Color.DARK_GRAY);

        ImageIcon backgroundIcon = new ImageIcon("background.jpg"); // Add your image here
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        mainFrame.add(backgroundLabel, BorderLayout.CENTER);

        JLabel title = new JLabel("KAE Bank", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(Color.RED);
        mainFrame.add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBackground(Color.DARK_GRAY);

        JButton depositBtn = new JButton("💰 Deposit");
        depositBtn.addActionListener(e -> transactionDialog("Deposit"));
        panel.add(depositBtn);

        JButton withdrawBtn = new JButton("💸 Withdraw");
        withdrawBtn.addActionListener(e -> transactionDialog("Withdraw"));
        panel.add(withdrawBtn);

        JButton infoBtn = new JButton("📋 Account Info");
        infoBtn.addActionListener(e -> showAccountInfo());
        panel.add(infoBtn);

        JButton statementBtn = new JButton("🧾 Mini Statement");
        statementBtn.addActionListener(e -> showStatement());
        panel.add(statementBtn);

        JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(e -> System.exit(0));
        panel.add(exitBtn);

        mainFrame.add(panel, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }

    private void register() {
        String name = registerNameField.getText().trim();
        String password = new String(registerPasswordField.getPassword()).trim();
        String pin = new String(registerPinField.getPassword()).trim();

        if (name.isEmpty() || password.isEmpty() || pin.isEmpty()) {
            showMsg("All registration fields must be filled!");
            return;
        }

        if (accounts.containsKey(name)) {
            showMsg("Account already exists!");
        } else {
            Account account = new Account(name, password, pin);
            accounts.put(name, account);
            logToFile(name, "Account created");
            showMsg("Registration successful!");
        }
    }

    private void login() {
        String name = loginNameField.getText().trim();
        String password = new String(loginPasswordField.getPassword()).trim();

        Account acc = accounts.get(name);
        if (acc != null && acc.password.equals(password)) {
            currentAccount = acc;
            loginFrame.dispose();
            showMainWindow();
        } else {
            showMsg("Invalid credentials!");
        }
    }

    private void transactionDialog(String type) {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField amountField = new JTextField();
        JPasswordField pinField = new JPasswordField();

        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Security Pin:"));
        panel.add(pinField);

        int result = JOptionPane.showConfirmDialog(mainFrame, panel, type, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String amountStr = amountField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();

            if (!pin.equals(currentAccount.pin)) {
                showMsg("Invalid pin!");
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);
                if (type.equals("Deposit")) {
                    currentAccount.balance += amount;
                    String msg = "Deposited: $" + amount;
                    currentAccount.transactions.add(msg);
                    logToFile(currentAccount.name, msg);
                    showMsg(msg);
                } else if (type.equals("Withdraw")) {
                    if (amount <= currentAccount.balance) {
                        currentAccount.balance -= amount;
                        String msg = "Withdrew: $" + amount;
                        currentAccount.transactions.add(msg);
                        logToFile(currentAccount.name, msg);
                        showMsg(msg);
                    } else {
                        showMsg("Insufficient funds.");
                    }
                }
            } catch (NumberFormatException e) {
                showMsg("Enter a valid amount.");
            }
        }
    }

    private void showAccountInfo() {
        String pin = JOptionPane.showInputDialog("Enter Security Pin:");
        if (pin != null && pin.equals(currentAccount.pin)) {
            String info = "Bank: KAE Bank\nAccount: " + currentAccount.name +
                    "\nBalance: $" + currentAccount.balance +
                    "\nTransactions: (see statement)";
            logToFile(currentAccount.name, "Viewed Account Info");
            showMsg(info);
        } else {
            showMsg("Incorrect Pin!");
        }
    }

    private void showStatement() {
        String bankName = JOptionPane.showInputDialog("Enter Bank Name:");
        if (bankName != null && bankName.equalsIgnoreCase("KAE")) {
            StringBuilder sb = new StringBuilder("Recent Transactions:\n");
            for (String t : currentAccount.transactions) {
                sb.append(t).append("\n");
            }
            logToFile(currentAccount.name, "Viewed Statement");
            showMsg(sb.toString());
        } else {
            showMsg("Invalid Bank Name!");
        }
    }

    private void logToFile(String username, String message) {
        String filePath= "c:/Users/elsie/Documents/transaction_log.txt";
        try(BufferedWriter bw= new BufferedWriter(new FileWriter(filePath,true))){
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            bw.write(timestamp + " - " + username + ": " + message);
            bw.newLine();
    } catch (IOException e) {
        System.err.println("Logging failed: " + e.getMessage());
        e.printStackTrace();
    }
}


    private void showMsg(String msg) {
        JOptionPane.showMessageDialog(mainFrame != null ? mainFrame : loginFrame, msg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Test::new);
    }
}