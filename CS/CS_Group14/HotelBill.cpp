#include <iostream>
#include <iomanip>
#include <vector>
using namespace std;

class HotelBill {
protected:
    float total;
    vector<pair<string, float>> items;  // Store item name and price

public:
    HotelBill() : total(0) {} // Constructor

    virtual void display_menu() {
        cout << setw(20) << "Hotel Menu" << endl;
        cout << "-------------------------" << endl;
        cout << "1. Pizza - $15.99" << endl;
        cout << "2. Salad - $5.50" << endl;
        cout << "3. Burger - $8.99" << endl;
        cout << "4. Water - $2.99" << endl;
        cout << "5. Exit menu" << endl;
    }

    virtual float calculate_total(float price, int quantity) {
        return price * quantity;
    }

    void add_item(const string& item, float price) {
        items.push_back(make_pair(item, price));
        total += price;  // Update total after adding item
    }

    float get_total() const {
        return total;
    }

    // Getter function to access items
    vector<pair<string, float>> get_items() const {
        return items;
    }

    void print_items() const {
        cout << "\nPurchased Items:\n";
        for (const auto& item : items) {
            cout << item.first << " - $" << fixed << setprecision(2) << item.second << endl;
        }
        cout << "-------------------\n";
        cout << "Total: $" << fixed << setprecision(2) << total << endl;
    }

    virtual ~HotelBill() {} // Virtual destructor
};
