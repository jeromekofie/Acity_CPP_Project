import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FinderGUI extends JFrame {

    private JTextField keywordField;
    private JTextArea resultArea;
    private JButton searchBtn, nextBtn, prevBtn;
    private JCheckBox caseSensitiveBox;
    private ArrayList<String> results = new ArrayList<>();
    private int currentIndex = 0;

    private DatabaseHandler dbHandler = new DatabaseHandler();

    public FinderGUI() {
        setTitle("Finder - SQL Search Tool");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // HEADER PANEL
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(58, 123, 213));
        JLabel title = new JLabel("Student Finder");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.blue);
        headerPanel.add(title);

        // SEARCH PANEL
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBackground(new Color(230, 230, 250)); // Lavender

        keywordField = new JTextField(20);
        searchBtn = new JButton("Search");
        caseSensitiveBox = new JCheckBox("Case Sensitive");

        searchBtn.setBackground(new Color(60, 179, 113)); // MediumSeaGreen
        searchBtn.setForeground(Color.blue);
        searchBtn.setFocusPainted(false);

        searchPanel.add(new JLabel("Keyword:"));
        searchPanel.add(keywordField);
        searchPanel.add(searchBtn);
        searchPanel.add(caseSensitiveBox);

        // RESULT AREA
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane resultScroll = new JScrollPane(resultArea);

        // NAVIGATION PANEL
        JPanel navPanel = new JPanel();
        navPanel.setBackground(new Color(245, 245, 245));

        prevBtn = new JButton("Previous");
        nextBtn = new JButton("Next");

        prevBtn.setBackground(new Color(255, 160, 122)); // LightSalmon
        nextBtn.setBackground(new Color(100, 149, 237)); // CornflowerBlue

        prevBtn.setForeground(Color.blue);
        nextBtn.setForeground(Color.blue);
        prevBtn.setFocusPainted(false);
        nextBtn.setFocusPainted(false);

        navPanel.add(prevBtn);
        navPanel.add(nextBtn);

        // Add all panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);
        add(resultScroll, BorderLayout.CENTER);
        add(navPanel, BorderLayout.SOUTH);

        // Add action listeners
        searchBtn.addActionListener(e -> performSearch());
        nextBtn.addActionListener(e -> showNext());
        prevBtn.addActionListener(e -> showPrevious());

        setVisible(true);
    }

    private void performSearch() {
        String keyword = keywordField.getText();
        boolean caseSensitive = caseSensitiveBox.isSelected();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a keyword.");
            return;
        }

        results = dbHandler.searchDatabase(keyword, caseSensitive);
        currentIndex = 0;

        if (results.isEmpty()) {
            resultArea.setText("No matches found.");
        } else {
            resultArea.setText(results.get(currentIndex));
        }
    }

    private void showNext() {
        if (currentIndex < results.size() - 1) {
            currentIndex++;
            resultArea.setText(results.get(currentIndex));
        } else {
            JOptionPane.showMessageDialog(this, "End of results.");
        }
    }

    private void showPrevious() {
        if (currentIndex > 0) {
            currentIndex--;
            resultArea.setText(results.get(currentIndex));
        } else {
            JOptionPane.showMessageDialog(this, "Start of results.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FinderGUI::new);
    }
}