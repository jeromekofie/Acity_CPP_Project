#include <iostream>
#include <fstream>
#include <vector> //the vector contacts stores all the contact objects that are read from the file
#include <string>
#include <algorithm>

using namespace std;
//local
class Contact {
private:
    string firstName, lastName, address, email;//enscapulations
    long long phoneNumber;

public:
   //cons//variable declration//assignment
    Contact(string fName = "", string lName = "", long long phone = 0, string addr = "", string mail = "")
        : firstName(fName), lastName(lName), phoneNumber(phone), address(addr), email(mail) {}

    //set
    void setFirstName(string fName) { firstName = fName; }
    void setLastName(string lName) { lastName = lName; }
    void setPhoneNumber(long long phone) { phoneNumber = phone; }
    void setAddress(string addr) { address = addr; }
    void setEmail(string mail) { email = mail; }

   //get
    string getFirstName() const { return firstName; }
    string getLastName() const { return lastName; }
    long long getPhoneNumber() const { return phoneNumber; }
    string getAddress() const { return address; }
    string getEmail() const { return email; }

   //func
    void display() const {
        cout << "Name: " << firstName << " " << lastName << endl;
        cout << "Phone: " << phoneNumber << endl;
        cout << "Address: " << address << endl;
        cout << "Email: " << email << endl;
    }

   //file
    void saveToFile() const {
        ofstream outFile("contacts.txt", ios::app);
        outFile << firstName << " " << lastName << " " << phoneNumber << " " << address << " " << email << endl;
        outFile.close();
    }

   //TF
    bool contactExists() const {
        ifstream inFile("contacts.txt");
        string fName, lName, addr, mail;
        long long phoneNum;
        while (inFile >> fName >> lName >> phoneNum >> ws) {
            getline(inFile, addr);
            getline(inFile, mail);
            if (phoneNum == phoneNumber) {
                return true;
            }
        }
        return false;
    }

   //the vector contacts stores all the contact objects that are read from the file
    static vector<Contact> loadContacts() {
        vector<Contact> contacts;
        ifstream inFile("contacts.txt");
        string fName, lName, addr, mail;
        long long phoneNum;
        while (inFile >> fName >> lName >> phoneNum >> ws) {
            getline(inFile, addr);
            getline(inFile, mail);
            contacts.push_back(Contact(fName, lName, phoneNum, addr, mail));//pushback allows you to write after a vector
        }
        return contacts;
    }

   
    static bool deleteContact(long long phone) {
        vector<Contact> contacts = loadContacts();
        bool found = false;

        ofstream outFile("contacts.txt", ios::trunc);
        for (const auto& contact : contacts) {
            if (contact.getPhoneNumber() == phone) {//if
                found = true;
                continue;
            }
            outFile << contact.getFirstName() << " " << contact.getLastName() << " " << contact.getPhoneNumber()
                    << " " << contact.getAddress() << " " << contact.getEmail() << endl;
        }

        outFile.close();
        return found;
    }

    // Sort contacts by name
    static void sortByName() {
        vector<Contact> contacts = loadContacts();
        sort(contacts.begin(), contacts.end(), [](const Contact& c1, const Contact& c2) {
            return c1.getFirstName() < c2.getFirstName();
        });
//LOOP
        for (const auto& contact : contacts) {
            contact.display();
        }
    }

 // Sort contacts by phone number
    static void sortByPhone() {
        vector<Contact> contacts = loadContacts();
        sort(contacts.begin(), contacts.end(), [](const Contact& c1, const Contact& c2) {
            return c1.getPhoneNumber() < c2.getPhoneNumber();
        });

        for (const auto& contact : contacts) {
            contact.display();
        }
    }
};
//menudr
void displayMenu() {
    cout << "\n--- Contact Management System ---" << endl;//n
    cout << "1. Add Contact" << endl;
    cout << "2. Show All Contacts" << endl;
    cout << "3. Search Contact by Phone Number\n" ;
    cout << "4. Delete Contact by Phone Number\n" ;
    cout << "5. Sort Contacts by Name\n" ;
    cout << "6. Sort Contacts by Phone Number" << endl;
    cout << "7. Exit\n";
    cout << "Enter your choice: ";
}