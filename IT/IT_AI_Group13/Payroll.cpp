#include <iostream>
#include <fstream>
#include <iomanip>
#include "Employee.cpp"
using namespace std;

class Payroll{
    public: 
        void generatePayslip (const EmployeeManager& empManager) {
            int id;
            cout << "Enter Employee ID to generate payslip: ";
            cin >> id;

            const auto& employees = empManager.getEmployees();
            auto it = find_if(employees.begin(), employees.end(),
                            [id](const Employee& emp) { return emp.id == id; });

            if (it != employees.end()) {
                double baseSalary = 0.0, bonus = 0.0, deductions = 0.0;

                cout << "Enter bonus: ";
                cin >> bonus;
                cout << "Enter deductions: ";
                cin >> deductions;

                if (it->salaryType == "Salaried") {
                    baseSalary = it->salary;
                } else if (it->salaryType == "Hourly") {
                    double hoursWorked;
                    cout << "Enter the number of hours the employee worked: ";
                    cin >> hoursWorked;
                    baseSalary = hoursWorked * it->salary;
                } else {
                    cout << "Invalid Salary Type for employee" << endl;
                    return;
                }

                double netSalary = baseSalary + bonus - deductions;

                ofstream outFile ("PaySlip.txt");
                if (!outFile){
                    cout << "Unable to open PaySlip.txt" << endl;
                    return;
                }
                outFile << "Salary Slip For The Month of December" << endl;
                outFile << "Employee ID:       " << it->id << endl;
                outFile << "Name:              " << it->name << endl;
                outFile << "Designation:       " << it->position << endl;
                outFile << "Department:        " << it->department << endl << endl;

                outFile << "Basic Salary:      $ " << fixed << setprecision(2) << baseSalary << endl;
                outFile << "Allowances:        $ " << bonus << endl;
                outFile << "Deduction:         $ " << deductions << endl;
                outFile << "Net Salary Amount: $ " << netSalary << endl << endl;

                string netSalaryInWords = "Amount in Words: Dollars " + to_string((int)netSalary) + " Only\n";

                outFile << netSalaryInWords;
                outFile << "Prepared By:       HR Department" << endl;
                outFile << "Approved By:       Manager" << endl;

                cout << "Payslip generated sucessfully" << endl;
            } else {
                cout << "Employee not found" << endl;
            }
        }
};