#include <ctime>
#include <string>
#include <fstream>
#include<iostream>

using namespace std;

class Rental {
private:
    string customerName;
    string vehicleId;
    time_t startDate;

public:
    Rental(string customerName = "", string vehicleId = "", time_t startDate = 0)
        : customerName(customerName), vehicleId(vehicleId), startDate(startDate) {}

    // Save rental to file
    void saveToFile(ofstream &outFile) {
        if (outFile.is_open()) {
            outFile << customerName << "," << vehicleId << "," << startDate << endl;
        }
    }

    // Load rental from file
    static Rental loadFromFile(ifstream &inFile) {
        string customerName, vehicleId;
        time_t startDate;

        if (getline(inFile, customerName, ',') &&
            getline(inFile, vehicleId, ',') &&
            (inFile >> startDate)) {
            inFile.ignore(); // To handle newline character
            return Rental(customerName, vehicleId, startDate);
        }

        return Rental();
    }

    // Display rental details
    void display() {
        cout << "Customer: " << customerName << ", Vehicle ID: " << vehicleId
             << ", Start Date: " << ctime(&startDate);
    }
};
