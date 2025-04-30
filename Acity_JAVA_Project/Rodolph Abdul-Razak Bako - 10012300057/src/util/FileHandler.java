package util;

import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileHandler {
    private static final String DEFAULT_FILE_PATH = "tasks.txt";

    public static void saveTasks(List<Task> tasks) throws IOException {
        saveTasks(tasks, DEFAULT_FILE_PATH);
    }
    
    public static void saveTasks(List<Task> tasks, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : tasks) {
                writer.write(task.toFileString());
                writer.newLine();
            }
        }
    }
    

    public static List<Task> loadTasks() throws IOException {
        return loadTasks(DEFAULT_FILE_PATH);
    }
    
    public static List<Task> loadTasks(String filePath) throws IOException {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        

        if (!file.exists()) {
            file.createNewFile();
            return tasks;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        tasks.add(Task.fromFileString(line));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error parsing task: " + e.getMessage());
                    }
                }
            }
        }
        
        return tasks;
    }
    

    public static void backupTasks(List<Task> tasks) throws IOException {
        String backupFileName = "backup_tasks_" + System.currentTimeMillis() + ".txt";
        saveTasks(tasks, backupFileName);
    }
}