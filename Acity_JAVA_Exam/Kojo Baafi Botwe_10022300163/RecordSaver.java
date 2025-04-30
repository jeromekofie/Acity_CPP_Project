import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

public class RecordSaver {
    
    public static interface SaveOption {
        void save(Student student, double gpa);
    }

   
    private static class FileSave implements SaveOption {
        public void save(Student student, double gpa) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("gpa_records.txt", true))) {
                writer.write(String.format("Name: %s, ID: %s, GPA: %.2f\n", student.getName(), student.getId(), gpa));
                writer.write("Courses:\n");
                for (Course course : student.getCourses()) {
                    writer.write(String.format("  - %s | Grade: %s | Credit Hours: %.2f\n",
                        course.getCourseName(), course.getGrade(), course.getCreditHours()));
                }
                writer.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    
    private static class DatabaseSave implements SaveOption {
        public void save(Student student, double gpa) {
            JOptionPane.showMessageDialog(null, "Database: under construction", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    
    public static SaveOption getSaveOption(String selectedMethod) {
        if ("File".equals(selectedMethod)) {
            return new FileSave();
        } else {
            return new DatabaseSave();
        }
    }
} 