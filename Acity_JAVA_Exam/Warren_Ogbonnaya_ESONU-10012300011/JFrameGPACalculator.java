import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.*;

public class JFrameGPACalculator extends JFrame {
    
    private static final Map<String, Double> GRADE_POINTS = new HashMap<>();
    static {
        GRADE_POINTS.put("A", 4.0);GRADE_POINTS.put("B+", 3.5);GRADE_POINTS.put("B", 3.0);GRADE_POINTS.put("C+", 2.5);GRADE_POINTS.put("C", 2.0);GRADE_POINTS.put("D", 1.5);GRADE_POINTS.put("E", 1.0);GRADE_POINTS.put("F", 0.0);
    }
    
    private JTextField nameField, idField, courseField, creditField;
    private JComboBox<String> gradeComboBox;
    private JTextArea resultArea, inputDisplayArea;
    private JButton addButton, deleteButton, updateButton, calculateButton, saveButton, addCourseButton;
    private JRadioButton fileRadio, dbRadio;
    
    private ArrayList<Student> students = new ArrayList<>();
    private DefaultListModel<String> studentListModel = new DefaultListModel<>();
    private DefaultListModel<String> courseListModel = new DefaultListModel<>();
    private JList<String> studentList, courseList;
    private Student currentStudent;
    
    public JFrameGPACalculator() {
        
        setTitle("JFrame GPA Calculator Wvrr3n");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        setContentPane(new JPanel() {
            private Image backgroundImage = new ImageIcon("C:\\Users\\DELL\\OneDrive\\Pictures\\download.jpeg").getImage(); // add your own image path and upload here

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        });

        initComponents();


        add(createInputPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        nameField = new JTextField(15);
        idField = new JTextField(15);
        courseField = new JTextField(15);
        creditField = new JTextField(5);
        
        gradeComboBox = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D", "E", "F"});
        
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        
        inputDisplayArea = new JTextArea(5, 30);
        inputDisplayArea.setEditable(false);
        inputDisplayArea.setBorder(BorderFactory.createTitledBorder("Current Student Info"));
        
        addButton = new JButton("Add New Student");
        deleteButton = new JButton("Delete Past Student");
        updateButton = new JButton("Update Current Student");
        calculateButton = new JButton("Calculate Average GPA");
        saveButton = new JButton("Save Data(File/DB)");
        addCourseButton = new JButton("Add Course");
        fileRadio = new JRadioButton("Save to File", true);
        dbRadio = new JRadioButton("Save to Database");
        ButtonGroup saveGroup = new ButtonGroup();
        saveGroup.add(fileRadio);
        saveGroup.add(dbRadio);
        
        studentList = new JList<>(studentListModel);
        studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentList.addListSelectionListener(e -> showSelectedStudent());
        
        courseList = new JList<>(courseListModel);
        courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        
        panel.add(new JLabel("Student Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Student ID:"));
        panel.add(idField);
        panel.add(new JLabel("Course Name:"));
        panel.add(courseField);
        panel.add(new JLabel("Credit Hours:"));
        panel.add(creditField);
        panel.add(new JLabel("Grade:"));
        panel.add(gradeComboBox);
        addDocumentListeners();       
        return panel;
    }
    
    private JPanel createCenterPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        

        JPanel listPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        JPanel studentPanel = new JPanel(new BorderLayout());
        studentPanel.setBorder(BorderFactory.createTitledBorder("Students"));
        studentPanel.add(new JScrollPane(studentList), BorderLayout.CENTER);
        
        JPanel coursePanel = new JPanel(new BorderLayout());
        coursePanel.setBorder(BorderFactory.createTitledBorder("Courses"));
        coursePanel.add(new JScrollPane(courseList), BorderLayout.CENTER);
        
        listPanel.add(studentPanel);
        listPanel.add(coursePanel);
        
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Results"));
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        
        mainPanel.add(listPanel, BorderLayout.CENTER);
        mainPanel.add(inputDisplayArea, BorderLayout.NORTH);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        panel.add(addButton);
        panel.add(addCourseButton);
        panel.add(deleteButton);
        panel.add(updateButton);
        panel.add(calculateButton);
        panel.add(saveButton);
        
        JPanel radioPanel = new JPanel(new FlowLayout());
        radioPanel.add(fileRadio);
        radioPanel.add(dbRadio);
        panel.add(radioPanel);
        
        addButton.addActionListener(e -> addStudent());
        addCourseButton.addActionListener(e -> addCourse());
        deleteButton.addActionListener(e -> deleteStudent());
        updateButton.addActionListener(e -> updateStudent());
        calculateButton.addActionListener(e -> calculateGPA());
        saveButton.addActionListener(e -> saveData());
        
        return panel;
    }
    
    private void addDocumentListeners() {
        DocumentListener dl = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { updateInputDisplay(); }
            public void removeUpdate(DocumentEvent e) { updateInputDisplay(); }
            public void insertUpdate(DocumentEvent e) { updateInputDisplay(); }
        };
        
        nameField.getDocument().addDocumentListener(dl);
        idField.getDocument().addDocumentListener(dl);
        courseField.getDocument().addDocumentListener(dl);
        creditField.getDocument().addDocumentListener(dl);
        
        gradeComboBox.addActionListener(e -> updateInputDisplay());
    }
    
    private void updateInputDisplay() {
        if (currentStudent != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Student Name: ").append(nameField.getText()).append("\n");
            sb.append("Student ID: ").append(idField.getText()).append("\n");
            sb.append("Current Course: ").append(courseField.getText()).append("\n");
            sb.append("Credits: ").append(creditField.getText()).append("\n");
            sb.append("Grade Score: ").append(gradeComboBox.getSelectedItem()).append("\n");
            
            inputDisplayArea.setText(sb.toString());
        }
    }
    
    private void addStudent() {
        try {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            
            if (name.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Student Name and ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            currentStudent = new Student(name, id);
            students.add(currentStudent);
            studentListModel.addElement(currentStudent.toString());
            
            courseField.setText("");
            creditField.setText("");
            gradeComboBox.setSelectedIndex(0);
            
            updateCoursesDisplay();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding student, Try again KELLY", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addCourse() {
        if (currentStudent == null) {
            JOptionPane.showMessageDialog(this, "Please select or add a student first, KELLY", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String course = courseField.getText().trim();
            double credits = Double.parseDouble(creditField.getText().trim());
            String grade = (String) gradeComboBox.getSelectedItem();
            
            if (course.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter course name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            currentStudent.addCourse(course, credits, grade);
            
            courseField.setText("");
            creditField.setText("");
            gradeComboBox.setSelectedIndex(0);
            
            updateCoursesDisplay();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid credit hours, check again", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showSelectedStudent() {
        int selectedIndex = studentList.getSelectedIndex();
        if (selectedIndex != -1) {
            currentStudent = students.get(selectedIndex);
            nameField.setText(currentStudent.getName());
            idField.setText(currentStudent.getId());
            updateCoursesDisplay();
        }
    }
    
    private void updateCoursesDisplay() {
        courseListModel.clear();
        if (currentStudent != null) {
            for (Course course : currentStudent.getCourses()) {
                courseListModel.addElement(course.getName() + " - " + 
                    course.getCredits() + " credits - Grade: " + course.getGrade());
            }
            updateInputDisplay();
        }
    }
    
    private void deleteStudent() {
        int selectedIndex = studentList.getSelectedIndex();
        if (selectedIndex != -1) {
            students.remove(selectedIndex);
            studentListModel.remove(selectedIndex);
            currentStudent = null;
            clearFields();
            courseListModel.clear();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateStudent() {
        if (currentStudent != null) {
            try {
                String name = nameField.getText().trim();
                String id = idField.getText().trim();
                
                if (name.isEmpty() || id.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter name and ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                currentStudent.setName(name);
                currentStudent.setId(id);
                
                // Update the list display
                int index = students.indexOf(currentStudent);
                if (index != -1) {
                    studentListModel.set(index, currentStudent.toString());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating student", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to update", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void calculateGPA() {
        if (currentStudent != null) {
            double gpa = currentStudent.calculateGPA();
            int totalCredits = (int) currentStudent.getTotalCredits();
            
            resultArea.setText(currentStudent.getDetailedInfo() + 
                             "\nTotal Credits: " + totalCredits +
                             "\nGPA: " + String.format("%.2f", gpa));
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to calculate GPA", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveData() {
        if (dbRadio.isSelected()) {
            JOptionPane.showMessageDialog(this, "Save to Database: Under Construction Kelly", "Alert by Wvrr3n", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        
        File file = new File("gpa_records.txt"); 
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Student student : students) {
                writer.println(student.getDetailedInfo());
                writer.println("Total Credits: " + student.getTotalCredits());
                writer.println("GPA: " + String.format("%.2f", student.calculateGPA()));
                writer.println("----------------------------------------");
            }
            JOptionPane.showMessageDialog(this, "Data saved successfully to " + file.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearFields() {
        nameField.setText("");
        idField.setText("");
        courseField.setText("");
        creditField.setText("");
        gradeComboBox.setSelectedIndex(0);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrameGPACalculator calculator = new JFrameGPACalculator();
            calculator.setVisible(true);
        });
    }
    
    private class Student {
        private String name;
        private String id;
        private ArrayList<Course> courses = new ArrayList<>();
        
        public Student(String name, String id) {
            this.name = name;
            this.id = id;
        }
        
        public void addCourse(String name, double credits, String grade) {
            courses.add(new Course(name, credits, grade));
        }
        
        public double calculateGPA() {
            double totalPoints = 0;
            double totalCredits = 0;
            
            for (Course course : courses) {
                totalPoints += GRADE_POINTS.get(course.getGrade()) * course.getCredits();
                totalCredits += course.getCredits();
            }
            
            return totalCredits > 0 ? totalPoints / totalCredits : 0;
        }
        
        public double getTotalCredits() {
            double total = 0;
            for (Course course : courses) {
                total += course.getCredits();
            }
            return total;
        }
        
        public String getDetailedInfo() {
            StringBuilder sb = new StringBuilder();
            sb.append("Student Name: ").append(name).append("\n");
            sb.append("Student ID: ").append(id).append("\n");
            sb.append("Courses:\n");
            
            for (Course course : courses) {
                sb.append("- ").append(course.getName()).append(": ")
                  .append(course.getCredits()).append(" credits, Grade: ")
                  .append(course.getGrade()).append("\n");
            }
            
            return sb.toString();
        }
        
        @Override
        public String toString() {
            return name + " (" + id + ")";
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        
        public String getId() {
            return id;
        }
        
        public ArrayList<Course> getCourses() {
            return courses;
        }
    }
    
    private class Course {
        private String name;
        private double credits;
        private String grade;
        
        public Course(String name, double credits, String grade) {
            this.name = name;
            this.credits = credits;
            this.grade = grade;
        }
        
        public String getName() {
            return name;
        }
        
        public double getCredits() {
            return credits;
        }
        
        public String getGrade() {
            return grade;
        }
    }
}