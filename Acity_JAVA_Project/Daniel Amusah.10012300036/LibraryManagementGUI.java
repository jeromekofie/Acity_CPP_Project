import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

class Book {
    String title;
    String author;
    boolean isAvailable = true;

    Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String toString() {
        return "Title: " + title + ", Author: " + author + ", Status: " + (isAvailable ? "Available" : "Borrowed");
    }
}

class Member {
    String memberID;
    String name;
    String type;

    Member(String memberID, String name, String type) {
        this.memberID = memberID;
        this.name = name;
        this.type = type;
    }

    public String toString() {
        return "[" + type + "] ID: " + memberID + ", Name: " + name;
    }
}

class Library {
    ArrayList<Book> books = new ArrayList<>();
    ArrayList<Member> members = new ArrayList<>();
    Connection conn;

    Library() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_db", "library_db", "Amusah_123_qt");
            System.out.println("Connected to database.");
            loadBooks();
            loadMembers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadBooks() throws SQLException {
        books.clear();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM books");
        while (rs.next()) {
            Book book = new Book(rs.getString("title"), rs.getString("author"));
            book.isAvailable = rs.getBoolean("is_available");
            books.add(book);
        }
    }

    void loadMembers() throws SQLException {
        members.clear();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM members");
        while (rs.next()) {
            String id = rs.getString("member_id");
            members.add(new Member(id, rs.getString("name"), rs.getString("type")));
        }
    }

    boolean authenticateMember(String id) {
        for (Member m : members) {
            if (m.memberID.equals(id)) return true;
        }
        return false;
    }

    String displayBooks() {
        try {
            loadBooks();
        } catch (SQLException e) {
            return "Error loading books: " + e.getMessage();
        }
        StringBuilder sb = new StringBuilder("Library Books:\n");
        for (Book b : books) sb.append(b).append("\n");
        return sb.toString();
    }

    String displayMembers() {
        try {
            loadMembers();
        } catch (SQLException e) {
            return "Error loading members: " + e.getMessage();
        }
        StringBuilder sb = new StringBuilder("Library Members:\n");
        for (Member m : members) sb.append(m).append("\n");
        return sb.toString();
    }

    String displayLogs() {
        StringBuilder sb = new StringBuilder("Activity Logs:\n");
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM logs");
            while (rs.next()) {
                sb.append("Member ID: ").append(rs.getString("member_id"))
                        .append(", Book: ").append(rs.getString("book_title"))
                        .append(", Action: ").append(rs.getString("action"))
                        .append(", Time: ").append(rs.getTimestamp("timestamp"))
                        .append("\n");
            }
        } catch (SQLException e) {
            return "Error loading logs: " + e.getMessage();
        }
        return sb.toString();
    }

    String borrowBook(String memberID, String bookTitle) {
        for (Book b : books) {
            if (b.title.equalsIgnoreCase(bookTitle)) {
                if (b.isAvailable) {
                    b.isAvailable = false;
                    updateBookAvailability(bookTitle, false);
                    logAction(memberID, bookTitle, "borrowed");
                    return "Book borrowed successfully!";
                } else {
                    return "Sorry, the book is already borrowed.";
                }
            }
        }
        return "Book not found in the library.";
    }

    String returnBook(String memberID, String bookTitle) {
        for (Book b : books) {
            if (b.title.equalsIgnoreCase(bookTitle)) {
                if (!b.isAvailable) {
                    b.isAvailable = true;
                    updateBookAvailability(bookTitle, true);
                    logAction(memberID, bookTitle, "returned");
                    return "Book returned successfully!";
                } else {
                    return "The book was not borrowed.";
                }
            }
        }
        return "Book not found in the library.";
    }

    void updateBookAvailability(String title, boolean isAvailable) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE books SET is_available = ? WHERE title = ?");
            ps.setBoolean(1, isAvailable);
            ps.setString(2, title);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void logAction(String memberID, String bookTitle, String action) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO logs (member_id, book_title, action) VALUES (?, ?, ?)");
            ps.setString(1, memberID);
            ps.setString(2, bookTitle);
            ps.setString(3, action);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public class LibraryManagementGUI {
    private static Library library = new Library();
    private static String currentMemberID = null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryManagementGUI::showLoginScreen);
    }

    public static void showLoginScreen() {
        JFrame loginFrame = new JFrame("Library Login");
        loginFrame.setSize(700, 400);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());

        ImageIcon backgroundIcon = new ImageIcon("BG.png"); // Your image path
        Image image = backgroundIcon.getImage().getScaledInstance(300, 400, Image.SCALE_SMOOTH);
        backgroundIcon = new ImageIcon(image);

        JLabel leftPanel = new JLabel(backgroundIcon);
        leftPanel.setPreferredSize(new Dimension(300, 400));
        leftPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel(
                "<html>" +
                        "<div style='text-align:center; color:white; font-family:Segoe UI, sans-serif;'>" +
                        "<h1 style='font-size:28px; margin-bottom:10px;'>Welcome</h1>" +
                        "<p style='font-size:16px; opacity:0.8;'>Unlock the world of books</p>" +
                        "</div>" +
                        "</html>", SwingConstants.CENTER);

        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setVerticalAlignment(SwingConstants.CENTER);
        leftPanel.add(welcomeLabel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel loginTitle = new JLabel("Member Login");
        loginTitle.setFont(new Font("Arial", Font.BOLD, 20));
        rightPanel.add(loginTitle, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        rightPanel.add(new JLabel("Member ID:"), gbc);

        gbc.gridx = 1;
        JTextField memberIdField = new JTextField(15);
        rightPanel.add(memberIdField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        rightPanel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String id = memberIdField.getText();
            if (library.authenticateMember(id)) {
                currentMemberID = id;
                loginFrame.dispose();
                showMainUI();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid Member ID");
            }
        });

        loginFrame.add(leftPanel, BorderLayout.WEST);
        loginFrame.add(rightPanel, BorderLayout.CENTER);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    public static void showMainUI() {
        JFrame frame = new JFrame("Library Management System");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(displayArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 3, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setBackground(new Color(245, 245, 245));

        Color buttonColor = new Color(33, 150, 243);
        Color hoverColor = new Color(30, 136, 229);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        JButton btnShowBooks = createStyledButton("📚 Show Books", buttonColor, hoverColor, buttonFont);
        JButton btnShowMembers = createStyledButton("👥 Show Members", buttonColor, hoverColor, buttonFont);
        JButton btnBorrowBook = createStyledButton("📖 Borrow Book", buttonColor, hoverColor, buttonFont);
        JButton btnReturnBook = createStyledButton("↩️ Return Book", buttonColor, hoverColor, buttonFont);
        JButton btnShowLogs = createStyledButton("📝 Show Logs", buttonColor, hoverColor, buttonFont);
        JButton btnExit = createStyledButton("🚪 Exit", buttonColor, hoverColor, buttonFont);

        btnShowBooks.addActionListener(e -> displayArea.setText(library.displayBooks()));
        btnShowMembers.addActionListener(e -> displayArea.setText(library.displayMembers()));
        btnShowLogs.addActionListener(e -> displayArea.setText(library.displayLogs()));

        btnBorrowBook.addActionListener(e -> {
            String title = JOptionPane.showInputDialog(frame, "Enter Book Title to Borrow:");
            if (title != null && !title.trim().isEmpty()) {
                displayArea.setText(library.borrowBook(currentMemberID, title.trim()));
            }
        });

        btnReturnBook.addActionListener(e -> {
            String title = JOptionPane.showInputDialog(frame, "Enter Book Title to Return:");
            if (title != null && !title.trim().isEmpty()) {
                displayArea.setText(library.returnBook(currentMemberID, title.trim()));
            }
        });

        btnExit.addActionListener(e -> System.exit(0));

        buttonPanel.add(btnShowBooks);
        buttonPanel.add(btnShowMembers);
        buttonPanel.add(btnBorrowBook);
        buttonPanel.add(btnReturnBook);
        buttonPanel.add(btnShowLogs);
        buttonPanel.add(btnExit);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JButton createStyledButton(String text, Color buttonColor, Color hoverColor, Font buttonFont) {
        JButton btn = new JButton(text);
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(buttonFont);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverColor);
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(buttonColor);
            }
        });
        return btn;
    }
}
