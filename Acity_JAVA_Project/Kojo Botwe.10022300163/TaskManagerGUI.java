import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TaskManagerGUI extends JFrame {
    private DatabaseManager dbManager;
    private JTextField descField, deadlineField, idField;
    private JComboBox<Priority> priorityBox;
    private JTable taskTable;
    private DefaultTableModel tableModel;

    public TaskManagerGUI() {
        dbManager = new DatabaseManager();
        setTitle("Task Manager");
        setSize(850, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("images\\final.jpg");
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPanel.setLayout(new BorderLayout());
        setContentPane(contentPanel);

        // ---------- Form Panel (Input Area) ----------
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel titleLabel = new JLabel("Task Manager");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        formPanel.add(createLabel("Description:"), gbc);
        gbc.gridx = 1;
        descField = createField();
        formPanel.add(descField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Deadline (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        deadlineField = createField();
        formPanel.add(deadlineField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Task ID to complete or delete:"), gbc);
        gbc.gridx = 1;
        idField = createField();
        formPanel.add(idField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Priority:"), gbc);
        gbc.gridx = 1;
        priorityBox = new JComboBox<>(Priority.values());
        priorityBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(priorityBox, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setOpaque(false);

        JButton addButton = createButton("Add Task", new Color(255, 87, 87));       // Red
        JButton viewButton = createButton("View Tasks", new Color(86, 179, 255));   // Blue
        JButton completeButton = createButton("Complete Task", new Color(144, 238, 144)); // Green
        JButton deleteButton = createButton("Delete Task", new Color(255, 204, 0)); // Yellow

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(deleteButton);

        formPanel.add(buttonPanel, gbc);

        // ---------- Output Panel (Table Area) ----------
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setOpaque(false);
        outputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        tableModel = new DefaultTableModel(new String[]{"ID", "Description", "Deadline", "Priority", "Status"}, 0);
        taskTable = new JTable(tableModel);
        taskTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        taskTable.setRowHeight(25);

        taskTable.setOpaque(false);
        taskTable.setBackground(new Color(255, 255, 255, 180));
        ((DefaultTableCellRenderer) taskTable.getDefaultRenderer(Object.class)).setOpaque(true);
        taskTable.setForeground(Color.BLACK);

        JTableHeader header = taskTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(255, 255, 255, 200));
        header.setOpaque(true);

        // Transparent background panel to hold the scroll pane
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(new Color(255, 255, 255, 180)); // semi-transparent white
        tableWrapper.setOpaque(true); // Must be opaque to block background

        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        tableWrapper.add(scrollPane, BorderLayout.CENTER);

        // Add wrapper to output panel
        outputPanel.add(tableWrapper, BorderLayout.CENTER);

        // ---------- Add Panels to Frame ----------
        contentPanel.add(formPanel, BorderLayout.NORTH);
        contentPanel.add(outputPanel, BorderLayout.CENTER);

        // ---------- Button Actions ----------
        addButton.addActionListener(e -> {
            String desc = descField.getText();
            String deadline = deadlineField.getText();
            Priority priority = (Priority) priorityBox.getSelectedItem();
            if (!desc.isEmpty() && !deadline.isEmpty()) {
                try {
                    LocalDate.parse(deadline, DateTimeFormatter.ISO_LOCAL_DATE);
                    dbManager.addTask(desc, deadline, priority);
                    JOptionPane.showMessageDialog(this, "Task added!");
                    descField.setText("");
                    deadlineField.setText("");
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
            }
        });

        viewButton.addActionListener(e -> {
            tableModel.setRowCount(0); // Clear table
            List<Task> tasks = dbManager.getTasks();
            for (Task task : tasks) {
                tableModel.addRow(new Object[]{
                        task.getId(),
                        task.getDescription(),
                        task.getDeadline(),
                        task.getPriority(),
                        task.getStatus()
                });
            }
        });

        completeButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                dbManager.markTaskAsCompleted(id);
                JOptionPane.showMessageDialog(this, "Task marked as completed.");
                idField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid task ID.");
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                dbManager.deleteTask(id);
                JOptionPane.showMessageDialog(this, "Task deleted.");
                idField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid task ID.");
            }
        });
    }

    private JTextField createField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        return field;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JButton createButton(String text, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK);
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaskManagerGUI().setVisible(true));
    }
}
