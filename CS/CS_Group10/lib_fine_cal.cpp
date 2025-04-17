#include <iostream>
#include <fstream>
#include <iomanip>
#include <cmath>
#include <string>
#include <vector>
#include <cstdlib> 
using namespace std;

//these are the types of books in the library
enum BookCategory { Fiction, NonFiction, Science, History };

//this is the main template for library objects like books or members
class LibraryEntity {
protected:
    string id;
    string name;

public:
    void setID(const string& entityID) { id = entityID; }
    string getID() const { return id; }
    void setName(const string& entityName) { name = entityName; }
    string getName() const { return name; }
    virtual void display() const {
        cout << "ID: " << id << ", Name: " << name << endl;
    }
};

//derived class for books....adds more details about books in the library
class Book : public LibraryEntity {
public:
    BookCategory category;
    void setCategory(BookCategory cat) { category = cat; }
    string getCategoryName() const {
        switch (category) {
            case Fiction: return "Fiction";
            case NonFiction: return "Non-Fiction";
            case Science: return "Science";
            case History: return "History";
        }
        return "Unknown";
    }
    void display() const override {
        LibraryEntity::display();
        cout << "Category: " << getCategoryName() << endl;
    }
};

//derived class for library members...adds more details about members 
class Member : public LibraryEntity {
public:
    void display() const override {
        cout << "Member - ";
        LibraryEntity::display();
    }
};

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

//this function adds a new loan to the file
void createLoanFile() {
    ofstream loanFile("loans.txt", ios::app);
    if (!loanFile) {
        cout << "Error: Could not open loan file.\n";
        return;
    }

 //ask the user for details about the loan
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

 //save the loan details to the file
    loanFile << memberID << "," << bookID << "," << dueDate << "," << returnDate << endl;
    loanFile.close();

    cout << "Loan record added successfully.\n";
}

//this function checks for late returns and calculates the fine
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

//go through each loan record in the file
    while (getline(inFile, line)) {
        string memberID, bookID, dueDateStr, returnDateStr;
        stringstream ss(line); // To split the line by commas
        getline(ss, memberID, ',');
        getline(ss, bookID, ',');
        getline(ss, dueDateStr, ',');
        getline(ss, returnDateStr, ',');

//convert the dates to numbers for calculation
        int dueDate = stoi(dueDateStr);
        int returnDate = stoi(returnDateStr);

//check if the book is late and calculate the fine
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


//boolean function to check overdue status
bool isOverdue(int dueDate, int returnDate) {
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