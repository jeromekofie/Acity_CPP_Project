import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FinderApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel, loginPanel, searchPanel;
    private JTextField usernameField, searchField, wordField, defField;
    private JPasswordField passwordField;
    private JTextArea resultArea;
    private JTable table;
    private DefaultTableModel model;
    private Connection conn;

    public FinderApp() {
        setTitle("FINDER - Search Tool");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        connectToMySQL();
        createDatabaseAndTable();

        initComponents();
    }

    private void connectToMySQL() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/?serverTimezone=UTC", "root", "Kekeli.10");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection Failed: " + e.getMessage());
        }
    }

    private void createDatabaseAndTable() {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS finderdb");
            stmt.executeUpdate("USE finderdb");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS words (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "word VARCHAR(255)," +
                    "definition TEXT)");

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM words");
            if (rs.next() && rs.getInt(1) == 0) {
                String[][] defaultWords = {
                        {"Algorithm", "A step-by-step procedure for solving a problem."},
                        {"Variable", "A container for storing data values."},
                        {"Object", "An instance of a class in OOP."},
                        {"Database", "An organized collection of structured information."},
                        {"Encapsulation", "Wrapping data and code together as a single unit."},
                        {"Inheritance", "Mechanism where a class inherits from another."},
                        {"Polymorphism", "Ability to take many forms."},
                        {"Abstraction", "Hiding internal details and showing functionality only."},
                        {"Function", "A block of code that performs a task."},
                        {"Class", "A blueprint for creating objects."}
                };
                PreparedStatement ps = conn.prepareStatement("INSERT INTO words (word, definition) VALUES (?, ?)");
                for (String[] pair : defaultWords) {
                    ps.setString(1, pair[0]);
                    ps.setString(2, pair[1]);
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        mainPanel = new JPanel(cardLayout = new CardLayout());

        // Login Panel
        loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(new Color(30, 30, 30));
        JLabel title = new JLabel("FINDER Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBounds(250, 50, 300, 40);
        loginPanel.add(title);

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        usernameField.setBounds(300, 150, 200, 30);
        passwordField.setBounds(300, 200, 200, 30);
        loginPanel.add(new JLabelBuilder("Username:", 200, 150, loginPanel));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabelBuilder("Password:", 200, 200, loginPanel));
        loginPanel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(350, 260, 100, 30);
        loginBtn.addActionListener(e -> login());
        loginPanel.add(loginBtn);

        // Search Panel
        searchPanel = new JPanel();
        searchPanel.setLayout(null);
        searchPanel.setBackground(new Color(20, 20, 20));

        searchField = new JTextField();
        searchField.setBounds(50, 30, 500, 30);
        searchPanel.add(searchField);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBounds(570, 30, 100, 30);
        searchBtn.addActionListener(e -> searchWord());
        searchPanel.add(searchBtn);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setBackground(Color.BLACK);
        resultArea.setForeground(Color.GREEN);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBounds(50, 80, 700, 150);
        searchPanel.add(scrollPane);

        wordField = new JTextField();
        defField = new JTextField();
        wordField.setBounds(50, 250, 200, 30);
        defField.setBounds(270, 250, 400, 30);
        searchPanel.add(wordField);
        searchPanel.add(defField);

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        addBtn.setBounds(50, 300, 100, 30);
        updateBtn.setBounds(170, 300, 100, 30);
        deleteBtn.setBounds(290, 300, 100, 30);
        searchPanel.add(addBtn);
        searchPanel.add(updateBtn);
        searchPanel.add(deleteBtn);

        // Table
        model = new DefaultTableModel(new String[]{"ID", "Word", "Definition"}, 0);
        table = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBounds(50, 350, 700, 180);
        searchPanel.add(tableScroll);

        loadTableData();

        // Button Actions
        addBtn.addActionListener(e -> addWord());
        updateBtn.addActionListener(e -> updateWord());
        deleteBtn.addActionListener(e -> deleteWord());
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                wordField.setText(model.getValueAt(row, 1).toString());
                defField.setText(model.getValueAt(row, 2).toString());
            }
        });

        // Add panels to main panel
        mainPanel.add(loginPanel, "login");
        mainPanel.add(searchPanel, "search");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }

    private void login() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        if (user.equals("admin") && pass.equals("admin")) {
            cardLayout.show(mainPanel, "search");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect credentials");
        }
    }

    private void searchWord() {
        String keyword = searchField.getText().trim();
        resultArea.setText("");
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("USE finderdb");
            ResultSet rs = stmt.executeQuery("SELECT * FROM words WHERE word LIKE '%" + keyword + "%' OR definition LIKE '%" + keyword + "%'");
            int line = 1;
            while (rs.next()) {
                resultArea.append("Line " + (line++) + ": " + rs.getString("word") + " - " + rs.getString("definition") + "\n");
            }
            if (line == 1) resultArea.setText("No matches found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTableData() {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("USE finderdb");
            ResultSet rs = stmt.executeQuery("SELECT * FROM words");
            model.setRowCount(0);
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("word"));
                row.add(rs.getString("definition"));
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addWord() {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO finderdb.words (word, definition) VALUES (?, ?)");
            ps.setString(1, wordField.getText());
            ps.setString(2, defField.getText());
            ps.executeUpdate();
            loadTableData();
            wordField.setText("");
            defField.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateWord() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        try {
            int id = (int) model.getValueAt(row, 0);
            PreparedStatement ps = conn.prepareStatement("UPDATE finderdb.words SET word=?, definition=? WHERE id=?");
            ps.setString(1, wordField.getText());
            ps.setString(2, defField.getText());
            ps.setInt(3, id);
            ps.executeUpdate();
            loadTableData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteWord() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        try {
            int id = (int) model.getValueAt(row, 0);
            PreparedStatement ps = conn.prepareStatement("DELETE FROM finderdb.words WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            loadTableData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class JLabelBuilder extends JLabel {
        public JLabelBuilder(String text, int x, int y, JPanel panel) {
            super(text);
            setForeground(Color.WHITE);
            setBounds(x, y, 100, 30);
            panel.add(this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FinderApp().setVisible(true));
    }
}
