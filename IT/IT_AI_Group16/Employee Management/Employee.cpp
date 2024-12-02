#ifndef EMPLOYEE_H
#define EMPLOYEE_H

#include <string>
#include <iostream>
using namespace std;

class Employee {
public:
    int id;
    string name;
    int age;
    string dob;  // Date of Birth
    string gender;
    string department;

    // Constructor
    Employee() : id(0), age(0) {}

    // Method to display employee details
    void display() const {
        cout << "\nEmployee ID: " << id
             << "\nName: " << name
             << "\nAge: " << age
             << "\nDate of Birth: " << dob
             << "\nGender: " << gender
             << "\nDepartment: " << department
             << "\n----------------------------\n";
    }
};

#endif // EMPLOYEE_H
