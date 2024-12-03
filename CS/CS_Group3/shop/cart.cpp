#include <iostream>
#include <vector>
#include <string>
#include <fstream> // For file handling
using namespace std;

// CartItem Class
class CartItem {
private:
    string name;
    double price;
    int quantity;

public:
    // Constructor
    CartItem(string itemName, double itemPrice, int itemQuantity)
        : name(itemName), price(itemPrice), quantity(itemQuantity) {}

    // Getters
    string getName() const { return name; }
    double getPrice() const { return price; }
    int getQuantity() const { return quantity; }

    // Setter for quantity
    void setQuantity(int newQuantity) { quantity = newQuantity; }
};

// Function to add an item to the cart
void addItemToCart(vector<CartItem>& cart, const string& name, double price, int quantity) {
    CartItem newItem(name, price, quantity);
    cart.push_back(newItem);
    cout << "Item added: " << name << " (Quantity: " << quantity << ")" << endl;
}

// Function to display all items in the cart and write to a file
void displayCart(const vector<CartItem>& cart) {
    ofstream outFile("cart_output.txt", ios::app); // Append mode
    if (!outFile) {
        cerr << "Error: Unable to open file for writing!" << endl;
        return;
    }

    if (cart.empty()) {
        cout << "The cart is empty!" << endl;
        outFile << "The cart is empty!" << endl;
        outFile.close();
        return;
    }

    cout << "Cart Contents:" << endl;
    outFile << "Cart Contents:" << endl;

    for (const auto& item : cart) {
        cout << "Name: " << item.getName()
             << ", Price: $" << item.getPrice()
             << ", Quantity: " << item.getQuantity() << endl;

        outFile << "Name: " << item.getName()
                << ", Price: $" << item.getPrice()
                << ", Quantity: " << item.getQuantity() << endl;
    }

    outFile.close();
}

// Function to calculate the total cost of the cart and write to a file
void calculateTotal(const vector<CartItem>& cart) {
    ofstream outFile("cart_output.txt", ios::app); // Append mode
    if (!outFile) {
        cerr << "Error: Unable to open file for writing!" << endl;
        return;
    }

    double total = 0.0;
    for (const auto& item : cart) {
        total += item.getPrice() * item.getQuantity();
    }

    cout << "Total cost of items in the cart: $" << total << endl;
    outFile << "Total cost of items in the cart: $" << total << endl;

    outFile.close();
}

// Function to remove an item from the cart by name
void removeItemFromCart(vector<CartItem>& cart, const string& name) {
    for (auto it = cart.begin(); it != cart.end(); ++it) {
        if (it->getName() == name) {
            cout << "Item removed: " << name << endl;
            cart.erase(it);
            return;
        }
    }
    cout << "Item not found: " << name << endl;
}

// Function to update the quantity of an item in the cart
void updateItemQuantity(vector<CartItem>& cart, const string& name, int newQuantity) {
    for (auto& item : cart) {
        if (item.getName() == name) {
            item.setQuantity(newQuantity);
            cout << "Updated " << name << " to quantity: " << newQuantity << endl;
            return;
        }
    }
    cout << "Item not found: " << name << endl;
}
