#include <iostream>
#include <fstream>
#include <vector> //for precised arrays.
#include <iomanip>
#include <string>
#include <cstdlib>
#include <limits>
#include <algorithm> // to find the value in range
#include"Book.cpp"
#include"Member.cpp"
#include"StudentMember.cpp"
using namespace std;
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
