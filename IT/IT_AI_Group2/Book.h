#ifndef BOOK_H
#define BOOK_H

#include <string>
#include <iomanip>
#include <iostream>
using namespace std;


class Book {
private:
    int bookID;
    string title;
    string author;
    bool isIssued;

public:
    // Constructor to initialize book details
    Book(int id, string t, string a) : bookID(id), title(t), author(a), isIssued(false) {

    }

    // Getters for accessing private data members
    int getBookID() const {
        return bookID;
    }
    string getTitle()  {
        return title;
    }
    string getAuthor()  {
        return author;
    }
    bool getIssuedStatus()  {
        return isIssued;
    }

    // Setter to update the issue status
    void setIssuedStatus(bool status) {
        isIssued = status;
    }

    // Display book details in a formatted way
    void displayBook()  {
        cout << setw(10) << bookID << setw(20) << title << setw(20) << author
             << setw(10) << (isIssued ? "Issued" : "Available") << endl;
    }
};

#endif
