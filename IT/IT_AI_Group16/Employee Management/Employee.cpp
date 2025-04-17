#ifndef EMPLOYEE_H
#define EMPLOYEE_H

#include <string>
#include <iostream>
#include <iomanip>

using namespace std;

class Employee {
public:
    int id;
    string name;
    int age;
    string dob;  
    string gender;
    string department;

    // Constructor
    Employee() : id(0), age(0) {}

    // Method to display employee details
   void display() const {
    cout << setw(15) << left << "Employee ID: " << setw(10) << right << id << endl;
    cout << setw(15) << left << "Name: " << setw(20) << left << name << endl;
    cout << setw(15) << left << "Age: " << setw(10) << right << age << endl;
    cout << setw(15) << left << "Date of Birth: " << setw(15) << left << dob << endl;
    cout << setw(15) << left << "Gender: " << setw(10) << left << gender << endl;
    cout << setw(15) << left << "Department: " << setw(20) << left << department << endl;
    cout << "----------------------------\n";
}

};

#endif // EMPLOYEE_H
