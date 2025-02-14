#include <iostream>
#include <string>
#include "FileHandler.cpp"
#include "TextSearcher.cpp"
using namespace std;

void showMenu() {
    cout << "\nMenu:\n";
    cout << "1. Create a new file\n";
    cout << "2. Write to the file\n";
    cout << "3. Search for a keyword in the file\n";
    cout << "4. Delete the file\n";
    cout << "5. Exit\n";
    cout << "Choose an option: ";
}

int main() {
    string filename = "example.txt";
    FileHandler fileHandler(filename);
    TextSearcher textSearcher(filename);   //Object Instantiation .instances of the specified classes are created and initialized with specific parameters. 
    //This allows the program to utilize the methods and properties defined within those classes for further operations.

    int choice;

    do {
        showMenu();
        cin >> choice;
        cin.ignore(); 

        if (choice == 1) {
            fileHandler.createFile();
        } else if (choice == 2) {
            cout << "Enter text to write into the file: ";
            string text;
            getline(cin, text);
            fileHandler.writeFile(text);
        } else if (choice == 3) {
            cout << "Enter keyword to search: ";
            string keyword;
            getline(cin, keyword);
            textSearcher.searchKeyword(keyword);
        } else if (choice == 4) {
            fileHandler.deleteFile();
        } else if (choice == 5) {
            cout << "Goodbye!\n";
        } else {
            cout << "Invalid choice. Try again.\n";
        }

    } while (choice != 5);

    return 0;
}

