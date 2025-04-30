import java.awt.*;
import java.io.*;
import java.nio.file.*;
import javax.swing.*;

public class GPACalculator {
    private JFrame frame;
    private JTextField nameField, rollField, courseField, creditField, grades;
    private JTextArea displayArea;
    private File file = new File("gpa_records.txt");

    public GPACalculator() {
        frame = new JFrame("GPA CALCULATOR");
         frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

     JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(255, 240, 245));

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(new Color(255, 240, 245));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Roll No:"));
        rollField = new JTextField();
        inputPanel.add(rollField);

        inputPanel.add(new JLabel("Course:"));
        courseField = new JTextField();
        inputPanel.add(courseField);

        inputPanel.add(new JLabel("Credit hours:"));
        creditField = new JTextField();
        inputPanel.add(creditField);

        inputPanel.add(new JLabel("Grade:"));
        grades = new JTextField();
        inputPanel.add(grades);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(255, 240, 245));
    JButton addButton = new JButton("Add");
        JButton viewButton = new JButton("View");
        JButton calcButton = new JButton("Calculate");
        JButton deleteButton = new JButton("Delete");
        JButton dbButton = new JButton("Save ");

        JButton[] buttons = {addButton, viewButton, calcButton, deleteButton, dbButton};
        for (JButton btn : buttons) {
            btn.setBackground(new Color(255, 182, 193));
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(100, 30));
        }

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(calcButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(dbButton);


        displayArea = new JTextArea(10, 40);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
        addButton.addActionListener(e -> addRecord());
        viewButton.addActionListener(e -> viewRecords());
        calcButton.addActionListener(e -> calcRecord());
        deleteButton.addActionListener(e -> deleteRecord());
        dbButton.addActionListener(e -> dbRecords());
    }

    private void addRecord() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(nameField.getText() + "," + rollField.getText() + "," + courseField.getText());
            bw.newLine();
            JOptionPane.showMessageDialog(frame, "Record added!");
            clearFields();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error writing to file.");
        }
    }

    private void viewRecords() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            displayArea.setText("");
            String line;
            while ((line = br.readLine()) != null) {
                displayArea.append(line + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading from file.");
        }
    }
    private void calcRecord() {
        try {
            java.util.List<String> lines = Files.readAllLines(file.toPath());
            String rollNo = rollField.getText();
            boolean found = false;

            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts.length >= 3 && parts[1].equals(rollNo)) {
                    double grade = Double.parseDouble(grades.getText());
                    double credit = Double.parseDouble(creditField.getText());
                    double gpa = grade * credit;

                    lines.set(i, parts[0] + "," + parts[1] + "," + parts[2] + "," + credit + "," + gpa);
                found = true;
                    break;
                }
            }

            if (found) {
                Files.write(file.toPath(), lines);
                JOptionPane.showMessageDialog(frame, "Record updated with GPA results");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(frame, "Roll No not found.");
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Error updating file. Please check your input.");
        }
    }

     private void deleteRecord() {
        try {
            java.util.List<String> lines = Files.readAllLines(file.toPath());
            String rollNo = rollField.getText();
            boolean removed = lines.removeIf(line -> {
                String[] parts = line.split(",");
                return parts.length >= 2 && parts[1].equals(rollNo);
            });

            if (removed) {
                Files.write(file.toPath(), lines);
                JOptionPane.showMessageDialog(frame, "Record deleted!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(frame, "Roll No not found.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error deleting record.");
        }
    }
    private void dbRecords() {
        JOptionPane.showMessageDialog(frame, "YET to be implemented.");
    }

     private void clearFields() {
        nameField.setText("");
    rollField.setText("");
        courseField.setText("");
        creditField.setText("");
        grades.setText("");
    }

      public static void main(String[] args) {
         SwingUtilities.invokeLater(GPACalculator::new);
    }
}