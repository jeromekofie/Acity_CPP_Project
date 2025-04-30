package dao.impl;

import dao.TaskDAO;
import model.Priority;
import model.Task;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskDAOImpl implements TaskDAO {
    
    @Override
    public Task create(Task task) throws Exception {
        String sql = "INSERT INTO tasks (title, description, priority, deadline, completed) VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getInstance().getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setString(3, task.getPriority().name());
            
            if (task.getDeadline() != null) {
                stmt.setDate(4, Date.valueOf(task.getDeadline()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            
            stmt.setBoolean(5, task.isCompleted());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating task failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                task.setId(rs.getInt(1));
            } else {
                throw new SQLException("Creating task failed, no ID obtained.");
            }
            
            return task;
            
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBConnection.closeConnection(conn);
        }
    }
    
    @Override
    public Task getById(int id) throws Exception {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractTaskFromResultSet(rs);
            }
            
            return null;
            
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBConnection.closeConnection(conn);
        }
    }
    
    @Override
    public List<Task> getAll() throws Exception {
        String sql = "SELECT * FROM tasks ORDER BY deadline ASC, priority DESC";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            List<Task> tasks = new ArrayList<>();
            
            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
            
            return tasks;
            
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean update(Task task) throws Exception {
        String sql = "UPDATE tasks SET title = ?, description = ?, priority = ?, deadline = ?, completed = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setString(3, task.getPriority().name());
            
            if (task.getDeadline() != null) {
                stmt.setDate(4, Date.valueOf(task.getDeadline()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            
            stmt.setBoolean(5, task.isCompleted());
            stmt.setInt(6, task.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
            
        } finally {
            if (stmt != null) stmt.close();
            DBConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM tasks WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
            
        } finally {
            if (stmt != null) stmt.close();
            DBConnection.closeConnection(conn);
        }
    }
    
    @Override
    public List<Task> searchByTitle(String title) throws Exception {
        String sql = "SELECT * FROM tasks WHERE title LIKE ? ORDER BY deadline ASC, priority DESC";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + title + "%");
            
            rs = stmt.executeQuery();
            
            List<Task> tasks = new ArrayList<>();
            
            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
            
            return tasks;
            
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBConnection.closeConnection(conn);
        }
    }
    
    @Override
    public List<Task> getTasksByPriority(String priority) throws Exception {
        String sql = "SELECT * FROM tasks WHERE priority = ? ORDER BY deadline ASC";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, priority);
            
            rs = stmt.executeQuery();
            
            List<Task> tasks = new ArrayList<>();
            
            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
            
            return tasks;
            
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBConnection.closeConnection(conn);
        }
    }
    
    private Task extractTaskFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task();
        
        task.setId(rs.getInt("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setPriority(Priority.valueOf(rs.getString("priority")));
        
        Date deadline = rs.getDate("deadline");
        if (deadline != null) {
            task.setDeadline(deadline.toLocalDate());
        }
        
        task.setCompleted(rs.getBoolean("completed"));
        
        return task;
    }
}