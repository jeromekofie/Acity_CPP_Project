#include <iostream>
#include <fstream>
#include <vector> //for precised arrays.
#include <iomanip>
#include <string>
#include <cstdlib>
#include <limits>
#include <algorithm> // to find the value in range
using namespace std;

// Book Class
class Book {
private:
    string title;
    string author;
    int id;
    bool isAvailable;
public:
    Book(string t, string a, int i) {
        title = t;
        author = a;
        id = i;
        isAvailable = true;
    }

    // Getters and Setters
    string getTitle() { return title; }
    string getAuthor() { return author; }
    int getId() { return id; }
    bool getAvailability() { return isAvailable; }
    void setAvailability(bool availability) { isAvailable = availability; }

    void displayBook() {
        cout << setw(5) << id << setw(20) << title << setw(20) << author
             << setw(20) << (isAvailable ? "Available" : "Borrowed") << endl;
    }
};

// Base Member Class
class Member {
protected:
    string name;
    int memberId;
    vector<int> borrowedBooks; // Store book IDs borrowed by this member
public:
    Member(string n, int id) : name(n), memberId(id) {}

    virtual void displayMember() {
        cout << setw(5) << memberId << setw(20) << name << endl;
    }

    void addBorrowedBook(int bookId) {
        borrowedBooks.push_back(bookId);
    }

    void returnBorrowedBook(int bookId) {
        auto it = find(borrowedBooks.begin(), borrowedBooks.end(), bookId);
        if (it != borrowedBooks.end()) {
            borrowedBooks.erase(it);
        }
    }

    void displayBorrowedBooks() {
        cout << "Books borrowed by " << name << " (ID: " << memberId << "):\n";
        if (borrowedBooks.empty()) {
            cout << "No books borrowed.\n";
        } else {
            for (int bookId : borrowedBooks) {
                cout << "Book ID: " << bookId << endl;
            }
        }
    }

    int getId() { return memberId; }
};

// Derived Classes
class StudentMember : public Member {
public:
    StudentMember(string n, int id) : Member(n, id) {}
};

class TeacherMember : public Member {
public:
    TeacherMember(string n, int id) : Member(n, id) {}
};

// Library Management System
class Library {
private:
    vector<Book> books;
    vector<Member*> members;
public:
    // Add a Book
    vector<Member*> getMembers() { return members; }
    void addBook(string title, string author, int id) {
        books.push_back(Book(title, author, id));
    }

    // Add a Member
    void addMember(Member* member) {
        members.push_back(member);
    }

    // Display Books
    void displayBooks() {
        cout << "Books in Library:\n";
        cout << setw(5) << "ID" << setw(20) << "Title" << setw(20) << "Author" << setw(20) << "Status" << endl;
        for (auto &book : books)
            book.displayBook();
    }

    // Display Members
    void displayMembers() {
        cout << "Library Members:\n";
        cout << setw(5) << "ID" << setw(20) << "Name" << endl;
        for (auto &member : members)
            member->displayMember();
    }

    // Borrow a Book
void borrowBook(int bookId, int memberId) {
    for (auto &book : books) {
        if (book.getId() == bookId) {
            if (book.getAvailability()) {
                for (auto &member : members) {
                    if (member->getId() == memberId) {
                        book.setAvailability(false);
                        member->addBorrowedBook(bookId);
                        cout << "Book borrowed successfully.\n";
                        return;
                    }
                }
                cout << "Member not found.\n";
                return;
            } else {
                cout << "Book already borrowed.\n";
                return;
            }
        }
    }
    cout << "Book not found.\n";
}

void returnBook(int bookId, int memberId) {
    for (auto &book : books) {
        if (book.getId() == bookId) {
            if (!book.getAvailability()) {
                for (auto &member : members) {
                    if (member->getId() == memberId) {
                        book.setAvailability(true);
                        member->returnBorrowedBook(bookId);
                        cout << "Book returned successfully.\n";
                        return;
                    }
                }
                cout << "Member not found.\n";
                return;
            } else {
                cout << "Book is not borrowed.\n";
                return;
            }
        }
    }
    cout << "Book not found.\n";
}


    // Delete a Book
    void deleteBook(int bookId) {
        for (auto it = books.begin(); it != books.end(); ++it) {
            if (it->getId() == bookId) {
                books.erase(it);
                cout << "Book deleted successfully.\n";
                return;
            }
        }
        cout << "Book not found.\n";
    }

void saveToFile() {
    // Open the file in append mode to keep adding data without overwriting
    ofstream file("Library_data.txt", ios::app);  // Open file in append mode
    if (!file) {
        cerr << "Error: Unable to open file for writing.\n";
        return;
    }

    // Saving all output to the file
    streambuf* originalBuffer = cout.rdbuf();  // Save the original buffer
    cout.rdbuf(file.rdbuf());  // Redirect cout to the file

    // Display all Books in Library
    cout << "\nBooks in Library:\n";
    cout << setw(5) << "ID" << setw(20) << "Title" << setw(20) << "Author" << setw(20) << "Status" << endl;
    for (auto &book : books)
        book.displayBook();

    // Display all Library Members
    cout << "\nLibrary Members:\n";
    cout << setw(5) << "ID" << setw(20) << "Name" << endl;
    for (auto &member : members)
        member->displayMember();

    // Restore the original cout buffer
    cout.rdbuf(originalBuffer);

    file.close();  // Close the file after writing
    cout << "Data saved successfully to Library_data.txt\n";
}


    // Load from File
    void loadFromFile() {
        ifstream file("Library_data.txt");
        if (!file) {
            cout << "No data file found. Starting fresh.\n";
            return;
        }
        string title, author;
        int id;
        bool availability;
        while (file >> id) {
            file.ignore();
            getline(file, title, ',');
            getline(file, author, ',');
            file >> availability;
            books.push_back(Book(title, author, id));
            books.back().setAvailability(availability);
        }
        file.close();
    }
};

int main() {
    Library library;
    library.loadFromFile();

    int choice, id, memberId;  // Declare memberId here
    string title, author, name;

    while (true) {
        cout << "\nLibrary Management System\n";
        cout << "1. Add Book\n2. Add Member\n3. Display Books\n4. Display Members\n5. Borrow Book\n6. Return Book\n7. Delete Book\n8. Borrowed Book By Member\n9. Returned Book By Member\n10. Exit\n";
        cout << "Enter choice: ";

        while (!(cin >> choice)) {
            cin.clear();
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
            cout << "Invalid input. Please enter a number: ";
        }

        switch (choice) {
            case 1:
                // Add Book
                cout << "Enter Book Title: ";
                cin.ignore();
                getline(cin, title);
                cout << "Enter Author: ";
                getline(cin, author);
                cout << "Enter Book ID: ";
                while (!(cin >> id)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Book ID: ";
                }
                library.addBook(title, author, id);
                break;

            case 2:
                // Add Member
                cout << "Enter Member Name: ";
                cin.ignore();
                getline(cin, name);
                cout << "Enter Member ID: ";
                while (!(cin >> memberId)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Member ID: ";
                }
                cout << "Enter Type (1-Student, 2-Teacher): ";
                int type;
                while (!(cin >> type) || (type != 1 && type != 2)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter 1 for Student or 2 for Teacher: ";
                }
                if (type == 1)
                    library.addMember(new StudentMember(name, memberId));
                else
                    library.addMember(new TeacherMember(name, memberId));
                break;

            case 3:
                // Display Books
                library.displayBooks();
                break;

            case 4:
                // Display Members
                library.displayMembers();
                break;

            case 5:
                // Borrow Book
                cout << "Enter Book ID to Borrow: ";
                while (!(cin >> id)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Book ID: ";
                }
                cout << "Enter Member ID: ";
                while (!(cin >> memberId)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Member ID: ";
                }
                library.borrowBook(id, memberId);
                break;

            case 6:
                // Return Book
                cout << "Enter Book ID to Return: ";
                while (!(cin >> id)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Book ID: ";
                }
                cout << "Enter Member ID: ";
                while (!(cin >> memberId)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Member ID: ";
                }
                library.returnBook(id, memberId);
                break;

            case 7:
                // Delete Book
                cout << "Enter Book ID to Delete: ";
                while (!(cin >> id)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Book ID: ";
                }
                library.deleteBook(id);
                break;

            case 8:
                // Display Borrowed Books by Member
                cout << "Enter Member ID to view borrowed books: ";
                while (!(cin >> memberId)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Member ID: ";
                }

                // Use the getter method to access members
                for (auto &member : library.getMembers()) {
                    if (member->getId() == memberId) {
                        member->displayBorrowedBooks();
                        break;
                    }
                }
                break;

            case 9:
                // Return Book by Member
                cout << "Enter Member ID to return books: ";
                while (!(cin >> memberId)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Member ID: ";
                }

                // Use the getter method to access members
                for (auto &member : library.getMembers()) {//auto: This keyword tells the compiler to automatically deduce the type of member
                    if (member->getId() == memberId) {
                        int bookId;
                        cout << "Enter Book ID to return: ";
                        while (!(cin >> bookId)) {
                            cin.clear();
                            cin.ignore(numeric_limits<streamsize>::max(), '\n');
                            cout << "Invalid input. Please enter a valid Book ID: ";
                        }
                        library.returnBook(bookId, memberId);
                        break;
                    }
                }
                break;

            case 10:
                library.saveToFile();
                cout << "Exiting...\n";
                return 0;
                break;


            default:
                cout << "Invalid choice. Try again.\n";
        }
    }
}

//end of main
