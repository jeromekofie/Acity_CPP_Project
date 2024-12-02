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

