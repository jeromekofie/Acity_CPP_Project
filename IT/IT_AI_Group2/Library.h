#ifndef LIBRARY_H
#define LIBRARY_H

#include "Book.h"
#include "Member.h"
#include <vector>
#include <fstream>
using namespace std;


class Library {
private:
    vector<Book> books;      // List of books
    vector<Member> members;  // List of members
    const double fineRate = 0.50; // Fine rate per overdue day

public:
    void addBook();
    void displayBooks();
    void addMember();
    void displayMembers();
    void borrowBook();
    void returnBook();
    void calculateFine();
};

#endif
