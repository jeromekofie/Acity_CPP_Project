#include <iostream>
#include <vector>
#include <iomanip>
#include <string>
using namespace std;

struct Item {
    string name;
    int quantity;
    double price;
};

class ShoppingCart {
public:
    vector<Item> items;

    void addItem() {
        Item newItem;
        cout << "Enter item name: ";
        cin.ignore();
        getline(cin, newItem.name);
        cout << "Enter item quantity: ";
        cin >> newItem.quantity;
        cout << "Enter item price: ";
        cin >> newItem.price;

        items.push_back(newItem);
        cout << "Item added successfully!\n";
    }

    void viewCart() {
        if (items.empty()) {
            cout << "Your cart is empty!\n";
            return;
        }

        cout << "\nYour Cart:\n";
        cout << left << setw(20) << "Item Name" << setw(10) << "Quantity" << setw(10) << "Price\n";
        cout << string(40, '-') << "\n";

        for (const auto& item : items) {
            cout << left << setw(20) << item.name
                 << setw(10) << item.quantity
                 << fixed << setprecision(2) << item.price << "\n";
        }

        double total = 0.0;
        for (const auto& item : items) {
            total += item.quantity * item.price;
        }

        cout << string(40, '-') << "\n";
        cout << "Total: $" << fixed << setprecision(2) << total << "\n";
    }
};