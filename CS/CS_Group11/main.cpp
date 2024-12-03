#include <iostream>
#include <iomanip>
#include <fstream>
#include <vector>
#include <string>

using namespace std;

// Struct to store item details
struct Item {
    string name;
    double price;
    int quantity;
};

// Function to display the cart items and totals
void displayCart(const vector<Item>& cart) {
    double total = 0.0;
    cout << "Item Name\tQuantity\tPrice\tTotal\n";
    for (const auto& item : cart) {
        double itemTotal = item.price * item.quantity;
        cout << item.name << "\t" << item.quantity << "\t\t$" << fixed << setprecision(2) << item.price
             << "\t$" << fixed << setprecision(2) << itemTotal << endl;
        total += itemTotal;
    }
    cout << "Total: $ " << fixed << setprecision(2) << total << endl;
}

// Function to save cart details to a text file
void saveCartToFile(const vector<Item>& cart) {
    ofstream outFile("shopping_cart.txt");
    if (outFile.is_open()) {
        outFile << "Item Name\tQuantity\tPrice\tTotal\n";
        for (const auto& item : cart) {
            double itemTotal = item.price * item.quantity;
            outFile << item.name << "\t" << item.quantity << "\t\t$" << fixed << setprecision(2) << item.price
                    << "\t$" << fixed << setprecision(2) << itemTotal << endl;
        }
        double total = 0.0;
        for (const auto& item : cart) {
            total += item.price * item.quantity;
        }
        outFile << "Total: $ " << fixed << setprecision(2) << total << endl;
        outFile.close();
        cout << "Cart saved to shopping_cart.txt" << endl;
    } else {
        cout << "Error: Could not open file for writing." << endl;
    }
}

// Function to add items to the cart
void addItemToCart(vector<Item>& cart, const string& name, double price, int quantity) {
    bool found = false;
    for (auto& item : cart) {
        if (item.name == name) {
            item.quantity += quantity;
            found = true;
            break;
        }
    }
    if (!found) {
        cart.push_back({name, price, quantity});
    }
}

int main() {
    vector<Item> cart;
    string name;
    double price;
    int quantity;
    char choice;

    do {
        cout << "Enter item name: ";
        cin >> ws; // To ignore leading whitespaces
        getline(cin, name);
        cout << "Enter item price: $";
        cin >> price;
        cout << "Enter quantity: ";
        cin >> quantity;

        addItemToCart(cart, name, price, quantity);

        cout << "Would you like to add another item? (y/n): ";
        cin >> choice;
    } while (choice == 'y' || choice == 'Y');

    cout << "\nYour Shopping Cart:\n";
    displayCart(cart);

    saveCartToFile(cart);

    return 0;
}