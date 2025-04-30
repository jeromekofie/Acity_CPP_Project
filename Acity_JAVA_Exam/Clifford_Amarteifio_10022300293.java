import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

/**
 * GPA CALCULATOR  Application
 * A program to Calculate the accumulated credit hours 
 * and scores and translate them in to credit hours
 */
public class GPAcalc  {
    private static final String TXT_FILE = "gpa_records.txt";
    private static final String[] MESSAGES = {
        "Grade saved successfully!",
        "Record added.",
        "Grade recorded.",
        "Successfully saved.",
        

    };

    private static class GuiComponents {
        private final JFrame frame;
        private final JTextField nameField;
        private final JTextField gradeField;
        private final JTextArea displayArea;
        private final JTextField courseField;
        private final JTextField idField;
        private final JTextField creditField;
        private final JTextField GPAfield;
        
        public GuiComponents() {
            frame = new JFrame("GPA CALCULATOR");
            nameField = new JTextField(20);
            gradeField = new JTextField(20);
            courseField = new JTextField(20);
            idField = new JTextField(20);
            creditField = new JTextField(20);
            GPAfield = new JTextField(20);
            displayArea = new JTextArea(10, 30);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GuiComponents gui = new GuiComponents();
            gui.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gui.frame.setSize(400, 300);

            JPanel mainPanel = new JPanel(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(5, 5, 5, 5);

            gui.displayArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(gui.displayArea);

            JButton saveButton = createButton("Save");
            JButton viewButton = createButton("View");
            JButton exitButton = createButton("Exit");
            

            saveButton.addActionListener(e -> {
                String Name = gui.nameField.getText();
                String Grade = gui.gradeField.getText();
                String Id = gui.idField.getText();
                String Course = gui.courseField.getText();
                
                saveGrade(Name, Grade, Id,Course, Course, gui);
            });

            viewButton.addActionListener(e -> viewGrades(gui));
            exitButton.addActionListener(e -> System.exit(0));

            assembleLayout(mainPanel, constraints, gui, saveButton, viewButton, exitButton, scrollPane);

            gui.frame.add(mainPanel);
            gui.frame.setVisible(true);
        });
    }

    private static void assembleLayout(JPanel mainPanel, GridBagConstraints c, 
            GuiComponents gui, JButton saveButton, JButton viewButton, JButton exitButton, JScrollPane scrollPane) {
        addComponentToLayout(mainPanel, new JLabel("Name:"), c, 0, 0);
        addComponentToLayout(mainPanel, gui.nameField, c, 1, 0);
        
        addComponentToLayout(mainPanel, new JLabel("ID:"), c, 0, 1);
        addComponentToLayout(mainPanel, gui.idField, c, 1, 1);
        
        addComponentToLayout(mainPanel, new JLabel("Course:"), c, 0, 2);
        addComponentToLayout(mainPanel, gui.courseField, c, 1, 2);
        
        addComponentToLayout(mainPanel, new JLabel("Grade:"), c, 0, 3);
        addComponentToLayout(mainPanel, gui.gradeField, c, 1, 3);

        addComponentToLayout(mainPanel, new JLabel("Credit Hours:"), c, 0, 4);
        addComponentToLayout(mainPanel, gui.creditField, c, 1, 4);

        addComponentToLayout(mainPanel, new JLabel("GPA:"), c, 0, 5);
        addComponentToLayout(mainPanel, gui.GPAfield, c, 1, 5);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(exitButton);
        
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;
        mainPanel.add(buttonPanel, c);

        c.gridy = 7;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        mainPanel.add(scrollPane, c);
    }

    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        return button;
    }

    private static void saveGrade(String studentName, String grade, String id, String course, String credit, GuiComponents gui) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TXT_FILE, true))) {
            String timestamp = LocalDateTime.now().toString();
            String entry = String.format("%s | Student: %s | GPA: %s", 
                timestamp, studentName, grade);
            writer.write(entry);
            writer.newLine();
            
            String message = MESSAGES[new Random().nextInt(MESSAGES.length)];
            JOptionPane.showMessageDialog(gui.frame, message);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(gui.frame, "Error saving grade: " + ex.getMessage());
        }
    }

    private static void viewGrades(GuiComponents gui) {
        try {
            List<String> grades = Files.readAllLines(Paths.get(TXT_FILE));
            gui.displayArea.setText(String.join("\n", grades));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(gui.frame, "Error reading grades: " + ex.getMessage());
        }
    }

    private static void addComponentToLayout(JPanel panel, Component comp, 
            GridBagConstraints c, int x, int y) {
        c.gridx = x;
        c.gridy = y;
        panel.add(comp, c);
    }
}