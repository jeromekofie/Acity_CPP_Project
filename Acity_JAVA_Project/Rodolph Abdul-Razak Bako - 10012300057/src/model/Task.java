package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public class Task {
    private int id;
    private String title;
    private String description;
    private Priority priority;
    private LocalDate deadline;
    private boolean completed;
    
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    

    public Task() {
        this.priority = Priority.MEDIUM;
        this.completed = false;
    }
    
    public Task(String title, String description, Priority priority, LocalDate deadline) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.completed = false;
    }
    
    public Task(int id, String title, String description, Priority priority, LocalDate deadline, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.completed = completed;
    }
    
        public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    public LocalDate getDeadline() {
        return deadline;
    }
    
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    public String getFormattedDeadline() {
        return deadline != null ? deadline.format(DATE_FORMATTER) : "";
    }
    
    public String toFileString() {
        return String.join("|", 
            String.valueOf(id),
            title,
            description,
            priority.name(),
            deadline != null ? deadline.format(DATE_FORMATTER) : "",
            String.valueOf(completed)
        );
    }
    
    public static Task fromFileString(String fileString) {
        String[] parts = fileString.split("\\|");
        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid task string format");
        }
        
        Task task = new Task();
        task.setId(Integer.parseInt(parts[0]));
        task.setTitle(parts[1]);
        task.setDescription(parts[2]);
        task.setPriority(Priority.valueOf(parts[3]));
        
        if (!parts[4].isEmpty()) {
            task.setDeadline(LocalDate.parse(parts[4], DATE_FORMATTER));
        }
        
        task.setCompleted(Boolean.parseBoolean(parts[5]));
        return task;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", priority=" + priority +
                ", deadline=" + (deadline != null ? deadline.format(DATE_FORMATTER) : "None") +
                ", completed=" + completed +
                '}';
    }
}