#pragma once
#include <iostream>
#include <string>
using namespace std;

// Enumerated data type for genres
enum Genre { FICTION, NON_FICTION, MYSTERY, SCIENCE_FICTION, FANTASY, BIOGRAPHY };

// Base class: Book
class Book {
protected:
    string title;
    string author;
    Genre genre;
    int id;

public:
    Book(string t = "", string a = "", Genre g = FICTION, int i = 0)
        : title(t), author(a), genre(g), id(i) {}

    string getTitle() const { return title; }
    string getAuthor() const { return author; }
    Genre getGenre() const { return genre; }
    int getId() const { return id; }

    void setTitle(string t) { title = t; }
    void setAuthor(string a) { author = a; }
    void setGenre(Genre g) { genre = g; }
    void setId(int i) { id = i; }

    virtual void display() const {
        cout << "\tID: " << id << "\n\tTitle: " << title
             << "\n\tAuthor: " << author
             << "\n\tGenre: " << genreToString(genre) << endl;
    }

    static string genreToString(Genre g) {
        switch (g) {
            case FICTION: return "Fiction";
            case NON_FICTION: return "Non-Fiction";
            case MYSTERY: return "Mystery";
            case SCIENCE_FICTION: return "Science Fiction";
            case FANTASY: return "Fantasy";
            case BIOGRAPHY: return "Biography";
            default: return "Unknown";
        }
    }
};
