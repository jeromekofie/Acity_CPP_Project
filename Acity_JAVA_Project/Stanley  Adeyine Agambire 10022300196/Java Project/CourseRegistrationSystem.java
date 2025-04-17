import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseRegistrationSystem extends JFrame {
    private JTable coursesTable;
    private JTable registeredTable;
    private DefaultTableModel coursesModel;
    private DefaultTableModel registeredModel;
    private DefaultTableModel enrolledStudentsModel;
    private TableRowSorter<DefaultTableModel> coursesSorter;
    private List<Student> students = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private JTextField studentIdField;
    private JTextField studentNameField;
    private JComboBox<String> departmentComboBox;
    private JComboBox<String> filterComboBox;
    private JTextField searchField;
    private DatabaseConnection dbConnection;

    public CourseRegistrationSystem() {
        try {
            // Initialize database connection
            dbConnection = new DatabaseConnection();
            
            // Set Montserrat font
            Font montserrat = Font.createFont(Font.TRUETYPE_FONT, 
                new File("Montserrat-Regular.ttf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(montserrat);
        } catch (IOException | FontFormatException e) {
            System.out.println("Montserrat font not found. Using default font.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setTitle("Academic City University - Course Registration System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize UI
        initUI();

        // Load data from database
        loadCoursesFromDatabase();
        loadStudentsFromDatabase();
    }

    private void initUI() {
        // Main panel with red and white theme
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header panel with logo
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 255, 255));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 0, 0)));

        // Load logo
        ImageIcon logoIcon = new ImageIcon("ac-l-267x300.png");
        Image logoImage = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 20));
        headerPanel.add(logoLabel, BorderLayout.WEST);

        // Title and subtitle
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(255, 255, 255));

        JLabel titleLabel = new JLabel("ACADEMIC CITY UNIVERSITY", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        titleLabel.setForeground(new Color(200, 0, 0));
        titlePanel.add(titleLabel, BorderLayout.NORTH);

        JLabel subtitleLabel = new JLabel("STUDENT COURSE REGISTRATION SYSTEM", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Montserrat", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        headerPanel.add(titlePanel, BorderLayout.CENTER);

        // Add header panel to the main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        contentPanel.setBackground(new Color(255, 255, 255));

        // Left panel - Courses
        JPanel coursesPanel = new JPanel(new BorderLayout());
        coursesPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 0, 0), 1),
                "Available Courses"));
        coursesPanel.setBackground(new Color(255, 255, 255));

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(new Color(255, 255, 255));

        filterPanel.add(new JLabel("Filter by Faculty:"));
        filterComboBox = new JComboBox<>(new String[]{"All", "Engineering", "Business", "Informatics", "Communication Arts"});
        filterComboBox.setFont(new Font("Montserrat", Font.PLAIN, 12));
        filterComboBox.addActionListener(e -> filterCourses());
        filterPanel.add(filterComboBox);

        filterPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        searchField.setFont(new Font("Montserrat", Font.PLAIN, 12));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterCourses(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterCourses(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterCourses(); }
        });
        filterPanel.add(searchField);

        coursesPanel.add(filterPanel, BorderLayout.NORTH);

        // Courses table
        coursesModel = new DefaultTableModel(new Object[]{"ID", "Course Name", "Department", "Credits", "Capacity", "Enrolled"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        coursesTable = new JTable(coursesModel);
        coursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coursesTable.setRowHeight(30);
        coursesTable.setFont(new Font("Montserrat", Font.PLAIN, 12));
        coursesTable.setShowGrid(false);
        coursesTable.setIntercellSpacing(new Dimension(0, 0));
        coursesTable.getTableHeader().setFont(new Font("Montserrat", Font.BOLD, 12));
        
        coursesSorter = new TableRowSorter<>(coursesModel);
        coursesTable.setRowSorter(coursesSorter);

        JScrollPane coursesScrollPane = new JScrollPane(coursesTable);
        coursesScrollPane.setBorder(BorderFactory.createEmptyBorder());
        coursesPanel.add(coursesScrollPane, BorderLayout.CENTER);

        // Register button
        JButton registerButton = createModernButton("Register Selected Course");
        registerButton.addActionListener(e -> registerCourse());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.add(registerButton);
        coursesPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Right panel - Student info and registered courses
        JPanel studentPanel = new JPanel(new BorderLayout());
        studentPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 0, 0), 1),
                "Student Information & Registered Courses"));
        studentPanel.setBackground(new Color(255, 255, 255));

        // Student form
        JPanel studentFormPanel = new JPanel(new GridBagLayout());
        studentFormPanel.setBackground(new Color(250, 250, 250));
        studentFormPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        studentFormPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx++;
        studentIdField = new JTextField(15);
        studentIdField.setFont(new Font("Montserrat", Font.PLAIN, 12));
        studentFormPanel.add(studentIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        studentFormPanel.add(new JLabel("Student Name:"), gbc);
        gbc.gridx++;
        studentNameField = new JTextField(15);
        studentNameField.setFont(new Font("Montserrat", Font.PLAIN, 12));
        studentFormPanel.add(studentNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        studentFormPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx++;
        departmentComboBox = new JComboBox<>(new String[]{
                "Engineering", "Business", "Informatics", "Communication Arts"
        });
        departmentComboBox.setFont(new Font("Montserrat", Font.PLAIN, 12));
        departmentComboBox.addActionListener(e -> filterCoursesByDepartment());
        studentFormPanel.add(departmentComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel formButtonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        formButtonPanel.setBackground(new Color(250, 250, 250));
        
        JButton saveStudentButton = createModernButton("Save Student");
        saveStudentButton.addActionListener(e -> saveStudent());
        formButtonPanel.add(saveStudentButton);

        JButton loadStudentButton = createModernButton("Load Student");
        loadStudentButton.addActionListener(e -> loadStudent());
        formButtonPanel.add(loadStudentButton);

        studentFormPanel.add(formButtonPanel, gbc);

        studentPanel.add(studentFormPanel, BorderLayout.NORTH);

        // Registered courses table
        registeredModel = new DefaultTableModel(new Object[]{"ID", "Course Name", "Credits"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        registeredTable = new JTable(registeredModel);
        registeredTable.setRowHeight(30);
        registeredTable.setFont(new Font("Montserrat", Font.PLAIN, 12));
        registeredTable.setShowGrid(false);
        registeredTable.setIntercellSpacing(new Dimension(0, 0));
        registeredTable.getTableHeader().setFont(new Font("Montserrat", Font.BOLD, 12));
        
        registeredTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                }
                return c;
            }
        });

        JScrollPane registeredScrollPane = new JScrollPane(registeredTable);
        registeredScrollPane.setBorder(BorderFactory.createEmptyBorder());
        studentPanel.add(registeredScrollPane, BorderLayout.CENTER);

        // Drop course button
        JButton dropButton = createModernButton("Drop Selected Course");
        dropButton.addActionListener(e -> dropCourse());
        
        JPanel dropButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dropButtonPanel.setBackground(new Color(255, 255, 255));
        dropButtonPanel.add(dropButton);
        studentPanel.add(dropButtonPanel, BorderLayout.SOUTH);

        contentPanel.add(coursesPanel);
        contentPanel.add(studentPanel);

        // Tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Montserrat", Font.BOLD, 12));

        // Add the existing content panel as the first tab
        tabbedPane.addTab("Course Registration", contentPanel);

        // Create a new panel for viewing enrolled students
        JPanel enrolledStudentsPanel = new JPanel(new BorderLayout());
        enrolledStudentsPanel.setBackground(new Color(255, 255, 255));

        // Table for displaying enrolled students
        enrolledStudentsModel = new DefaultTableModel(
            new Object[]{"Student ID", "Student Name", "Department", "Enrolled Courses"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable enrolledStudentsTable = new JTable(enrolledStudentsModel);
        enrolledStudentsTable.setRowHeight(30);
        enrolledStudentsTable.setFont(new Font("Montserrat", Font.PLAIN, 12));
        enrolledStudentsTable.setShowGrid(false);
        enrolledStudentsTable.setIntercellSpacing(new Dimension(0, 0));
        enrolledStudentsTable.getTableHeader().setFont(new Font("Montserrat", Font.BOLD, 12));

        // Populate the table with student data
        updateEnrolledStudentsTable();

        JScrollPane enrolledStudentsScrollPane = new JScrollPane(enrolledStudentsTable);
        enrolledStudentsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        enrolledStudentsPanel.add(enrolledStudentsScrollPane, BorderLayout.CENTER);

        // Add the enrolled students panel as the second tab
        tabbedPane.addTab("Enrolled Students", enrolledStudentsPanel);

        // Replace the content panel with the tabbed pane
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Footer panel
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(255, 255, 255));
        footerPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(200, 0, 0)));

        JLabel footerLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<b>Engineering</b> - Africa needs more engineers to build its infrastructure, "
                + "manufacturing and healthcare sectors, and alternative energy resources.</div></html>");
        footerLabel.setFont(new Font("Montserrat", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(100, 100, 100));
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerLabel.setBorder(new EmptyBorder(10, 20, 10, 20));
        footerPanel.add(footerLabel, BorderLayout.CENTER);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createModernButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(200, 0, 0));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Montserrat", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(180, 0, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(200, 0, 0));
            }
        });
        
        return button;
    }

    private void loadCoursesFromDatabase() {
        try {
            courses.clear();
            ResultSet rs = dbConnection.executeQuery("SELECT * FROM courses");
            while (rs.next()) {
                Course course = new Course(
                    rs.getString("course_id"),
                    rs.getString("course_name"),
                    rs.getString("department"),
                    rs.getInt("credits"),
                    rs.getInt("capacity")
                );
                courses.add(course);
                
                // Load enrolled students for this course
                ResultSet enrolledRs = dbConnection.executeQuery(
                    "SELECT student_id FROM student_courses WHERE course_id = ?", 
                    rs.getString("course_id"));
                while (enrolledRs.next()) {
                    Student student = findStudent(enrolledRs.getString("student_id"));
                    if (student != null) {
                        course.enrollStudent(student);
                    }
                }
            }
            updateCoursesTable();
        } catch (SQLException e) {
            showErrorDialog("Error loading courses from database: " + e.getMessage());
        }
    }

    private void loadStudentsFromDatabase() {
        try {
            students.clear();
            ResultSet rs = dbConnection.executeQuery("SELECT * FROM students");
            while (rs.next()) {
                Student student = new Student(
                    rs.getString("student_id"),
                    rs.getString("student_name"),
                    rs.getString("department")
                );
                students.add(student);
                
                // Load registered courses for this student
                ResultSet coursesRs = dbConnection.executeQuery(
                    "SELECT course_id FROM student_courses WHERE student_id = ?",
                    rs.getString("student_id"));
                while (coursesRs.next()) {
                    Course course = findCourse(coursesRs.getString("course_id"));
                    if (course != null) {
                        student.registerCourse(course);
                    }
                }
            }
            updateEnrolledStudentsTable();
        } catch (SQLException e) {
            showErrorDialog("Error loading students from database: " + e.getMessage());
        }
    }

    private void updateCoursesTable() {
        coursesModel.setRowCount(0);
        for (Course course : courses) {
            coursesModel.addRow(new Object[]{
                    course.getId(),
                    course.getName(),
                    course.getDepartment(),
                    course.getCredits(),
                    course.getCapacity(),
                    course.getEnrolledStudents().size()
            });
        }
    }

    private void filterCourses() {
        String filterText = searchField.getText().toLowerCase();
        String selectedFaculty = (String) filterComboBox.getSelectedItem();

        RowFilter<DefaultTableModel, Object> rf = new RowFilter<DefaultTableModel, Object>() {
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                String department = entry.getStringValue(2);
                String courseName = entry.getStringValue(1).toLowerCase();
                String courseId = entry.getStringValue(0).toLowerCase();

                // Faculty filter
                if (!selectedFaculty.equals("All") && !department.equals(selectedFaculty)) {
                    return false;
                }

                // Search text filter
                if (!filterText.isEmpty()) {
                    return courseName.contains(filterText) || courseId.contains(filterText);
                }

                return true;
            }
        };

        coursesSorter.setRowFilter(rf);
    }

    private void filterCoursesByDepartment() {
        String department = (String) departmentComboBox.getSelectedItem();
        filterComboBox.setSelectedItem(department);
    }

    private void registerCourse() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow == -1) {
            showErrorDialog("Please select a course to register");
            return;
        }

        String studentId = studentIdField.getText().trim();
        if (studentId.isEmpty()) {
            showErrorDialog("Please enter student ID");
            return;
        }

        Student student = findStudent(studentId);
        if (student == null) {
            showErrorDialog("Student not found. Please save student first.");
            return;
        }

        int modelRow = coursesTable.convertRowIndexToModel(selectedRow);
        String courseId = (String) coursesModel.getValueAt(modelRow, 0);
        Course course = findCourse(courseId);

        if (course != null) {
            if (!course.getDepartment().equals(student.getDepartment())) {
                showErrorDialog("You can only register for courses in your department (" + student.getDepartment() + ")");
                return;
            }

            if (course.getEnrolledStudents().size() >= course.getCapacity()) {
                showErrorDialog("Course is full");
                return;
            }

            if (student.getRegisteredCourses().contains(course)) {
                showErrorDialog("Student is already registered for this course");
                return;
            }

            try {
                // Update database
                dbConnection.executeUpdate(
                    "INSERT INTO student_courses (student_id, course_id) VALUES (?, ?)",
                    studentId, courseId);
                
                // Update in-memory objects
                student.registerCourse(course);
                course.enrollStudent(student);
                
                // Update UI
                updateRegisteredCoursesTable(student);
                updateCoursesTable();
                updateEnrolledStudentsTable();
                
                showSuccessDialog("Course registered successfully");
            } catch (SQLException e) {
                showErrorDialog("Database error: " + e.getMessage());
            }
        }
    }

    private void dropCourse() {
        int selectedRow = registeredTable.getSelectedRow();
        if (selectedRow == -1) {
            showErrorDialog("Please select a course to drop");
            return;
        }

        String studentId = studentIdField.getText().trim();
        if (studentId.isEmpty()) {
            showErrorDialog("Please enter student ID");
            return;
        }

        Student student = findStudent(studentId);
        if (student == null) {
            showErrorDialog("Student not found");
            return;
        }

        String courseId = (String) registeredModel.getValueAt(selectedRow, 0);
        Course course = findCourse(courseId);

        if (course != null) {
            try {
                // Update database
                dbConnection.executeUpdate(
                    "DELETE FROM student_courses WHERE student_id = ? AND course_id = ?",
                    studentId, courseId);
                
                // Update in-memory objects
                student.dropCourse(course);
                course.removeStudent(student);
                
                // Update UI
                updateRegisteredCoursesTable(student);
                updateCoursesTable();
                updateEnrolledStudentsTable();
                
                showSuccessDialog("Course dropped successfully");
            } catch (SQLException e) {
                showErrorDialog("Database error: " + e.getMessage());
            }
        }
    }

    private void saveStudent() {
        String id = studentIdField.getText().trim();
        String name = studentNameField.getText().trim();
        String department = (String) departmentComboBox.getSelectedItem();

        if (id.isEmpty() || name.isEmpty()) {
            showErrorDialog("Please fill in all fields");
            return;
        }

        try {
            // Check if student exists in database
            ResultSet rs = dbConnection.executeQuery(
                "SELECT * FROM students WHERE student_id = ?", id);
            
            if (rs.next()) {
                // Update existing student
                dbConnection.executeUpdate(
                    "UPDATE students SET student_name = ?, department = ? WHERE student_id = ?",
                    name, department, id);
            } else {
                // Insert new student
                dbConnection.executeUpdate(
                    "INSERT INTO students (student_id, student_name, department) VALUES (?, ?, ?)",
                    id, name, department);
                
                // Add to in-memory list
                students.add(new Student(id, name, department));
            }
            
            showSuccessDialog("Student saved successfully");
            updateEnrolledStudentsTable();
            updateRegisteredCoursesTable(findStudent(id));
        } catch (SQLException e) {
            showErrorDialog("Database error: " + e.getMessage());
        }
    }

    private void loadStudent() {
        String id = studentIdField.getText().trim();
        if (id.isEmpty()) {
            showErrorDialog("Please enter student ID");
            return;
        }

        try {
            ResultSet rs = dbConnection.executeQuery(
                "SELECT * FROM students WHERE student_id = ?", id);
            
            if (rs.next()) {
                // Update UI fields
                studentIdField.setText(rs.getString("student_id"));
                studentNameField.setText(rs.getString("student_name"));
                departmentComboBox.setSelectedItem(rs.getString("department"));
                
                // Add to in-memory list if not already there
                if (findStudent(id) == null) {
                    students.add(new Student(
                        rs.getString("student_id"),
                        rs.getString("student_name"),
                        rs.getString("department")
                    ));
                }
                
                // Load registered courses
                updateRegisteredCoursesTable(findStudent(id));
                updateEnrolledStudentsTable();
                
                showSuccessDialog("Student loaded successfully");
            } else {
                showErrorDialog("Student not found in database");
            }
        } catch (SQLException e) {
            showErrorDialog("Database error: " + e.getMessage());
        }
    }

    private void updateRegisteredCoursesTable(Student student) {
        registeredModel.setRowCount(0);
        if (student != null) {
            for (Course course : student.getRegisteredCourses()) {
                registeredModel.addRow(new Object[]{
                        course.getId(),
                        course.getName(),
                        course.getCredits()
                });
            }
        }
    }

    private void updateEnrolledStudentsTable() {
        enrolledStudentsModel.setRowCount(0);
        for (Student student : students) {
            StringBuilder enrolledCourses = new StringBuilder();
            for (Course course : student.getRegisteredCourses()) {
                if (enrolledCourses.length() > 0) {
                    enrolledCourses.append(", ");
                }
                enrolledCourses.append(course.getName());
            }
            enrolledStudentsModel.addRow(new Object[]{
                    student.getId(),
                    student.getName(),
                    student.getDepartment(),
                    enrolledCourses.toString()
            });
        }
    }

    private Student findStudent(String id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }

    private Course findCourse(String id) {
        for (Course course : courses) {
            if (course.getId().equals(id)) {
                return course;
            }
        }
        return null;
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CourseRegistrationSystem system = new CourseRegistrationSystem();
            system.setVisible(true);
        });
    }
}

class DatabaseConnection {
    private Connection connection;

    public DatabaseConnection() throws SQLException {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/course_registration";
        String username = "root";
        String password = "_Stan.leeyy123"; // Change to your database password
        
        // Establish connection
        connection = DriverManager.getConnection(url, username, password);
        
        // Initialize database schema if needed
        initializeDatabase();
    }

    private void initializeDatabase() throws SQLException {
        // Create tables if they don't exist
        String createStudentsTable = "CREATE TABLE IF NOT EXISTS students (" +
                "student_id VARCHAR(20) PRIMARY KEY, " +
                "student_name VARCHAR(100) NOT NULL, " +
                "department VARCHAR(50) NOT NULL)";
        
        String createCoursesTable = "CREATE TABLE IF NOT EXISTS courses (" +
                "course_id VARCHAR(20) PRIMARY KEY, " +
                "course_name VARCHAR(100) NOT NULL, " +
                "department VARCHAR(50) NOT NULL, " +
                "credits INT NOT NULL, " +
                "capacity INT NOT NULL)";
        
        String createStudentCoursesTable = "CREATE TABLE IF NOT EXISTS student_courses (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "student_id VARCHAR(20) NOT NULL, " +
                "course_id VARCHAR(20) NOT NULL, " +
                "FOREIGN KEY (student_id) REFERENCES students(student_id), " +
                "FOREIGN KEY (course_id) REFERENCES courses(course_id), " +
                "UNIQUE (student_id, course_id))";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createStudentsTable);
            stmt.execute(createCoursesTable);
            stmt.execute(createStudentCoursesTable);
            
            // Insert sample data if tables are empty
            if (isTableEmpty("students")) {
                insertSampleData();
            }
        }
    }

    private boolean isTableEmpty(String tableName) throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName)) {
            rs.next();
            return rs.getInt(1) == 0;
        }
    }

    private void insertSampleData() throws SQLException {
        // Insert sample students
        String[] studentInserts = {
            "INSERT INTO students VALUES ('S1001', 'Kwame Asante', 'Engineering')",
            "INSERT INTO students VALUES ('S1002', 'Ama Mensah', 'Business')",
            "INSERT INTO students VALUES ('S1003', 'Yaw Boateng', 'Informatics')",
            "INSERT INTO students VALUES ('S1004', 'Esi Nyarko', 'Communication Arts')"
        };
        
        // Insert sample courses
        String[] courseInserts = {
            // Engineering courses
            "INSERT INTO courses VALUES ('ENG101', 'Introduction to Electronics & Communication Engineering', 'Engineering', 3, 30)",
            "INSERT INTO courses VALUES ('ENG102', 'Computer Engineering Fundamentals', 'Engineering', 3, 25)",
            "INSERT INTO courses VALUES ('ENG103', 'Mechanical Systems Design', 'Engineering', 4, 20)",
            "INSERT INTO courses VALUES ('ENG104', 'Electrical Circuits Theory', 'Engineering', 3, 30)",
            "INSERT INTO courses VALUES ('ENG105', 'Industrial Systems Analysis', 'Engineering', 3, 25)",
            "INSERT INTO courses VALUES ('ENG106', 'Robotics and Automation', 'Engineering', 4, 20)",
            "INSERT INTO courses VALUES ('ENG107', 'Biomedical Instrumentation', 'Engineering', 3, 15)",
            
            // Business courses
            "INSERT INTO courses VALUES ('BUS201', 'Principles of Accounting', 'Business', 3, 40)",
            "INSERT INTO courses VALUES ('BUS202', 'Banking and Financial Systems', 'Business', 3, 35)",
            "INSERT INTO courses VALUES ('BUS203', 'Human Resource Management', 'Business', 3, 30)",
            "INSERT INTO courses VALUES ('BUS204', 'Marketing Principles', 'Business', 3, 40)",
            "INSERT INTO courses VALUES ('BUS205', 'Entrepreneurship and Innovation', 'Business', 3, 25)",
            
            // Informatics courses
            "INSERT INTO courses VALUES ('INF301', 'Artificial Intelligence Fundamentals', 'Informatics', 4, 25)",
            "INSERT INTO courses VALUES ('INF302', 'Information Technology Systems', 'Informatics', 3, 30)",
            "INSERT INTO courses VALUES ('INF303', 'Advanced Computer Science', 'Informatics', 4, 20)",
            "INSERT INTO courses VALUES ('INF304', 'Data Structures and Algorithms', 'Informatics', 4, 25)",
            
            // Communication Arts courses
            "INSERT INTO courses VALUES ('COM401', 'Journalism and Mass Communication', 'Communication Arts', 3, 30)",
            "INSERT INTO courses VALUES ('COM402', 'Advertising Strategies', 'Communication Arts', 3, 25)",
            "INSERT INTO courses VALUES ('COM403', 'Public Relations Practice', 'Communication Arts', 3, 25)"
        };
        
        try (Statement stmt = connection.createStatement()) {
            // Insert students
            for (String sql : studentInserts) {
                stmt.execute(sql);
            }
            
            // Insert courses
            for (String sql : courseInserts) {
                stmt.execute(sql);
            }
        }
    }

    public ResultSet executeQuery(String sql, Object... params) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        return stmt.executeQuery();
    }

    public int executeUpdate(String sql, Object... params) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

class Student implements Serializable {
    private String id;
    private String name;
    private String department;
    private List<Course> registeredCourses = new ArrayList<>();

    public Student(String id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public List<Course> getRegisteredCourses() { return registeredCourses; }

    public void registerCourse(Course course) {
        registeredCourses.add(course);
    }

    public void dropCourse(Course course) {
        registeredCourses.remove(course);
    }
}

class Course implements Serializable {
    private String id;
    private String name;
    private String department;
    private int credits;
    private int capacity;
    private List<Student> enrolledStudents = new ArrayList<>();

    public Course(String id, String name, String department, int credits, int capacity) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.credits = credits;
        this.capacity = capacity;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public int getCredits() { return credits; }
    public int getCapacity() { return capacity; }
    public List<Student> getEnrolledStudents() { return enrolledStudents; }

    public void enrollStudent(Student student) {
        enrolledStudents.add(student);
    }

    public void removeStudent(Student student) {
        enrolledStudents.remove(student);
    }
}