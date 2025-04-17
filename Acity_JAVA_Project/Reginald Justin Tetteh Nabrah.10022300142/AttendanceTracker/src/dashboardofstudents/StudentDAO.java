/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dashboardofstudents;

/**
 *
 * @author palac
 */
import java.sql.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

public class StudentDAO {

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/teachers", "root", "");
    }

    public void insertStudent(Student s) throws SQLException {
        String sql = "INSERT INTO `list of students` (`Full Name`, Age, Gender, `Parent/Guardian's Phone Number`) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, s.getFullName());
            pst.setInt(2, s.getAge());
            pst.setString(3, s.getGender());
            pst.setString(4, s.getParentPhone());
            pst.executeUpdate();
        }
    }

    public void updateStudent(Student s) throws SQLException {
        String sql = "UPDATE `list of students` SET `Full Name`=?, Age=?, Gender=?, `Parent/Guardian's Phone Number`=? WHERE ID=?";
        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, s.getFullName());
            pst.setInt(2, s.getAge());
            pst.setString(3, s.getGender());
            pst.setString(4, s.getParentPhone());
            pst.setInt(5, s.getId());
            pst.executeUpdate();
        }
    }

    public void deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM `list of students` WHERE ID=?";
        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM `list of students` ORDER BY ID DESC";
        try (Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student(
                    rs.getInt("ID"),
                    rs.getString("Full Name"),
                    rs.getInt("Age"),
                    rs.getString("Gender"),
                    rs.getString("Parent/Guardian's Phone Number")
                );
                list.add(s);
            }
        }
        return list;
    }

    public void loadTable(DefaultTableModel model) {
        model.setRowCount(0); // Clear table
        try {
            List<Student> list = getAllStudents();
            for (Student s : list) {
                model.addRow(new Object[]{
                    s.getId(),
                    s.getFullName(),
                    s.getAge(),
                    s.getGender(),
                    s.getParentPhone()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

