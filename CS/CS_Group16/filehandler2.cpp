#include <iostream>
#include <fstream>
#include <string>

using namespace std;

class FileHandler {
private:
    string filename;

public:
    
    FileHandler(string file) {
        filename = file; 
    }

    
    string getFilename() const {
        return filename;
    }

    
    void createFile() {
        ofstream file(filename); 
        if (file) {
            cout << "File created: " << getFilename() << endl;
        } else {
            cout << "Failed to create file: " << getFilename() << endl;
        }
    }

    void writeFile(string text) {
        ofstream file(filename); 
        if (file) {
            file << text << endl;
            cout << "Text written to file: " << getFilename() << endl;
        } else {
            cout << "Failed to write to file: " << getFilename() << endl;
        }
    }

    void deleteFile() {
        if (remove(filename.c_str()) == 0) { // 0 means deleted
            cout << "File deleted: " << getFilename() << endl;
        } else {
            cout << "Failed to delete file: " << getFilename() << endl;
        }
    }
};