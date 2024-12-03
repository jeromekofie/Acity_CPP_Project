#ifndef COURSE_H
#define COURSE_H

#include "Student.cpp"
#include <string>
#include <vector>
#include <iostream>
using namespace std;

// Declaration
class Course : public Student
{
private:
    string name;
    int limit;
    vector<Student> enrolledStudents;

public:
    Course(const string &name, int limit) : name(name), limit(limit) {};
    bool enrollStudent(const Student &student)
    {
        if (enrolledStudents.size() < limit)
        {
            enrolledStudents.push_back(student);
            return true;
        }
        return false; // Course limit reached
    };
    void listStudents() const
    {
        cout << "\nStudents enrolled in " << name << ":\n";
        if (enrolledStudents.empty())
        {
            cout << "No students enrolled yet.\n";
        }
        else
        {
            for (const auto &student : enrolledStudents)
            {
                cout << "ID: " << student.getId() << ", Name: " << student.getName() << "\n";
            }
        }
    };
    string getName() const
    {
        return name;
    };
};
#endif
