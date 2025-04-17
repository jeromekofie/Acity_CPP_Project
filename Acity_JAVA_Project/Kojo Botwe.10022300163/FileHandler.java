import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileHandler {
    public static void saveTasksToFile(ArrayList<Task> tasks, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Task task : tasks) {
                writer.write(task.getDetails());
                writer.write("---------------------\n");
            }
            System.out.println("Tasks saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
}
