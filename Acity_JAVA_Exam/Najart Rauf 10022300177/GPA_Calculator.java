import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GPA_Calculator {

    static class StudentReportCardFrame extends JFrame {
        public StudentReportCardFrame() {
            setTitle("GPA Calculator");
            setSize(800, 700); // Increased window size
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            getContentPane().setBackground(new Color(240, 248, 255));

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel header = new JLabel("GPA Calculator");
            header.setFont(new Font("Arial", Font.BOLD, 24)); // Larger font for header
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(header, gbc);

            String[] infoLabels = {"Name", "Roll Number", "Semester"};
            JTextField[] infoFields = new JTextField[infoLabels.length];

            for (int i = 0; i < infoLabels.length; i++) {
                gbc.gridwidth = 1;
                gbc.gridy++;
                gbc.gridx = 0;
                panel.add(createLabel(infoLabels[i] + ":"), gbc);
                gbc.gridx = 1;
                infoFields[i] = new JTextField(20);
                panel.add(infoFields[i], gbc);
            }

            JLabel gradesLabel = new JLabel("Enter Grades and Credit Hours:");
            gradesLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font for section label
            gbc.gridy++;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            panel.add(gradesLabel, gbc);

            JTextField[] gradeFields = new JTextField[5];
            JTextField[] creditFields = new JTextField[5];
            String[] subjects = {"Subject 1", "Subject 2", "Subject 3", "Subject 4", "Subject 5"};

            for (int i = 0; i < subjects.length; i++) {
                gbc.gridwidth = 1;
                gbc.gridy++;
                gbc.gridx = 0;
                panel.add(createLabel(subjects[i] + " Grade (A-F):"), gbc);
                gbc.gridx = 1;
                gradeFields[i] = new JTextField(5);
                panel.add(gradeFields[i], gbc);

                gbc.gridx = 0;
                gbc.gridy++;
                panel.add(createLabel(subjects[i] + " Credit Hours:"), gbc);
                gbc.gridx = 1;
                creditFields[i] = new JTextField(5);
                panel.add(creditFields[i], gbc);
            }

            JButton calculateBtn = new JButton("Calculate GPA");
            styleButton(calculateBtn);
            gbc.gridy++;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            panel.add(calculateBtn, gbc);

            calculateBtn.addActionListener(e -> {
                try {
                    float totalPoints = 0;
                    float totalCredits = 0;

                    for (int i = 0; i < gradeFields.length; i++) {
                        String grade = gradeFields[i].getText().toUpperCase();
                        float credit = Float.parseFloat(creditFields[i].getText());
                        float gradePoint = getGradePoint(grade);

                        totalPoints += gradePoint * credit;
                        totalCredits += credit;
                    }

                    float gpa = totalPoints / totalCredits;
                    saveToFile(infoFields[0].getText(), infoFields[1].getText(), infoFields[2].getText(), gpa);
                    JOptionPane.showMessageDialog(this, "GPA Calculated: " + String.format("%.2f", gpa));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please check your entries.");
                }
            });

            add(panel);
            setVisible(true);
        }

        private float getGradePoint(String grade) {
            switch (grade) {
                case "A":
                    return 4.0f;
                case "B+":
                    return 3.5f;
                case "B":
                    return 3.0f;
                case "C+":
                    return 2.5f;
                case "C":
                    return 2.0f;
                case "D":
                    return 1.5f;
                case "E":
                    return 1.0f;
                case "F":
                    return 0.0f;
                default:
                    throw new IllegalArgumentException("Invalid grade: " + grade);
            }
        }

        private void saveToFile(String name, String roll, String semester, float gpa) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("gpa_records.txt", true))) {
                writer.write("Name: " + name + ", Roll: " + roll + ", Semester: " + semester + ", GPA: " + String.format("%.2f", gpa));
                writer.newLine();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving to file.");
            }
        }

        private JLabel createLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            label.setForeground(new Color(60, 60, 60));
            return label;
        }

        private void styleButton(JButton button) {
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setBackground(new Color(255, 182, 193));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentReportCardFrame::new);
    }
}