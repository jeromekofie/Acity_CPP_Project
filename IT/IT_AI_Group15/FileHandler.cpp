#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include "KeywordSearcher.cpp"
using namespace std;

class FileHandler: public KeywordSearcher{
private:
    string filename;

public:
    void setFilename(string name) {
         filename = name; 
         }

    string getFilename()  { 
        return filename; 
        }

    bool fileExists()  {
        ifstream file(filename);
        return file.is_open();
    }

    vector<string> readFile() {
        ifstream file(filename);
        vector<string> lines;

        string line;
        while (getline(file, line)) {
            lines.push_back(line);
        }
        file.close();
        return lines;
    }

    void saveToFile(vector<string> lines, string outputFilename) {
        ofstream outFile(outputFilename);
        if (!outFile.is_open()) {
            cerr << "Error opening output file." << endl;
            return;
        }
        for (auto line : lines) {
            outFile << line << endl;
        }
        outFile.close();
        cout << "Results saved to " << outputFilename << endl;
    }
};
