/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dashboardofstudents;

/**
 *
 * @author palac
 */
public class Student {
    private int id;
    private String fullName;
    private int age;
    private String gender;
    private String parentPhone;

    public Student(int id, String fullName, int age, String gender, String parentPhone) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.parentPhone = parentPhone;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getParentPhone() { return parentPhone; }

    public void setId(int id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setParentPhone(String parentPhone) { this.parentPhone = parentPhone; }
}



