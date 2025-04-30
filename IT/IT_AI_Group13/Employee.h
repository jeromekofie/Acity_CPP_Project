#ifndef EMPLOYEE_H
#define EMPLOYEE_H

#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <iomanip>
#include <algorithm>
using namespace std;

struct Employee { // Constructors
    int id; //declaration
    string name;
    string department;
    string position;
    string salaryType; // "Salaried" or "Hourly"
    double salary;

    void display() const;
};

class EmployeeManager { //class
private:
    vector<Employee> employees; // vectors
    const string filename = "Employees_Info.txt";

    void saveToFile();

public:
    EmployeeManager(); //Encapsulation
    void addEmployee();
    void displayAllEmployees() const;
    void displayEmployeeById() const;
    void editEmployee();
    void deleteEmployee();
    const vector<Employee>& getEmployees() const;
};

#endif
