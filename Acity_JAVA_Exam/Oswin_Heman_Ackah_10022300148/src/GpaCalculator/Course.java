/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package GpaCalculator;

/**
 *
 * @author kelvi
 */
public class Course {
    String name;
    int creditHours;
    String grade;

    public Course(String name, int creditHours, String grade) {
        this.name = name;
        this.creditHours = creditHours;
        this.grade = grade;
    }

    public double getGradePoint() {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "B+": return 3.5;
            case "B": return 3.0;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "D": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }

    int getCreditHours() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

