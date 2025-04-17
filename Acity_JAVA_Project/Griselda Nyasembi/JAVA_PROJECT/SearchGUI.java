import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class SearchGUI extends JFrame {
    private FileHandler fileHandler = new FileHandler();
    private DatabaseHandler dbHandler = new DatabaseHandler();
    private JTextField fileField, keywordField;
    private JTextArea resultArea;
    private BufferedImage backgroundImg;

    private List<String> results = new ArrayList<>();
    private int currentIndex = 0;

    public SearchGUI() {
        try {
            BufferedImage original = ImageIO.read(new File("background.jpg"));
            backgroundImg = blurImage(original, 23);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setUndecorated(true);
        setSize(600, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(new BackgroundPanel());
        getContentPane().setLayout(new BorderLayout());

        Color blushPink = new Color(243, 200, 202);
        Color softWhite = new Color(255, 255, 255, 220);

        JLabel title = new JLabel("Text Search Tool", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.DARK_GRAY);
        title.setBorder(new EmptyBorder(12, 0, 10, 0));

        fileField = new JTextField(20);
        keywordField = new JTextField(20);
        resultArea = new JTextArea(6, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resultArea.setForeground(Color.DARK_GRAY);
        resultArea.setBackground(softWhite);
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new LineBorder(blushPink, 2, true));

        JButton browseBtn = new RoundedButton("Browse", blushPink);
        JButton searchBtn = new RoundedButton("🔍 Search", blushPink);
        JButton saveBtn = new RoundedButton("💾 Save", blushPink);
        JButton nextBtn = new RoundedButton("Next ▶", blushPink);
        JButton prevBtn = new RoundedButton("◀ Prev", blushPink);

        browseBtn.addActionListener(e -> chooseFile());
        searchBtn.addActionListener(e -> searchKeyword());
        saveBtn.addActionListener(e -> saveResults());
        nextBtn.addActionListener(e -> showNextResult());
        prevBtn.addActionListener(e -> showPreviousResult());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel fileLabel = new JLabel("File:");
        JLabel keywordLabel = new JLabel("Keyword:");
        fileLabel.setForeground(Color.DARK_GRAY);
        keywordLabel.setForeground(Color.DARK_GRAY);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(fileLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(fileField, gbc);
        gbc.gridx = 2;
        formPanel.add(browseBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(keywordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(keywordField, gbc);
        gbc.gridx = 2;
        formPanel.add(searchBtn, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(saveBtn, gbc);

        JPanel navPanel = new JPanel();
        navPanel.setOpaque(false);
        navPanel.add(prevBtn);
        navPanel.add(nextBtn);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(20, 20, 10, 20));
        center.add(formPanel, BorderLayout.NORTH);
        center.add(scrollPane, BorderLayout.CENTER);
        center.add(navPanel, BorderLayout.SOUTH);

        add(title, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    private void showCurrentResult() {
        if (results.isEmpty()) {
            resultArea.setText("No matches found.");
        } else {
            resultArea.setText(results.get(currentIndex));
        }
    }

    private void showNextResult() {
        if (!results.isEmpty() && currentIndex < results.size() - 1) {
            currentIndex++;
            showCurrentResult();
        }
    }

    private void showPreviousResult() {
        if (!results.isEmpty() && currentIndex > 0) {
            currentIndex--;
            showCurrentResult();
        }
    }

    private BufferedImage blurImage(BufferedImage image, int radius) {
        float[] matrix = new float[radius * radius];
        float value = 1.0f / (radius * radius);
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = value;
        }
        BufferedImageOp op = new ConvolveOp(new Kernel(radius, radius, matrix), ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            if (!selectedFile.getName().endsWith(".txt") &&
                !selectedFile.getName().endsWith(".log") &&
                !selectedFile.getName().endsWith(".csv")) {
                JOptionPane.showMessageDialog(null, "Please select a valid text file (.txt, .log, .csv).");
                return;
            }
            fileField.setText(selectedFile.getAbsolutePath());
            fileHandler.setFilename(selectedFile.getAbsolutePath());
        }
    }

    private void searchKeyword() {
        String keyword = keywordField.getText();
        if (fileHandler.getFilename() == null || keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide both file and keyword.");
            return;
        }
        List<String> lines = fileHandler.readFile();
        fileHandler.search(lines, keyword);
        results = fileHandler.getResults();
        currentIndex = 0;
        showCurrentResult();
    }

    private void saveResults() {
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No results to save.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        int option = chooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            fileHandler.saveToFile(results, file.getAbsolutePath());
            dbHandler.insertSearch(fileHandler.getFilename(), keywordField.getText(), results);
            JOptionPane.showMessageDialog(this, "Results saved to " + file.getName() + " and database.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SearchGUI().setVisible(true));
    }

    class BackgroundPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImg != null) {
                g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}

class RoundedButton extends JButton {
    public RoundedButton(String text, Color bgColor) {
        super(text);
        setBackground(bgColor);
        setForeground(Color.DARK_GRAY);
        setFocusPainted(false);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setBorder(new RoundedBorder(12));
    }
}

class RoundedBorder implements Border {
    private int radius;
    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(radius, radius, radius, radius);
    }

    public boolean isBorderOpaque() {
        return false;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(new Color(200, 200, 200, 80));
        g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
}

class FileHandler {
    private String filename;
    private java.util.List<String> results = new java.util.ArrayList<>();

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public java.util.List<String> readFile() {
        java.util.List<String> lines = new java.util.ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null)
                lines.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public void search(java.util.List<String> lines, String keyword) {
        results.clear();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).toLowerCase().contains(keyword.toLowerCase())) {
                results.add("Line " + (i + 1) + ": " + lines.get(i));
            }
        }
    }

    public java.util.List<String> getResults() {
        return results;
    }

    public void saveToFile(java.util.List<String> results, String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (String line : results)
                writer.write(line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class DatabaseHandler {
    private Connection conn;

    public DatabaseHandler() {
        try {
            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/searchdb", "root", "yourpassword");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertSearch(String filename, String keyword, java.util.List<String> results) {
        String sql = "INSERT INTO searches (filename, keyword, result_text) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (String result : results) {
                stmt.setString(1, filename);
                stmt.setString(2, keyword);
                stmt.setString(3, result);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
