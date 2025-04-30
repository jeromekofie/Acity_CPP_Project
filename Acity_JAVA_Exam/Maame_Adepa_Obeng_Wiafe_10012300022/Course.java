 public class Course {
    private String courseName;
    private int creditHours;
    private String grade;
    
    public Course(String courseName, int creditHours, String grade) {
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.grade = grade;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public int getCreditHours() {
        return creditHours;
    }
    
    public String getGrade() {
        return grade;
    }
    
    public double getGradePoints() {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "B+": return 3.5;
            case "B": return 3.0;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "D+": return 1.5;
            case "D": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }
} 

