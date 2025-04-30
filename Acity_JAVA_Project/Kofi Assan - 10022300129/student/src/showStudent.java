/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author HP
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;

class Student {
    private final int id;
    private final String name;
    private final String parent;
    private final String level;
    private final String phone;
    private final String city;
    private final String course;
    private final int coursePosition;
    

    public Student(int id, String name, String parent, String level, String phone, String city, String course, int coursePosition) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.level = level;
        this.phone = phone;
        this.city = city;
        this.course = course;
        this.coursePosition = coursePosition;
     
    }

    @Override
    public String toString() {
        return id + "\t" + name + "\t\t" + parent + "\t" + level + "\t" + phone + "\t" + city + "\t" + course + "\t" +  coursePosition;
    }
}

public class showStudent {

    public static void saveStudentsToFile() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Vector<Student> studentList = new Vector<>();

        try {
            conn = databaseConnection.connection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM STUDENT";
            rs = stmt.executeQuery(sql);

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("id");
                String name = rs.getString("stdName");
                String parent = rs.getString("stdParent");
                String level = rs.getString("stdLevel");
                String phone = rs.getString("stdPhone");
                String city = rs.getString("stdCity");
                String course = rs.getString("stdCourse");
                int coursePosition = rs.getInt("coursePosition");
                studentList.add(new Student(id, name, parent, level, phone, city, course, coursePosition));
            }

            if (!hasData) {
                System.out.println("No student records found in database");
            } else {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("students_output.txt"))) {
                    writer.write("ID\tName\tParent\tLevel\tPhone\tCity\tCourse\tCourse Position\n");
                    for (Student student : studentList) {
                        writer.write(student.toString() + "\n");
                    }
                    System.out.println("Student Data written to students_output.txt");
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    static void saveStudentsToExcel() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

