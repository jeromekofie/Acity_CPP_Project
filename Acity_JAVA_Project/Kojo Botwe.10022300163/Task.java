public class Task {
    private int id;
    private String description;
    private String deadline;
    private boolean isCompleted;
    private Priority priority;

    // Constructor for new tasks (default: not completed)
    public Task(int id, String description, String deadline, Priority priority) {
        this(id, description, deadline, priority, false);
    }

    // Constructor for tasks from DB (may be completed)
    public Task(int id, String description, String deadline, Priority priority, boolean isCompleted) {
        this.id = id;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.isCompleted = isCompleted;
        
    }

    public void markAsCompleted() {
        this.isCompleted = true;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getDeadline() {
        return deadline;
    }

    public Priority getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getStatus() {
        return isCompleted ? "Completed":"Pending";
    }

    

    // Utility method to show task details
    public String getDetails() {
        return "ID: " + id +
                "\nDescription: " + description +
                "\nDeadline: " + deadline +
                "\nStatus: " + (isCompleted ? "Completed" : "Pending") +
                "\nPriority: " + priority.name() + "\n";
    }

    // To set task completion status from the DB or GUI
    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
