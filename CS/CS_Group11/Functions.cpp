#include <iostream>
#include <iomanip>
#include <vector>
#include <string>
using namespace std;

struct Item {
    string name;
    int quantity;
    double price;
};

void calculateTotal(const vector<Item>& items) {
    double total = 0.0;
    for (const auto& item : items) {
        total += item.quantity * item.price;
    }
    cout << "Total: $" << fixed << setprecision(2) << total << "\n";
}