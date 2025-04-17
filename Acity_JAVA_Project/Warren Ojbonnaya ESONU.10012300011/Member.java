import java.io.*;
import java.util.*;

public class Member {
    protected String name;
    protected int memberId;
    protected List<Integer> borrowedBooks = new ArrayList<>();

    public Member(String name, int memberId) {
        this.name = name;
        this.memberId = memberId;
    }

    // Getters
    public int getId() {
        return memberId;
    }

    // Method to display member details
    public void displayMember() {
        System.out.printf("%-5d %-20s%n", memberId, name);
    }

    // Add borrowed book to the list
    public void addBorrowedBook(int bookId) {
        borrowedBooks.add(bookId);
    }

    // Return borrowed book from the list
    public void returnBorrowedBook(int bookId) {
        borrowedBooks.remove(Integer.valueOf(bookId));
    }

    // Display list of borrowed books
    public void displayBorrowedBooks() {
        try (FileWriter fw = new FileWriter("displayborrowedbook.txt", true); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("Books borrowed by " + name + " (ID: " + memberId + "):\n");

            if (borrowedBooks.isEmpty()) {
                bw.write("No books borrowed.\n");
            } else {
                for (int bookId : borrowedBooks) {
                    bw.write("Book ID: " + bookId + "\n");
                }
            }
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }
}