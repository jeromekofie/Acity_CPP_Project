import java.sql.SQLException;
import java.util.*;
import javax.swing.*;

public class Library {

    public Library() {
        // No initialization needed for database-backed implementation
    }

    // Add a book to the library
    public void addBook(String title, String author, int id) {
        try {
            DatabaseConnection.addBook(new Book(title, author, id));
            JOptionPane.showMessageDialog(null, "Book added successfully: " + title + " by " + author + " (ID: " + id + ")");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding book: " + e.getMessage());
        }
    }

    // Add a member to the library
    public void addMember(Member member) {
        try {
            DatabaseConnection.addMember(member);
            JOptionPane.showMessageDialog(null, "Member added successfully: " + member.name + " (ID: " + member.getId() + ")");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding member: " + e.getMessage());
        }
    }

    // Get all books
    public List<Book> getBooks() {
        try {
            return DatabaseConnection.getAllBooks();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving books: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Get all members
    public List<Member> getMembers() {
        try {
            return DatabaseConnection.getAllMembers();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving members: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Borrow a book
    public void borrowBook(int bookId, int memberId) {
        try {
            DatabaseConnection.borrowBook(bookId, memberId);
            JOptionPane.showMessageDialog(null, "Book borrowed successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error borrowing book: " + e.getMessage());
        }
    }

    // Return a book
    public void returnBook(int bookId, int memberId) {
        try {
            DatabaseConnection.returnBook(bookId, memberId);
            JOptionPane.showMessageDialog(null, "Book returned successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error returning book: " + e.getMessage());
        }
    }

    // Delete a book
    public void deleteBook(int bookId) {
        try {
            DatabaseConnection.deleteBook(bookId);
            JOptionPane.showMessageDialog(null, "Book deleted successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting book: " + e.getMessage());
        }
    }

    // Display all books
    public void displayBooks() {
        List<Book> books = getBooks();
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No books in the library.");
            return;
        }
        StringBuilder sb = new StringBuilder("--- Books in Library ---\n");
        for (Book book : books) {
            sb.append(book.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    // Display all members
    public void displayMembers() {
        List<Member> members = getMembers();
        if (members.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No members in the library.");
            return;
        }
        StringBuilder sb = new StringBuilder("--- Library Members ---\n");
        for (Member member : members) {
            sb.append(member.getId()).append(": ").append(member.name).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}
