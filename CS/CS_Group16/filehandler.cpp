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

    void createFile() {
        ofstream file(filename);
        if (file) {
            cout << "File created.\n";
        } else {
            cout << "Failed to create file.\n";
        }
    }

    void writeFile(string text) {
        ofstream file(filename); // Overwrites the file
        if (file) {
            file << text << endl;
            cout << "Text written to file.\n";
        } else {
            cout << "Failed to write to file.\n";
        }
    }

    void deleteFile() {
        if (remove(filename.c_str()) == 0) {
            cout << "File deleted.\n";
        } else {
            cout << "Failed to delete file.\n";
        }
    }
};
