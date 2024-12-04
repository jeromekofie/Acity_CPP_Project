#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <stdexcept>
#include <limits>

using namespace std;

// Contact class definition
class Contact {
public:
    string name, phoneNumber, email, address, birthday, gender, occupation, company, emergencyContact, socialMediaHandle, notes;
    int age;

    // Constructor to initialize a contact
    Contact(string name, string phone, string email, string address, int age, string birthday, 
            string gender, string occupation, string company, string emergencyContact, 
            string socialMediaHandle, string notes)
        : name(name), phoneNumber(phone), email(email), address(address), age(age), birthday(birthday),
          gender(gender), occupation(occupation), company(company), emergencyContact(emergencyContact),
          socialMediaHandle(socialMediaHandle), notes(notes) {}

    // Display contact details
    void display() const {
        cout << "\nName: " << name << endl;
        cout << "Phone Number: " << phoneNumber << endl;
        cout << "Email: " << email << endl;
        cout << "Address: " << address << endl;
        cout << "Age: " << age << endl;
        cout << "Birthday: " << birthday << endl;
        cout << "Gender: " << gender << endl;
        cout << "Occupation: " << occupation << endl;
        cout << "Company: " << company << endl;
        cout << "Emergency Contact: " << emergencyContact << endl;
        cout << "Social Media Handle: " << socialMediaHandle << endl;
        cout << "Notes: " << notes << endl;
    }
};

// ContactManagementSystem class definition
class ContactManagementSystem {
public:
    vector<Contact> contacts;  // A vector to store contacts

    // Function to add a contact
    void addContact() {
        string name, phone, email, address, birthday, gender, occupation, company, emergencyContact, socialMediaHandle, notes;
        int age;

        cout << "Enter name: ";
        getline(cin, name);
        cout << "Enter phone number: ";
        getline(cin, phone);
        cout << "Enter email: ";
        getline(cin, email);
        cout << "Enter address: ";
        getline(cin, address);
        
        while (true) {
            cout << "Enter age: ";
            cin >> age;
            if (cin.fail()) {
                cout << "Invalid input! Please enter a valid age.\n";
                cin.clear(); // clear input buffer
                cin.ignore(numeric_limits<streamsize>::max(), '\n'); // discard invalid input
            } else {
                cin.ignore(); // To clear the newline character left by cin
                break;
            }
        }

        cout << "Enter birthday: ";
        getline(cin, birthday);
        cout << "Enter gender: ";
        getline(cin, gender);
        cout << "Enter occupation: ";
        getline(cin, occupation);
        cout << "Enter company: ";
        getline(cin, company);
        cout << "Enter emergency contact: ";
        getline(cin, emergencyContact);
        cout << "Enter social media handle: ";
        getline(cin, socialMediaHandle);
        cout << "Enter any notes: ";
        getline(cin, notes);

        // Add the new contact to the vector
        contacts.push_back(Contact(name, phone, email, address, age, birthday, gender, occupation, company, emergencyContact, socialMediaHandle, notes));
        cout << "Contact added successfully!" << endl;
    }

    // Function to display all contacts
    void displayAllContacts() const {
        if (contacts.empty()) {
            cout << "No contacts available!" << endl;
            return;
        }

        for (const auto& contact : contacts) {
            contact.display();
        }
    }

    // Function to search for a contact by name
    void searchContactByName() const {
        string name;
        cout << "Enter the name to search: ";
        getline(cin, name);
        bool found = false;

        for (const auto& contact : contacts) {
            if (contact.name == name) {
                contact.display();
                found = true;
            }
        }

        if (!found) {
            cout << "Contact not found!" << endl;
        }
    }

    // Function to delete a contact by name
    void deleteContact() {
        string name;
        cout << "Enter the name of the contact to delete: ";
        getline(cin, name);

        auto it = remove_if(contacts.begin(), contacts.end(), [&name](const Contact& contact) {
            return contact.name == name;
        });

        if (it != contacts.end()) {
            contacts.erase(it, contacts.end());
            cout << "Contact deleted successfully!" << endl;
        } else {
            cout << "Contact not found!" << endl;
        }
    }

    // Function to display total number of contacts
    void displayTotalContacts() const {
        cout << "Total contacts: " << contacts.size() << endl;
    }

    // Function to search for a contact by phone
    void searchContactByPhone() const {
        string phone;
        cout << "Enter the phone number to search: ";
        getline(cin, phone);
        bool found = false;

        for (const auto& contact : contacts) {
            if (contact.phoneNumber == phone) {
                contact.display();
                found = true;
            }
        }

        if (!found) {
            cout << "Contact not found!" << endl;
        }
    }

    // Function to display menu
    void displayMenu() const {
        cout << "\n---- Contact Management System ----" << endl;
        cout << "1. Add a Contact" << endl;
        cout << "2. Display All Contacts" << endl;
        cout << "3. Search Contact by Name" << endl;
        cout << "4. Edit a Contact (Not implemented yet)" << endl;
        cout << "5. Delete a Contact" << endl;
        cout << "6. Display Total Contacts" << endl;
        cout << "7. Search Contact by Phone" << endl;
        cout << "8. Display Contacts by Age (Not implemented yet)" << endl;
        cout << "9. Display Contacts by Birthday (Not implemented yet)" << endl;
        cout << "10. Sort Contacts by Name" << endl;
        cout << "11. Sort Contacts by Age" << endl;
        cout << "12. Exit" << endl;
    }

    // Function to sort contacts by name
    void sortContactsByName() {
        sort(contacts.begin(), contacts.end(), [](const Contact& a, const Contact& b) {
            return a.name < b.name;
        });
        cout << "Contacts sorted by name." << endl;
    }

    // Function to sort contacts by age
    void sortContactsByAge() {
        sort(contacts.begin(), contacts.end(), [](const Contact& a, const Contact& b) {
            return a.age < b.age;
        });
        cout << "Contacts sorted by age." << endl;
    }
};

// Main function
int main() {
    ContactManagementSystem system;
    int choice;

    while (true) {
        system.displayMenu();
        cout << "Enter your choice (1-12): ";
        cin >> choice;
        cin.ignore(); // Clear newline character after integer input

        switch (choice) {
            case 1: system.addContact(); break;
            case 2: system.displayAllContacts(); break;
            case 3: system.searchContactByName(); break;
            case 4: cout << "Edit functionality not implemented yet." << endl; break;
            case 5: system.deleteContact(); break;
            case 6: system.displayTotalContacts(); break;
            case 7: system.searchContactByPhone(); break;
            case 8: cout << "Display contacts by age not implemented yet." << endl; break;
            case 9: cout << "Display contacts by birthday not implemented yet." << endl; break;
            case 10: system.sortContactsByName(); break;
            case 11: system.sortContactsByAge(); break;
            case 12: cout << "Exiting..." << endl; return 0;
            default: cout << "Invalid choice! Please enter a valid option (1-12)." << endl;
        }
    }

    return 0;
}

        