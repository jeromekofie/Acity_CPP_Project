#ifndef CONTACT_H
#define CONTACT_H

#include <iostream>
#include <fstream>
#include <cstring>
using namespace std;

class Contact {
private:
    char fName[50], lName[50], address[50], email[50];
    long long phone_num;

public:
    
    void createContact() {
        cout << "Enter first name: ";
        cin >> fName;
        cout << "Enter last name: ";
        cin >> lName;
        cout << "Enter phone number: ";
        cin >> phone_num;
        cout << "Enter address: ";
        cin >> address;
        cout << "Enter email: ";
        cin >> email;
    }

    void showContact() const {
        cout << "Name: " << fName << " " << lName << endl;
        cout << "Phone: " << phone_num << endl;
        cout << "Address: " << address << endl;
        cout << "Email: " << email << endl;
    }

    long long getPhoneNumber() const {
        return phone_num;
    }

    static bool contactExists(long long phone) {
        ifstream inFile("CMS.dat", ios::binary);
        Contact temp;
        while (inFile.read(reinterpret_cast<char*>(&temp), sizeof(temp))) {
            if (temp.getPhoneNumber() == phone) {
                inFile.close();
                return true;
            }
        }
        inFile.close();
        return false;
    }

    void writeToFile() {
        ofstream outFile("CMS.dat", ios::binary | ios::app);
        outFile.write(reinterpret_cast<const char*>(this), sizeof(*this));
        outFile.close();
    }

    static void displayAllContacts() {
        ifstream inFile("CMS.dat", ios::binary);
        if (!inFile) {
            cout << "No contacts found!" << endl;
            return;
        }
        Contact temp;
        while (inFile.read(reinterpret_cast<char*>(&temp), sizeof(temp))) {
            temp.showContact();
            cout << "--------------------" << endl;
        }
        inFile.close();
    }

    static bool deleteContact(long long phone) {
        ifstream inFile("CMS.dat", ios::binary);
        ofstream outFile("Temp.dat", ios::binary);

        if (!inFile) {
            cout << "No contacts found!" << endl;
            return false;
        }

        Contact temp;
        bool found = false;
        while (inFile.read(reinterpret_cast<char*>(&temp), sizeof(temp))) {
            if (temp.getPhoneNumber() == phone) {
                found = true;  
            } else {
                outFile.write(reinterpret_cast<const char*>(&temp), sizeof(temp));
            }
        }

        inFile.close();
        outFile.close();

        remove("CMS.dat");
        rename("Temp.dat", "CMS.dat");

        return found;
    }

    static void sortContacts(bool byName) {
        ifstream inFile("CMS.dat", ios::binary);
        ofstream outFile("Temp.dat", ios::binary);
        Contact contacts[100]; 
        int count = 0;

       
        while (inFile.read(reinterpret_cast<char*>(&contacts[count]), sizeof(Contact))) {
            count++;
        }
        inFile.close();

       
        bool swapCondition;
        if (byName) {
            swapCondition = (strcmp(contacts[i].fName, contacts[j].fName) > 0);  // Compare names
        } else {

            swapCondition = (contacts[i].getPhoneNumber() > contacts[j].getPhoneNumber());  // Compare phone numbers
        }


                if (swapCondition) {
                    Contact temp = contacts[i];
                    contacts[i] = contacts[j];
                    contacts[j] = temp;
                }
            }
        }

       
        for (int i = 0; i < count; i++) {
            outFile.write(reinterpret_cast<const char*>(&contacts[i]), sizeof(Contact));
        }
        outFile.close();

        remove("CMS.dat");
        rename("Temp.dat", "CMS.dat");

        cout << "Contacts sorted successfully!" << endl;
    }
};

#endif 

