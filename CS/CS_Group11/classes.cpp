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


    // Getter for accessing `items`
    const vector<Item>& getItems() const {
        return items;
    }

    // Setter for modifying `items`
    void setItems(const vector<Item>& newItems) {
        items = newItems;
    }
};
