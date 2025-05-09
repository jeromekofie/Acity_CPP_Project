public class Course {
    private String courseName;
    private String grade;
    private double creditHours;

    public Course(String courseName, String grade, double creditHours) {
        this.courseName = courseName;
        this.grade = grade;
        this.creditHours = creditHours;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getGrade() {
        return grade;
    }

    public double getCreditHours() {
        return creditHours;
    }
} 