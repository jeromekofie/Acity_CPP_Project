
public class Book {
    private String title;
    private String author;
    private int id;
    private boolean isAvailable;

    public Book(String title, String author, int id) {
        this(title, author, id, true);
    }

    public Book(String title, String author, int id, boolean isAvailable) {
        this.title = title;
        this.author = author;
        this.id = id;
        this.isAvailable = isAvailable;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getId() {
        return id;
    }

    public boolean getAvailability() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return String.format("%d: %s by %s (%s)",
                id, title, author, isAvailable ? "Available" : "Borrowed");
    }
}