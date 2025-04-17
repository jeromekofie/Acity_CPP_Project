package librarybooksearchtool2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class BookUI extends JFrame {
    private JTextField searchField, titleField, authorField, genreField, deleteIdField;
    private JTextArea resultArea;
    private JComboBox<String> searchType;
    private DefaultListModel<Book> searchResultsModel;
    private JList<Book> resultList;

    private BookDatabase db;

    public BookUI(BookDatabase db) {
        this.db = db;
        setTitle("📚 Library Book Search");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.decode("#F4F4F4"));

        Font font = new Font("Segoe UI", Font.PLAIN, 16);

        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBackground(Color.decode("#FFFFFF"));
        searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBackground(Color.decode("#FFFFFF"));

        searchField = new JTextField(20);
        searchField.setFont(font);
        searchType = new JComboBox<>(new String[]{"title", "author", "genre"});
        searchType.setFont(font);
        JButton searchBtn = new JButton("Search");
        searchBtn.setFont(font);

        inputPanel.add(new JLabel("Search:"));
        inputPanel.add(searchField);
        inputPanel.add(searchType);
        inputPanel.add(searchBtn);

        searchPanel.add(inputPanel, BorderLayout.NORTH);

        // Smart search results (JList)
        searchResultsModel = new DefaultListModel<>();
        resultList = new JList<>(searchResultsModel);
        resultList.setFont(font);
        JScrollPane resultScroll = new JScrollPane(resultList);
        searchPanel.add(resultScroll, BorderLayout.CENTER);

        // Static results area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(font);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Add/Delete panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(Color.decode("#FFFFFF"));

        titleField = new JTextField();
        authorField = new JTextField();
        genreField = new JTextField();
        deleteIdField = new JTextField();

        titleField.setFont(font);
        authorField.setFont(font);
        genreField.setFont(font);
        deleteIdField.setFont(font);

        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);
        formPanel.add(new JLabel("Genre:"));
        formPanel.add(genreField);
        formPanel.add(new JLabel("Delete Book ID:"));
        formPanel.add(deleteIdField);

        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(Color.decode("#FFFFFF"));
        JButton addBtn = new JButton("Add Book");
        JButton deleteBtn = new JButton("Delete Book");
        addBtn.setFont(font);
        deleteBtn.setFont(font);
        actionPanel.add(addBtn);
        actionPanel.add(deleteBtn);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.decode("#FFFFFF"));
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(actionPanel, BorderLayout.SOUTH);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Button click search
        searchBtn.addActionListener(e -> {
            try {
                String keyword = searchField.getText();
                String option = searchType.getSelectedItem().toString();
                List<Book> results = db.searchBooks(option, keyword);
                resultArea.setText("");
                for (Book b : results) {
                    resultArea.append(b.getId() + ": " + b.getTitle() + " by " + b.getAuthor() + " [" + b.getGenre() + "]\n");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Search failed: " + ex.getMessage());
            }
        });

        // Smart live search
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { searchLive(); }
            public void removeUpdate(DocumentEvent e) { searchLive(); }
            public void changedUpdate(DocumentEvent e) {}

            private void searchLive() {
                String keyword = searchField.getText().trim();
                if (keyword.length() < 2) {
                    searchResultsModel.clear();
                    return;
                }
                String option = searchType.getSelectedItem().toString();
                List<Book> matches = db.searchBooks(option, keyword);
                searchResultsModel.clear();
                for (Book book : matches) {
                    searchResultsModel.addElement(book);
                }
            }
        });

        addBtn.addActionListener(e -> {
            try {
                Book book = new Book(0, titleField.getText(), authorField.getText(), genreField.getText());
                db.addBook(book);
                JOptionPane.showMessageDialog(this, "Book added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Add failed: " + ex.getMessage());
            }
        });

        deleteBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(deleteIdField.getText());
                db.deleteBook(id);
                JOptionPane.showMessageDialog(this, "Book deleted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Delete failed: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}
