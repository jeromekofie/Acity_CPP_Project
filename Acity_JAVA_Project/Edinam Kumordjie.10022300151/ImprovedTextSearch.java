import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ImprovedTextSearch extends JFrame {
    // GUI Components
    private JTextArea textArea;
    private JTextField searchField;
    private JButton openButton, searchButton, nextButton, prevButton;
    private JLabel statusLabel;
    private JCheckBox caseCheckBox, wholeWordCheckBox;

    // Search variables
    private List<Integer> matchPositions;
    private int currentMatchIndex = -1;
    private String fileContent = "";
    private List<String> fileLines;

    public ImprovedTextSearch() {
        setupWindow();
        createComponents();
        arrangeComponents();
        setupEventHandlers();
    }

    private void setupWindow() {
        setTitle("Improved Text Search");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void createComponents() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setTabSize(2);
        textArea.setForeground(Color.WHITE); // Set text color to white for better readability

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        searchField = new JTextField(20);
        openButton = new JButton("Open File");
        searchButton = new JButton("Search");
        nextButton = new JButton("Next");
        prevButton = new JButton("Previous");
        caseCheckBox = new JCheckBox("Case Sensitive");
        wholeWordCheckBox = new JCheckBox("Whole Word");
        statusLabel = new JLabel("Ready to search");

        nextButton.setEnabled(false);
        prevButton.setEnabled(false);
    }

    private void arrangeComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(openButton);

        JPanel middlePanel = new JPanel(new GridBagLayout());
        middlePanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        middlePanel.add(new JLabel("Search:"), gbc);

        gbc.gridx = 1;
        middlePanel.add(searchField, gbc);

        gbc.gridx = 2;
        middlePanel.add(searchButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        middlePanel.add(caseCheckBox, gbc);

        gbc.gridx = 1;
        middlePanel.add(wholeWordCheckBox, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(prevButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(statusLabel);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            private Image backgroundImage = new ImageIcon("C:\\Users\\edina\\Downloads\\mick-haupt-eQ2Z9ay9Wws-unsplash.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

                // Apply a dark overlay with more transparency
                g2d.setColor(new Color(0, 0, 0, 100)); // Semi-transparent black (increased opacity)
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(middlePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        openButton.addActionListener(e -> openFile());
        searchButton.addActionListener(e -> findMatches());
        nextButton.addActionListener(e -> showNextMatch());
        prevButton.addActionListener(e -> showPreviousMatch());
        searchField.addActionListener(e -> findMatches());
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                StringBuilder content = new StringBuilder();
                fileLines = new ArrayList<>();
                String line;

                while ((line = reader.readLine()) != null) {
                    fileLines.add(line);
                    content.append(line).append("\n");
                }
                reader.close();

                fileContent = content.toString();
                textArea.setText(fileContent);
                statusLabel.setText("Loaded: " + selectedFile.getName());
                resetSearch();
            } catch (IOException e) {
                showError("Error reading file: " + e.getMessage());
            }
        }
    }

    private void findMatches() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            showError("Please enter search text");
            return;
        }
        if (fileContent.isEmpty()) {
            showError("Please open a file first");
            return;
        }

        boolean caseSensitive = caseCheckBox.isSelected();
        boolean wholeWord = wholeWordCheckBox.isSelected();

        String searchIn = caseSensitive ? fileContent : fileContent.toLowerCase();
        String searchFor = caseSensitive ? searchText : searchText.toLowerCase();

        matchPositions = new ArrayList<>();
        int index = 0;

        while (index < searchIn.length()) {
            index = searchIn.indexOf(searchFor, index);
            if (index == -1) break;

            if (!wholeWord || isWholeWord(index, searchFor.length())) {
                matchPositions.add(index);
            }

            index += searchFor.length();
        }

        updateSearchResults();
    }

    private boolean isWholeWord(int position, int length) {
        if (position > 0) {
            char before = fileContent.charAt(position - 1);
            if (Character.isLetterOrDigit(before) || before == '_') {
                return false;
            }
        }

        if (position + length < fileContent.length()) {
            char after = fileContent.charAt(position + length);
            if (Character.isLetterOrDigit(after) || after == '_') {
                return false;
            }
        }

        return true;
    }

    private void updateSearchResults() {
        if (matchPositions.isEmpty()) {
            statusLabel.setText("No matches found");
            nextButton.setEnabled(false);
            prevButton.setEnabled(false);
        } else {
            statusLabel.setText("Found " + matchPositions.size() + " matches");
            currentMatchIndex = -1;
            nextButton.setEnabled(true);
            showNextMatch();
        }
    }

    private void showNextMatch() {
        if (matchPositions == null || matchPositions.isEmpty()) return;

        currentMatchIndex++;
        if (currentMatchIndex >= matchPositions.size()) {
            currentMatchIndex = 0;
        }

        highlightMatch();
        prevButton.setEnabled(true);

        if (currentMatchIndex == matchPositions.size() - 1) {
            nextButton.setEnabled(false);
        }
    }

    private void showPreviousMatch() {
        if (matchPositions == null || matchPositions.isEmpty()) return;

        currentMatchIndex--;
        if (currentMatchIndex < 0) {
            currentMatchIndex = matchPositions.size() - 1;
        }

        highlightMatch();
        nextButton.setEnabled(true);

        if (currentMatchIndex == 0) {
            prevButton.setEnabled(false);
        }
    }

    private void highlightMatch() {
        int position = matchPositions.get(currentMatchIndex);
        String searchText = searchField.getText();

        textArea.select(position, position + searchText.length());
        textArea.grabFocus();
        textArea.setCaretPosition(position);

        statusLabel.setText("Match " + (currentMatchIndex + 1) + " of " + matchPositions.size());
    }

    private void resetSearch() {
        matchPositions = null;
        currentMatchIndex = -1;
        nextButton.setEnabled(false);
        prevButton.setEnabled(false);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImprovedTextSearch searcher = new ImprovedTextSearch();
            searcher.setVisible(true);
        });
    }
}