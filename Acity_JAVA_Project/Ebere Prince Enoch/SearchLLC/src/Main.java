import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private JFrame frame;
    private JTextField keywordField;
    private JTextArea resultArea;
    private JButton searchButton, prevButton, nextButton, clearButton, historyButton, darkModeButton;
    private JLabel statusLabel;

    private ArrayList<String> lines;
    private ArrayList<String> results;
    private int currentIndex = 0;
    private boolean darkMode = false;

    private final KeywordSearcher searcher = new KeywordSearcher();
    private final FileHandler fileHandler = new FileHandler();
    private final DatabaseHandler dbHandler = new DatabaseHandler();

    public Main() {
        lines = fileHandler.readFile("stuff.txt");
        results = new ArrayList<>();
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Keyword Searcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 420);
        frame.setLayout(new BorderLayout());

        // ===== Top Panel: Keyword Input =====
        keywordField = new JTextField(20);
        searchButton = new JButton("Search");
        clearButton = new JButton("Clear");
        historyButton = new JButton("Search History");

        darkModeButton = new JButton("🌙");
        darkModeButton.setPreferredSize(new Dimension(60, 25)); // 2/5 typical button width

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Keyword:"));
        topPanel.add(keywordField);
        topPanel.add(searchButton);
        topPanel.add(clearButton);
        topPanel.add(historyButton);
        topPanel.add(darkModeButton);
        frame.add(topPanel, BorderLayout.NORTH);

        // ===== Center Panel: Result Text Area =====
        resultArea = new JTextArea(5, 50);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(resultArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(500, 100));
        frame.add(scrollPane, BorderLayout.CENTER);

        // ===== Bottom Panel: Navigation Buttons and Status =====
        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");
        statusLabel = new JLabel("Enter a keyword to begin.");

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        navPanel.add(prevButton);
        navPanel.add(nextButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(navPanel, BorderLayout.NORTH);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // ===== Add Listeners =====
        addListeners();

        frame.setVisible(true);
    }

    private void addListeners() {
        searchButton.addActionListener(e -> performSearch());

        clearButton.addActionListener(e -> {
            keywordField.setText("");
            resultArea.setText("");
            statusLabel.setText("Cleared.");
            results.clear();
            currentIndex = 0;
        });

        historyButton.addActionListener(e -> showSearchHistory());

        nextButton.addActionListener(e -> {
            if (currentIndex < results.size() - 1) {
                currentIndex++;
                showResult();
                statusLabel.setText("Result " + (currentIndex + 1) + " of " + results.size());
            } else {
                statusLabel.setText("End of results.");
            }
        });

        prevButton.addActionListener(e -> {
            if (currentIndex > 0) {
                currentIndex--;
                showResult();
                statusLabel.setText("Result " + (currentIndex + 1) + " of " + results.size());
            } else {
                statusLabel.setText("Start of results.");
            }
        });

        darkModeButton.addActionListener(e -> toggleDarkMode());
    }

    private void performSearch() {
        String keyword = keywordField.getText().trim();
        if (keyword.isEmpty()) {
            statusLabel.setText("Keyword cannot be empty.");
            return;
        }

        results = searcher.search(lines, keyword);
        dbHandler.saveKeyword(keyword, results.size());
        currentIndex = 0;

        if (results.isEmpty()) {
            resultArea.setText("");
            statusLabel.setText("No matches found.");
        } else {
            showResult();
            statusLabel.setText("Found " + results.size() + " match(es). Showing result 1.");
        }
    }

    private void showResult() {
        if (!results.isEmpty() && currentIndex >= 0 && currentIndex < results.size()) {
            resultArea.setText(results.get(currentIndex));
        }
    }

    private void showSearchHistory() {
        List<String> history = dbHandler.searchKeywordHistory("");
        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No search history found.");
        } else {
            StringBuilder sb = new StringBuilder("Search History:\n\n");
            for (String entry : history) {
                sb.append(entry).append("\n");
            }
            JOptionPane.showMessageDialog(frame, sb.toString(), "Keyword History", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void toggleDarkMode() {
        darkMode = !darkMode;

        // Dark mode colors
        Color bg = darkMode ? new Color(30, 30, 47) : UIManager.getColor("Panel.background");
        Color text = darkMode ? new Color(224, 224, 224) : Color.BLACK;
        Color fieldBg = darkMode ? new Color(44, 44, 62) : Color.WHITE;
        Color buttonBg = darkMode ? new Color(58, 58, 77) : null;
        Color bottomPanelBg = darkMode ? new Color(44, 44, 62) : UIManager.getColor("Panel.background");

        // Apply colors to frame, panels, and buttons
        frame.getContentPane().setBackground(bg);
        for (Component comp : frame.getContentPane().getComponents()) {
            if (comp instanceof JPanel panel) {
                panel.setBackground(bg);
                for (Component c : panel.getComponents()) {
                    if (c instanceof JButton) {
                        c.setBackground(buttonBg);
                        c.setForeground(text);
                    } else if (c instanceof JLabel) {
                        c.setForeground(text);
                    }
                }
            }
        }

        // Apply specific colors for result area and keyword field
        keywordField.setBackground(fieldBg);
        keywordField.setForeground(text);
        resultArea.setBackground(fieldBg);
        resultArea.setForeground(text);
        statusLabel.setForeground(text);

        // Apply color to bottom panel
        Component[] bottomPanelComponents = ((JPanel) frame.getContentPane().getComponent(2)).getComponents();
        for (Component comp : bottomPanelComponents) {
            if (comp instanceof JPanel) {
                comp.setBackground(bottomPanelBg);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
