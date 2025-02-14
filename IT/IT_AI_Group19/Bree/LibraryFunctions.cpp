#pragma once
#include "Book.cpp"
#include <vector>
#include <cstdlib>
#include <ctime>
using namespace std;

class Library {
private:
    vector<Book> books;

    int generateId() const {
        return rand() % 10000 + 1; // Random ID between 1 and 10000
    }

public:
    Library() { srand(time(0)); }

    void addBook() {
        string title, author;
        int genreChoice;
        cout << "Enter title: ";
        cin.ignore();
        getline(cin, title);
        cout << "Enter author: ";
        getline(cin, author);
        cout << "Choose genre (0: Fiction, 1: Non-Fiction, 2: Mystery, 3: Science Fiction, 4: Fantasy, 5: Biography): ";
        cin >> genreChoice;

        Genre genre = static_cast<Genre>(genreChoice);
        int id = generateId();
        books.emplace_back(title, author, genre, id);

        cout << "Book added with ID " << id << endl;
    }

    void searchBooks();
    void saveToFile();
    void loadFromFile();
    void menu();
};
