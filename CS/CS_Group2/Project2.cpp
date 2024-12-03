#include <iostream>
#include <fstream>
#include <vector> //for precised arrays.
#include <iomanip>
#include <string>
#include <cstdlib>
#include <limits>
#include <algorithm> // to find the value in range
#include"Library.cpp"
#include"Book.cpp"
#include"Member.cpp"
#include"StudentMember.cpp"


using namespace std;



int main() {
    Library library;
    library.loadFromFile();

    int choice, id, memberId;  // Declare memberId here
    string title, author, name;

    while (true) {
        cout << "\nLibrary Management System\n";
        cout << "1. Add Book\n2. Add Member\n3. Display Books\n4. Display Members\n5. Borrow Book\n6. Return Book\n7. Delete Book\n8. Borrowed Book By Member\n9. Returned Book By Member\n10. Exit\n";
        cout << "Enter choice: ";

        while (!(cin >> choice)) {
            cin.clear();
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
            cout << "Invalid input. Please enter a number: ";
        }

        switch (choice) {
            case 1:
                // Add Book
                cout << "Enter Book Title: ";
                cin.ignore();
                getline(cin, title);
                cout << "Enter Author: ";
                getline(cin, author);
                cout << "Enter Book ID: ";
                while (!(cin >> id)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Book ID: ";
                }
                library.addBook(title, author, id);
                break;

            case 2:
                // Add Member
                cout << "Enter Member Name: ";
                cin.ignore();
                getline(cin, name);
                cout << "Enter Member ID: ";
                while (!(cin >> memberId)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Member ID: ";
                }
                cout << "Enter Type (1-Student, 2-Teacher): ";
                int type;
                while (!(cin >> type) || (type != 1 && type != 2)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter 1 for Student or 2 for Teacher: ";
                }
                if (type == 1)
                    library.addMember(new StudentMember(name, memberId));
                else
                    library.addMember(new TeacherMember(name, memberId));
                break;

            case 3:
                // Display Books
                library.displayBooks();
                break;

            case 4:
                // Display Members
                library.displayMembers();
                break;

            case 5:
                // Borrow Book
                cout << "Enter Book ID to Borrow: ";
                while (!(cin >> id)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Book ID: ";
                }
                cout << "Enter Member ID: ";
                while (!(cin >> memberId)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Member ID: ";
                }
                library.borrowBook(id, memberId);
                break;

            case 6:
                // Return Book
                cout << "Enter Book ID to Return: ";
                while (!(cin >> id)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Book ID: ";
                }
                cout << "Enter Member ID: ";
                while (!(cin >> memberId)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Member ID: ";
                }
                library.returnBook(id, memberId);
                break;

            case 7:
                // Delete Book
                cout << "Enter Book ID to Delete: ";
                while (!(cin >> id)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Book ID: ";
                }
                library.deleteBook(id);
                break;

            case 8:
                // Display Borrowed Books by Member
                cout << "Enter Member ID to view borrowed books: ";
                while (!(cin >> memberId)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Member ID: ";
                }

                // Use the getter method to access members
                for (auto &member : library.getMembers()) {
                    if (member->getId() == memberId) {
                        member->displayBorrowedBooks();
                        break;
                    }
                }
                break;

            case 9:
                // Return Book by Member
                cout << "Enter Member ID to return books: ";
                while (!(cin >> memberId)) {
                    cin.clear();
                    cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    cout << "Invalid input. Please enter a valid Member ID: ";
                }

                // Use the getter method to access members
                for (auto &member : library.getMembers()) {//auto: This keyword tells the compiler to automatically deduce the type of member
                    if (member->getId() == memberId) {
                        int bookId;
                        cout << "Enter Book ID to return: ";
                        while (!(cin >> bookId)) {
                            cin.clear();
                            cin.ignore(numeric_limits<streamsize>::max(), '\n');
                            cout << "Invalid input. Please enter a valid Book ID: ";
                        }
                        library.returnBook(bookId, memberId);
                        break;
                    }
                }
                break;

            case 10:
                library.saveToFile();
                cout << "Exiting...\n";
                return 0;
                break;


            default:
                cout << "Invalid choice. Try again.\n";
        }
    }
}

//end of main
