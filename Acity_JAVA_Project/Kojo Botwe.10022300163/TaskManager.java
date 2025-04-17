import java.util.ArrayList;

public class TaskManager {
    private DatabaseManager dbManager;  // Use DatabaseManager for DB operations

    public TaskManager() {
        dbManager = new DatabaseManager();  // Initialize DatabaseManager
    }

    public void addTask(String description, String deadline, Priority priority) {
        dbManager.addTask(description, deadline, priority);  // Add task to DB
    }

    public void markTaskAsCompleted(int id) {
        dbManager.markTaskAsCompleted(id);  // Mark task as completed in DB
    }

    public void deleteTask(int id) {
        dbManager.deleteTask(id);  // Delete task from DB
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(dbManager.getTasks());  // Retrieve tasks from DB
    }
}
