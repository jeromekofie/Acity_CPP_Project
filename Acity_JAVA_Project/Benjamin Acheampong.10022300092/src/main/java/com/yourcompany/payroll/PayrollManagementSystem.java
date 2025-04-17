package com.yourcompany.payroll;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

// Employee classes remain unchanged
abstract class Employee {
    protected String name;
    protected int id;
    protected double baseSalary;

    public Employee(String name, int id, double baseSalary) {
        this.name = name;
        this.id = id;
        this.baseSalary = baseSalary;
    }

    public abstract double calculateSalary();
    public abstract String getEmployeeType();
    
    public String displayInfo() {
        return "Name: " + name + "\nID: " + id + "\nType: " + getEmployeeType();
    }
}

class SalariedEmployee extends Employee {
    public SalariedEmployee(String name, int id, double baseSalary) {
        super(name, id, baseSalary);
    }

    @Override public double calculateSalary() {
        double bonus = calculateBonus();
        double deductions = calculateDeductions();
        return baseSalary + bonus - deductions;
    }
    private double calculateBonus() { return baseSalary * 0.1; }
    private double calculateDeductions() { return baseSalary * 0.05; }
    @Override public String getEmployeeType() { return "Salaried Employee"; }
}

class HourlyEmployee extends Employee {
    private int hoursWorked;
    private double hourlyRate;

    public HourlyEmployee(String name, int id, double hourlyRate, int hoursWorked) {
        super(name, id, 0);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    @Override public double calculateSalary() {
        return (hoursWorked * hourlyRate) + calculateBonus() - calculateDeductions();
    }
    private double calculateBonus() { return (hoursWorked * hourlyRate) * 0.05; }
    private double calculateDeductions() { return (hoursWorked * hourlyRate) * 0.03; }
    @Override public String getEmployeeType() { return "Hourly Employee"; }
}

public class PayrollManagementSystem extends JFrame {
    private JPanel currentPanel;
    private CardLayout cardLayout;
    private String currentUser;
    private List<Employee> employees = new ArrayList<>();
    
    // UI Styling
    private Color darkOverlay = new Color(0, 0, 0, 160); // Reduced opacity
    private Color panelColor = new Color(60, 60, 60, 200);
    private Color accentColor = new Color(70, 130, 180);
    private Color textColor = Color.WHITE;
    private Color buttonTextColor = Color.BLACK;
    private Font appFont = new Font("Segoe UI", Font.PLAIN, 14);
    
    // MongoDB
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> employeeCollection;
    private MongoCollection<Document> salariedCollection;
    
    // Background image
    private Image backgroundImage;

    public PayrollManagementSystem() {
        // Load background image
        try {
            backgroundImage = new ImageIcon("C:\\Users\\kojob\\Documents\\PROGRAMMING\\Java\\End of Semester Project\\src\\main\\resources\\christian-wiediger-WkfDrhxDMC8-unsplash.jpg").getImage();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not load background image", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Frame setup
        setTitle("Payroll Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create layered pane for background
        JLayeredPane layeredPane = new JLayeredPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Draw background image scaled to fit
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    // Draw dark overlay with reduced opacity
                    g.setColor(darkOverlay);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        layeredPane.setLayout(new BorderLayout());

        // Main content panel
        cardLayout = new CardLayout();
        currentPanel = new JPanel(cardLayout);
        currentPanel.setOpaque(false);
        currentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Create all panels
        createMainMenuPanel();
        createLoginPanel();
        createAccountPanel();
        createEmployeeTypePanel();
        createSalariedEmployeePanel();
        createHourlyEmployeePanel();
        createResultsPanel();

        layeredPane.add(currentPanel, BorderLayout.CENTER);
        add(layeredPane);

        // MongoDB connection
        try {
            System.setProperty("com.mongodb.disableDnsSrv", "true");
            String connectionString = "mongodb+srv://kojoben29:Ost0UIZdvRIEDMRJ@cluster0.7ajsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
            mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase("PayrollManagementSystem");
            employeeCollection = database.getCollection("pms");
            salariedCollection = database.getCollection("salarypms");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to MongoDB", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // UI Component Factories
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(accentColor);
        button.setForeground(buttonTextColor);
        button.setFont(appFont.deriveFont(Font.BOLD, 14f));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor.brighter(), 1),
            BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Enhanced hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(accentColor.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 1),
                    BorderFactory.createEmptyBorder(12, 25, 12, 25)
                ));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(accentColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor.brighter(), 1),
                    BorderFactory.createEmptyBorder(12, 25, 12, 25)
                ));
            }
        });
        return button;
    }

    private JLabel createStyledLabel(String text, int size, boolean bold) {
        JLabel label = new JLabel(text);
        label.setForeground(textColor);
        label.setFont(appFont.deriveFont(bold ? Font.BOLD : Font.PLAIN, size));
        return label;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setBackground(new Color(70, 70, 70, 200));
        field.setForeground(textColor);
        field.setFont(appFont);
        field.setCaretColor(Color.WHITE); // White blinking cursor
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private JPanel createContentPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(panelColor.getRed(), panelColor.getGreen(), panelColor.getBlue(), 180)); // More transparent
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        return panel;
    }

    // Panel Creation Methods
    private void createMainMenuPanel() {
        JPanel panel = createContentPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(15, 10, 15, 10);
        
        JLabel title = createStyledLabel("PAYROLL MANAGEMENT SYSTEM", 28, true);
        title.setForeground(accentColor);
        panel.add(title, gbc);
        
        JLabel subtitle = createStyledLabel("Streamline Your Employee Compensation", 16, false);
        panel.add(subtitle, gbc);
        
        // Add some vertical space
        gbc.insets = new Insets(30, 10, 10, 10);
        panel.add(Box.createVerticalStrut(20), gbc);
        
        JButton createBtn = createStyledButton("CREATE ACCOUNT");
        createBtn.addActionListener(e -> showCreateAccount());
        panel.add(createBtn, gbc);
        
        JButton loginBtn = createStyledButton("LOGIN");
        loginBtn.addActionListener(e -> showLogin());
        panel.add(loginBtn, gbc);
        
        JButton exitBtn = createStyledButton("EXIT");
        exitBtn.addActionListener(e -> System.exit(0));
        panel.add(exitBtn, gbc);
        
        currentPanel.add(panel, "MainMenu");
    }

    private void createLoginPanel() {
        JPanel panel = createContentPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 5, 10, 5);
        
        JLabel title = createStyledLabel("LOGIN TO YOUR ACCOUNT", 22, true);
        title.setForeground(accentColor);
        panel.add(title, gbc);
        
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(createStyledLabel("Username:", 14, false), gbc);
        JTextField userField = createStyledTextField(20);
        panel.add(userField, gbc);
        
        panel.add(createStyledLabel("Password:", 14, false), gbc);
        JPasswordField passField = new JPasswordField(20);
        passField.setBackground(new Color(70, 70, 70, 200));
        passField.setForeground(textColor);
        passField.setFont(appFont);
        passField.setCaretColor(Color.WHITE); // White blinking cursor
        passField.setBorder(((JTextField)createStyledTextField(10)).getBorder());
        panel.add(passField, gbc);
        
        JButton loginBtn = createStyledButton("LOGIN");
        loginBtn.addActionListener(e -> {
            if (login(userField.getText(), new String(passField.getPassword()))) {
                currentUser = userField.getText();
                showEmployeeTypePanel();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(loginBtn, gbc);
        
        JButton backBtn = createStyledButton("BACK TO MAIN MENU");
        backBtn.addActionListener(e -> showMainMenu());
        panel.add(backBtn, gbc);
        
        currentPanel.add(panel, "Login");
    }

    private void createAccountPanel() {
        JPanel panel = createContentPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 5, 10, 5);
        
        JLabel title = createStyledLabel("CREATE NEW ACCOUNT", 22, true);
        title.setForeground(accentColor);
        panel.add(title, gbc);
        
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(createStyledLabel("Username:", 14, false), gbc);
        JTextField userField = createStyledTextField(20);
        panel.add(userField, gbc);
        
        panel.add(createStyledLabel("Password:", 14, false), gbc);
        JPasswordField passField = new JPasswordField(20);
        passField.setBackground(new Color(70, 70, 70, 200));
        passField.setForeground(textColor);
        passField.setFont(appFont);
        passField.setCaretColor(Color.WHITE);
        passField.setBorder(((JTextField)createStyledTextField(10)).getBorder());
        panel.add(passField, gbc);
        
        JButton createBtn = createStyledButton("CREATE ACCOUNT");
        createBtn.addActionListener(e -> {
            if (createAccount(userField.getText(), new String(passField.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Account created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Account creation failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(createBtn, gbc);
        
        JButton backBtn = createStyledButton("BACK TO MAIN MENU");
        backBtn.addActionListener(e -> showMainMenu());
        panel.add(backBtn, gbc);
        
        currentPanel.add(panel, "CreateAccount");
    }

    private void createEmployeeTypePanel() {
        JPanel panel = createContentPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(15, 10, 15, 10);
        
        JLabel welcome = createStyledLabel("Welcome, " + (currentUser != null ? currentUser : "User") + "!", 22, true);
        welcome.setForeground(accentColor);
        panel.add(welcome, gbc);
        
        JLabel prompt = createStyledLabel("Select Employee Type to Add:", 18, false);
        panel.add(prompt, gbc);
        
        // Add vertical space
        panel.add(Box.createVerticalStrut(30), gbc);
        
        JButton salariedBtn = createStyledButton("SALARIED EMPLOYEE");
        salariedBtn.setPreferredSize(new Dimension(250, 60));
        salariedBtn.addActionListener(e -> showSalariedEmployeePanel());
        panel.add(salariedBtn, gbc);
        
        JButton hourlyBtn = createStyledButton("HOURLY EMPLOYEE");
        hourlyBtn.setPreferredSize(new Dimension(250, 60));
        hourlyBtn.addActionListener(e -> showHourlyEmployeePanel());
        panel.add(hourlyBtn, gbc);
        
        JButton logoutBtn = createStyledButton("LOGOUT");
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            showMainMenu();
        });
        panel.add(logoutBtn, gbc);
        
        currentPanel.add(panel, "EmployeeType");
    }

    private void createSalariedEmployeePanel() {
        JPanel panel = createContentPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 5, 10, 5);
        
        JLabel title = createStyledLabel("SALARIED EMPLOYEE DETAILS", 22, true);
        title.setForeground(accentColor);
        panel.add(title, gbc);
        
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(createStyledLabel("Name:", 14, false), gbc);
        JTextField nameField = createStyledTextField(20);
        panel.add(nameField, gbc);
        
        panel.add(createStyledLabel("ID:", 14, false), gbc);
        JTextField idField = createStyledTextField(20);
        panel.add(idField, gbc);
        
        panel.add(createStyledLabel("Base Salary (GHS):", 14, false), gbc);
        JTextField salaryField = createStyledTextField(20);
        panel.add(salaryField, gbc);
        
        JButton calculateBtn = createStyledButton("CALCULATE SALARY");
        calculateBtn.addActionListener(e -> {
            try {
                Employee emp = new SalariedEmployee(
                    nameField.getText(), 
                    Integer.parseInt(idField.getText()), 
                    Double.parseDouble(salaryField.getText())
                );
                employees.add(emp);
                showResults(emp);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input values", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(calculateBtn, gbc);
        
        JButton backBtn = createStyledButton("BACK");
        backBtn.addActionListener(e -> showEmployeeTypePanel());
        panel.add(backBtn, gbc);
        
        currentPanel.add(panel, "SalariedEmployee");
    }

    private void createHourlyEmployeePanel() {
        JPanel panel = createContentPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 5, 10, 5);
        
        JLabel title = createStyledLabel("HOURLY EMPLOYEE DETAILS", 22, true);
        title.setForeground(accentColor);
        panel.add(title, gbc);
        
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(createStyledLabel("Name:", 14, false), gbc);
        JTextField nameField = createStyledTextField(20);
        panel.add(nameField, gbc);
        
        panel.add(createStyledLabel("ID:", 14, false), gbc);
        JTextField idField = createStyledTextField(20);
        panel.add(idField, gbc);
        
        panel.add(createStyledLabel("Hourly Rate (GHS):", 14, false), gbc);
        JTextField rateField = createStyledTextField(20);
        panel.add(rateField, gbc);
        
        panel.add(createStyledLabel("Hours Worked:", 14, false), gbc);
        JTextField hoursField = createStyledTextField(20);
        panel.add(hoursField, gbc);
        
        JButton calculateBtn = createStyledButton("CALCULATE SALARY");
        calculateBtn.addActionListener(e -> {
            try {
                Employee emp = new HourlyEmployee(
                    nameField.getText(),
                    Integer.parseInt(idField.getText()),
                    Double.parseDouble(rateField.getText()),
                    Integer.parseInt(hoursField.getText())
                );
                employees.add(emp);
                showResults(emp);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input values", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(calculateBtn, gbc);
        
        JButton backBtn = createStyledButton("BACK");
        backBtn.addActionListener(e -> showEmployeeTypePanel());
        panel.add(backBtn, gbc);
        
        currentPanel.add(panel, "HourlyEmployee");
    }

    private void createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create a styled text area with better appearance
        JTextArea resultsArea = new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(60, 60, 60, 220));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        resultsArea.setEditable(false);
        resultsArea.setFont(appFont.deriveFont(14f));
        resultsArea.setForeground(textColor);
        resultsArea.setCaretColor(textColor);
        resultsArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        resultsArea.setOpaque(false);
        
        // Create line border with accent color
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        // Button panel with improved layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setOpaque(false);
        
        JButton newBtn = createStyledButton("ADD NEW EMPLOYEE");
        newBtn.addActionListener(e -> showEmployeeTypePanel());
        
        JButton saveBtn = createStyledButton("SAVE DATA");
        saveBtn.addActionListener(e -> {
            boolean success = true;
            for (Employee emp : employees) {
                if (!savePayrollData1(emp)) {
                    success = false;
                }
            }
            if (success) {
                JOptionPane.showMessageDialog(this, "Data saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Some data could not be saved", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        JButton logoutBtn = createStyledButton("LOGOUT");
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            employees.clear();
            showMainMenu();
        });
        
        buttonPanel.add(newBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(logoutBtn);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        currentPanel.add(panel, "Results");
    }

    // Navigation methods
    private void showMainMenu() { cardLayout.show(currentPanel, "MainMenu"); }
    private void showLogin() { cardLayout.show(currentPanel, "Login"); }
    private void showCreateAccount() { cardLayout.show(currentPanel, "CreateAccount"); }
    private void showEmployeeTypePanel() { cardLayout.show(currentPanel, "EmployeeType"); }
    private void showSalariedEmployeePanel() { cardLayout.show(currentPanel, "SalariedEmployee"); }
    private void showHourlyEmployeePanel() { cardLayout.show(currentPanel, "HourlyEmployee"); }
    private void showResults(Employee employee) {
        JTextArea resultsArea = (JTextArea) ((JScrollPane) ((JPanel) currentPanel.getComponent(
            currentPanel.getComponentCount() - 1)).getComponent(0)).getViewport().getView();
        
        // Enhanced results display with better formatting
        resultsArea.setText("\n=== EMPLOYEE INFORMATION ===\n\n");
        resultsArea.append(employee.displayInfo() + "\n\n");
        resultsArea.append("=== SALARY CALCULATION ===\n\n");
        resultsArea.append(String.format("  Total Salary: GHS %,.2f\n\n", employee.calculateSalary()));
        
        resultsArea.append("=== ALL EMPLOYEES ===\n\n");
        for (Employee emp : employees) {
            resultsArea.append(emp.displayInfo() + "\n");
            resultsArea.append(String.format("  Salary: GHS %,.2f\n\n", emp.calculateSalary()));
        }
        
        cardLayout.show(currentPanel, "Results");
    }

    // File operations (unchanged)
    private boolean createAccount(String username, String password) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("data1.txt", true))) {
            writer.println(username + ":" + password);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    private boolean login(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data1.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(":");
                if (credentials.length == 2 && credentials[0].trim().equals(username) && credentials[1].trim().equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }
    
    private boolean savePayrollData1(Employee employee) {
        try {
            Document doc = new Document("name", employee.name)
                .append("id", employee.id)
                .append("type", employee.getEmployeeType())
                .append("salary", employee.calculateSalary());
            
            if (employee.getEmployeeType().equals("Hourly Employee")) {
                employeeCollection.insertOne(doc);
            } else {
                salariedCollection.insertOne(doc);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("OptionPane.background", new Color(45, 45, 45));
            UIManager.put("Panel.background", new Color(45, 45, 45));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(70, 130, 180));
            UIManager.put("Button.foreground", Color.BLACK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            PayrollManagementSystem system = new PayrollManagementSystem();
            system.setVisible(true);
        });
    }
}
