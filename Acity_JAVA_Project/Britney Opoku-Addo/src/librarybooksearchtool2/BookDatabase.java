package librarybooksearchtool2;

import java.sql.*;
import java.util.*;

public class BookDatabase {
    private Connection conn;

    public BookDatabase() throws Exception {
        String url = "jdbc:mysql://localhost:3306/library";
        String user = "root";
        String password = "Britney11addo"; // Replace with your actual password

        conn = DriverManager.getConnection(url, user, password);
    }

    public void addBook(Book book) throws SQLException {
        String query = "INSERT INTO books (title, author, genre) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, book.getTitle());
        stmt.setString(2, book.getAuthor());
        stmt.setString(3, book.getGenre());
        stmt.executeUpdate();
    }

    public void deleteBook(int id) throws SQLException {
        String query = "DELETE FROM books WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public List<Book> searchBooks(String type, String keyword) {
        List<Book> list = new ArrayList<>();
        String query = "SELECT * FROM books WHERE " + type + " LIKE ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("genre")
                );
                list.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
