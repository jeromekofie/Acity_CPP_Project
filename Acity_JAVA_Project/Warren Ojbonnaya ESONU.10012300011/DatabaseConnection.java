import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    // Static block to ensure the JDBC driver is loaded
    static {
        try {
            // Make sure you have the MariaDB JDBC driver JAR in your classpath
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Throw a runtime exception to indicate a critical configuration error
            throw new RuntimeException("Failed to load MariaDB JDBC driver. Ensure the JAR is in the classpath.", e);
        }
    }

    // --- Database Connection Details ---
    // !!! IMPORTANT: Update these details for your specific database setup !!!
    private static final String URL = "jdbc:mariadb://localhost:3306/wvrren"; // Your DB URL and name
    private static final String USER = "root"; // Your DB username
    private static final String PASSWORD = "SYSTEM"; // Your DB password

    /**
     * Establishes a connection to the database.
     *
     * @return A Connection object.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Attempts to create the necessary database tables if they don't already exist.
     * Call this once at application startup.
     */
    public static void initializeDatabase() {
        String createBooksTableSQL = "CREATE TABLE IF NOT EXISTS books (" +
                "id INT PRIMARY KEY, " +
                "title VARCHAR(255) NOT NULL, " +
                "author VARCHAR(255) NOT NULL, " +
                "is_available BOOLEAN DEFAULT TRUE" +
                ");";

        String createMembersTableSQL = "CREATE TABLE IF NOT EXISTS members (" +
                "id INT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "type VARCHAR(50) NOT NULL" + // e.g., 'student', 'teacher'
                ");";

        String createBorrowedBooksTableSQL = "CREATE TABLE IF NOT EXISTS borrowed_books (" +
                "book_id INT NOT NULL, " +
                "member_id INT NOT NULL, " +
                "borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "PRIMARY KEY (book_id), " + // A book can only be borrowed by one member
                "FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE" +
                ");";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            System.out.println("Initializing database schema if needed...");
            stmt.execute(createBooksTableSQL);
            System.out.println("Table 'books' checked/created.");
            stmt.execute(createMembersTableSQL);
            System.out.println("Table 'members' checked/created.");
            stmt.execute(createBorrowedBooksTableSQL);
            System.out.println("Table 'borrowed_books' checked/created.");
            System.out.println("Database initialization check complete.");

        } catch (SQLException e) {
            System.err.println("Error during database initialization: " + e.getMessage());
            // Depending on the error, the application might not function correctly.
            // Consider more robust error handling or logging here.
        }
    }

    // --- Book CRUD operations ---

    public static void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (id, title, author, is_available) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, book.getId());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setBoolean(4, book.getAvailability()); // Use getAvailability()
            stmt.executeUpdate();
        }
    }

    public static List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("id"),
                        rs.getBoolean("is_available")));
            }
        }
        return books;
    }

    public static void updateBookAvailability(int bookId, boolean available) throws SQLException {
        String sql = "UPDATE books SET is_available = ? WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, available);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        }
    }

    public static void deleteBook(int bookId) throws SQLException {
        // Note: ON DELETE CASCADE on borrowed_books table handles related records
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        }
    }

    // --- Member CRUD operations ---

    public static void addMember(Member member) throws SQLException {
        String sql = "INSERT INTO members (id, name, type) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, member.getId());
            stmt.setString(2, member.name); // Accessing protected field 'name' - consider getter
            // Determine type based on instance
            String memberType = "unknown";
            if (member instanceof StudentMember) {
                memberType = "student";
            } else if (member instanceof TeacherMember) {
                memberType = "teacher";
            }
            stmt.setString(3, memberType);
            stmt.executeUpdate();
        }
    }

    public static List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String type = rs.getString("type");
                if ("student".equalsIgnoreCase(type)) {
                    members.add(new StudentMember(rs.getString("name"), rs.getInt("id")));
                } else if ("teacher".equalsIgnoreCase(type)) {
                    members.add(new TeacherMember(rs.getString("name"), rs.getInt("id")));
                }
                // Handle other types or null if necessary
            }
        }
        return members;
    }

    public static void deleteMember(int memberId) throws SQLException {
        // Note: ON DELETE CASCADE on borrowed_books table handles related records
        String sql = "DELETE FROM members WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            stmt.executeUpdate();
        }
    }

    // --- Borrow/Return operations ---

    public static void borrowBook(int bookId, int memberId) throws SQLException {
        // Check if book is available first (optional but recommended)
        // Transaction might be needed here to ensure atomicity

        updateBookAvailability(bookId, false); // Mark book as unavailable

        String sql = "INSERT INTO borrowed_books (book_id, member_id) VALUES (?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, memberId);
            stmt.executeUpdate();
        }
        // Consider adding error handling if the INSERT fails after UPDATE
    }

    public static void returnBook(int bookId, int memberId) throws SQLException {
        // Transaction might be needed here

        updateBookAvailability(bookId, true); // Mark book as available

        String sql = "DELETE FROM borrowed_books WHERE book_id = ? AND member_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, memberId); // Ensure only the correct member's borrow record is deleted
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                // Optional: Handle case where the borrow record didn't exist or book wasn't
                // marked unavailable
                System.err.println("Warning: No borrow record found for book ID " + bookId + " and member ID "
                        + memberId + " during return.");
                // May need to ensure book availability is still set correctly
            }
        }
        // Consider adding error handling if the DELETE fails after UPDATE
    }

    /**
     * Retrieves a list of books currently borrowed by a specific member.
     *
     * @param memberId The ID of the member.
     * @return A List of Book objects borrowed by the member.
     * @throws SQLException If a database access error occurs.
     */
    public static List<Book> getBorrowedBooksByMember(int memberId) throws SQLException {
        List<Book> borrowedBooksList = new ArrayList<>();
        String sql = "SELECT b.id, b.title, b.author, b.is_available " +
                "FROM books b " +
                "JOIN borrowed_books bb ON b.id = bb.book_id " +
                "WHERE bb.member_id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    borrowedBooksList.add(new Book(
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getInt("id"),
                            rs.getBoolean("is_available") // Should be false if borrowed
                    ));
                }
            }
        }
        return borrowedBooksList;
    }

} // End of DatabaseConnection class