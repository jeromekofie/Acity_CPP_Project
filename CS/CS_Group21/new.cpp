#include <iostream>
#include <fstream>
#include <iomanip>
#include <cstring>
#include <cmath> // for pow
#include <cstdlib> // for rand

using namespace std;

// Constants
const int MAX_NAME_LENGTH = 50;
const int MAX_STUDENTS = 20;
const int MAX_BOOKS = 15;

// Base Class
class Person {
protected:
    char name[MAX_NAME_LENGTH];
    int roll;

public:
    Person() : roll(0) {
        strcpy(name, "Unknown");
    }

    void setName(const char* newName) {
        strncpy(name, newName, MAX_NAME_LENGTH);
    }

    const char* getName() const {
        return name;
    }

    void setRoll(int newRoll) {
        roll = newRoll;
    }

    int getRoll() const {
        return roll;
    }
};

// Derived Class
class Student : public Person {
private:
    double balance;

public:
    Student() : balance(0.0) {}

    void deposit(double amount) {
        balance += amount;
    }

    bool deduct(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    double getBalance() const {
        return balance;
    }

    void display() const {
        cout << setw(10) << roll << setw(20) << name << setw(15) << fixed << setprecision(2) << balance << endl;
    }
};

// Book Class
class Book {
private:
    char title[MAX_NAME_LENGTH];
    char author[MAX_NAME_LENGTH];
    int isbn;
    bool available;

public:
    Book() : isbn(0), available(true) {
        strcpy(title, "Unknown");
        strcpy(author, "Unknown");
    }

    void setDetails(const char* newTitle, const char* newAuthor, int newIsbn) {
        strncpy(title, newTitle, MAX_NAME_LENGTH);
        strncpy(author, newAuthor, MAX_NAME_LENGTH);
        isbn = newIsbn;
        available = true;
    }

    void display() const {
        cout << setw(20) << title << setw(20) << author << setw(10) << isbn << setw(10) << (available ? "Yes" : "No") << endl;
    }

    int getISBN() const {
        return isbn;
    }

    bool isAvailable() const {
        return available;
    }

    void issue() {
        available = false;
    }

    void returnBook() {
        available = true;
    }

    const char* getTitle() const {
        return title;
    }

    const char* getAuthor() const {
        return author;
    }
};

// Global Arrays for Students and Books
Student students[MAX_STUDENTS];
Book books[MAX_BOOKS];
int studentCount = 0, bookCount = 0;

// Function Prototypes
void adminMenu();
void studentMenu();
void addStudent();
void viewStudents();
void addBook();
void viewBooks();
void issueBook();
void saveToFile();
void loadFromFile();
void showMainMenu();

// Main Program
int main() {
    loadFromFile();

    showMainMenu();

    return 0;
}

// Function to display main menu
void showMainMenu() {
    int choice;
    do {
        cout << "\nLibrary System Menu\n";
        cout << "1. Admin Login\n";
        cout << "2. Student Login\n";
        cout << "0. Exit\n";
        cout << "Enter your choice: ";
        cin >> choice;

        switch (choice) {
            case 1:
                adminMenu();
                break;
            case 2:
                studentMenu();
                break;
            case 0:
                saveToFile();
                cout << "Exiting program. Data saved.\n";
                break;
            default:
                cout << "Invalid choice. Try again.\n";
        }
    } while (choice != 0);
}

// Admin Menu
void adminMenu() {
    int choice;
    do {
        cout << "\nAdmin Menu\n";
        cout << "1. Add Student\n";
        cout << "2. View Students\n";
        cout << "3. Add Book\n";
        cout << "4. View Books\n";
        cout << "0. Logout\n";
        cout << "Enter your choice: ";
        cin >> choice;

        switch (choice) {
            case 1:
                addStudent();
                break;
            case 2:
                viewStudents();
                break;
            case 3:
                addBook();
                break;
            case 4:
                viewBooks();
                break;
            case 0:
                cout << "Logging out...\n";
                break;
            default:
                cout << "Invalid choice. Try again.\n";
        }
    } while (choice != 0);
}

// Student Menu
void studentMenu() {
    int roll;
    cout << "Enter your roll number: ";
    cin >> roll;

    // Find student
    int index = -1;
    for (int i = 0; i < studentCount; i++) {
        if (students[i].getRoll() == roll) {
            index = i;
            break;
        }
    }

    if (index == -1) {
        cout << "Student not found. Returning to main menu.\n";
        return;
    }

    int choice;
    do {
        cout << "\nStudent Menu\n";
        cout << "1. View Balance\n";
        cout << "2. Deposit Amount\n";
        cout << "3. Issue Book\n";
        cout << "0. Logout\n";
        cout << "Enter your choice: ";
        cin >> choice;

        switch (choice) {
            case 1:
                cout << "Your balance: $" << fixed << setprecision(2) << students[index].getBalance() << endl;
                break;
            case 2: {
                double amount;
                cout << "Enter amount to deposit: ";
                cin >> amount;
                students[index].deposit(amount);
                cout << "Deposit successful. New balance: $" << students[index].getBalance() << endl;
                break;
            }
            case 3:
                issueBook();
                break;
            case 0:
                cout << "Logging out...\n";
                break;
            default:
                cout << "Invalid choice. Try again.\n";
        }
    } while (choice != 0);
}

// Add a new student
void addStudent() {
    if (studentCount >= MAX_STUDENTS) {
        cout << "Student limit reached.\n";
        return;
    }

    int roll;
    char name[MAX_NAME_LENGTH];
    cout << "Enter roll number: ";
    cin >> roll;
    cout << "Enter name: ";
    cin.ignore();
    cin.getline(name, MAX_NAME_LENGTH);

    students[studentCount].setRoll(roll);
    students[studentCount].setName(name);
    students[studentCount].deposit(50); // Initial deposit
    studentCount++;
    cout << "Student added successfully.\n";
}

// View all students
void viewStudents() {
    cout << setw(10) << "Roll" << setw(20) << "Name" << setw(15) << "Balance\n";
    for (int i = 0; i < studentCount; i++) {
        students[i].display();
    }
}

// Add a new book
void addBook() {
    if (bookCount >= MAX_BOOKS) {
        cout << "Book limit reached.\n";
        return;
    }

    char title[MAX_NAME_LENGTH], author[MAX_NAME_LENGTH];
    int isbn;
    cout << "Enter book title: ";
    cin.ignore();
    cin.getline(title, MAX_NAME_LENGTH);
    cout << "Enter book author: ";
    cin.getline(author, MAX_NAME_LENGTH);
    cout << "Enter book ISBN: ";
    cin >> isbn;

    books[bookCount].setDetails(title, author, isbn);
    bookCount++;
    cout << "Book added successfully.\n";
}

// View all books
void viewBooks() {
    cout << setw(20) << "Title" << setw(20) << "Author" << setw(10) << "ISBN" << setw(10) << "Available\n";
    for (int i = 0; i < bookCount; i++) {
        books[i].display();
    }
}

// Issue a book
void issueBook() {
    int isbn;
    cout << "Enter book ISBN to issue: ";
    cin >> isbn;

    for (int i = 0; i < bookCount; i++) {
        if (books[i].getISBN() == isbn) {
            if (books[i].isAvailable()) {
                books[i].issue();
                cout << "Book issued successfully.\n";
            } else {
                cout << "Book is currently unavailable.\n";
            }
            return;
        }
    }
    cout << "Book not found.\n";
}

// Save data to file
void saveToFile() {
    ofstream out("library_data.txt");
    out << studentCount << " " << bookCount << "\n";

    for (int i = 0; i < studentCount; i++) {
        out << students[i].getRoll() << " " << students[i].getName() << " " << students[i].getBalance() << "\n";
    }

    for (int i = 0; i < bookCount; i++) {
        out << books[i].getISBN() << " " << books[i].getTitle() << " " << books[i].getAuthor() << " " << books[i].isAvailable() << "\n";
    }

    out.close();
}

// Load data from file
void loadFromFile() {
    ifstream in("library_data.txt");

    if (in) {
        in >> studentCount >> bookCount;
        for (int i = 0; i < studentCount; i++) {
            int roll;
            char name[MAX_NAME_LENGTH];
            double balance;
            in >> roll;
            in.ignore();
            in.getline(name, MAX_NAME_LENGTH);
            in >> balance;
            students[i].setRoll(roll);
            students[i].setName(name);
            students[i].deposit(balance);
        }

        for (int i = 0; i < bookCount; i++) {
            int isbn;
            char title[MAX_NAME_LENGTH], author[MAX_NAME_LENGTH];
            bool available;
            in >> isbn;
            in.ignore();
            in.getline(title, MAX_NAME_LENGTH);
            in.getline(author, MAX_NAME_LENGTH);
            in >> available;
            books[i].setDetails(title, author, isbn);
            if (!available) {
                books[i].issue();
            }
        }
    }

    in.close();
}
