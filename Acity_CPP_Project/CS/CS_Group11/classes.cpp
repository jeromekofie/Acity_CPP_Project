#include <iostream>
#include <vector>
#include <string>
#include "loops.cpp"
#include "fuctions.cpp"

using namespace std;

// Structure to represent an item in the cart
struct Item {
    string name;
    int quantity;
    double price;
};

// Class representing the ShoppingCart
class ShoppingCart {
public:
    vector <Item> items;

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
        displayCartLoop(items); // Display items using loop
        calculateTotal(items);  // Calculate the total price
    }
};
