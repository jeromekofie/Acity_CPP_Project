#pragma once
#include "LibraryFunctions.cpp"
#include <fstream>
#include <iostream>
using namespace std;

void Library::searchBooks() {
    int choice;
    cout << "\nSearch by:\n1. Title\n2. Author\n3. Genre\nEnter choice: ";
    cin >> choice;

    switch (choice) {
    case 1: {
        string title;
        cout << "Enter title: ";
        cin.ignore();
        getline(cin, title);
        for (const auto &book : books) {
            if (book.getTitle() == title) {
                book.display();
            }
        }
        break;
    }
    case 2: {
        string author;
        cout << "Enter author: ";
        cin.ignore();
        getline(cin, author);
        for (const auto &book : books) {
            if (book.getAuthor() == author) {
                book.display();
            }
        }
        break;
    }
    case 3: {
        int genreChoice;
        cout << "Enter genre (0: Fiction, 1: Non-Fiction, 2: Mystery, 3: Science Fiction, 4: Fantasy, 5: Biography): ";
        cin >> genreChoice;
        Genre genre = static_cast<Genre>(genreChoice);
        for (const auto &book : books) {
            if (book.getGenre() == genre) {
                book.display();
            }
        }
        break;
    }
    default:
        cout << "Invalid choice!\n";
    }
}

void Library::saveToFile() {
    ofstream file("library.txt");
    if (!file) {
        cerr << "Error opening file for writing!\n";
        return;
    }
    for (const auto &book : books) {
        file << book.getId() << "," << book.getTitle() << ","
             << book.getAuthor() << "," << book.getGenre() << "\n";
    }
    file.close();
    cout << "Books saved to file successfully.\n";
}

void Library::loadFromFile() {
    ifstream file("library.txt");
    if (!file) {
        cerr << "Error opening file for reading!\n";
        return;
    }
    books.clear();
    string line, title, author;
    int id, genre;
    while (getline(file, line)) {
        size_t pos1 = line.find(',');
        size_t pos2 = line.find(',', pos1 + 1);
        size_t pos3 = line.find(',', pos2 + 1);

        id = stoi(line.substr(0, pos1));
        title = line.substr(pos1 + 1, pos2 - pos1 - 1);
        author = line.substr(pos2 + 1, pos3 - pos2 - 1);
        genre = stoi(line.substr(pos3 + 1));

        books.emplace_back(title, author, static_cast<Genre>(genre), id);
    }
    file.close();
    cout << "Books loaded from file successfully.\n";
}
