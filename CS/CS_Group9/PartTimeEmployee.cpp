#pragma once
#include <iostream>
#include <fstream>
#include <limits>  // For numeric_limits
#include "employees.cpp"

using namespace std;

class PartTimeEmployee : public Employee {
private:
    double hourlyRate;
    int hoursWorked;
    double medicalAllowance;
    double taxDeduction;
    double grossSalary;

public:
    // Set hourly rate, hours worked, allowances, and deductions
    void setHourlyRate(double rate) { hourlyRate = rate; }
    void setHoursWorked(int hours) { hoursWorked = hours; }
    void setAllowances(double medical) { medicalAllowance = medical; }
    void setDeductions(double tax) { taxDeduction = tax; }

    // Getters
    double getHourlyRate() const { return hourlyRate; }
    int getHoursWorked() const { return hoursWorked; }
    double getMedicalAllowance() const { return medicalAllowance; }
    double getTaxDeduction() const { return taxDeduction; }

    // Overriding the pure virtual function to calculate salary
    double calculateMonthlySalary() const override {
        double grossSalary = (hourlyRate * hoursWorked) + medicalAllowance + taxDeduction;
        return grossSalary;
    }
    double calculateNetSalary() const {
    double netSalary = (hourlyRate * hoursWorked) + medicalAllowance - taxDeduction;
    return netSalary;
}

    // Save PartTimeEmployee details to a file
    void saveToFile(ofstream& outFile) const {
        outFile << "Employee Type :PartTime,\n"; // Identifier for PartTimeEmployee
        outFile <<"Employee ID: " << getId() << ".\n"; // Unique ID
        outFile <<"Employee Name: " << getName() << ".\n" <<"Employee Address: " << getAddress() << ".\n" <<"Employee Phone Number: "<< getPhoneNumber() << ".\n" <<"Employee Email: "<< getEmail() << ".\n";
         outFile <<"Gross Salary: $"<< calculateMonthlySalary() << ".\n" <<"Net Salary: $"<<calculateNetSalary() <<"\n";
    }

    // Load data from file with improved error handling and validation
  void loadFromFile(ifstream& inFile) {
    int id;
    string name, address, phone, email;
    double grossSalary, netSalary;

    // Read data directly from the file stream without detailed validation
    inFile >> id;
    inFile.ignore(); // Ignore comma or delimiter
    getline(inFile, name, ',');
    getline(inFile, address, ',');
    getline(inFile, phone, ',');
    getline(inFile, email, ',');
    inFile >> grossSalary;
    inFile.ignore(); // Ignore comma
    inFile >> netSalary;
    
    // Set the details without further validation
   calculateMonthlySalary();
   calculateNetSalary();

    cout << "PartTimeEmployee loaded successfully.\n";
}


    // Method to generate a professional payslip
    void generatePayslip() const {
        double grossSalary = (hourlyRate * hoursWorked) + medicalAllowance;
        double netSalary = calculateNetSalary();

        cout << "--------------------------------------------------\n";
        cout << "                   V TECH                    \n";
        cout << "              Part-Time Employee Payslip          \n";
        cout << "--------------------------------------------------\n";
        cout << "Employee ID: " << getId() << endl;
        cout << "Name: " << getName() << endl;
        cout << "Job Title: Part-Time Employee\n";
        cout << "--------------------------------------------------\n";
        cout << "Hourly Rate: $" << hourlyRate << endl;
        cout << "Hours Worked: " << hoursWorked << endl;
        cout << "--------------------------------------------------\n";
        cout << "Gross Salary: $" << grossSalary << endl;
        cout << "--------------------------------------------------\n";
        cout << "Net Salary: $" << netSalary << endl;
        cout << "--------------------------------------------------\n";
    }
};
