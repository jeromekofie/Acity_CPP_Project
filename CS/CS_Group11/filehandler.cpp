#include <iostream>
#include <fstream>
#include <vector>
#include "Classes.cpp"

using namespace std;

// Function to save the cart to a file
void saveCart(const ShoppingCart& cart) {
    ofstream file("CartDetails.txt");
    if (!file) {
        cout << "Error saving cart to file!\n";
        return;
    }

    for (const auto& item : cart.getItems()) { // Use getter to access items
        file << item.name << "," << item.quantity << "," << item.price << "\n";
    }

    file.close();
    cout << "Cart saved successfully!\n";
}

// Function to load the cart from a file
void loadCart(ShoppingCart& cart) {
    ifstream file("CartDetails.txt");
    if (!file) {
        cout << "Error loading cart from file!\n";
        return;
    }

    vector<Item> loadedItems;
    string name;
    int quantity;
    double price;

    while (file >> name >> quantity >> price) {
        loadedItems.push_back({name, quantity, price});
    }

    cart.setItems(loadedItems); // Use setter to update items
    file.close();
    cout << "Cart loaded successfully!\n";
}
