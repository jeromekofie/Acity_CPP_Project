import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GpaCalculator {

    private static final String filename = "gpa_records.txt";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Java Exam- Jesudunyin Ayobolu- 10012300031");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);

       
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(20, 20, 350, 400);
        panel.setBackground(Color.WHITE);
        frame.add(panel);

        JLabel nameLabel = new JLabel("Student Name:");
        nameLabel.setBounds(10, 20, 100, 25);
        panel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(120, 20, 200, 25);
        panel.add(nameField);

        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setBounds(10, 60, 100, 25);
        panel.add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(120, 60, 200, 25);
        panel.add(idField);

        JLabel courseLabel = new JLabel("Course Name:");
        courseLabel.setBounds(10, 100, 100, 25);
        panel.add(courseLabel);

        JTextField courseField = new JTextField();
        courseField.setBounds(120, 100, 200, 25);
        panel.add(courseField);

        JLabel coursecredits = new JLabel("Credit Hours:");
        coursecredits.setBounds(10, 140, 100, 25);
        panel.add(coursecredits);

        JTextField coursecreditsfield = new JTextField();
        coursecreditsfield.setBounds(120, 140, 200, 25);
        panel.add(coursecreditsfield);

        JLabel stu_scorelabel = new JLabel("Grade:");
        stu_scorelabel.setBounds(10, 180, 100, 25);
        panel.add(stu_scorelabel);

        JComboBox<String> gradeComboBox = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D", "E", "F"});
        gradeComboBox.setBounds(120, 180, 200, 25);
        panel.add(gradeComboBox);

        JLabel storageLabel = new JLabel("Save to:");
        storageLabel.setBounds(10, 220, 100, 25);
        panel.add(storageLabel);

        JComboBox<String> saveComboBox = new JComboBox<>(new String[]{"File", "Database"});
        saveComboBox.setBounds(120, 220, 200, 25);
        panel.add(saveComboBox);

       
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 5, 5));
        buttonPanel.setBounds(20, 430, 150, 150);
        buttonPanel.setBackground(Color.GREEN);
        
        frame.add(buttonPanel);


        JButton addbutton = new JButton("Add Student");
        addbutton.setBackground(Color.GREEN);
        buttonPanel.add(addbutton);

        JButton delbutton = new JButton("Delete Student");
        delbutton.setBackground(Color.GREEN);
        buttonPanel.add(delbutton);

        JButton editbutton = new JButton("Edit Student");
        editbutton.setBackground(Color.GREEN);
        buttonPanel.add(editbutton);

        JButton calculategpabutton = new JButton("Calculate GPA");
        calculategpabutton.setBackground(Color.GREEN);
        buttonPanel.add(calculategpabutton);

        JButton saveButton = new JButton("Save Data");
        saveButton.setBackground(Color.GREEN);
        buttonPanel.add(saveButton);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBounds(400, 20, 550, 550);
        frame.add(tablePanel);

        String[] columnNames = {"Student Name", "ID", "Course", "Credit Hours", "Grade", "Grade Point", "Current GPA"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setBackground (Color.GREEN);
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane);

        loadFromFile(model);

       
        addbutton.addActionListener(e -> {
            String name = nameField.getText();
            String id = idField.getText();
            String course = courseField.getText();
              String creditStr = coursecreditsfield.getText();
            String grade = (String) gradeComboBox.getSelectedItem();

            if (name.isEmpty() || id.isEmpty() || course.isEmpty() || creditStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                return;
            }

            try {
                int credit = Integer.parseInt(creditStr);
                double gradePoint = getGradePoint(grade);


                double totalgradepoints = 0;
                int totalcredithours = 0;

                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 1).toString().equals(id)) {
                        int existingCredit = Integer.parseInt(model.getValueAt(i, 3).toString());
                        double existingGP = Double.parseDouble(model.getValueAt(i, 5).toString());
                        totalgradepoints += existingGP * existingCredit;
                        totalcredithours += existingCredit;
                    }
                }

                totalgradepoints += gradePoint * credit;
                totalcredithours += credit;
                double gpa = totalcredithours > 0 ? totalgradepoints / totalcredithours : 0;

                model.addRow(new Object[]{
                    name, id, course,  credit, grade, String.format("%.1f", gradePoint),String.format("%.2f", gpa)
                });

                clearFields(nameField, idField, courseField, coursecreditsfield);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers for credit hours.");
            }
        });

        delbutton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                model.removeRow(selectedRow);
                recalculateGPA(model);
                saveToFile(model);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a row to delete.");
            }
        });

        editbutton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                nameField.setText(model.getValueAt(selectedRow, 0).toString());
                idField.setText(model.getValueAt(selectedRow, 1).toString());
                courseField.setText(model.getValueAt(selectedRow, 2).toString());
                coursecreditsfield.setText(model.getValueAt(selectedRow, 3).toString());
                gradeComboBox.setSelectedItem(model.getValueAt(selectedRow, 4).toString());

                model.removeRow(selectedRow);
                recalculateGPA(model);
            } else {
                JOptionPane.showMessageDialog(frame, " select a row to edit bro.");
            }
        });

        calculategpabutton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Kindly select a student in the table!!!!. ");
                return;
            }

            String selectedID = model.getValueAt(selectedRow, 1).toString();
            String studentName = model.getValueAt(selectedRow, 0).toString();

            double totalgradepoints = 0;
            int totalcredithours = 0;

            for (int i = 0; i < model.getRowCount(); i++) {
                String id = model.getValueAt(i, 1).toString();
                if (id.equals(selectedID)) {
                    double gradePoint = Double.parseDouble(model.getValueAt(i, 5).toString());
                    int credit = Integer.parseInt(model.getValueAt(i, 3).toString());

                    totalgradepoints += gradePoint * credit;
                    totalcredithours += credit;
                }
            }

            if (totalcredithours == 0) {
                JOptionPane.showMessageDialog(frame, "No credit hours found for this student. he's not in kelly's class(lol)");
                return;
            }

            double gpa = totalgradepoints / totalcredithours;
            String formattedGPA = String.format("%.2f", gpa);

            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 1).toString().equals(selectedID)) {
                    model.setValueAt(formattedGPA, i, 6);
                }
            }

            saveToFile(model);
            JOptionPane.showMessageDialog(frame, "Calculated GPA for " + studentName + ": " + formattedGPA);
        });

        saveButton.addActionListener(e -> {
            String storageOption = (String) saveComboBox.getSelectedItem();
            
            if (storageOption.equals("File")) {
                saveToFile(model);
                JOptionPane.showMessageDialog(frame, "Data saved to " + filename);
            } else {
                JOptionPane.showMessageDialog(frame, "Database: Under Construction.");
            }
        });

        frame.setVisible(true);
    }

    private static void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private static void recalculateGPA(DefaultTableModel model) {
      
        for (int row = 0; row < model.getRowCount(); row++) {
            String currentID = model.getValueAt(row, 1).toString();
            
            double totalgradepoints = 0;
            int totalcredithours = 0;
            
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 1).toString().equals(currentID)) {
                    double gradePoint = Double.parseDouble(model.getValueAt(i, 5).toString());
                    int credit = Integer.parseInt(model.getValueAt(i, 3).toString());
                    totalgradepoints += gradePoint * credit;
                    totalcredithours += credit;
                }
            }
            
            double gpa = totalcredithours > 0 ? totalgradepoints / totalcredithours : 0;
            
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 1).toString().equals(currentID)) {
                    model.setValueAt(String.format("%.2f", gpa), i, 6);
                }
            }
        }
    }

    public static double getGradePoint(String grade) {
        switch (grade) {
            case "A": return 4.0;
            case "B+": return 3.5;
            case "B": return 3.0;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "D": return 1.5;
            case "E": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }

    private static void saveToFile(DefaultTableModel model) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    bw.write(model.getValueAt(i, j).toString());
                    if (j < model.getColumnCount() - 1) bw.write(",");
                }
                bw.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error saving to file: " + ex.getMessage());
        }
    }

    private static void loadFromFile(DefaultTableModel model) {
        File file = new File(filename);
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
                JOptionPane.showMessageDialog(null, "Error loading data from file: " + ex.getMessage());
            }
        }
    }
}
