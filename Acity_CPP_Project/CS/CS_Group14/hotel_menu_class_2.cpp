#include <iostream>
#include <iomanip>
#include <fstream>
using namespace std;

class DisplayBill
{
public:
    void display_bill(float grandTotal) {
    // Open the file to write the bill
    ofstream billFile("Hotel_Bill.txt");
    if (billFile.is_open()) {
        billFile << "Hotel DIOR" << endl;
        billFile << "\n" ;
        billFile << "E:levy - 20$\n" 
                    "Covid Tax - 10$\n"
                    "VAT - 8$\n" 
                    "\n";
        billFile << "_____________________________" <<endl;
        billFile << "Your total is: $" << fixed << setprecision(2) << grandTotal << endl;

        billFile.close(); // Close the file after writing
    } else {
        cout << "Unable to open file for writing." << endl;
    }
    cout << "Your total is: $" << fixed << setprecision(2) << grandTotal << endl;
}




};
