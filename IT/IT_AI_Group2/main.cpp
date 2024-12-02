#include "Library.h"
#include <iostream>
using namespace std;

int main() {
    Library library;
    int choice;

    do {
        cout <<" ______________________________________";
        cout << "\n--- Library Management System ---\n";
        cout << "______________________________________";
        cout << "                                      ";
        cout << "1. Add Book\n";
        cout << "2. Display Books\n";
        cout << "3. Add Member\n";
        cout << "4. Display Members\n";
        cout << "5. Borrow Book\n";
        cout << "6. Return Book\n";
        cout << "7. Exit\n";
        cout << "Enter your choice: ";
        cin >> choice;

        switch (choice) {
        case 1:
            library.addBook();
            break;
        case 2:
            library.displayBooks();
            break;
        case 3:
            library.addMember();
            break;
        case 4:
            library.displayMembers();
            break;
        case 5:
            library.borrowBook();
            break;
        case 6:
            library.returnBook();
            break;
        case 7:
            cout << "Exiting the program. Goodbye!\n";
            break;
        default:
            cout << "Invalid choice! Please try again.\n";
        }
    } while (choice != 7);

    return 0;
}
