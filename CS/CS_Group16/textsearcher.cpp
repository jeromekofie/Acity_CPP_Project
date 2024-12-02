#include <iostream>
#include <fstream>
#include <string>

using namespace std;

class TextSearcher {
    string filename;

public:
    TextSearcher(string file) {
        filename = file;
    }

    void search(string keyword) {
        ifstream file(filename.c_str());
        if (!file.is_open()) {
            cout << "Error: Cannot open file for reading.\n";
            return;
        }

        string line;
        int lineNumber = 0;
        bool found = false;

        while (getline(file, line)) {
            lineNumber++;
            if (line.find(keyword) != string::npos) {
                found = true;
                cout << "Found on line " << lineNumber << ": " << line << endl;
            }
        }

        if (!found) {
            cout << "Keyword not found in the file.\n";
        }

        file.close();
    }
};
