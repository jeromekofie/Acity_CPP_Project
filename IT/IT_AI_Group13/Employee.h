#ifndef EMPLOYEE_H
#define EMPLOYEE_H

#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <iomanip>
#include <algorithm>
using namespace std;

struct Employee {
    int id;
    string name;
    string department;
    string position;
    string salaryType; // "Salaried" or "Hourly"
    double salary;

    void display() const;
};

class EmployeeManager {
private:
    vector<Employee> employees;
    const string filename = "Employees_Info.txt";

    void saveToFile();

public:
    EmployeeManager();
    void addEmployee();
    void displayAllEmployees() const;
    void displayEmployeeById() const;
    void editEmployee();
    void deleteEmployee();
    const vector<Employee>& getEmployees() const;
};

#endif
