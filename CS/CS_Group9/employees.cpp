#pragma once
#include <iostream>
#include <string>
#include <unordered_set>
using namespace std;

class Employee {
private:
    static unordered_set<int> assignedIds; // Set to keep track of assigned IDs
    int id;
    string name;
    string address;
    string phoneNumber;
    string email;
    string nextofkin;
    string emergency_contact;

public:
    // Method to set employee details
    bool setDetails(int employeeId, const string& employeeName, const string& employeeAddress,
                    const string& employeePhoneNumber, const string& employeeEmail,
                    const string& employeeNextofkin, const string& employeeEmergencyContact) {
        if (assignedIds.find(employeeId) != assignedIds.end()) {
            cout << "Error: Employee ID " << employeeId << " is already assigned to another employee.\n";
            return false;
        }
        if (id > 0) {
            assignedIds.erase(id); // Remove previous ID if changing details
        }
        id = employeeId;
        assignedIds.insert(employeeId);
        name = employeeName;
        address = employeeAddress;
        phoneNumber = employeePhoneNumber;
        email = employeeEmail;
        nextofkin = employeeNextofkin;
        emergency_contact = employeeEmergencyContact;
        return true;
    }

    // Getters for employee data
    int getId() const { return id; }
    string getName() const { return name; }
    string getAddress() const { return address; }
    string getPhoneNumber() const { return phoneNumber; }
    string getEmail() const { return email; }
    string getEmergency_contact() const { return emergency_contact; }
    string getnextofkin() const { return nextofkin; }

    // Virtual function for generating pay (for polymorphism)
    virtual double calculateMonthlySalary() const = 0;  // Pure virtual function to be implemented by subclasses

    // Destructor to clean up assigned ID when an object is destroyed
    virtual ~Employee() {
        if (id > 0) {
            assignedIds.erase(id);
        }
    }
};

// Initialize the static member
unordered_set<int> Employee::assignedIds;