import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;


class ExcelStudent {
    private final int id;
    private final String name;
    private final String parent;
    private final String level;
    private final String phone;
    private final String city;
    private final String course;
    private final int coursePosition;

    public ExcelStudent(int id, String name, String parent, String level, String phone, String city, String course, int coursePosition) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.level = level;
        this.phone = phone;
        this.city = city;
        this.course = course;
        this.coursePosition = coursePosition;
    }

    public Object[] toObjectArray() {
        return new Object[]{id, name, parent, level, phone, city, course, coursePosition};
    }
}

public class showStudentinExcel {

    public static void saveStudentsToExcel() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        Vector<ExcelStudent> studentList = new Vector<>();

        try {
            conn = databaseConnection.connection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM STUDENT";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("stdName");
                String parent = rs.getString("stdParent");
                String level = rs.getString("stdLevel");
                String phone = rs.getString("stdPhone");
                String city = rs.getString("stdCity");
                String course = rs.getString("stdCourse");
                int coursePosition = rs.getInt("coursePosition");

                
                studentList.add(new ExcelStudent(id, name, parent, level, phone, city, course, coursePosition));
            }

            if (studentList.isEmpty()) {
                System.out.println("No student records found in database");
                return;
            }

            
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Students");

            String[] headers = {"ID", "Name", "Parent", "Level", "Phone", "City", "Course", "Course Position"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (ExcelStudent student : studentList) {
                Row row = sheet.createRow(rowNum++);
                Object[] data = student.toObjectArray();
                for (int i = 0; i < data.length; i++) {
                    Cell cell = row.createCell(i);
                    if (data[i] instanceof String) {
                        cell.setCellValue((String) data[i]);
                    } else if (data[i] instanceof Integer) {
                        cell.setCellValue((Integer) data[i]);
                    }
                }
            }

            
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream("students_output.xlsx")) {
                workbook.write(fileOut);
            }

            workbook.close();
            System.out.println("Student data written to students_output.xlsx");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}