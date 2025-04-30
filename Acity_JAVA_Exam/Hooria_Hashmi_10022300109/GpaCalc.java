//Name Hooria Haider Hashmi


//importing necessary libraries
import java.awt.*; //used for building the GUI
import java.io.FileWriter; // used for writing to file
import javax.swing.*; //used for building gui properties
import javax.swing.table.DefaultTableModel; //managing data in the tables

public class GpaCalc extends JFrame { // creates the class
    private JTextField txtName, txtID; //creates the field for the Name and ID
    private JTextField[] txtCourses = new JTextField[3]; //creates three extra text fields for three different courses 
    private JComboBox<Integer> cmbCredits; //creates a combo box for the credits 
    private JComboBox<String>[] cmbGrades = new JComboBox[3]; //creates another combo box for the grade options
    private JComboBox<String> cmbSaveOption; //a combo box is used because the option to save to file or database is given 
    private JTable table; //creation of table with different columns 
    private DefaultTableModel tableModel; //
    private JTextArea gradeReferenceArea; //an area for a general text message showing the grades and credit points allocated to each grade

    public GpaCalc() {
        setTitle("This is a GPA Calculator made for exam"); //sets the title of the application 
        setDefaultCloseOperation(EXIT_ON_CLOSE); //it exits when closed seamlessly
        setSize(900, 600); //sets the size of the window pop-up
        initComponents(); //lists all the components of the window which will be stated later 
        setVisible(true); //makes the sure the window pops up when loaded 
    }
    private void initComponents() {
        getContentPane().setBackground(new Color(220, 255, 220)); //sets the background color as green 
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)); //gives borders to the window 
        mainPanel.setBackground(new Color(220, 255, 220));

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(220, 255, 220));
        GroupLayout layout = new GroupLayout(inputPanel);
        inputPanel.setLayout(layout); 
        layout.setAutoCreateGaps(true); 
        layout.setAutoCreateContainerGaps(true);

        JLabel lblName = new JLabel("Enter the Name of Student:"); //this displays the first label as student name 
        JLabel lblID = new JLabel("Student ID Required:"); //displays the next credential as student ID
        JLabel lblSave = new JLabel("Save Option (You have a choice):"); //displays the save option 
        JLabel lblCredits = new JLabel("Credit Hours (Enter a number):"); //displays the credit hours but the user is told to enter the number from the reference sheet

        JLabel[] lblCourses = new JLabel[3]; //gives three different labels for courses 
        JLabel[] lblGrades = new JLabel[3];  //gives three different labels for grades

        for (int i = 0; i < 3; i++) { //for loop for iteration over the three courses and three grades that can be taken at a time
            lblCourses[i] = new JLabel("Course " + (i + 1)); //for course inputs 
            lblGrades[i] = new JLabel("Grade " + (i + 1)); //for grade inputs
        }
        txtName = new JTextField(15); //sets the size of the input field for name 
        txtID = new JTextField(15); //sets the sizee for the input field for ID
        Integer[] creditOptions = {1, 2, 3, 4, 5}; //credit points as some courses have more credit hours
        cmbCredits = new JComboBox<>(creditOptions);
        String[] gradeOptions = {"A", "B+", "B", "C+", "C", "D", "E", "F"}; //grades available (B+ and C+)

        for (int i = 0; i < 3; i++) { //For loop for iteration over each course and grade taken
            txtCourses[i] = new JTextField(15);
            cmbGrades[i] = new JComboBox<>(gradeOptions);
        }

        cmbSaveOption = new JComboBox<>(new String[]{"File", "Database"}); //shows the options to either save to file or database as required in the exam sheet

        //the buttons that will be used 
        JButton btnCalculate = new JButton("Calculate GPA"); //this is to calculate the GPA
        btnCalculate.addActionListener(e -> calculateGPA()); //this activates the button

        JButton btnClear = new JButton("Clear Fields"); //this is to clear fields on the window 
        btnClear.addActionListener(e -> clearFields()); //this activates the clear button

        JButton btnDelete = new JButton("Delete Row"); //this deletes the row selected 
        btnDelete.addActionListener(e -> deleteRow()); //this activates the delete button

        JButton btnUpdate = new JButton("Update Row"); //this updates whatever is in the row
        btnUpdate.addActionListener(e -> updateRow()); //this activates the update button

        //the grade reference is to show the user the grade breakdown 
        //it gives a fair idea what the user might get based on their input
        gradeReferenceArea = new JTextArea();
        gradeReferenceArea.setEditable(false);
        gradeReferenceArea.setBackground(new Color(220, 255, 220)); //sets the color
        gradeReferenceArea.setFont(new Font("Monospaced", Font.BOLD, 12)); //boldens the text message 
        gradeReferenceArea.setText("GRADE POINT REFERENCE:\n----------------------\nA  = 4.0 points\nB+ = 3.5 points\nB  = 3.0 points\nC+ = 2.5 points\nC  = 2.0 points\nD  = 1.5 points\nE  = 1.0 points\nF  = 0.0 points");

        JScrollPane referenceScrollPane = new JScrollPane(gradeReferenceArea); //making the message for users to know the grading system
        referenceScrollPane.setPreferredSize(new Dimension(280, 120)); //sets dimension
        referenceScrollPane.setBorder(BorderFactory.createTitledBorder("Grade Point Scale"));
       
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup() 
                .addGroup(layout.createParallelGroup()
                        .addComponent(lblName)
                        .addComponent(lblID)
                        .addComponent(lblCourses[0])
                        .addComponent(lblGrades[0])
                        .addComponent(lblCourses[1])
                        .addComponent(lblGrades[1])
                        .addComponent(lblCourses[2])
                        .addComponent(lblGrades[2])
                        .addComponent(lblCredits)
                        .addComponent(lblSave)
                        .addComponent(btnCalculate)
                        .addComponent(btnClear)
                        .addComponent(btnDelete)
                        .addComponent(btnUpdate))
                .addGroup(layout.createParallelGroup()
                        .addComponent(txtName)
                        .addComponent(txtID)
                        .addComponent(txtCourses[0])
                        .addComponent(cmbGrades[0])
                        .addComponent(txtCourses[1])
                        .addComponent(cmbGrades[1])
                        .addComponent(txtCourses[2])
                        .addComponent(cmbGrades[2])
                        .addComponent(cmbCredits)
                        .addComponent(cmbSaveOption));
                        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblName).addComponent(txtName))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblID).addComponent(txtID))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblCourses[0]).addComponent(txtCourses[0]))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblGrades[0]).addComponent(cmbGrades[0]))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblCourses[1]).addComponent(txtCourses[1]))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblGrades[1]).addComponent(cmbGrades[1]))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblCourses[2]).addComponent(txtCourses[2]))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblGrades[2]).addComponent(cmbGrades[2]))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblCredits).addComponent(cmbCredits))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblSave).addComponent(cmbSaveOption))
                        .addComponent(btnCalculate)
                        .addComponent(btnClear)
                        .addComponent(btnDelete)
                        .addComponent(btnUpdate);
        
                layout.setHorizontalGroup(layout.createParallelGroup().addGroup(hGroup));
                layout.setVerticalGroup(vGroup);
        
                

        //for table creation
        JPanel tablePanel = new JPanel(new BorderLayout()); 
        tablePanel.setBackground(new Color(220, 255, 220));
        String[] columns = {"Name", "ID", "Course", "Credit", "Grade", "GPA"}; //gives the names of each column as data will be stored there for the user to see
        tableModel = new DefaultTableModel(columns, 0); //sets the column to begin from zero
        table = new JTable(tableModel);
        table.setBackground(new Color(240, 255, 240)); //sets the bakground color as white
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(220, 255, 220));
        bottomPanel.add(referenceScrollPane); //sets the reference pane 

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, tablePanel); //this is to divide the input section from the display section
        splitPane.setDividerLocation(350);
        splitPane.setBackground(new Color(220, 255, 220)); //sets the background color as white

        mainPanel.add(splitPane, BorderLayout.CENTER); //centers the borders 
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);  
        getContentPane().add(mainPanel); //adds the main panel 
    }
    //the CRUD operations are put in different classes 
    private void calculateGPA() {
        try {
            String name = txtName.getText();
            String id = txtID.getText();
            String saveOption = cmbSaveOption.getSelectedItem().toString();

            if (name.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and ID are required.");
                return;
            }
           //making use of getters 
            int credit = (int) cmbCredits.getSelectedItem();
            double totalPoints = 0; //variable declaration
            //using for loop to calculate the GPA 
            for (int i = 0; i < 3; i++) {
                String grade = cmbGrades[i].getSelectedItem().toString();
                double points = getGradePoint(grade);
                totalPoints += credit * points; //grade points * credit hours as stated in the question
            }
            //GPA Calculation Formula GPA = sum(Grade points * credit hours) / sum(total credit hours)
            double totalCredits = credit * 3; //multiplies the credit by 3 to get the total credits
            double gpa = totalPoints / totalCredits;
            String gpaStr = String.format("%.2f", gpa); //conversion from int to str

            for (int i = 0; i < 3; i++) { //performs an iteration using for loop
                tableModel.addRow(new Object[]{
                        name, id,
                        txtCourses[i].getText(),
                        credit,
                        cmbGrades[i].getSelectedItem(), //making use of getters
                        i == 0 ? gpaStr : "" //the gpa must be converted into a string
                });
            }
           //FILE SAVING CODE USING (MAKING USE OF IF-ELSE STATEMENT)
           //taught in class
            if (saveOption.equals("File")) {
                FileWriter writer = new FileWriter("gpa_records.txt", true); //saves the file with the name gpa records.txt as given in the question paper
                writer.write("Name: " + name + ", ID: " + id + ", GPA: " + gpaStr + "\n"); //writes to file in the order given
                writer.close();
                JOptionPane.showMessageDialog(this, "Saved to file."); //Gives the message "saved to file"
            } else {
                JOptionPane.showMessageDialog(this, "Database save: Under Construction."); //since we can only focus on one 
            }
        } catch (Exception e) { //uses catch keyword
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); //gives an error message otherwise
        }
    }
    //clears  the fields for new input 
    private void clearFields() {
        txtName.setText("");
        txtID.setText("");
        for (int i = 0; i < 3; i++) {
            txtCourses[i].setText(""); 
            cmbGrades[i].setSelectedIndex(0); //sets the index to 0 thereby clearing it
        }
        cmbCredits.setSelectedIndex(0);
        cmbSaveOption.setSelectedIndex(0);
    }
    //performs the delete operation
    private void deleteRow() {
        int selectedRow = table.getSelectedRow(); //gets from the table
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow); //if row is selected delete the row else ask the user to select a row
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }
    //performs the update operation
    private void updateRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String name = txtName.getText();
            String id = txtID.getText();
            int credit = (int) cmbCredits.getSelectedItem();

            for (int i = 0; i < 3; i++) { //making use of loops to update selected rows
                tableModel.setValueAt(name, selectedRow + i, 0); //setting value at 
                tableModel.setValueAt(id, selectedRow + i, 1);
                tableModel.setValueAt(txtCourses[i].getText(), selectedRow + i, 2);
                tableModel.setValueAt(credit, selectedRow + i, 3);
                tableModel.setValueAt(cmbGrades[i].getSelectedItem(), selectedRow + i, 4);
            }
            //updates gpa as well
            double totalPoints = 0;
            for (int i = 0; i < 3; i++) {
                totalPoints += getGradePoint(cmbGrades[i].getSelectedItem().toString()) * credit;
            }

            double gpa = totalPoints / (credit * 3);
            tableModel.setValueAt(String.format("%.2f", gpa), selectedRow, 5);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to update."); //show a message to ask the user to select a row to update
        }
    }
    //allocates each grade with its corresponding gpa
    private double getGradePoint(String grade) {
        return switch (grade) { //uses the switch case statements instead to return the credit based on grade
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
    //to run the program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new GpaCalc();
        });
    }
}







