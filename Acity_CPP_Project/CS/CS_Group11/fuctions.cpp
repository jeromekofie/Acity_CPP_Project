#include <iostream>
#include <iomanip>
#include "classes.cpp"

using namespace std;

// Function to calculate the total of the cart
void calculateTotal(const vector<Item>& items) {
    double total = 0.0;
    for (const auto & item : items) {
        total += item.quantity * item.price;
    }
    cout << "Total: $" << fixed << setprecision(2) << total << "\n";
}
