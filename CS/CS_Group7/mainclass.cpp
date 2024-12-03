#include <iostream>
#include "Contact.cpp"

using namespace std;

void displayMenu() {
    cout << "Contact Management System" << endl;
    cout << "1. Add Contact" << endl;
    cout << "2. Show All Contacts" << endl;
    cout << "3. Search Contact" << endl;
    cout << "4. Delete Contact" << endl;
    cout << "5. Sort Contacts by Name" << endl;
    cout << "6. Sort Contacts by Phone Number" << endl;
    cout << "7. Exit" << endl;
    cout << "Enter your choice: ";
}

int main() {
    int choice;

    do {
        displayMenu();
        cin >> choice;

        switch (choice) {
            case 1: {
                Contact newContact;
                newContact.createContact();
                if (Contact::contactExists(newContact.getPhoneNumber())) {
                    cout << "A contact with this phone number already exists!" << endl;
                } else {
                    newContact.writeToFile();
                    cout << "Contact added successfully!" << endl;
                }
                break;
            }
            case 2:
                Contact::displayAllContacts();
                break;
            case 3: {
                long long phone;
                cout << "Enter phone number to search: ";
                cin >> phone;
                if (Contact::contactExists(phone)) {
                    cout << "Contact found and displayed above!" << endl;
                } else {
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
                Contact::sortContacts(true);
                break;
            case 6:
                Contact::sortContacts(false);
                break;
            case 7:
                cout << "Exiting Contact Management System. Goodbye!" << endl;
                break;
            default:
                cout << "Invalid choice! Please try again." << endl;
        }
    } while (choice != 7);

    return 0;
}

