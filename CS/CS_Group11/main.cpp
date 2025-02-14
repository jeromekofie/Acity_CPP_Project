#include <iostream>
#include "shop.cpp" // This includes the `ShoppingCart` and `Item` classes

using namespace std;

int main() {
    ShoppingCart cart;
    string name;
    double price;
    int quantity;
    char choice;

    do {
        cout << "\nMenu:\n";
        cout << "1. Add Item to Cart\n";
        cout << "2. Display Cart\n";
        cout << "3. Save Cart to File\n";
        cout << "4. Exit\n";
        cout << "Enter your choice: ";
        cin >> choice;

        switch (choice) {
            case '1':
                cout << "Enter item name: ";
                cin >> ws; // To ignore leading whitespaces
                getline(cin, name);
                cout << "Enter item price (in cedis): ";
                cin >> price;
                cout << "Enter quantity: ";
                cin >> quantity;
                cart.addItem(name, price, quantity);
                break;

            case '2':
                cout << "\nYour Shopping Cart:\n";
                cart.displayCart();
                break;

            case '3':
                cart.saveToFile("shopping_cart.txt");
                break;

            case '4':
                cout << "Exiting the program. Thank you!\n";
                break;

            default:
                cout << "Invalid choice. Please try again.\n";
        }
    } while (choice != '4');

    return 0;
}
