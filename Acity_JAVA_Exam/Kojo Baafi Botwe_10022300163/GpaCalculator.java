public class GpaCalculator {
    public static double calculateGpa(Student student) {
        double totalWeightedPoints = 0;
        double totalCreditHours = 0;
        for (Course course : student.getCourses()) {
            double gradePoint = getGradePoint(course.getGrade());
            totalWeightedPoints += gradePoint * course.getCreditHours();
            totalCreditHours += course.getCreditHours();
        }
        if (totalCreditHours == 0) return 0;
        return totalWeightedPoints / totalCreditHours;
    }

    public static double getGradePoint(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "B+": return 3.5;
            case "B": return 3.0;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "D": return 1.5;
            case "E": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }
} 