#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

class Contact {
public:
    string name;
    string phoneNumber;
    string email;
    string address;
    int age;
    string birthday;
    string gender;
    string occupation;
    string company;
    string emergencyContact;
    string socialMediaHandle;
    string notes;

    // Constructor to initialize the contact
    Contact(string name, string phone, string email, string address, int age, string birthday,
            string gender, string occupation, string company, string emergencyContact,
            string socialMediaHandle, string notes)
        : name(name), phoneNumber(phone), email(email), address(address), age(age), birthday(birthday),
          gender(gender), occupation(occupation), company(company), emergencyContact(emergencyContact),
          socialMediaHandle(socialMediaHandle), notes(notes) {}

    // Display the contact details
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

class ContactManagementSystem {
private:
    vector<Contact> contacts;

public:
    void addContact() {
        string name, phone, email, address, birthday, gender, occupation, company, emergencyContact, socialMediaHandle, notes;
        int age;

        // Collecting details from the user
        cout << "Enter Name: ";
        getline(cin, name);
        cout << "Enter Phone Number: ";
        getline(cin, phone);
        cout << "Enter Email: ";
        getline(cin, email);
        cout << "Enter Address: ";
        getline(cin, address);
        cout << "Enter Age: ";
        cin >> age;
        cin.ignore();  // to clear the newline character left by cin
        cout << "Enter Birthday (YYYY-MM-DD): ";
        getline(cin, birthday);
        cout << "Enter Gender: ";
        getline(cin, gender);
        cout << "Enter Occupation: ";
        getline(cin, occupation);
        cout << "Enter Company: ";
        getline(cin, company);
        cout << "Enter Emergency Contact: ";
        getline(cin, emergencyContact);
        cout << "Enter Social Media Handle: ";
        getline(cin, socialMediaHandle);
        cout << "Enter Notes: ";
        getline(cin, notes);

        // Create a new contact and add it to the list
        contacts.push_back(Contact(name, phone, email, address, age, birthday, gender, occupation, company, emergencyContact, socialMediaHandle, notes));
        cout << "Contact added successfully!" << endl;
    }

    void displayAllContacts() const {
        if (contacts.empty()) {
            cout << "No contacts available." << endl;
            return;
        }
        for (size_t i = 0; i < contacts.size(); ++i) {
            cout << "\nContact " << i + 1 << ":" << endl;
            contacts[i].display();
            cout << "-----------------------------" << endl;
        }
    }

    void searchContactByName() const {
        string name;
        cout << "Enter the name to search: ";
        getline(cin, name);

        bool found = false;
        for (size_t i = 0; i < contacts.size(); ++i) {
            if (contacts[i].name == name) {
                contacts[i].display();
                found = true;
                break;
            }
        }

        if (!found) {
            cout << "Contact not found!" << endl;
        }
    }

    void editContact() {
        string name;
        cout << "Enter the name of the contact you want to edit: ";
        getline(cin, name);

        bool found = false;
        for (size_t i = 0; i < contacts.size(); ++i) {
            if (contacts[i].name == name) {
                found = true;

                string phone, email, address, birthday, gender, occupation, company, emergencyContact, socialMediaHandle, notes;
                int age;

                cout << "Edit Phone Number: ";
                getline(cin, phone);
                cout << "Edit Email: ";
                getline(cin, email);
                cout << "Edit Address: ";
                getline(cin, address);
                cout << "Edit Age: ";
                cin >> age;
                cin.ignore();
                cout << "Edit Birthday (YYYY-MM-DD): ";
                getline(cin, birthday);
                cout << "Edit Gender: ";
                getline(cin, gender);
                cout << "Edit Occupation: ";
                getline(cin, occupation);
                cout << "Edit Company: ";
                getline(cin, company);
                cout << "Edit Emergency Contact: ";
                getline(cin, emergencyContact);
                cout << "Edit Social Media Handle: ";
                getline(cin, socialMediaHandle);
                cout << "Edit Notes: ";
                getline(cin, notes);

                contacts[i] = Contact(name, phone, email, address, age, birthday, gender, occupation, company, emergencyContact, socialMediaHandle, notes);
                cout << "Contact updated successfully!" << endl;
                break;
            }
        }

        if (!found) {
            cout << "Contact not found!" << endl;
        }
    }

    void deleteContact() {
        string name;
        cout << "Enter the name of the contact you want to delete: ";
        getline(cin, name);

        bool found = false;
        for (size_t i = 0; i < contacts.size(); ++i) {
            if (contacts[i].name == name) {
                contacts.erase(contacts.begin() + i);
                cout << "Contact deleted successfully!" << endl;
                found = true;
                break;
            }
        }

        if (!found) {
            cout << "Contact not found!" << endl;
        }
    }

    void displayMenu() const {
        cout << "\nContact Management System" << endl;
        cout << "1. Add Contact" << endl;
        cout << "2. Display All Contacts" << endl;
        cout << "3. Search Contact by Name" << endl;
        cout << "4. Edit Contact" << endl;
        cout << "5. Delete Contact" << endl;
        cout << "6. Display Total Contacts" << endl;
        cout << "7. Search Contact by Phone Number" << endl;
        cout << "8. Display Contacts by Age" << endl;
        cout << "9. Display Contacts by Birthday" << endl;
        cout << "10. Sort Contacts by Name" << endl;
        cout << "11. Sort Contacts by Age" << endl;
        cout << "12. Exit" << endl;
    }

    void displayTotalContacts() const {
        cout << "Total Contacts: " << contacts.size() << endl;
    }

    void searchContactByPhone() const {
        string phone;
        cout << "Enter the phone number to search: ";
        getline(cin, phone);

        bool found = false;
        for (size_t i = 0; i < contacts.size(); ++i) {
            if (contacts[i].phoneNumber == phone) {
                contacts[i].display();
                found = true;
                break;
            }
        }

        if (!found) {
            cout << "Contact not found!" << endl;
        }
    }

    void displayContactsByAge() const {
        int age;
        cout << "Enter the age to search: ";
        cin >> age;
        cin.ignore();

        bool found = false;
        for (size_t i = 0; i < contacts.size(); ++i) {
            if (contacts[i].age == age) {
                contacts[i].display();
                found = true;
            }
        }

        if (!found) {
            cout << "No contacts found with age " << age << endl;
        }
    }

    void displayContactsByBirthday() const {
        string birthday;
        cout << "Enter the birthday to search (YYYY-MM-DD): ";
        getline(cin, birthday);

        bool found = false;
        for (size_t i = 0; i < contacts.size(); ++i) {
            if (contacts[i].birthday == birthday) {
                contacts[i].display();
                found = true;
            }
        }

        if (!found) {
            cout << "No contacts found with birthday " << birthday << endl;
        }
    }

    void sortContactsByName() {
        sort(contacts.begin(), contacts.end(), [](const Contact &a, const Contact &b) {
            return a.name < b.name;
        });
        cout << "Contacts sorted by name." << endl;
    }

    void sortContactsByAge() {
        sort(contacts.begin(), contacts.end(), [](const Contact &a, const Contact &b) {
            return a.age < b.age;
        });
        cout << "Contacts sorted by age." << endl;
    }
};

int main() {
    ContactManagementSystem system;
    int choice;

    while (true) {
        system.displayMenu();
        cout << "Enter your choice (1-12): ";
        cin >> choice;
        cin.ignore();  // to ignore the newline character left by cin

        switch (choice) {
            case 1: system.addContact(); break;
            case 2: system.displayAllContacts(); break;
            case 3: system.searchContactByName(); break;
            case 4: system.editContact(); break;
            case 5: system.deleteContact(); break;
            case 6: system.displayTotalContacts(); break;
            case 7: system.searchContactByPhone(); break;
            case 8: system.displayContactsByAge(); break;
            case 9: system.displayContactsByBirthday(); break;
            case 10: system.sortContactsByName(); break;
            case 11: system.sortContactsByAge(); break;
            case 12: cout << "Exiting..." << endl; return 0;
            default: cout << "Invalid choice! Please enter a valid option (1-12)." << endl;
        }
    }
    return 0;
}

        