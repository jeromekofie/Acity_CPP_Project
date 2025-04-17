#include <iostream>
#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <algorithm>// searching and sorting
#include "contact1.cpp"


int main() {
    int choice;//declaration
//dw
    do {
        displayMenu();
        cin >> choice;
        cin.ignore();
//sw dv
        switch (choice) {
            case 1: {
                string firstName, lastName, address, email;
                long long phoneNumber;
                cout << "Enter first name: ";
                getline(cin, firstName);
                cout << "Enter last name: ";
                getline(cin, lastName);
                cout << "Enter phone number: ";
                cin >> phoneNumber;
                cin.ignore();
                cout << "Enter address: ";
                getline(cin, address);
                cout << "Enter email: ";
                getline(cin, email);

                Contact newContact(firstName, lastName, phoneNumber, address, email);
                
         
                if (newContact.contactExists()) {
                    cout << "Contact with this phone number already exists!" << endl;
                } else {
                    newContact.saveToFile();
                    cout << "Contact added successfully!" << endl;
                }
                break;
            }
            case 2: {
                vector<Contact> contacts = Contact::loadContacts();
                if (contacts.empty()) {
                    cout << "No contacts available." << endl;
                } else {
                    for (const auto& contact : contacts) {
                        contact.display();
                        cout << "-----------------------------" << endl;
                    }
                }
                break;
            }
            case 3: {
                long long phone;
                cout << "Enter phone number to search: ";
                cin >> phone;
                vector<Contact> contacts = Contact::loadContacts();
                bool found = false;
                for (const auto& contact : contacts) { //floop
                    if (contact.getPhoneNumber() == phone) {
                        contact.display();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    cout << "Contact not found!" << endl;
                }
                break;
            }
            case 4: {
                long long phone;
                cout << "Enter phone number to delete: ";
                cin >> phone;
                if (Contact::deleteContact(phone)) {
                    cout << "Contact deleted successfully!" << endl;
                } else {
                    cout << "Contact not found!" << endl;
                }
                break;
            }
            case 5:
                Contact::sortByName();// uses the contact from the con class in the main
                break;
            case 6:
                Contact::sortByPhone();
                break;
            case 7:
                cout << "Exiting system...\n" ;
                break;
            default:
                cout << "Invalid choice! Try again.\n" ;
        }
    } while (choice != 7);

    return 0;
}