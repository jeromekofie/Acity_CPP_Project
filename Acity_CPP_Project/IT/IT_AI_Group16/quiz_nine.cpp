#include <iostream>
#include <string>
#include <fstream> // For file operations
using namespace std;

// Define a maximum number of employees
const int MAX_EMPLOYEES = 100;

// Employee structure
struct Employee {
    int id;
    string name;
    int age;
    string dob; // Date of Birth
    string gender;
    string department;
};

Employee employees[MAX_EMPLOYEES]; // Array to store employee data
int employeeCount = 0; // Track the number of employees

// Function to add an employee
void addEmployee() {
    if (employeeCount < MAX_EMPLOYEES) {
        Employee emp;
        cout << "Enter Employee ID: ";
        cin >> emp.id;
        cin.ignore(); // To clear the newline character
        cout << "Enter Name: ";
        getline(cin, emp.name);
        cout << "Enter Age: ";
        cin >> emp.age;
        cin.ignore(); // To clear the newline character
        cout << "Enter Date of Birth (DD/MM/YYYY): ";
        getline(cin, emp.dob);
        cout << "Enter Gender: ";
        getline(cin, emp.gender);
        cout << "Enter Department: ";
        getline(cin, emp.department);
        
        employees[employeeCount] = emp; // Store the employee
        employeeCount++; // Increment the employee count
        cout << "Employee added successfully!\n";
    } else {
        cout << "Employee list is full!\n";
    }
}

// Function to display all employees
void displayEmployees() {
    if (employeeCount == 0) {
        cout << "No employees to display.\n";
        return;
    }
    for (int i = 0; i < employeeCount; i++) {
        cout << "\nEmployee ID: " << employees[i].id
             << "\nName: " << employees[i].name
             << "\nAge: " << employees[i].age
             << "\nDate of Birth: " << employees[i].dob
             << "\nGender: " << employees[i].gender
             << "\nDepartment: " << employees[i].department
             << "\n----------------------------\n";
    }
}