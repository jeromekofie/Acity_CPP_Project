#pragma once
#include <iostream>
#include <fstream>
#include <limits>  // For numeric_limits
#include "employees.cpp"

using namespace std;

class FullTimeEmployee : public Employee {
private:
    double monthlySalary;
    double medicalAllowance;
    double housingAllowance;
    double taxDeduction;

public:
    // Method to set salary and allowances
    void setSalary(double salary) { monthlySalary = salary; }
    void setAllowances(double medical, double housing) {
        medicalAllowance = medical;
        housingAllowance = housing;
    }
    void setDeductions(double tax) { taxDeduction = tax; }

    // Getters
    double getSalary() const { return monthlySalary; }
    double getMedicalAllowance() const { return medicalAllowance; }
    double getHousingAllowance() const { return housingAllowance; }
    double getTaxDeduction() const { return taxDeduction; }

    // Overriding the pure virtual function to calculate salary
    double calculateMonthlySalary() const override {
        return monthlySalary + medicalAllowance + housingAllowance + taxDeduction;
    }
    double calculateNetSalary() const {
    double netSalary = monthlySalary + medicalAllowance + housingAllowance - taxDeduction;
    return netSalary;
    }

    // Save FullTimeEmployee details to a file
    void saveToFile(ofstream& outFile) const {
        outFile << " Employee Type: FullTime. \n"; // Identifier for FullTimeEmployee
        outFile <<"Employee ID: " << getId() << ".\n"; // Unique ID
        outFile <<"Employee Name: " << getName() << "./n" <<"Employee Address: "<< getAddress() << ".\n" <<"Employee    Phone Number: "<< getPhoneNumber() << ".\n" <<"Employee Email: "<< getEmail() << ".\n";
        outFile <<"Gross Salary: $"<< calculateMonthlySalary() << ".\n" <<"Net Salary: $"<<calculateNetSalary() <<"\n";
    }

    void loadFromFile(ifstream& inFile) {
    int id;
    string name, address, phone, email;
    double salary, medical, housing, tax;

    // Read data directly from the file stream without detailed validation
    inFile >>  id ;
    inFile.ignore(); // Ignore comma or delimiter
    getline(inFile, name, ',');
    getline(inFile, address, ',');
    getline(inFile, phone, ',');
    getline(inFile, email, ',');
    inFile >> salary;
    inFile.ignore(); // Ignore comma
    inFile >> medical;
    inFile.ignore(); // Ignore comma
    inFile >> housing;
    inFile.ignore(); // Ignore comma
    inFile >> tax;

    // Set the details without further validation
    setSalary(salary);
    setAllowances(medical, housing);
    setDeductions(tax);

    cout << "FullTimeEmployee loaded successfully.\n";
}

    // Method to generate a professional payslip
    void generatePayslip() const {
        double grossSalary = monthlySalary + medicalAllowance + housingAllowance + taxDeduction;
        double netSalary = calculateNetSalary();

        cout << "--------------------------------------------------\n";
        cout << "                   COMPANY NAME                   \n";
        cout << "              Full-Time Employee Payslip          \n";
        cout << "--------------------------------------------------\n";
        cout << "Employee ID: " << getId() << endl;
        cout << "Name: " << getName() << endl;
        cout << "Job Title: Full-Time Employee\n";
        cout << "--------------------------------------------------\n";
        cout << "Basic Monthly Salary: $" << monthlySalary << endl;
        cout << "Medical Allowance: $" << medicalAllowance << endl;
        cout << "Housing Allowance: $" << housingAllowance << endl;
        cout << "--------------------------------------------------\n";
        cout << "Gross Salary: $" << grossSalary << endl;
        cout << "Tax Deduction: $" << taxDeduction << endl;
        cout << "--------------------------------------------------\n";
        cout << "Net Salary: $" << netSalary << endl;
        cout << "--------------------------------------------------\n";
    }
};
