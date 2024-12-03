#include <iostream>
#include <fstream>
#include <string>
using namespace std;

class TextSearcher {
private:
    string filename;
public:
    TextSearcher(string file) {
        filename = file;
    }

    void searchKeyword(string keyword) {
        ifstream file(filename);
        if (!file) {
            cout << "Could not open the file.\n";
            return;
        }

        string line;
        int lineNumber = 0;
        bool found = false;

        while (getline(file, line)) {
            lineNumber++;
            if (line.find(keyword) != string::npos) {
                found = true;
                cout << "Line " << lineNumber << ": " << line << endl;
            }
        }

        if (!found) {
            cout << "No matches found for \"" << keyword << "\".\n";
        }

        file.close();
    }
};
