//The TextSearcher class in the program is designed to help you search for specific keywords or phrases in a text file.
// destructor Ensures the file is closed automatically when the object is destroyed.
///string::npos: A constant that means "no position" or "not found


#include <iostream>
#include <fstream>
#include <string>
using namespace std;

class TextSearcher {
private:
    ifstream file; 
public:
   
    TextSearcher(string filename) {
        file.open(filename);
        if (!file) { 
            cout << "Could not open the file " << filename << endl;
        }
    }

    void searchKeyword(string keyword) {
        if (!file) {
            cout << "File not available for searching.\n";
            return;
        }

        string line;
        int lineNumber = 0;
        int foundCount = 0;

        while (getline(file, line)) {
            lineNumber++;
            if (line.find(keyword) != string::npos) {  //unique  It is typically used to indicate that a substring or character was not found in a string.
                foundCount++;
                cout << "Line " << lineNumber << ": " << line << endl;
            }
        }

        if (foundCount == 0) {
            cout << "No matches found for \"" << keyword << "\".\n";
        } else {
            cout << "Total matches found: " << foundCount << endl;
        }
    }
      TextSearcher() {
        if (file.is_open()) {
            file.close();
        }
    }

};
//When functions like find(), rfind(), or other search-related methods do not find the specified substring or character, 
//they return string::npos. This allows programmers to easily check for successful searches
//This means that when you use string::npos, it effectively represents an invalid position within the string, signaling that no match was found
