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
