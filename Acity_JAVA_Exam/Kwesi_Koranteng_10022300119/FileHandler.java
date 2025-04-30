import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileHandler {
    private static final String FILE_NAME = "gpa_records.txt";

    public static void saveToFile(String name, String id, String result) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            writer.println("\n" + "=".repeat(80));
            writer.println("GPA RECORD");
            writer.println("=".repeat(80));
            writer.println("Date: " + now.format(formatter));
            writer.println("-".repeat(80));
            writer.println(result);
            writer.println("=".repeat(80));
            
            System.out.println("Record saved successfully to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }
} 