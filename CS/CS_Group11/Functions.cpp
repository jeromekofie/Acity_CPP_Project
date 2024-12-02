#include <iostream>
#include <iomanip>
#include <vector>

using namespace std;

// Structure to represent an item in the cart
struct Item {
    string name;
    int quantity;
    double price;
};

// Function to calculate the total of the cart
void calculateTotal(const vector<Item>& items) {
    double total = 0.0;
    for (const auto& item : items) {
        total += item.quantity * item.price;
    }
    cout << "Total: $" << fixed << setprecision(2) << total << "\n";
}
