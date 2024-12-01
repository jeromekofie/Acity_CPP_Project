#include <iostream>
#include <iomanip>
#include <vector>
#include <fstream>
#include <string>
#include <cstdlib>
#include <cmath>

using namespace std;

/*Enumerated data type for gender*/
enum Gender { MALE, FEMALE };

/* Student class*/
class Student {

private:
    string name;
    int age;
    string course;
    Gender gender;

    /* Daily attendance: 1 for present, 0 for absent */
    vector<int> attendance; 

public:
    /* Constructor */
    Student(string n, int a, string c, Gender g) 
        : name(n), age(a), course(c), gender(g), attendance(30, 0) {}

    /* Getters */
    string getName() const{ 
        return name; 
    }
    
    int getAge() const{ 
        return age; 
    }
    
    string getCourse() const{ 
        return course; 
    }
    
    Gender getGender() const{ 
        return gender; 
    }
    
    int getAttendance(int day) const{ 
        return attendance[day - 1]; 
    }
    
    /* Setters */
    void markAttendance(int day, int status){
        
        if (day >= 1 && day <= 30)
            attendance[day - 1] = status;
        
        else
            throw invalid_argument("Invalid day. Must be between 1 and 30.");
    }
 
    /* Display attendance summary */ 
    void displayAttendance() const{
        int total = 0;
        
        for (int i : attendance) total += i;
        
        cout << "Attendance Summary for " << name << ": " << total << "/30 days\n";
    }
};

/* Attendance Tracker class*/
class AttendanceTracker {

private:
    vector<Student> students;

    /*Helper function for gender display*/
    string genderToString(Gender g) const {
        return (g == MALE) ? "Male" : "Female";
    }

public:
    /*Add a student*/
    void addStudent(const Student& student) {
        students.push_back(student);
    }

    /* Mark attendance for a specific student and day */
    void markAttendance(){
        string name;
        int day, status;

        cout << "Enter student's name: ";
        cin.ignore();
        getline(cin, name);

        cout << "Enter day (1-30): ";
        cin >> day;
        cout << "Mark attendance (1 for present, 0 for absent): ";
        cin >> status;

        for (Student& s : students) {
            if (s.getName() == name) {
                try {
                    s.markAttendance(day, status);
                    cout << "Attendance marked successfully for " << name << " on Day " << day << ".\n";
                } catch (const invalid_argument& e) {
                    cout << e.what() << endl;
                }
                return;
            }
        }
        cout << "Student not found.\n";
    }
    
    /* Display attendance of all students */
    void displayAllAttendance() const{
        
        cout << "\n=== Attendance Records ===\n";
        
        for (const Student& s : students) {
            cout << "Name: " << s.getName()
                 << ", Age: " << s.getAge()
                 << ", Course: " << s.getCourse()
                 << ", Gender: " << genderToString(s.getGender()) << "\n";
            s.displayAttendance();
        }
    }