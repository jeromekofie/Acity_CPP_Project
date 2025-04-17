import javax.swing.SwingUtilities;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static FileHandler fileHandler = new FileHandler();
    private static DatabaseHandler dbHandler = new DatabaseHandler();
    private static KeywordSearcher keywordSearcher = new KeywordSearcher();
    private static List<String> searchResults = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FinderGUI::new); // Launch GUI in parallel

        while (true) {
            printMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> searchInDatabase();
                case 2 -> searchInFile();
                case 3 -> viewResults();
                case 4 -> saveResultsToFile();
                case 5 -> exitProgram();
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Student Keyword Finder ===");
        System.out.println("1. Search in database");
        System.out.println("2. Search in text file");
        System.out.println("3. View results");
        System.out.println("4. Save results to file");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void searchInDatabase() {
        System.out.print("Enter keyword to search in database: ");
        String keyword = scanner.nextLine();
        searchResults = dbHandler.searchWord(keyword);
        System.out.println("Search completed. Results found: " + searchResults.size());
    }

    private static void searchInFile() {
        System.out.print("Enter file name to read from (e.g., students.txt): ");
        String filename = scanner.nextLine();
        ArrayList<String> lines = fileHandler.readFile(filename);

        System.out.print("Enter keyword to search in file: ");
        String keyword = scanner.nextLine();

        keywordSearcher.search(lines, keyword);
        searchResults = keywordSearcher.getResults();

        System.out.println("Search completed. Results found: " + searchResults.size());
    }

    private static void viewResults() {
        if (searchResults.isEmpty()) {
            System.out.println("No results found.");
        } else {
            System.out.println("Search Results:");
            searchResults.forEach(System.out::println);
        }
    }

    private static void saveResultsToFile() {
        if (searchResults.isEmpty()) {
            System.out.println("No results to save.");
            return;
        }

        System.out.print("Enter output file name (e.g., output.txt): ");
        String outputFile = scanner.nextLine();
        fileHandler.saveToFile(new ArrayList<>(searchResults), outputFile);
    }

    private static void exitProgram() {
        System.out.println("Exiting... Goodbye!");
        scanner.close();
        System.exit(0);
    }
}