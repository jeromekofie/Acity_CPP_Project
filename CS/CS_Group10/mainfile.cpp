#include <iostream>
#include <fstream>
#include <iomanip>
#include <sstream>
#include "books.cpp"  
#include "member.cpp"
using namespace std;

//declaring functions we'll use later in the code
void createLoanFile();
void calculateFine(const string& loanFile);
bool isOverdue(int dueDate, int returnDate);
void displayMenu();

//the fine amount for each extra day a book is late
const double FINE_RATE = 0.50;

int main() {
    int choice;
    bool exit = false;

    while (!exit) {
        displayMenu();
        cin >> choice;

        switch (choice) {
            case 1:
                createLoanFile();
                break;
            case 2:
                calculateFine("loans.txt");
                break;
            case 3:
                cout << "Exiting the program.\n";
                exit = true;
                break;
            default:
                cout << "Invalid choice. Please try again.\n";
                break;
        }
    }

    return 0;
}

void createLoanFile() {
    ofstream loanFile("loans.txt", ios::app);
    if (!loanFile) {
        cout << "Error: Could not open loan file.\n";
        return;
    }

    string memberID, bookID;
    int dueDate, returnDate;

    cout << "Enter Member ID: ";
    cin >> memberID;
    cout << "Enter Book ID: ";
    cin >> bookID;
    cout << "Enter Due Date (YYYYMMDD): ";
    cin >> dueDate;
    cout << "Enter Return Date (YYYYMMDD): ";
    cin >> returnDate;

    loanFile << memberID << "," << bookID << "," << dueDate << "," << returnDate << endl;
    loanFile.close();

    cout << "Loan record added successfully.\n";
}

void calculateFine(const string& loanFile) {
    ifstream inFile(loanFile);
    if (!inFile) {
        cout << "Error: Could not open loan file.\n";
        return;
    }

    string line;
    double totalFine = 0.0;

    cout << fixed << setprecision(2);
    cout << setw(10) << "Member ID" << setw(10) << "Book ID"
         << setw(12) << "Due Date" << setw(15) << "Return Date"
         << setw(10) << "Fine\n";
    cout << string(55, '-') << endl;

    while (getline(inFile, line)) {
        string memberID, bookID, dueDateStr, returnDateStr;
        stringstream ss(line);
        getline(ss, memberID, ',');
        getline(ss, bookID, ',');
        getline(ss, dueDateStr, ',');
        getline(ss, returnDateStr, ',');

        int dueDate = 0, returnDate = 0;
        try {
            dueDate = stoi(dueDateStr);
            returnDate = stoi(returnDateStr);
        } catch (const invalid_argument& e) {
            cout << "Invalid date format encountered. Skipping this record.\n";
            continue; // Skip the current record if the date is invalid
        }

        double fine = 0.0;
        if (isOverdue(dueDate, returnDate)) {
            fine = (returnDate - dueDate) * FINE_RATE;
        }
        totalFine += fine;

        cout << setw(10) << memberID << setw(10) << bookID
             << setw(12) << dueDate << setw(15) << returnDate
             << setw(10) << fine << endl;
    }

    cout << "\nTotal Fine Collected: $" << totalFine << endl;
    inFile.close();
}

bool isOverdue(int dueDate, int returnDate) {
    //check if return date is later than due date
    return returnDate > dueDate;
}

//function to display menu to the user
void displayMenu() {
    cout << "\nLibrary Fine Calculator\n";
    cout << "1. Add Loan Record\n";
    cout << "2. Calculate Fines\n";
    cout << "3. Exit\n";
    cout << "Enter your choice: ";
}
