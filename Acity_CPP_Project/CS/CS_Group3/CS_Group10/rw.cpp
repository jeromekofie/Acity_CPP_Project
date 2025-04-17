#include <iostream>
#include <fstream>
#include <iomanip>
#include <sstream>
#include "books.cpp"  // Assuming this includes the Book class
#include "member.cpp" // Assuming this includes the Member class
using namespace std;

// *File Handling - Including necessary header files for file operations*
//declaring functions we'll use later in the code
void createLoanFile();
void calculateFine(const string& loanFile);
bool isOverdue(int dueDate, int returnDate);
void displayMenu();

// *Constants - Using constant for fine rate, which does not change*
const double FINE_RATE = 1.0;

int main() {
    int choice;
    bool exit = false;
    
    // *Loops - Using a while loop to keep showing the menu until user chooses to exit*
    while (!exit) {
        displayMenu();  // Display the menu
        cin >> choice;  // Get user choice

        // *Decision Making - Using a switch statement to handle user choices*
        switch (choice) {
            case 1:
                createLoanFile();  // Call function to create loan record
                break;
            case 2:
                calculateFine("loans.txt");  // Call function to calculate fine from file
                break;
            case 3:
                cout << "Exiting the program.\n";  // Exit message
                exit = true;  // Set exit flag to true
                break;
            default:
                cout << "Invalid choice. Please try again.\n";  // Error message for invalid choice
                break;
        }
    }

    return 0;
}

// *Functions - Creating a function to add loan records to a file*
void createLoanFile() {
    // *File Handling - Using ofstream to open file for appending loan records*
    ofstream loanFile("loans.txt", ios::app);
    if (!loanFile) {
        cout << "Error: Could not open loan file.\n";  // Error if file cannot be opened
        return;
    }

    string memberID, bookID;
    int dueDate, returnDate;

    // *Variable Declaration and Initialization - Declaring and initializing variables*
    cout << "Enter Member ID: ";
    cin >> memberID;
    cout << "Enter Book ID: ";
    cin >> bookID;
    cout << "Enter Due Date (YYYYMMDD): ";
    cin >> dueDate;
    cout << "Enter Return Date (YYYYMMDD): ";
    cin >> returnDate;

    // *File Handling - Writing the entered loan record to the file*
    loanFile << memberID << "," << bookID << "," << dueDate << "," << returnDate << endl;
    loanFile.close();  // Closing the file

    cout << "Loan record added successfully.\n";
}

// *Functions - Creating a function to calculate fines based on loan records*
void calculateFine(const string& loanFile) {
    // *File Handling - Using ifstream to open and read from the loan file*
    ifstream inFile(loanFile);
    if (!inFile) {
        cout << "Error: Could not open loan file.\n";  // Error if file cannot be opened
        return;
    }

    string line;
    double totalFine = 0.0;

    // *Formatted Output - Using fixed, setprecision, and setw for formatting output*
    cout << fixed << setprecision(2);
    cout << setw(10) << "Member ID" << setw(10) << "Book ID"
         << setw(12) << "Due Date" << setw(15) << "Return Date"
         << setw(10) << "Fine\n";
    cout << string(55, '-') << endl;

    // *Loops - Using a while loop to process each loan record in the file*
    while (getline(inFile, line)) {
        string memberID, bookID, dueDateStr, returnDateStr;
        stringstream ss(line);  // *String Manipulation - Using stringstream to split CSV data*
        getline(ss, memberID, ',');
        getline(ss, bookID, ',');
        getline(ss, dueDateStr, ',');
        getline(ss, returnDateStr, ',');

        int dueDate = 0, returnDate = 0;
        try {
            // *Exception Handling - Using try-catch to handle invalid date format*
            dueDate = stoi(dueDateStr);  // Convert string to integer for due date
            returnDate = stoi(returnDateStr);  // Convert string to integer for return date
        } catch (const invalid_argument& e) {
            cout << "Invalid date format encountered. Skipping this record.\n";
            continue;  // *Continue - Skip the current record if the date is invalid*
        }

        double fine = 0.0;
        if (isOverdue(dueDate, returnDate)) {
            // *Decision Making - Checking if the book is overdue*
            fine = (returnDate - dueDate) * FINE_RATE;
        }
        totalFine += fine;

        // *Formatted Output - Outputting loan record and calculated fine in a structured format*
        cout << setw(10) << memberID << setw(10) << bookID
             << setw(12) << dueDate << setw(15) << returnDate
             << setw(10) << fine << endl;
    }

    // *Formatted Output - Display total fine collected*
    cout << "\nTotal Fine Collected: GHC" << totalFine << endl;
    inFile.close();  // Close the file
}

// *Boolean Functions - A function that checks if the book is overdue*
bool isOverdue(int dueDate, int returnDate) {
    // *Decision Making - Returns true if return date is later than due date*
    return returnDate > dueDate;
}

// *Functions - Display menu options for the user*
void displayMenu() {
    // **Escape Sequences - Using newline \n for formatting output**
    cout << "\nLibrary Fine Calculator\n";
    cout << "1. Add Loan Record\n";
    cout << "2. Calculate Fines\n";
    cout << "3. Exit\n";
    cout << "Enter your choice: ";
}