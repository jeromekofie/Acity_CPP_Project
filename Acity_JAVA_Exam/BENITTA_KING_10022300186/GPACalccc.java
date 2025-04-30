import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class GPACalccc {

    private static final String file_name = "gpa_records.txt";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Student GPA Calculator");
        frame.setSize(1000, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(245, 245, 245));

        
        JLabel header = new JLabel("GPA Calculator", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setBorder(new EmptyBorder(10, 0, 10, 0));
        frame.add(header, BorderLayout.NORTH);

        
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topButtonPanel.setBackground(new Color(230, 230, 250));

        JButton addbutton = new JButton("Add Student");
        JButton delbutton = new JButton("Delete");
        JButton editbutton = new JButton("Edit");
        JButton calculategpabutton = new JButton("Calculate GPA");
        JButton saveButton = new JButton("Save");

        Font buttonFont = new Font("SansSerif", Font.PLAIN, 12);
        for (JButton btn : new JButton[]{addbutton, delbutton, editbutton, calculategpabutton, saveButton}) {
            btn.setFont(buttonFont);
            btn.setBackground(new Color(100, 149, 237));
            btn.setForeground(Color.WHITE);
        }

        topButtonPanel.add(addbutton);
        topButtonPanel.add(delbutton);
        topButtonPanel.add(editbutton);
        topButtonPanel.add(calculategpabutton);
        topButtonPanel.add(saveButton);

        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2, 10, 10));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(new Color(245, 245, 245));
        inputPanel.setPreferredSize(new Dimension(350, 0));

        JTextField name_Field = new JTextField();
        JTextField id_Field = new JTextField();
        JTextField course_Field = new JTextField();
        JTextField coursecreditsfield = new JTextField();
        JComboBox<String> gradeComboBox = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D", "E", "F"});
        JComboBox<String> storageComboBox = new JComboBox<>(new String[]{"FILE", "DATABASE"});

        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(name_Field);
        inputPanel.add(new JLabel("Student ID:"));
        inputPanel.add(id_Field);
        inputPanel.add(new JLabel("Course Name:"));
        inputPanel.add(course_Field);
        inputPanel.add(new JLabel("Credit Hours:"));
        inputPanel.add(coursecreditsfield);
        inputPanel.add(new JLabel("Grade:"));
        inputPanel.add(gradeComboBox);
        inputPanel.add(new JLabel("Save to:"));
        inputPanel.add(storageComboBox);

        
        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columnNames = {"Name", "ID", "Course", "Credits", "Grade", "Grade Point", "GPA"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane);

        loadFromFile(model);

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(topButtonPanel, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        frame.add(inputPanel, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);

        // Action listeners remain same
        addbutton.addActionListener(e -> {
            String name = name_Field.getText();
            String id = id_Field.getText();
            String course = course_Field.getText();
            String creditStr = coursecreditsfield.getText();
            String grade = (String) gradeComboBox.getSelectedItem();

            if (name.isEmpty() || id.isEmpty() || course.isEmpty() || creditStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                return;
            }

            try {
                int credit = Integer.parseInt(creditStr);
                double gradePoint = getGradePoint(grade);
                double totalWeightedPoints = 0;
                int totalCredits = 0;

                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 1).toString().equals(id)) {
                        int existingCredit = Integer.parseInt(model.getValueAt(i, 3).toString());
                        double existingGP = Double.parseDouble(model.getValueAt(i, 5).toString());
                        totalWeightedPoints += existingGP * existingCredit;
                        totalCredits += existingCredit;
                    }
                }

                totalWeightedPoints += gradePoint * credit;
                totalCredits += credit;
                double gpa = totalCredits > 0 ? totalWeightedPoints / totalCredits : 0;

                model.addRow(new Object[]{name, id, course, credit, grade, String.format("%.1f", gradePoint), String.format("%.2f", gpa)});
                name_Field.setText(""); id_Field.setText(""); course_Field.setText(""); coursecreditsfield.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Enter a valid number for credits.");
            }
        });

        delbutton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                model.removeRow(selectedRow);
                recalculateGPA(model);
                saveToFile(model);
            } else {
                JOptionPane.showMessageDialog(frame, "Select a row to delete.");
            }
        });

        editbutton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                name_Field.setText(model.getValueAt(selectedRow, 0).toString());
                id_Field.setText(model.getValueAt(selectedRow, 1).toString());
                course_Field.setText(model.getValueAt(selectedRow, 2).toString());
                coursecreditsfield.setText(model.getValueAt(selectedRow, 3).toString());
                gradeComboBox.setSelectedItem(model.getValueAt(selectedRow, 4).toString());
                model.removeRow(selectedRow);
                recalculateGPA(model);
            } else {
                JOptionPane.showMessageDialog(frame, "Select a row to edit.");
            }
        });

        calculategpabutton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Select a student to calculate GPA.");
                return;
            }

            String selectedID = model.getValueAt(selectedRow, 1).toString();
            String studentName = model.getValueAt(selectedRow, 0).toString();
            double totalWeightedPoints = 0;
            int totalCredits = 0;

            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 1).toString().equals(selectedID)) {
                    double gradePoint = Double.parseDouble(model.getValueAt(i, 5).toString());
                    int credit = Integer.parseInt(model.getValueAt(i, 3).toString());
                    totalWeightedPoints += gradePoint * credit;
                    totalCredits += credit;
                }
            }

            double gpa = totalWeightedPoints / totalCredits;
            String formattedGPA = String.format("%.2f", gpa);

            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 1).toString().equals(selectedID)) {
                    model.setValueAt(formattedGPA, i, 6);
                }
            }

            saveToFile(model);
            JOptionPane.showMessageDialog(frame, studentName + "'s GPA is " + formattedGPA);
        });

        saveButton.addActionListener(e -> {
            String storageOption = (String) storageComboBox.getSelectedItem();
            if ("FILE".equals(storageOption)) {
                saveToFile(model);
                JOptionPane.showMessageDialog(frame, "Data saved to " + file_name);
            } else {
                JOptionPane.showMessageDialog(frame, "Database option is under construction.");
            }
        });

        frame.setVisible(true);
    }

    private static void recalculateGPA(DefaultTableModel model) {
        for (int row = 0; row < model.getRowCount(); row++) {
            String currentID = model.getValueAt(row, 1).toString();
            double totalWeightedPoints = 0;
            int totalCredits = 0;

            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 1).toString().equals(currentID)) {
                    double gradePoint = Double.parseDouble(model.getValueAt(i, 5).toString());
                    int credit = Integer.parseInt(model.getValueAt(i, 3).toString());
                    totalWeightedPoints += gradePoint * credit;
                    totalCredits += credit;
                }
            }

            double gpa = totalCredits > 0 ? totalWeightedPoints / totalCredits : 0;

            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 1).toString().equals(currentID)) {
                    model.setValueAt(String.format("%.2f", gpa), i, 6);
                }
            }
        }
    }

    public static double getGradePoint(String grade) {
        return switch (grade) {
            case "A" -> 4.0;
            case "B+" -> 3.5;
            case "B" -> 3.0;
            case "C+" -> 2.5;
            case "C" -> 2.0;
            case "D" -> 1.5;
            case "E" -> 1.0;
            default -> 0.0;
        };
    }

    private static void saveToFile(DefaultTableModel model) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file_name))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    bw.write(model.getValueAt(i, j).toString());
                    if (j < model.getColumnCount() - 1) bw.write(",");
                }
                bw.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error saving: " + ex.getMessage());
        }
    }

    private static void loadFromFile(DefaultTableModel model) {
        File file = new File(file_name);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == model.getColumnCount()) {
                        model.addRow(data);
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error loading data: " + ex.getMessage());
            }
        }
    }
}

