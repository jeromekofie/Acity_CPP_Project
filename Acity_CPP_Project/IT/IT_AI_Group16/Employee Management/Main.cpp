#include <iostream>
#include "EmployeeManager.cpp"

using namespace std;

int main() {
    EmployeeManager manager;
    int choice;

    do {
        cout << "\nEmployee Management System\n";
        cout << "1. Add Employee\n";
        cout << "2. Display Employees\n";
        cout << "3. Delete Employee\n";
        cout << "4. Save Employees to File\n";
        cout << "5. Exit\n";
        cout << "Enter your choice: ";
        cin >> choice;

        switch (choice) {
            case 1:
                manager.addEmployee();
                break;
            case 2:
                manager.displayEmployees();
                break;
            case 3:
                manager.deleteEmployee();
                break;
            case 4:
                manager.saveToFile();
                break;
            case 5:
                cout << "Exiting program.\n";
                break;
            default:
                cout << "Invalid choice. Please try again.\n";
        }
    } while (choice != 5);

    return 0;
}
