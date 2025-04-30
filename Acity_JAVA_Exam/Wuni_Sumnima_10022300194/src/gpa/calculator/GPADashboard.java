import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GPADashboard extends JFrame {

    private JTextField nameField, courseField, creditField;
    private JComboBox<String> gradeBox;
    private JTable table;
    private DefaultTableModel model;

    private static final String[] GRADES = {"4.0", "3.5", "3.0", "2.5", "2.0", "1.5", "0.0"};
    private static final HashMap<String, Double> gradeMap = new HashMap<>();

    static {
        gradeMap.put("4.0", 4.0);
        gradeMap.put("3.5", 3.5);
        gradeMap.put("3.0", 3.0);
        gradeMap.put("2.5", 2.5);
        gradeMap.put("2.0", 2.0);
        gradeMap.put("1.5", 1.5);
        gradeMap.put("0.0", 0.0);
    }

    public GPADashboard() {
        setTitle("GPA Calculator");
        setSize(700, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(200, 200, 255));  // light purple

        setLayout(null);

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(30, 30, 100, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(130, 30, 150, 25);
        add(nameField);

        JLabel courseLabel = new JLabel("Courses");
        courseLabel.setBounds(30, 70, 100, 25);
        add(courseLabel);

        courseField = new JTextField();
        courseField.setBounds(130, 70, 150, 25);
        add(courseField);

        JLabel creditLabel = new JLabel("Credit hours");
        creditLabel.setBounds(30, 110, 100, 25);
        add(creditLabel);

        creditField = new JTextField();
        creditField.setBounds(130, 110, 150, 25);
        add(creditField);

        JLabel gradeLabel = new JLabel("Grades");
        gradeLabel.setBounds(30, 150, 100, 25);
        add(gradeLabel);

        gradeBox = new JComboBox<>(GRADES);
        gradeBox.setBounds(130, 150, 100, 25);
        add(gradeBox);

        // Table setup
        model = new DefaultTableModel(new String[]{"Student Name", "Courses", "Credit Hours", "Grades"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(320, 30, 350, 200);
        add(scrollPane);

        // Buttons
        JButton addBtn = new JButton("Add");
        addBtn.setBounds(30, 200, 80, 30);
        add(addBtn);

        JButton saveBtn = new JButton("Save");
        saveBtn.setBounds(120, 200, 80, 30);
        add(saveBtn);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(30, 240, 80, 30);
        add(updateBtn);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(120, 240, 80, 30);
        add(deleteBtn);

        // Actions
        addBtn.addActionListener(e -> {
            String name = nameField.getText();
            String course = courseField.getText();
            String credit = creditField.getText();
            String grade = (String) gradeBox.getSelectedItem();

            if (name.isEmpty() || course.isEmpty() || credit.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            model.addRow(new Object[]{name, course, credit, grade});
            clearFields();
        });

        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) model.removeRow(selectedRow);
        });

        updateBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                model.setValueAt(nameField.getText(), selectedRow, 0);
                model.setValueAt(courseField.getText(), selectedRow, 1);
                model.setValueAt(creditField.getText(), selectedRow, 2);
                model.setValueAt(gradeBox.getSelectedItem(), selectedRow, 3);
                clearFields();
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = table.getSelectedRow();
                nameField.setText(model.getValueAt(row, 0).toString());
                courseField.setText(model.getValueAt(row, 1).toString());
                creditField.setText(model.getValueAt(row, 2).toString());
                gradeBox.setSelectedItem(model.getValueAt(row, 3).toString());
            }
        });

        saveBtn.addActionListener(e -> {
            double totalPoints = 0;
            int totalCredits = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                double grade = gradeMap.get(model.getValueAt(i, 3).toString());
                int credit = Integer.parseInt(model.getValueAt(i, 2).toString());
                totalPoints += grade * credit;
                totalCredits += credit;
            }
            double gpa = (totalCredits == 0) ? 0 : totalPoints / totalCredits;
            JOptionPane.showMessageDialog(this, String.format("Calculated GPA: %.2f", gpa));
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void clearFields() {
        nameField.setText("");
        courseField.setText("");
        creditField.setText("");
        gradeBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GPADashboard::new);
    }
}
