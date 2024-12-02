#include <iostream>
#include <iomanip>
#include <vector>
#include <string>  // Needed for the `string` type

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

int main() {
    // Example usage
    vector<Item> cart = {
        {"Apple", 2, 0.99},
        {"Bread", 1, 2.50},
        {"Milk", 1, 1.99}
    };

    calculateTotal(cart);

    return 0;
}
