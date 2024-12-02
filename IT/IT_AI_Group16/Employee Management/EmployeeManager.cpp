#ifndef EMPLOYEEMANAGER_H
#define EMPLOYEEMANAGER_H

#include "Employee.cpp"
#include <fstream> // For file operations

class EmployeeManager {
private:
    Employee employees[100]; // Array to store employees
    int employeeCount;       // Number of employees

public:
    // Constructor
    EmployeeManager() : employeeCount(0) {}

    // Add an employee
    void addEmployee() {
        if (employeeCount >= 100) {
            cout << "Employee list is full!\n";
            return;
        }

        Employee emp;
        cout << "Enter Employee ID: ";
        cin >> emp.id;
        cin.ignore();
        cout << "Enter Name: ";
        getline(cin, emp.name);
        cout << "Enter Age: ";
        cin >> emp.age;
        cin.ignore();
        cout << "Enter Date of Birth (DD/MM/YYYY): ";
        getline(cin, emp.dob);
        cout << "Enter Gender: ";
        getline(cin, emp.gender);
        cout << "Enter Department: ";
        getline(cin, emp.department);

        employees[employeeCount] = emp;
        employeeCount++;
        cout << "Employee added successfully!\n";
    }

    // Display all employees
    void displayEmployees() const {
        if (employeeCount == 0) {
            cout << "No employees to display.\n";
            return;
        }

        for (int i = 0; i < employeeCount; i++) {
            employees[i].display();
        }
    }

    // Delete an employee by ID
    void deleteEmployee() {
        int id;
        cout << "Enter Employee ID to delete: ";
        cin >> id;

        bool found = false;
        for (int i = 0; i < employeeCount; i++) {
            if (employees[i].id == id) {
                // Shift the array elements left
                for (int j = i; j < employeeCount - 1; j++) {
                    employees[j] = employees[j + 1];
                }
                employeeCount--;
                cout << "Employee deleted successfully.\n";
                found = true;
                break;
            }
        }
        if (!found) {
            cout << "Employee not found.\n";
        }
    }

    // Save employees to a file
    void saveToFile() const {
        ofstream file("employees.txt");
        if (!file) {
            cout << "Error opening file for writing.\n";
            return;
        }

        for (int i = 0; i < employeeCount; i++) {
            file << "Employee ID: " << employees[i].id << endl;
            file << "Name: " << employees[i].name << endl;
            file << "Age: " << employees[i].age << endl;
            file << "Date of Birth: " << employees[i].dob << endl;
            file << "Gender: " << employees[i].gender << endl;
            file << "Department: " << employees[i].department << endl;
            file << "----------------------------\n";
        }

        file.close();
        cout << "Employee data saved to file 'employees.txt'.\n";
    }
};

#endif // EMPLOYEEMANAGER_H
