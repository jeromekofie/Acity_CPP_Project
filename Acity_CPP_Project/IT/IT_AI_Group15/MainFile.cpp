#include <iostream>
#include <vector>
#include "FileHandler.cpp"
#include "KeywordSearcher.cpp"
using namespace std;

int main() {
    FileHandler file;

    while (true) {
        cout << "\nMenu:\n1. Enter file name\n2. Search keyword\n3. Save results to file\n4. Exit\nChoose an option: ";
        int choice;
        cin >> choice;

        if (choice == 1) {
            string filename;
            cout << "Enter the filename: ";
            cin >> filename;
            file.setFilename(filename);

            if (file.fileExists()) {
                cout << "File '" << filename << "' exists. You can now perform operations on it." << endl;
            } else {
                cout << "Error: File '" << filename << "' does not exist. Please enter a valid file name." << endl;
            }
        } else if (choice == 2) {
            string keyword;
            cout << "Enter the keyword or phrase to search: ";
            cin.ignore(); // Ignore the leftover newline
            getline(cin, keyword);

            vector<string> lines = file.readFile();
            if (!lines.empty()) {
                file.search(lines, keyword)
                ;
                file.navigateResults();
            }
        } else if (choice == 3) {
            string outputFilename;
            cout << "Enter the output filename: ";
            cin >> outputFilename;

            vector<string> results = file.getResults();
            if (!results.empty()) {
                file.saveToFile(results, outputFilename);
            } else {
                cout << "No results to save." << endl;
            }
        } else if (choice == 4) {
            cout << "Exiting program." << endl;
            break;
        } else {
            cout << "Invalid option. Please try again." << endl;
        }
    }

    return 0;
}
