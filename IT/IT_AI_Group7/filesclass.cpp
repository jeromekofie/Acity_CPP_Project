#include <iostream>
#include <vector>
#include <string>
#include <fstream>
// #include "taskclass.cpp"
#include "taskclass.cpp"

using namespace std;

class FileHandler { 
public:
    static void saveTasksToFile(const vector<Task>& tasks, const string& filename) {
        ofstream outFile(filename);
        if (outFile.is_open()) {
            for (const auto& task : tasks) {
                outFile << task.getDetails() << '\n'; // Save using the getDetails format
                outFile << "---------------------\n"; // Optional: Add a separator for clarity
            }
            outFile.close();
        } else {
            cerr << "Error: Could not open file for writing.\n";
        }
    }

    static void loadTasksFromFile(vector<Task*>& tasks, const string& filename, int& nextId) {
        // Extend this function to load tasks from a file
    }
};
