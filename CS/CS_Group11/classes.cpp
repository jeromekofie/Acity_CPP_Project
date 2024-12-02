#include <iostream>
#include <vector>
#include <string>

using namespace std;

// Structure to represent an item in the cart
struct Item {
    string name;
    int quantity;
    double price;
};

// Function prototypes for external dependencies
void displayCartLoop(const vector<Item>& cart);
void calculateTotal(const vector<Item>& items);

// Class representing the ShoppingCart
class ShoppingCart {
private:
    vector<Item> items; // Encapsulation: private member

public:
    // Add an item to the cart
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

    // View all items in the cart
    void viewCart() const {
        if (items.empty()) {
            cout << "Your cart is empty!\n";
            return;
        }
        displayCartLoop(items); // Display items using loop
        calculateTotal(items);  // Calculate the total price
    }

    // Getter for accessing `items`
    const vector<Item>& getItems() const {
        return items;
    }

    // Setter for modifying `items`
    void setItems(const vector<Item>& newItems) {
        items = newItems;
    }
};
