public class Course {
    private String name;
    private String grade;
    private int creditHours;

    public Course(String name, String grade, int creditHours) {
        this.name = name;
        this.grade = grade;
        this.creditHours = creditHours;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public int getCreditHours() {
        return creditHours;
    }
} 