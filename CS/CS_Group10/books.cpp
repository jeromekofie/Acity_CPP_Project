/*To create a library fine calculator that tracks overdue books,and calculates fines, we will build a solution using C++.
The following is a detailed breakdown of the implementation, covering the compulsory concepts and the chosen additional concepts.

1.Compulsory Concepts
a. Decision Mkaing (if-else) will be used to check if a book is overdue and calculate fines accordingly. 

b. A loop will be used to read multiple books and calculate their fines iterating over a list of loans. 
In other words, loops are going to be used to process multiple books in the library, calculate fines for overdue books, 
and iterate through a list of loans to determine which books are overdue and how much fine they have accumulated. Ergo, the for and while loops will be used. 

 c. The program will create and read loan data from text files to track which books are overdue and ned fines calculated.
 The operations of Create, Read, Update, and Delete(CRUD) will allow the program to maintain records of books and their loan status.
 
 d. A function for fine calculations, displaying results with arguments passed to them for flexibility will be created.
 
 e. The program will have a base class Book with inherited classes for different types of books, such as Ebook and RegularBook.*/

#pragma once
#include "LibraryEntity.cpp"

enum BookCategory { Fiction, NonFiction, Science, History };

//derived class for books....adds more details about books in the library

/*This defines a new class Book that inherits from another class called LibraryEntity.
: public LibraryEntity means that Book is a subclass (derived class) of LibraryEntity. 
This implies that Book inherits the properties and behaviors (members and methods) of LibraryEntity,
and can also define its own specific properties or methods.*/

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
