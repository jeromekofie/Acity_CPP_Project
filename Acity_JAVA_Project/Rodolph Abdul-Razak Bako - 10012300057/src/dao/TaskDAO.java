package dao;

import model.Task;
import java.util.List;

/**
 * Data Access Object interface for Task operations
 */
public interface TaskDAO {
    Task create(Task task) throws Exception;
    Task getById(int id) throws Exception;
    List<Task> getAll() throws Exception;
    boolean update(Task task) throws Exception;
    boolean delete(int id) throws Exception;
    List<Task> searchByTitle(String title) throws Exception;
    List<Task> getTasksByPriority(String priority) throws Exception;
}