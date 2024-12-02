#include "Library.h"
#include <iostream>
#include <iomanip>
#include <ctime>
#include <algorithm>
using namespace std;

// Function to add a new book to the library
void Library::addBook() {
    int id;
    string title, author;

    cout << "Enter Book ID: ";
    cin >> id;
    cin.ignore(); // To handle newline character in the input buffer

    cout << "Enter Book Title: ";
    getline(cin, title);

    cout << "Enter Author Name: ";
    getline(cin, author);

    // Create a new book and add it to the list
    books.emplace_back(id, title, author);
    cout << "Book added successfully!\n";
}

// Function to display all books
void Library::displayBooks() {
    cout << setw(10) << "Book ID" << setw(20) << "Title" << setw(20) << "Author" << setw(10) << "Status\n";
    cout << string(60, '-') << endl;
    for (const auto& book : books) {
        book.displayBook();
    }
}

// Function to add a new member to the library
void Library::addMember() {
    int id;
    string name;

    cout << "Enter Member ID: ";
    cin >> id;
    cin.ignore(); // Handle newline character in input buffer

    cout << "Enter Member Name: ";
    getline(cin, name);

    // Create a new member and add it to the list
    members.emplace_back(id, name);
    cout << "Member added successfully!\n";
}

// Function to display all members
void Library::displayMembers() {
    cout << setw(10) << "Member ID" << setw(20) << "Name" << setw(20) << "Books Borrowed\n";
    cout << string(60, '-') << endl;
    for (const auto& member : members) {
        member.displayMember();
    }
}

// Function to issue a book to a member
void Library::borrowBook() {
    int bookID, memberID;
    cout << "Enter Member ID: ";
    cin >> memberID;

    // Find the member
    auto member = find_if(members.begin(), members.end(), [memberID](const Member& m) { return m.getMemberID() == memberID; });
    if (member == members.end()) {
        cout << "Member not found!\n";
        return;
    }

    cout << "Enter Book ID: ";
    cin >> bookID;

    // Find the book
    auto book = find_if(books.begin(), books.end(), [bookID](const Book& b) { return b.getBookID() == bookID; });
    if (book == books.end()) {
        cout << "Book not found!\n";
        return;
    }

    // Check if the book is already issued
    if (book->getIssuedStatus()) {
        cout << "Book is already borrowed!\n";
        return;
    }

    // Issue the book
    book->setIssuedStatus(true);
    member->borrowBook(bookID);
    cout << "Book borrowed successfully!\n";
}

// Function to return a book and calculate fine
void Library::returnBook() {
    int bookID, memberID;
    cout << "Enter Member ID: ";
    cin >> memberID;

    // Find the member
    auto member = find_if(members.begin(), members.end(), [memberID](const Member& m) { return m.getMemberID() == memberID; });
    if (member == members.end()) {
        cout << "Member not found!\n";
        return;
    }

    cout << "Enter Book ID: ";
    cin >> bookID;

    // Find the book
    auto book = find_if(books.begin(), books.end(), [bookID](const Book& b) { return b.getBookID() == bookID; });
    if (book == books.end()) {
        cout << "Book not found!\n";
        return;
    }

    // Ensure the book is issued
    if (!book->getIssuedStatus()) {
        cout << "This book was not borrowed!\n";
        return;
    }

    // Return the book
    book->setIssuedStatus(false);
    member->returnBook(bookID);

    // Calculate the fine based on overdue days
    int overdueDays;
    cout << "Enter number of overdue days: ";
    cin >> overdueDays;

    double fine = fineRate * overdueDays;
    cout << "Book returned successfully! Fine: $" << fixed << setprecision(2) << fine << endl;
}
