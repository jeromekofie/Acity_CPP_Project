package view;

import dao.TaskDAO;
import dao.impl.TaskDAOImpl;
import model.Priority;
import model.Task;
import util.FileHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


public class MainFrame extends JFrame {
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JTextField titleField, deadlineField, searchField;
    private JTextArea descriptionArea;
    private JComboBox<Priority> priorityComboBox;
    private JCheckBox completedCheckBox;
    private JButton addButton, updateButton, deleteButton, clearButton, searchButton;
    private JLabel statusLabel;
    
    private Task currentTask;
    private TaskDAO taskDAO;
    
    
    private final Color primaryColor = new Color(59, 89, 152);  
    private final Color secondaryColor = new Color(242, 242, 242); 
    private final Color accentColor = new Color(66, 103, 178);  
    private final Color textColor = new Color(51, 51, 51);      
    private final Color successColor = new Color(76, 175, 80);  
    private final Color warningColor = new Color(255, 152, 0);  
    private final Color errorColor = new Color(244, 67, 54);    
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public MainFrame() {
        taskDAO = new TaskDAOImpl();
        setLookAndFeel();
        initComponents();
        loadTasks();
    }
    
    private void setLookAndFeel() {
        try {
            
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            

        } catch (Exception e) {
            System.err.println("Failed to set look and feel: " + e.getMessage());
        }
    }
    
    private void initComponents() {
        setTitle("Rodolph's Task Management System");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        
        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        
        JPanel tablePanel = createTablePanel();
        
       
        JPanel formPanel = createFormPanel();
        
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tablePanel, formPanel);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.7);
        add(splitPane, BorderLayout.CENTER);
        
        
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setPreferredSize(new Dimension(900, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Rodolph's Task Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        
        tableModel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Title", "Priority", "Deadline", "Completed"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        
        taskTable = new JTable(tableModel);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskTable.setRowHeight(30);
        taskTable.setFont(new Font("Arial", Font.PLAIN, 14));
        taskTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        taskTable.getTableHeader().setBackground(primaryColor);
        taskTable.getTableHeader().setForeground(Color.WHITE);
        
        
        taskTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    try {
                        currentTask = taskDAO.getById(id);
                        populateFields(currentTask);
                        updateButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                        updateStatus("Task selected: " + currentTask.getTitle(), Color.BLACK);
                    } catch (Exception ex) {
                        showError("Error loading task: " + ex.getMessage());
                    }
                }
            }
        });
        
        
        taskTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                    
                    
                    Object priorityValue = table.getModel().getValueAt(row, 2); // Priority column
                    if (priorityValue != null) {
                        if (priorityValue.toString().equals("HIGH")) {
                            c.setBackground(new Color(255, 230, 230)); // Light red
                        } else if (priorityValue.toString().equals("MEDIUM")) {
                            c.setBackground(new Color(255, 255, 230)); // Light yellow
                        } else if (priorityValue.toString().equals("LOW")) {
                            c.setBackground(new Color(230, 255, 230)); // Light green
                        }
                    }
                    
                   
                    Object completedValue = table.getModel().getValueAt(row, 4); // Completed column
                    if (completedValue != null && completedValue.toString().equals("Yes")) {
                        Font currentFont = c.getFont();
                        ((JLabel) c).setFont(new Font(currentFont.getName(), Font.ITALIC, currentFont.getSize()));
                    }
                }
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(primaryColor));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        
        JPanel searchPanel = createSearchPanel();
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        
        return tablePanel;
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        searchPanel.setBackground(secondaryColor);
        
        searchPanel.add(new JLabel("Search by Title: "));
        
        searchField = new JTextField(20);
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        searchPanel.add(searchField);
        
        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        
        searchButton = createStyledButton("Search", accentColor);
        searchButton.addActionListener(this::searchTasks);
        searchPanel.add(searchButton);
        
        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        
        JButton refreshButton = createStyledButton("Refresh All", accentColor);
        refreshButton.addActionListener(e -> {
            loadTasks();
            updateStatus("All tasks refreshed", Color.BLACK);
        });
        searchPanel.add(refreshButton);
        
        return searchPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout(5, 5));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(primaryColor), "Task Details"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        fieldsPanel.add(titleLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        titleField = new JTextField(20);
        titleField.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldsPanel.add(titleField, gbc);
        
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel priorityLabel = new JLabel("Priority:");
        priorityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        fieldsPanel.add(priorityLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        priorityComboBox = new JComboBox<>(Priority.values());
        priorityComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldsPanel.add(priorityComboBox, gbc);
        
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel deadlineLabel = new JLabel("Deadline (yyyy-MM-dd):");
        deadlineLabel.setFont(new Font("Arial", Font.BOLD, 14));
        fieldsPanel.add(deadlineLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        deadlineField = new JTextField(10);
        deadlineField.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldsPanel.add(deadlineField, gbc);
        
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel completedLabel = new JLabel("Completed:");
        completedLabel.setFont(new Font("Arial", Font.BOLD, 14));
        fieldsPanel.add(completedLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        completedCheckBox = new JCheckBox();
        fieldsPanel.add(completedCheckBox, gbc);
        
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        fieldsPanel.add(descriptionLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        fieldsPanel.add(descScrollPane, gbc);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Create buttons panel
        JPanel buttonsPanel = createButtonsPanel();
        formPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        return formPanel;
    }
    
    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        addButton = createStyledButton("Add Task", accentColor);
        addButton.addActionListener(this::addTask);
        
        updateButton = createStyledButton("Update Task", accentColor);
        updateButton.addActionListener(this::updateTask);
        updateButton.setEnabled(false);
        
        deleteButton = createStyledButton("Delete Task", errorColor);
        deleteButton.addActionListener(this::deleteTask);
        deleteButton.setEnabled(false);
        
        clearButton = createStyledButton("Clear Form", warningColor);
        clearButton.addActionListener(e -> clearForm());
        
        JButton exportButton = createStyledButton("Export to File", accentColor);
        exportButton.addActionListener(this::exportTasks);
        
        JButton importButton = createStyledButton("Import from File", accentColor);
        importButton.addActionListener(this::importTasks);
        
        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(clearButton);
        buttonsPanel.add(exportButton);
        buttonsPanel.add(importButton);
        
        return buttonsPanel;
    }
    
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setPreferredSize(new Dimension(getWidth(), 25));
        
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        statusBar.add(statusLabel, BorderLayout.WEST);
        
        return statusBar;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void loadTasks() {
        try {
            List<Task> tasks = taskDAO.getAll();
            populateTable(tasks);
            updateStatus("Loaded " + tasks.size() + " tasks", Color.BLACK);
        } catch (Exception e) {
            showError("Error loading tasks: " + e.getMessage());
        }
    }
    
    private void populateTable(List<Task> tasks) {
        
        tableModel.setRowCount(0);
        
       
        for (Task task : tasks) {
            tableModel.addRow(new Object[]{
                task.getId(),
                task.getTitle(),
                task.getPriority(),
                task.getFormattedDeadline(),
                task.isCompleted() ? "Yes" : "No"
            });
        }
    }
    
    private void populateFields(Task task) {
        titleField.setText(task.getTitle());
        descriptionArea.setText(task.getDescription());
        priorityComboBox.setSelectedItem(task.getPriority());
        
        if (task.getDeadline() != null) {
            deadlineField.setText(task.getFormattedDeadline());
        } else {
            deadlineField.setText("");
        }
        
        completedCheckBox.setSelected(task.isCompleted());
    }
    
    private void clearForm() {
        currentTask = null;
        titleField.setText("");
        descriptionArea.setText("");
        priorityComboBox.setSelectedItem(Priority.MEDIUM);
        deadlineField.setText("");
        completedCheckBox.setSelected(false);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        taskTable.clearSelection();
        updateStatus("Form cleared", Color.BLACK);
    }
    
    private Task getTaskFromForm() {
        Task task = new Task();
        
        if (currentTask != null) {
            task.setId(currentTask.getId());
        }
        
        if (titleField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        
        task.setTitle(titleField.getText().trim());
        task.setDescription(descriptionArea.getText().trim());
        task.setPriority((Priority) priorityComboBox.getSelectedItem());
        task.setCompleted(completedCheckBox.isSelected());
        
        String deadlineStr = deadlineField.getText().trim();
        if (!deadlineStr.isEmpty()) {
            try {
                task.setDeadline(LocalDate.parse(deadlineStr, DATE_FORMATTER));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd");
            }
        }
        
        return task;
    }
    
    private void addTask(ActionEvent e) {
        try {
            Task task = getTaskFromForm();
            Task createdTask = taskDAO.create(task);
            updateStatus("Task added successfully: " + task.getTitle(), successColor);
            clearForm();
            loadTasks();
        } catch (Exception ex) {
            showError("Error adding task: " + ex.getMessage());
        }
    }
    
    private void updateTask(ActionEvent e) {
        if (currentTask == null) {
            showError("No task selected");
            return;
        }
        
        try {
            Task task = getTaskFromForm();
            boolean updated = taskDAO.update(task);
            
            if (updated) {
                updateStatus("Task updated successfully: " + task.getTitle(), successColor);
                clearForm();
                loadTasks();
            } else {
                showError("Failed to update task");
            }
        } catch (Exception ex) {
            showError("Error updating task: " + ex.getMessage());
        }
    }
    
    private void deleteTask(ActionEvent e) {
        if (currentTask == null) {
            showError("No task selected");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this task?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean deleted = taskDAO.delete(currentTask.getId());
                
                if (deleted) {
                    updateStatus("Task deleted successfully: " + currentTask.getTitle(), successColor);
                    clearForm();
                    loadTasks();
                } else {
                    showError("Failed to delete task");
                }
            } catch (Exception ex) {
                showError("Error deleting task: " + ex.getMessage());
            }
        }
    }
    
    private void searchTasks(ActionEvent e) {
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            loadTasks();
            return;
        }
        
        try {
            List<Task> tasks = taskDAO.searchByTitle(searchTerm);
            populateTable(tasks);
            updateStatus("Found " + tasks.size() + " tasks matching '" + searchTerm + "'", Color.BLACK);
        } catch (Exception ex) {
            showError("Error searching tasks: " + ex.getMessage());
        }
    }
    
    private void exportTasks(ActionEvent e) {
        try {
            List<Task> tasks = taskDAO.getAll();
            FileHandler.saveTasks(tasks);
            updateStatus("Tasks exported successfully to tasks.txt", successColor);
            
            JOptionPane.showMessageDialog(this, 
                "Tasks exported successfully to tasks.txt",
                "Export Successful", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Error exporting tasks: " + ex.getMessage());
        }
    }
    
    private void importTasks(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Importing will add tasks from file. Continue?",
                "Confirm Import",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
                
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                List<Task> tasks = FileHandler.loadTasks();
                int imported = 0;
                
                for (Task task : tasks) {
                  
                    task.setId(0);
                    taskDAO.create(task);
                    imported++;
                }
                
                updateStatus("Imported " + imported + " tasks successfully", successColor);
                JOptionPane.showMessageDialog(this, 
                    imported + " tasks imported successfully!",
                    "Import Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
                loadTasks();
            } catch (Exception ex) {
                showError("Error importing tasks: " + ex.getMessage());
            }
        }
    }
    
    private void updateStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
    
    private void showError(String message) {
        updateStatus(message, errorColor);
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}