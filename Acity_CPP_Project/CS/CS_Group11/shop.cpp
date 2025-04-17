#include <iostream>
#include <iomanip>
#include <fstream>
#include <vector>
#include <unordered_map>
#include <string>

using namespace std;

// Struct to store item details
struct Item {
    string name;
    double price;
    int quantity;

    // Constructor
    Item(const string& itemName, double itemPrice, int itemQuantity)
        : name(itemName), price(itemPrice), quantity(itemQuantity) {}
};

// Predefined prices for items
unordered_map<string, double> prices = {
    {"Apple", 2}, {"Apricot", 3}, {"Banana", 5}, {"Blackberry", 8},
    {"Blueberry", 5}, {"Boysenberry", 4}, {"Canary", 2}, {"Melon", 7},
    {"Cantaloupe", 9}, {"Casaba Melon", 90}, {"Cherimoya", 77}, {"Cherry", 45},
    {"Christmas Melon", 45}, {"Clementine", 34}, {"Cranberry", 56},
    {"Crenshaw Melon", 67}, {"Grapes", 5}, {"Grapefruit", 45}, {"Guava", 34},
    {"Honeydew Melon", 44}, {"Lemon", 2}, {"Lime", 1}, {"Loganberry", 3},
    {"Longan", 4}, {"Loquat", 4}, {"Lychee", 5}, {"Mandarin", 4},
    {"Mango", 45}, {"Mangosteen", 4}, {"Mamoncillo", 4}, {"Minneola", 34},
    {"Musk Melon", 44}, {"Nance", 55}, {"Nectarine", 56}, {"Orange", 56}
};

// Function to add items to the cart
void addItemToCart(vector<Item>& cart, const string& name, int quantity) {
    if (prices.find(name) == prices.end()) {
        cout << "Item not found in the price list. Please try again.\n";
        return;
    }
    double price = prices[name];
    for (auto& item : cart) {
        if (item.name == name) {
            item.quantity += quantity;
            return;
        }
    }
    cart.emplace_back(name, price, quantity);
}

// Function to display the cart items and totals
void displayCart(const vector<Item>& cart) {
    double total = 0.0;
    cout << "Item Name\tQuantity\tPrice\tTotal\n";
    for (const auto& item : cart) {
        double itemTotal = item.price * item.quantity;
        cout << item.name << "\t" << item.quantity << "\t\tcedis" << fixed << setprecision(2) << item.price
             << "\tcedis" << fixed << setprecision(2) << itemTotal << endl;
        total += itemTotal;
    }
    cout << "Total: cedis " << fixed << setprecision(2) << total << endl;
}

// Function to save cart details to a text file
void saveCartToFile(const vector<Item>& cart) {
    ofstream outFile("shopping_cart.txt");
    if (outFile.is_open()) {
        outFile << "Item Name\tQuantity\tPrice\tTotal\n";
        for (const auto& item : cart) {
            double itemTotal = item.price * item.quantity;
            outFile << item.name << "\t" << item.quantity << "\t\tcedis" << fixed << setprecision(2) << item.price
                    << "\tcedis" << fixed << setprecision(2) << itemTotal << endl;
        }
        double total = 0.0;
        for (const auto& item : cart) {
            total += item.price * item.quantity;
        }
        outFile << "Total: cedis " << fixed << setprecision(2) << total << endl;
        outFile.close();
        cout << "Cart saved to shopping_cart.txt\n";
    } else {
        cout << "Error: Could not open file for writing.\n";
    }
}
