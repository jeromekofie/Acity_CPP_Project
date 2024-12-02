#ifndef MEMBER_H
#define MEMBER_H

#include <string>
#include <vector>
using namespace std;

// Represents a library member
class Member {
private:
    int memberID;
    string name;
    vector<int> borrowedBooks;

public:
    // Constructor to initialize member details
    Member(int id, string n) : memberID(id), name(n) {

    }

    // Getters for member details
    int getMemberID() const {
        return memberID;
    }
    string getName() {
        return name;
    }
    vector<int> getBorrowedBooks() {
        return borrowedBooks;
    }

    // Add a book ID to the borrowed list
    void borrowBook(int bookID) {
         borrowedBooks.push_back(bookID);
    }

    // Remove a book ID from the borrowed list
    void returnBook(int bookID) {
        borrowedBooks.erase(remove(borrowedBooks.begin(), borrowedBooks.end(), bookID), borrowedBooks.end());
    }

    // Display member details
    void displayMember() const {
        cout << "Member ID: " << memberID << ", Name: " << name << endl;
        cout << "Borrowed Books: ";
        for (int bookID : borrowedBooks) {
            cout << bookID << " ";
        }
        cout << endl;
    }
};

#endif
