import java.util.ArrayList;
import java.util.List;

public class Student {
    private String name;
    private String id;
    private List<Course> courses;
    
    public Student(String name, String id) {
        this.name = name;
        this.id = id;
        this.courses = new ArrayList<>();
    }
    
    public void addCourse(Course course) {
        courses.add(course);
    }
    
    public double calculateGPA() {
        if (courses.isEmpty()) {
            return 0.0;
        }
        
        double totalGradePoints = 0;
        int totalCreditHours = 0;
        
        for (Course course : courses) {
            totalGradePoints += course.getGradePoints() * course.getCreditHours();
            totalCreditHours += course.getCreditHours();
        }
        
        return totalGradePoints / totalCreditHours;
    }
    
    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }
    
    public List<Course> getCourses() {
        return courses;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Student Name: ").append(name).append("\n");
        sb.append("Student ID: ").append(id).append("\n");
        sb.append("Courses:\n");
        
        for (Course course : courses) {
            sb.append(course.getCourseName())
              .append(" - Credits: ")
              .append(course.getCreditHours())
              .append(" - Grade: ")
              .append(course.getGrade())
              .append("\n");
        }
        
        sb.append("GPA: ").append(String.format("%.2f", calculateGPA()));
        return sb.toString();
    }
} 