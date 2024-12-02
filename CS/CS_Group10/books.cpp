#pragma once
#include "LibraryEntity.cpp"

enum BookCategory { Fiction, NonFiction, Science, History };

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
