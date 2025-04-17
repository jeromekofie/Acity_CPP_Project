#include <iostream>
#include <fstream>
#include <iomanip>
#include <string>
#include <vector>
#include <cmath>  // For pow()
#include <ctime>  // For date calculations
#include <bits/stdc++.h>

using namespace std;


// Global Constants
const int DAILY_FINE = 5; // Fine per day for overdue books


// Base class for common attributes
class LibraryEntity {
protected:
    int id;
    string name;
public:
    LibraryEntity(int id, string name) : id(id), name(name) {
    }

    virtual void displayInfo() const = 0; // Polymorphism: Abstract function
};



// class for Members
class Member : public LibraryEntity {
public:
    Member(int id, string name) : LibraryEntity(id, name) {
    }

    // Getter methods
    int getID() const {
        return id;
    }

    string getName() const {
        return name;
    }

    void displayInfo() const override {
        cout << "Member ID: " << id << ", Name: " << name << endl;
    }
};



//  class for Books
class Book : public LibraryEntity {
    string issueDate;  // Format: YYYY-MM-DD
    string dueDate;    // Format: YYYY-MM-DD

public:
    Book(int id, string name, string issueDate, string dueDate) : LibraryEntity(id, name), issueDate(issueDate), dueDate(dueDate) {

    }

    void displayInfo() const override {
        cout << "Book ID: " << id << ", Title: " << name << ", Issue Date: " << issueDate << ", Due Date: " << dueDate << endl;
    }

    string getDueDate() const {
        return dueDate;
    }

    string getIssueDate() const {
        return issueDate;
    }

    int getID() const {
        return id;
    }

    string getName() const {
         return name;
    }
};



// Fine Calculator Function
int calculateFine(const string& returnDate, const string& dueDate) {
    // Simplified date difference calculation (Assumes valid format: YYYY-MM-DD)
    int returnYear = stoi(returnDate.substr(0, 4));
    int returnMonth = stoi(returnDate.substr(5, 2));
    int returnDay = stoi(returnDate.substr(8, 2));

    int dueYear = stoi(dueDate.substr(0, 4));
    int dueMonth = stoi(dueDate.substr(5, 2));
    int dueDay = stoi(dueDate.substr(8, 2));

    // Calculate days overdue
    int daysOverdue = (returnYear - dueYear) * 365 + (returnMonth - dueMonth) * 30 + (returnDay - dueDay);
    return (daysOverdue > 0) ? daysOverdue * DAILY_FINE : 0;
}


// CRUD Operations: Add, View, Delete
void addLoan(const Member& member, const Book& book, const string& returnDate) {
    ofstream file("loans.txt", ios::app); // Append mode
    if (file.is_open()) {
        int fine = calculateFine(returnDate, book.getDueDate());
        file << setw(10) << left << "Member ID"
             << setw(25) << left << "Member Name"
             << setw(10) << left << "Book ID"
             << setw(30) << left << "Book Title"
             << setw(15) << left << "Issue Date"
             << setw(15) << left << "Due Date"
             << setw(15) << left << "Return Date"
             << setw(10) << right << "Fine(in cedis)"
             << endl;

        file << "---------------------------------------------------------------------------------------------------------------------------------\n";
        file << setw(10) << left << member.getID()
             << setw(25) << left << member.getName()
             << setw(10) << left << book.getID()
             << setw(30) << left << book.getName()
             << setw(15) << left << book.getIssueDate()
             << setw(15) << left << book.getDueDate()
             << setw(15) << left << returnDate
             << setw(10) << right << fine
             << endl;
        file << "---------------------------------------------------------------------------------------------------------------------------------\n";
        file.close();
        cout << "Loan added successfully!" << endl;
    } else {
        cerr << "Error opening file!" << endl;
    }
}

void viewLoans() {
    ifstream file("loans.txt");
    if (file.is_open()) {
        string line;
        cout << "---------------------------------------------------------------------------------------------------------------------------------\n";
        cout << setw(10) << left << "Member ID"
             << setw(25) << left << "Member Name"
             << setw(10) << left << "Book ID"
             << setw(30) << left << "Book Title"
             << setw(15) << left << "Issue Date"
             << setw(15) << left << "Due Date"
             << setw(15) << left << "Return Date"
             << setw(10) << right << "Fine(in cedis)" << endl;
        cout << "---------------------------------------------------------------------------------------------------------------------------------\n";
        while (getline(file, line)) {
            cout << line << endl;
        }
        file.close();
    } else {
        cerr << "Error opening file!" << endl;
    }
}


void deleteLoan(int bookID) {
    ifstream file("loans.txt");
    ofstream tempFile("temp.txt");

    if (file.is_open() && tempFile.is_open()) {
        string line;
        bool found = false;

        // Skip the header and divider lines
        getline(file, line); // Header line
        tempFile << line << endl;
        getline(file, line); // Divider line
        tempFile << line << endl;

        while (getline(file, line)) {
            // Extract the Book ID from the current line
            stringstream ss(line);
            int currentBookID;
            ss >> currentBookID;

            // If the Book ID matches, skip this line
            if (currentBookID == bookID) {
                found = true;
                continue;
            }

            // Otherwise, write the line to the temp file
            tempFile << line << endl;
        }

        file.close();
        tempFile.close();

        // Replace the original file with the updated file
        remove("loans.txt");
        rename("temp.txt", "loans.txt");

        if (found) {
            cout << "Loan deleted successfully!" << endl;
        } else {
            cout << "Loan with Book ID " << bookID << " not found." << endl;
        }
    } else {
        cerr << "Error opening file!" << endl;
    }
}



// Menu-driven Program
void displayMenu() {
    cout << "\n*****************************************************\n";
    cout << "          Library Fine Calculator Menu\n";
    cout << "*****************************************************\n";
    cout << "                                                         \n";
    cout << "1. Add Book Loan\n";
    cout << "2. View All Loans\n";
    cout << "3. Delete a Loan\n";
    cout << "4. Calculate Fine (5 cedis charge)\n";
    cout << "5. Exit\n";
    cout << "                                                         \n";
    cout << "Enter your choice: ";
}

int main() {
    vector<Book> books;
    vector<Member> members;

    int choice;
    do {
        displayMenu();
        cin >> choice;
        cin.ignore(); // Ignore trailing newline

        switch (choice) {
        case 1: {
            int memberID, bookID;
            string returnDate, memberName, bookName, issueDate, dueDate;

            cout << "Enter Member ID: ";
            cin >> memberID;
            cin.ignore(); // Ignore newline
            cout << "Enter Member Name: ";
            getline(cin, memberName);

            cout << "Enter Book ID: ";
            cin >> bookID;
            cin.ignore();
            cout << "Enter Book Name: ";
            getline(cin, bookName);
            cout << "Enter Issue Date (YYYY-MM-DD): ";
            getline(cin, issueDate);
            cout << "Enter Due Date (YYYY-MM-DD): ";
            getline(cin, dueDate);

            Member newMember(memberID, memberName);
            Book newBook(bookID, bookName, issueDate, dueDate);

            cout << "Enter Return Date (YYYY-MM-DD): ";
            getline(cin, returnDate);

            addLoan(newMember, newBook, returnDate);
            break;
        }
        case 2:
            viewLoans();
            break;
        case 3: {
            int id;
            cout << "Enter Member ID to delete: ";
            cin >> id;
            deleteLoan(id);
            break;
        }
        case 4: {
            string returnDate, dueDate;
            cout << "Enter Return Date (YYYY-MM-DD): ";
            cin >> returnDate;
            cout << "Enter Due Date (YYYY-MM-DD): ";
            cin >> dueDate;
            float fine = calculateFine(returnDate, dueDate);
            cout << "Fine calculated: " << fine << " cedis.\n";
            break;
        }
        case 5:
            cout << "Exiting program. Goodbye!" << endl;
            break;
        default:
            cout << "Invalid choice. Try again." << endl;
        }
    } while (choice != 5);

    return 0;
}
