
#include <iostream>
#include <fstream>
#include <cstring>
#include <string>  // Include the string library
using namespace std;

class Contact {
private:
    string fName, lName, address, email;  // Use std::string instead of char arrays
    long long phone_num;

public:
    void createContact() {
        cout << "Enter first name: ";
        cin >> fName;
        cout << "Enter last name: ";
        cin >> lName;
        cout << "Enter phone number: ";
        cin >> phone_num;
        cin.ignore();  // Ignore the newline character from previous input
        cout << "Enter address: ";
        getline(cin, address);  // Use getline for address
        cout << "Enter email: ";
        getline(cin, email);  // Use getline for email
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
        ifstream inFile("CMS.txt", ios::in);  // Open in text mode for reading
        string fName, lName, address, email;
        long long phone_num;
        while (inFile >> fName >> lName >> phone_num) {
            inFile.ignore();  // Ignore the remaining characters of the line
            getline(inFile, address);  // Read the entire address line
            getline(inFile, email);  // Read the entire email line
            if (phone_num == phone) {
                inFile.close();
                return true;
            }
        }
        inFile.close();
        return false;
    }

    void writeToFile() {
        ofstream outFile("CMS.txt", ios::app);  // Open in text mode for appending
        outFile << fName << " " << lName << " " << phone_num << endl;
        outFile << address << endl;  // Write the address on a new line
        outFile << email << endl;  // Write the email on a new line
        outFile.close();
    }

    static void displayAllContacts() {
        ifstream inFile("CMS.txt", ios::in);  // Open in text mode for reading
        if (!inFile) {
            cout << "No contacts found!" << endl;
            return;
        }
        string fName, lName, address, email;
        long long phone_num;
        while (inFile >> fName >> lName >> phone_num) {
            inFile.ignore();  // Ignore the remaining characters of the line
            getline(inFile, address);  // Read the entire address line
            getline(inFile, email);  // Read the entire email line
            cout << "Name: " << fName << " " << lName << endl;
            cout << "Phone: " << phone_num << endl;
            cout << "Address: " << address << endl;
            cout << "Email: " << email << endl;
            cout << "--------------------" << endl;
        }
        inFile.close();
    }

    static bool deleteContact(long long phone) {
        ifstream inFile("CMS.txt", ios::in);  // Open in text mode for reading
        ofstream outFile("Temp.txt", ios::out);  // Open in text mode for writing

        if (!inFile) {
            cout << "No contacts found!" << endl;
            return false;
        }

        string fName, lName, address, email;
        long long phone_num;
        bool found = false;
        while (inFile >> fName >> lName >> phone_num) {
            inFile.ignore();  // Ignore the remaining characters of the line
            getline(inFile, address);  // Read the entire address line
            getline(inFile, email);  // Read the entire email line
            if (phone_num == phone) {
                found = true;
            } else {
                outFile << fName << " " << lName << " " << phone_num << endl;
                outFile << address << endl;
                outFile << email << endl;
            }
        }

        inFile.close();
        outFile.close();

        remove("CMS.txt");
        rename("Temp.txt", "CMS.txt");

        return found;
    }

    static void sortContacts(bool byName) {
        ifstream inFile("CMS.txt", ios::in);  // Open in text mode for reading
        ofstream outFile("Temp.txt", ios::out);  // Open in text mode for writing
        Contact contacts[100];
        int count = 0;

        while (inFile >> contacts[count].fName >> contacts[count].lName >> contacts[count].phone_num) {
            inFile.ignore();
            getline(inFile, contacts[count].address);
            getline(inFile, contacts[count].email);
            count++;
        }
        inFile.close();

        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                bool swapCondition;

                if (byName) {
                    swapCondition = (contacts[i].fName > contacts[j].fName);
                } else {
                    swapCondition = (contacts[i].phone_num > contacts[j].phone_num);
                }

                if (swapCondition) {
                    Contact temp = contacts[i];
                    contacts[i] = contacts[j];
                    contacts[j] = temp;
                }
            }
        }

        for (int i = 0; i < count; i++) {
            outFile << contacts[i].fName << " " << contacts[i].lName << " " << contacts[i].phone_num << endl;
            outFile << contacts[i].address << endl;
            outFile << contacts[i].email << endl;
        }

        outFile.close();
        remove("CMS.txt");
        rename("Temp.txt", "CMS.txt");

        cout << "Contacts sorted successfully!" << endl;
    }
};










