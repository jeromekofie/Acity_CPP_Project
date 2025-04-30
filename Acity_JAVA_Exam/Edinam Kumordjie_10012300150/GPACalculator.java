import javax.swing.*; //file yet to be implemented
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class GPACalculator extends JFrame { //making it a window with title, borders, and buttons.

    private JTextField nameField = new JTextField(15); //the part where you input for student's name 
    private JTextField idField = new JTextField(15); //the paart for student's ID.
    private JTextField creditField = new JTextField(5);//This represents the creddit hours of the course
    private JComboBox<String> gradeCombo = new JComboBox<>(new String[]{"A","B+","B","C+","C","D","E","F"});
    private DefaultTableModel courseModel = new DefaultTableModel(new Object[]{"Course","Credits","Grade"}, 0);//This holds the rows of course entriesand it acts like a data model for a table.
    private JTextArea resultArea = new JTextArea(3,20);
    private Connection conn;

    public GPACalculator() {
        setupFrame();
        setupDatabase();
        setupUI();
    } //so this part initializes the window sets up database connection and shows you how the interface is gonna look like

    private void setupFrame() {
        setTitle("GPA Calculator");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }//This sets the window title and size.It ensures the app exits when the window closes.WE'll use the borderlayout here.


    private void setupDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gpa_db", "root", "Ashun123!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Couldn't connect to database");
        }
    }//where the connectio to the datatbase starts 


    private void setupUI() {
        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(0,2,5,5));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Credits:"));
        inputPanel.add(creditField);
        inputPanel.add(new JLabel("Grade:"));
        inputPanel.add(gradeCombo);//It Contains labeled text fields for name, ID, credits, and grade selection. it also uses GridLayout to align components in two columns.


        // jButtons..why is it called jbuttons tho
        JButton addBtn = new JButton("Add Course");
        JButton calcBtn = new JButton("Calculate GPA");
        JButton saveBtn = new JButton("Save");

        addBtn.addActionListener(e -> addCourse());
        calcBtn.addActionListener(e -> calculateGPA());
        saveBtn.addActionListener(e -> saveData());

        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(calcBtn);
        btnPanel.add(saveBtn);

        //the Layoutof the interface
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(new JTable(courseModel)), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        add(new JScrollPane(resultArea), BorderLayout.EAST);
    }

    private void addCourse() {
        if (!creditField.getText().isEmpty()) {
            courseModel.addRow(new Object[]{
                "Course " + (courseModel.getRowCount()+1),
                creditField.getText(),
                gradeCombo.getSelectedItem()
            });
        }
    }

    private void calculateGPA() {
        Map<String, Double> grades = Map.of(
            "A",4.0, "B+",3.5, "B",3.0, 
            "C+",2.5, "C",2.0, "D",1.5, "E",1.0, "F",0.0
        );//gives a mapping of letter grades to GPA points.(please i learnt something new and implemented it.its made my work unique.muah!no bars)
        

        double points = 0, credits = 0;
        for (int i = 0; i < courseModel.getRowCount(); i++) {
            double cr = Double.parseDouble(courseModel.getValueAt(i,1).toString());
            String gr = courseModel.getValueAt(i,2).toString();
            points += cr * grades.get(gr);
            credits += cr;
        }

        double gpa = points * credits /credits;
        resultArea.setText(String.format("Name: %s\nID: %s\nGPA: %.2f", 
            nameField.getText(), idField.getText(), gpa));
    } 

    private void saveData() {
        try {
            String sql = "INSERT INTO gpa_records (name, id, gpa) VALUES (?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);//This creates a prepared statement using the SQL string and the existing database connection
            stmt.setString(1, nameField.getText()); //This Sets the first in the query with the text entered in the name text field.
            stmt.setString(2, idField.getText());
            stmt.setDouble(3, Double.parseDouble(resultArea.getText().split("GPA: ")[1]));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Saved to database and File is Yet to be Implemented");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving: " + e.getMessage());
        }
    }//This method saves the student's name, ID, and GPA into the MySQL gpa_records table after the GPA has been calculated.

    public static void main(String[] args) {
        new GPACalculator().setVisible(true);
    }
}


//CREATE DATABASE IF NOT EXISTS gpa_db;

//USE gpa_db;

//CREATE TABLE IF NOT EXISTS gpa_records (
 //   id VARCHAR(20) PRIMARY KEY,
   // name VARCHAR(100) NOT NULL,
   // gpa DOUBLE NOT NULL
//);
