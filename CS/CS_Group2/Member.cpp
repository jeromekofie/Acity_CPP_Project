#include <iostream>
#include <fstream>
#include <vector> //for precised arrays.
#include <iomanip>
#include <string>
#include <cstdlib>
#include <limits>
#include <algorithm> // to find the value in range
using namespace std;

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
