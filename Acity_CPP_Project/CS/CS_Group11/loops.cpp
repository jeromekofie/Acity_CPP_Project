#include <iostream>
#include <iomanip>
#include "classes.cpp"

using namespace std;

// Function to display the cart using loops
void displayCartLoop(const vector<Item>& cart) {
    cout << "\nYour Cart:\n";
    cout << left << setw(20) << "Item Name" << setw(10) << "Quantity" << setw(10) << "Price\n";
    cout << string(40, '-') << "\n";

    for (const auto& item : cart) {
        cout << left << setw(20) << item.name
             << setw(10) << item.quantity
             << fixed << setprecision(2) << item.price << "\n";
    }
}
