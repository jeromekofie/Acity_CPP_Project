#include <iostream>
#include <windows.h>
#include <memory>
#include "EmployeeClasses.cpp"

using namespace std;

// Function to simulate loading animation
void Cdelay(int ms) {
    Sleep(ms);
}

void loading() {
    system("cls"); // Clear the screen

    string loadingText = "Loading ";
    int progressBarLength = 20; // Length of the progress bar
    int xPos = (getConsoleWidth() - (loadingText.length() + progressBarLength)) / 2;
    int yPosText = 12; // Y position for the loading text
    int yPosBar = 12;  // Y position for the progress bar

    // Display loading text
    gotoXY(xPos, yPosText);
    cout << loadingText;

    // Display progress bar
    gotoXY(xPos + loadingText.length(), yPosBar); // Position progress bar next to the text
    for (int i = 0; i < progressBarLength; i++) {
        cout << char(254); // Filled rectangle character
        Sleep(100); // Simulate loading delay
    }

    Sleep(500); // Pause before clearing
    system("cls"); // Clear the screen after loading completes
}

int main() {
    cout << fixed << setprecision(2);

    bool exitProgram = false;

    while (!exitProgram) {
        loading();

        displayCentered("Payroll Management System");
        int choice;
        string username, password;

        displayCentered("1. Create Account");
        displayCentered("2. Login");
        displayCentered("3. Exit");
        displayCentered("Choose an option: ", true);
        cin >> choice;
        cin.ignore();

        if (choice == 1) {
            system("cls");
            loading();
            displayCentered("Enter username: ", true);
            getline(cin, username);
            displayCentered("Enter password: ", true);
            getline(cin, password);
            if (createAccount(username, password)) {
                displayCentered("Account created successfully!");
            } else {
                displayCentered("Account creation failed.");
                continue;
            }
        } else if (choice == 2) {
            system("cls");
            loading();
            displayCentered("Enter username: ", true);
            getline(cin, username);
            displayCentered("Enter password: ", true);
            getline(cin, password);
            if (!login(username, password)) {
                displayCentered("Login failed.");
                continue;
            }
        } else if (choice == 3) {
            system("cls");
            loading();
            displayCentered("Exiting the program...");
            exitProgram = true;
            break;
        }

        if (!exitProgram) {
            loading();
            displayCentered("Welcome, " + username + "!");

            int empType;
            displayCentered("Choose Employee Type:");
            displayCentered("1. Salaried Employee");
            displayCentered("2. Hourly Employee");
            displayCentered("Choice: ", true);
            cin >> empType;

            unique_ptr<Employee> employee;
            if (empType == 1) {
                double baseSalary;
                displayCentered("Enter Name: ", true);
                string name;
                cin >> name;
                displayCentered("Enter ID: ", true);
                int id;
                cin >> id;
                displayCentered("Enter Base Salary: ", true);
                cin >> baseSalary;

                employee = make_unique<SalariedEmployee>(name, id, baseSalary);
            } else {
                double hourlyRate;
                int hoursWorked;
                displayCentered("Enter Name: ", true);
                string name;
                cin >> name;
                displayCentered("Enter ID: ", true);
                int id;
                cin >> id;
                displayCentered("Enter Hourly Rate: ", true);
                cin >> hourlyRate;
                displayCentered("Enter Hours Worked: ", true);
                cin >> hoursWorked;

                employee = make_unique<HourlyEmployee>(name, id, hourlyRate, hoursWorked);
            }

            loading();
            cout << fixed << setprecision(2);
            displayCentered("Total Salary: GHS " + to_string(employee->calculateSalary()));

            savePayrollData(*employee);
            displayCentered("Payroll data saved successfully.");
        }

        displayCentered("Returning to main menu...");
        Sleep(1500);
        system("cls");
    }

    return 0;
}

