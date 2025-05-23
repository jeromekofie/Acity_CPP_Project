#include <iostream>
#include <vector>
#include "shop.cpp"

using namespace std;

int main() {
    vector<Item> cart;
    string name;
    int quantity;
    char choice;

    do {
        cout << "Enter item name: ";
        cin >> ws; // To ignore leading whitespaces
        getline(cin, name);
        cout << "Enter quantity: ";
        cin >> quantity;

        addItemToCart(cart, name, quantity);

        cout << "Would you like to add another item? (y/n): ";
        cin >> choice;
    } while (choice == 'y' || choice == 'Y');

    cout << "\nYour Shopping Cart:\n";
    displayCart(cart);

    saveCartToFile(cart);

    return 0;
}
