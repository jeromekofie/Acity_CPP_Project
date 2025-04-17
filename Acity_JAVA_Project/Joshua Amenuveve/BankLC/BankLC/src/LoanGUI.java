import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.sql.*;
import javax.swing.table.JTableHeader;

public class LoanGUI extends JFrame {
    JTextField idField, principalField, rateField, timeField;
    JTextArea resultArea;
    JTable loanTable;
    DefaultTableModel tableModel;

    public LoanGUI() {
        setTitle("Bank Loan Calculator");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Colors and fonts
        Color backgroundColor = darkenColor(new Color(30, 30, 30), 0.2f);
        Color textColor = darkenColor(Color.WHITE, 0.2f);
        Color accentColor = new Color(180, 60, 60);
        Color fieldColor = darkenColor(new Color(30, 30, 30), 0.2f);
        Color fieldUnderlineColor = new Color(150, 150, 150);
        Font font = new Font("Serif", Font.PLAIN, 16);

        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("TextArea.font", font);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Serif", Font.BOLD, 16));

        // === Loan Form Tab ===
        JPanel loanFormPanel = new JPanel(new BorderLayout(15, 15));
        loanFormPanel.setBackground(backgroundColor);
        loanFormPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Bank Loan Calculator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        titleLabel.setForeground(accentColor);
        loanFormPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(backgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idField = createUnderlinedField(fieldColor, textColor, fieldUnderlineColor);
        principalField = createUnderlinedField(fieldColor, textColor, fieldUnderlineColor);
        rateField = createUnderlinedField(fieldColor, textColor, fieldUnderlineColor);
        timeField = createUnderlinedField(fieldColor, textColor, fieldUnderlineColor);

        addField(formPanel, gbc, 0, "ID:", idField, textColor);
        addField(formPanel, gbc, 1, "Principal ($):", principalField, textColor);
        addField(formPanel, gbc, 2, "Rate (%):", rateField, textColor);
        addField(formPanel, gbc, 3, "Time (years):", timeField, textColor);
        loanFormPanel.add(formPanel, BorderLayout.CENTER);

        resultArea = new JTextArea(4, 40);
        resultArea.setEditable(false);
        resultArea.setBackground(fieldColor);
        resultArea.setForeground(textColor);
        resultArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "Result"));
        JScrollPane scrollPane = new JScrollPane(resultArea);

        JButton calcBtn = styleButton("Calculate", accentColor, textColor);
        JButton saveBtn = styleButton("Save", accentColor, textColor);
        JButton readBtn = styleButton("Read", accentColor, textColor);
        JButton updateBtn = styleButton("Update", accentColor, textColor);
        JButton deleteBtn = styleButton("Delete", accentColor, textColor);
        JButton newBtn = styleButton("New", accentColor, textColor);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(calcBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(readBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(newBtn);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(backgroundColor);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        loanFormPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Event Listeners
        calcBtn.addActionListener(e -> calculate());
        saveBtn.addActionListener(e -> createLoan());
        readBtn.addActionListener(e -> readLoan());
        updateBtn.addActionListener(e -> updateLoan());
        deleteBtn.addActionListener(e -> deleteLoan());
        newBtn.addActionListener(e -> clearFields());

        // === All Loans Tab ===
        // === All Loans Tab ===
        JPanel allLoansPanel = new JPanel(new BorderLayout(10, 10));
        allLoansPanel.setBackground(backgroundColor);
        allLoansPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        tableModel = new DefaultTableModel(new String[]{"ID", "Principal", "Rate", "Time"}, 0);
        loanTable = new JTable(tableModel);
        loanTable.setFont(font);
        loanTable.setRowHeight(24);
        loanTable.setBackground(fieldColor);
        loanTable.setForeground(textColor);
        loanTable.setGridColor(accentColor);
        loanTable.setSelectionBackground(accentColor);
        loanTable.setSelectionForeground(Color.WHITE);

        // Customize table header
        JTableHeader header = loanTable.getTableHeader();
        header.setBackground(accentColor);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Serif", Font.BOLD, 16));

        JScrollPane tableScroll = new JScrollPane(loanTable);
        tableScroll.getViewport().setBackground(backgroundColor);  // match scroll pane background
        allLoansPanel.add(tableScroll, BorderLayout.CENTER);

        // Refresh Button
        JButton refreshBtn = styleButton("Refresh", accentColor, textColor);
        refreshBtn.addActionListener(e -> loadAllLoans());

        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshPanel.setBackground(backgroundColor);
        refreshPanel.add(refreshBtn);

        allLoansPanel.add(refreshPanel, BorderLayout.SOUTH);

        // Tabs
        tabbedPane.addTab("Loan Form", loanFormPanel);
        tabbedPane.addTab("All Loans", allLoansPanel);
        add(tabbedPane);

        // Load data on tab switch
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) {
                loadAllLoans();
            }
        });

        setVisible(true);
    }

    private JTextField createUnderlinedField(Color bg, Color fg, Color underline) {
        JTextField field = new JTextField(12);
        field.setBackground(bg);
        field.setForeground(fg);
        field.setCaretColor(fg);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, underline));
        return field;
    }

    private JButton styleButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int y, String labelText, JTextField field, Color fg) {
        gbc.gridx = 0;
        gbc.gridy = y;
        JLabel label = new JLabel(labelText);
        label.setForeground(fg);
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void calculate() {
        try {
            double principal = Double.parseDouble(principalField.getText());
            double rate = Double.parseDouble(rateField.getText());
            int time = Integer.parseInt(timeField.getText());
            Loan loan = new Loan(0, principal, rate, time);
            double payment = loan.calculateMonthlyPayment();
            resultArea.setText(String.format("Monthly Payment: $%.2f", payment));
        } catch (NumberFormatException e) {
            resultArea.setText("Please enter valid numbers.");
        }
    }

    private void createLoan() {
        try (Connection conn = DB.getConnection()) {
            String sql = "INSERT INTO loans VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(idField.getText()));
            ps.setDouble(2, Double.parseDouble(principalField.getText()));
            ps.setDouble(3, Double.parseDouble(rateField.getText()));
            ps.setInt(4, Integer.parseInt(timeField.getText()));
            ps.executeUpdate();
            saveToFile();
            resultArea.setText("Saved to database and file.");
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void readLoan() {
        try (Connection conn = DB.getConnection()) {
            String sql = "SELECT * FROM loans WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(idField.getText()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                principalField.setText(rs.getString("principal"));
                rateField.setText(rs.getString("rate"));
                timeField.setText(rs.getString("time"));
                calculate();
            } else {
                resultArea.setText("Loan not found.");
            }
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void updateLoan() {
        try (Connection conn = DB.getConnection()) {
            String sql = "UPDATE loans SET principal=?, rate=?, time=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, Double.parseDouble(principalField.getText()));
            ps.setDouble(2, Double.parseDouble(rateField.getText()));
            ps.setInt(3, Integer.parseInt(timeField.getText()));
            ps.setInt(4, Integer.parseInt(idField.getText()));
            ps.executeUpdate();
            resultArea.setText("Updated loan.");
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void deleteLoan() {
        try (Connection conn = DB.getConnection()) {
            String sql = "DELETE FROM loans WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(idField.getText()));
            ps.executeUpdate();
            resultArea.setText("Deleted loan.");
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void saveToFile() {
        try (FileWriter fw = new FileWriter("loan_file.txt", true)) {
            fw.write(String.format("ID: %s, Principal: $%s, Rate: %s%%, Time: %s years\n",
                    idField.getText(), principalField.getText(),
                    rateField.getText(), timeField.getText()));
        } catch (Exception e) {
            resultArea.setText("File write error.");
        }
    }

    private void clearFields() {
        idField.setText("");
        principalField.setText("");
        rateField.setText("");
        timeField.setText("");
        resultArea.setText("");
    }

    private void loadAllLoans() {
        try (Connection conn = DB.getConnection()) {
            tableModel.setRowCount(0); // clear table
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM loans");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getDouble("principal"),
                        rs.getDouble("rate"),
                        rs.getInt("time")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading loans: " + e.getMessage());
        }
    }

    private Color darkenColor(Color color, float percent) {
        int r = (int)(color.getRed() * (1 - percent));
        int g = (int)(color.getGreen() * (1 - percent));
        int b = (int)(color.getBlue() * (1 - percent));
        return new Color(Math.max(r, 0), Math.max(g, 0), Math.max(b, 0));
    }

    public static void main(String[] args) {
        new LoginGUI(); // Entry point
        // new LoanGUI(); // Uncomment for direct access
    }
}