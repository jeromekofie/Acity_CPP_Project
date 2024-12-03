#include "LibraryFunctions.cpp"
#include "LibrarySearchSave.cpp"
#include "Book.cpp"
#include <iostream>
using namespace std;

void Library::menu() {
    int choice;
    do {
        cout << "\nLibrary Menu\n";
        cout << "1. Add Book\n";
        cout << "2. Search Books\n";
        cout << "3. Save Books to File\n";
        cout << "4. Load Books from File\n";
        cout << "5. Exit\n";
        cout << "Enter choice: ";
        cin >> choice;

        switch (choice) {
            case 1: addBook(); break;
            case 2: searchBooks(); break;
            case 3: saveToFile(); break;
            case 4: loadFromFile(); break;
            case 5: cout << "Exiting...\n"; break;
            default: cout << "Invalid choice!\n";
        }
    } while (choice != 5);
}

int main() {
    Library library;
    library.menu();
    return 0;
}
