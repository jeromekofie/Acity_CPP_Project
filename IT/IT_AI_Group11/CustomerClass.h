#include <iostream>
#include <iomanip>
#include <fstream>
#include "StationeryClass.h"
using namespace std;

class Customer {
private:
    Stationery cart[100];
    int quantities[100];
    int cartSize = 0;
    string customerName;

public:
    void setCustomerName(string name) { 
        customerName = name; 
        }

    void addToCart(Stationery product, int quantity) {
        if (cartSize < 100) {
            cart[cartSize] = product;
            quantities[cartSize] = quantity;
            cartSize++;
            cout << "Added " << quantity << " of " << product.getName() << " to the cart." << endl;
        } else {
            cout << "Cart is full!" << endl;
        }
    }

    void viewCart() {
        if (cartSize == 0) {
            cout << "Your cart is empty!" << endl;
        } else {
            cout << "\n========== Your Cart ==========" << endl;
            cout << setw(20) << left << "Product Name"
                 << setw(10) << left << "Quantity"
                 << setw(10) << "Price" << endl;
            for (int i = 0; i < cartSize; i++) {
                cout << setw(20) << left << cart[i].getName()
                     << setw(10) << left << quantities[i]
                     << "$" << setw(10) << fixed << setprecision(2) << cart[i].getPrice() << endl;
            }
        }
    }

    void checkout() {
        if (cartSize == 0) {
            cout << "Your cart is empty!" << endl;
            return;
        }

        double total = 2.0;
        ofstream receipt("receipt.txt");
        receipt << "========== Receipt ==========" << endl;
        receipt << "Customer Name: " << customerName << endl;
        receipt << setw(20) << left << "Product Name"
                << setw(10) << left << "Quantity"
                << setw(10) << "Price" << endl;

        for (int i = 0; i < cartSize; i++) {
            double itemTotal = quantities[i] * cart[i].getPrice();
            total += itemTotal;

            receipt << setw(20) << left << cart[i].getName()
                    << setw(10) << left << quantities[i]
                    << "$" << setw(10) << fixed << setprecision(2) << itemTotal << endl;
        }

        receipt << "==============================" << endl;
        receipt << "Kelly Tax: $2.00 " << endl;
        receipt << "==============================" << endl;
        receipt << "Total: $" << fixed << setprecision(2) << total << endl;
        receipt.close();

        cout << "Checkout successful! Receipt saved to 'receipt.txt'." << endl;
        cartSize = 0; 
    }
};
