#include <iostream>
#include <iomanip>
#include <fstream>
#include <vector>

using namespace std;

// Class to represent an Item in the shopping cart
class Item {
private:
    string name;
    double price;
    int quantity;

public:
    // Constructors
    Item() : name(""), price(0.0), quantity(0) {}
    Item(const string& name, double price, int quantity)
        : name(name), price(price), quantity(quantity) {}

    // Getter functions
    string getName() const { return name; }
    double getPrice() const { return price; }
    int getQuantity() const { return quantity; }

    // Setter functions
    void setQuantity(int qty) { quantity = qty; }

    // Function to calculate the total cost of the item
    double calculateTotal() const { return price * quantity; }

    // Function to display item details
    void display() const {
        cout << name << "\t" << quantity << "\t\tcedis" << fixed << setprecision(2) << price
             << "\tcedis" << fixed << setprecision(2) << calculateTotal() << endl;
    }
};

// Class to manage the shopping cart
class ShoppingCart {
private:
    vector<Item> cart;

public:
    // Function to add an item to the cart
    void addItem(const string& name, double price, int quantity) {
        for (auto& item : cart) {
            if (item.getName() == name) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        cart.emplace_back(name, price, quantity);
    }

    // Function to display the cart contents
    void displayCart() const {
        double total = 0.0;
        cout << "Item Name\tQuantity\tPrice\tTotal\n";
        for (const auto& item : cart) {
            item.display();
            total += item.calculateTotal();
        }
        cout << "Total: cedis " << fixed << setprecision(2) << total << endl;
    }

    // Function to save the cart details to a file
    void saveToFile(const string& filename) const {
        ofstream outFile(filename);
        if (outFile.is_open()) {
            outFile << "Item Name\tQuantity\tPrice\tTotal\n";
            double total = 0.0;
            for (const auto& item : cart) {
                outFile << item.getName() << "\t" << item.getQuantity()
                        << "\t\tcedis" << fixed << setprecision(2) << item.getPrice()
                        << "\tcedis" << fixed << setprecision(2) << item.calculateTotal() << endl;
                total += item.calculateTotal();
            }
            outFile << "Total: cedis " << fixed << setprecision(2) << total << endl;
            outFile.close();
            cout << "Cart saved to " << filename << endl;
        } else {
            cout << "Error: Could not open file for writing." << endl;
        }
    }
};


