#include <iostream>
#include <fstream>
#include <vector> //for precised arrays.
#include <iomanip>
#include <string>
#include <cstdlib>
#include <limits>
#include <algorithm> // to find the value in range
using namespace std;

class Book {
private:
    string title;
    string author;
    int id;
    bool isAvailable;
public:
    Book(string t, string a, int i) {
        title = t;
        author = a;
        id = i;
        isAvailable = true;
    }

    // Getters and Setters
    string getTitle() { return title; }
    string getAuthor() { return author; }
    int getId() { return id; }
    bool getAvailability() { return isAvailable; }
    void setAvailability(bool availability) { isAvailable = availability; }

    void displayBook() {
        cout << setw(5) << id << setw(20) << title << setw(20) << author
             << setw(20) << (isAvailable ? "Available" : "Borrowed") << endl;
    }
};

