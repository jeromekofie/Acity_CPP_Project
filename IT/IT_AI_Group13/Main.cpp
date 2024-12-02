#include <iostream>
#include "Employee.h"
#include "Payroll.cpp"
using namespace std;

int main() {
    EmployeeManager empManager;
    Payroll payroll;
    int choice;

    do {
        cout << "\n--- Payroll Management System ---\n"
             << "1. Add Employee" << endl
             << "2. Edit Employee" << endl
             << "3. Delete Employee" << endl
             << "4. Display Employees" << endl
             << "5. Generate Payslip" << endl
             << "6. Exit" << endl
             << "Choice: ";
        cin >> choice;

        switch (choice) {
            case 1: empManager.addEmployee(); break;
            case 2: empManager.editEmployee(); break;
            case 3: empManager.deleteEmployee(); break;
            case 4: {
                int viewChoice;
                cout << "1. View All Employees" << endl
                    << "2. View Specific Employee" << endl
                    << "Choice: ";
                cin >> viewChoice;
                if (viewChoice == 1) empManager.displayAllEmployees();
                else if (viewChoice == 2) empManager.displayEmployeeById();
                else cout << "Invalid choice." << endl;
                break;
            }
            case 5: payroll.generatePayslip(empManager); break;
            case 6: cout << "Exiting program." << endl; break;
            default: cout << "Invalid choice." << endl; break;
        }
    } while (choice != 6);

    return 0;
}
