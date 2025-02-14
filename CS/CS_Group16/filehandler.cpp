//The FileHandler class in the program is responsible for managing basic file operations such as creating, writing to, and deleting files.
//filename.c_str():Converts the filename string into a C-style string (const char*), which is required by the remove function.
//When you open a file in output mode using ofstream in C++, it allows you to write data to the file.
//ofstream file(filename); creates an emty file


#include <iostream>
#include <fstream>
#include <string>
using namespace std;

class FileHandler {
private:
    string filename; //decla
public:
    FileHandler(string file) {
        filename = file;  //ass
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
        ofstream file(filename); 
        if (file) {
            file << text << endl;
            cout << "Text written to file.\n";
        } else {
            cout << "Failed to write to file.\n";
        }
    }

    void deleteFile() {
        if (remove(filename.c_str()) == 0) { //0 means deleted
            cout << "File deleted.\n";
        } else {
            cout << "Failed to delete file.\n";
        }
    }
};
