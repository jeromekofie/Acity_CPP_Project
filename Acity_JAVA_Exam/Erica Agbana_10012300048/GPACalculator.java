import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


public class GPACalculator extends JFrame implements ActionListener {
    private JTextField nameField, idField;
    private JTextField[] courseFields, creditFields, gradesFields;
    private JComboBox<String> saveOptionBox;
    private JTextArea outputArea;
    private JButton calculateButton;

    public GPACalculator(){ 
        setTitle("GPA Calculator App");
        setSize(250,20 );
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Color lilac = new Color(200,162,200);
        Color lavender = new Color(230, 230, 250);
        Color softPurple = new Color (147, 112, 219 );
        Color White = Color.WHITE;


        JLabel header = new JLabel ("GPA Calculator", JLabel.CENTER);
        header.setFont(new Font("Times New Roman", Font.BOLD,20) );
        header.setOpaque(true);
        header.setBackground(softPurple);
        header.setForeground(White);
        add(header, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(10,4,10,10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));  
        inputPanel.setBackground(lavender);

        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        courseFields = new JTextField[8];
        gradesFields = new JTextField[8];
        creditFields = new JTextField[8];

        for(int i = 0; i < 8; i++ ){
            inputPanel.add (new JLabel("Course"+(i+1)+":"));
            courseFields [i] =  new JTextField();
            inputPanel.add(courseFields[i]);

            inputPanel.add(new JLabel ("Grade(A-f):"));
            gradesFields [i] = new JTextField();
            inputPanel.add(gradesFields[i]);
            
            inputPanel.add(new JLabel("Credit Hours:"));
            creditFields [i] = new JTextField();
            inputPanel.add(creditFields[i]);    
        }

        inputPanel.add(new JLabel("Save to:"));
        saveOptionBox = new JComboBox <> (new String []{"File", "Database"});
        inputPanel.add(saveOptionBox);

        calculateButton = new JButton("Calculate GPA");
        calculateButton.setBackground(lilac);
        calculateButton.setForeground(Color.BLACK);
        calculateButton.addActionListener(this);
        inputPanel.add(calculateButton);
      
        add(inputPanel, BorderLayout.CENTER);


        outputArea = new JTextArea();
        outputArea.setFont(new Font("Arial", Font.PLAIN, 14));
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(245, 240, 255));
        outputArea.setBorder(BorderFactory.createTitledBorder("Results"));
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);
    }
    
    private double getPoint(String grade){
        switch (grade.toUpperCase()){
            case "A": return 4.0;
            case "B+": return 3.5;
            case "B" : return 3.0;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "D": return 1.5;
            case "E": return 1.0;
            case "F": return 0.0;
            default: return -1;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        StringBuilder result = new StringBuilder("Name: " + name + "| ID:" + id + "\n\n");

        double totalPoints = 0;
        int totalCredits = 0;

        try{
            for(int i = 0; i < 8; i++){
                String course = courseFields[i].getText().trim();
                int credit = Integer.parseInt(creditFields[i].getText().trim());
                String grade = gradesFields[i].getText().trim();
                double point =getPoint(grade);

                if (point < 0) throw new Exception("Grade Entered is Invalid!");
                totalPoints += point * credit;
                totalCredits += credit;

                result.append("Course: ").append(course)
                                      .append("| Credit: ").append(credit)
                                      .append("| Grade: ").append(grade)
                                      .append("| Point: ").append(point)
                                      .append("\n" );

            }
            double gpa = totalPoints / totalCredits;
            result.append("\nGPA: ").append(String.format("%.2f", gpa)).append("\n");

            String option = (String) saveOptionBox.getSelectedItem();
            if (option.equals("File")){
                saveToFile(result.toString());
                result.append("Saved to File: gpa_records.txt\n");
                result.append("Database: Under Construction");
            }else{
                result.append("File: Yet to be Implemented\n");
                result.append("Database: Under Construction\n");
            }
            outputArea.setText(result.toString());

        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Please Enter Valid Data", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void saveToFile(String content) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("gpa_records.txt", true))) {
            writer.println(content);
            writer.println("---------------------------------------------------");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error writing to file.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
public static void main(String [] args){
    SwingUtilities.invokeLater(() -> new GPACalculator().setVisible(true));
}
}


    
        


    