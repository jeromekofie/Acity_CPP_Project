#include <iostream>
#include <fstream>
#include <string>
#include <iomanip>
#include <limits>
#include "FullTimeEmployee.cpp"
#include "PartTimeEmployee.cpp"
#include "employees.cpp"
using namespace std;

int main() {
    const int MAX_EMPLOYEES = 1000;
    Employee* employees[MAX_EMPLOYEES];
    int employeeCount = 0;
    int choice;

    do {
        cout << "\nEmployee Management System\n";
        cout << "1. Add Full-Time Employee\n";
        cout << "2. Add Part-Time Employee\n";
        cout << "3. Display All Employees\n";
        cout << "4. Save Employees to File\n";
        cout << "5. Load Employees from File\n";
        cout << "6. Generate Payslip\n";
        cout << "7. Delete Employee\n";
        cout << "8. Exit\n";
        cout << "Enter your choice: ";
        cin >> choice;

        switch (choice) {
        case 1: {
            if (employeeCount < MAX_EMPLOYEES) {
                employees[employeeCount] = new FullTimeEmployee();

                int id;
                string name, address, phone, email, emergency_contact, nextofkin;
                double salary,medicalAllowance,housingAllowance,taxDeduction;

                cout << "Enter Full-Time Employee ID: ";
                cin >> id;
                cin.ignore();
                cout << "Enter Full Name: ";
                getline(cin, name);
                cout << "Enter Address: ";
                getline(cin, address);
                cout << "Enter Phone Number: ";
                getline(cin, phone);
                cout << "Enter Email: ";
                getline(cin, email);
                cout << "Enter Emergency Contact: ";
                getline(cin, emergency_contact);
                cout << "Enter Next of Kin: ";
                getline(cin, nextofkin);
                 cout << "Enter Medical Allowance: $";
                cin >> medicalAllowance;
                cout << "Enter Housing Allowance (Full-Time Only): $";
                cin >> housingAllowance;
                cout << "Enter Tax Deduction: $";
                cin >> taxDeduction;
                cout << "Enter Monthly Salary: $";
                cin >> salary;
                if (cin.fail()) {
                    cout << "Invalid input! Please enter a valid numeric salary.\n";
                    cin.clear();  // Clears the error flag
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');  // Discards the invalid input
                    continue;  // Restart the loop for this input
                }

                employees[employeeCount]->setDetails(id, name, address, phone, email, emergency_contact, nextofkin);
                FullTimeEmployee* fullTimeEmp = static_cast<FullTimeEmployee*>(employees[employeeCount]);
                fullTimeEmp->setSalary(salary);
                fullTimeEmp->setAllowances(medicalAllowance, housingAllowance);
                fullTimeEmp->setDeductions(taxDeduction);

                employeeCount++;
            } else {
                cout << "Employee list is full.\n";
            }
            break;
            cout<<"Employee Added Succesfully\n";
        }
        case 2: {
            if (employeeCount < MAX_EMPLOYEES) {
                employees[employeeCount] = new PartTimeEmployee();

                int id;
                string name, address, phone, email,emergency_contact,nextofkin;
                double hourlyRate;
                int hoursWorked,medicalAllowance,taxDeduction;

                cout << "Enter Part-Time Employee ID: ";
                cin >> id;
                cin.ignore();
                cout << "Enter Full Name: ";
                getline(cin, name);
                cout << "Enter Address: ";
                getline(cin, address);
                cout << "Enter Phone Number: ";
                getline(cin, phone);
                cout << "Enter Email: ";
                getline(cin, email);
                cout << "Enter Emergency Contact: ";
                getline(cin, emergency_contact);
                cout << "Enter Next of Kin: ";
                getline(cin, nextofkin);
                cout << "Enter Hourly Rate: $";
                cin >> hourlyRate;
                cout << "Enter Hours Worked: ";
                cin >> hoursWorked;
                cout << "Enter Medical Allowance: $";
                cin >> medicalAllowance;
                cout << "Enter Tax Deduction: $";
                cin >> taxDeduction;
                

                employees[employeeCount]->setDetails(id, name, address, phone, email, emergency_contact, nextofkin);
                PartTimeEmployee* partTimeEmp = static_cast<PartTimeEmployee*>(employees[employeeCount]);
                partTimeEmp->setHourlyRate(hourlyRate);
                partTimeEmp->setHoursWorked(hoursWorked);

                employeeCount++;
            } else {
                cout << "Employee list is full.\n";
            }
            break;
            cout<<"Employee Entered Succesfully \n";

        }
        case 3: {
            for (int i = 0; i < employeeCount; i++) {
                   cout << "--------------------------------------------------\n";
                   cout << "                   V-TECH                   \n";
                cout << "Employee ID: " << employees[i]->getId() << "\n";
                cout << "Employee Name: " << employees[i]->getName() << "\n";
                cout << "Employee Address: " << employees[i]->getAddress() << "\n";
                cout << "Employee Phone Number: " << employees[i]->getPhoneNumber() << "\n";
                cout << "Employee Email: " << employees[i]->getEmail() << "\n";
                cout << "Employee Salary: $" << employees[i]->calculateMonthlySalary() << "\n";
                cout << "Next of Kin:"  << employees[i]->getEmergency_contact() << "\n";
                cout << "Emergency Contact: " << employees[i]->getnextofkin() << "\n";
                cout<<"Employees Displayed Succesfully \n";

            }
            break;
        }
        case 4: {
            ofstream outFile("employees.txt");
            for (int i = 0; i < employeeCount; i++) {
                FullTimeEmployee* fullTimeEmp = dynamic_cast<FullTimeEmployee*>(employees[i]);
                if (fullTimeEmp) {
                    fullTimeEmp->saveToFile(outFile);
                }
                PartTimeEmployee* partTimeEmp = dynamic_cast<PartTimeEmployee*>(employees[i]);
                if (partTimeEmp) {
                    partTimeEmp->saveToFile(outFile);
                }
            }
            outFile.close();
            cout << "Employee data saved to employees.txt.\n";
            break;
        }
case 5: {
    ifstream inFile("employees.txt"); // Open the file for reading

    if (!inFile.is_open()) {
        cout << "Error: Could not open the file for reading.\n";
        break; //return to main menu if file cannot be opened
    }

    // Check if the file is empty
    inFile.seekg(0, ios::end);
    if (inFile.tellg() == 0) {
        cout << "Error: The file is empty.\n";
        inFile.close();
       break;
    }

    // Reset file pointer to the beginning for reading
    inFile.seekg(0, ios::beg);

    string line;
    cout << "Employee Information:\n";
    cout << "---------------------\n";
    while (getline(inFile, line)) {
        // Display each line with better formatting
        cout << line << endl;
    }
    cin.get(); // Wait for user input to continue
    inFile.close(); // Close the file after reading
   break;
}


    case 6: {
    int employeeId;
    cout << "Enter employee ID to generate payslip: ";
    cin >> employeeId;
    bool found = false;

    for (int i = 0; i < employeeCount; i++) {
        if (employees[i]->getId() == employeeId) {
            FullTimeEmployee* fullTimeEmp = dynamic_cast<FullTimeEmployee*>(employees[i]);
            if (fullTimeEmp) {
                fullTimeEmp->generatePayslip();
            } else {
                PartTimeEmployee* partTimeEmp = dynamic_cast<PartTimeEmployee*>(employees[i]);
                if (partTimeEmp) {
                    partTimeEmp->generatePayslip();
                }
            }
            found = true;
            break;
        }
    }

    if (!found) {
        cout << "Employee ID not found.\n";
    }
    break;
}

        
        case 7: {
            int idToDelete;
            cout << "Enter employee ID to delete: ";
            cin >> idToDelete;
            for (int i = 0; i < employeeCount; i++) {
                if (employees[i]->getId() == idToDelete) {
                    delete employees[i];
                    employees[i] = nullptr;
                    // Shift elements to remove gap
                    for (int j = i; j < employeeCount - 1; j++) {
                        employees[j] = employees[j + 1];
                    }
                    employeeCount--;
                    cout << "Employee deleted.\n";
                    break;
                }
            }
            break;
        }
        case 8:
            cout << "Exiting the program.\n";
            break;
        default:
            cout << "Invalid choice. Please try again.\n";
            break;
        }

    } while (choice != 8);

    // Free dynamically allocated memory
    for (int i = 0; i < employeeCount; i++) {
        delete employees[i];
    }

    return 0;
}
