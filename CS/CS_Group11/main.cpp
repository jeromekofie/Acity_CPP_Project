#include <iostream>
#include "ShoppingCart.cpp"  // Include the ShoppingCart class definition
#include "FileHandler.cpp"  // Include the file handling functions
#include "DecisionMaking.cpp"  // Other modules you may have
#include "Loops.cpp"  // Other modules you may have
#include "Functions.cpp"  // Other modules you may have

using namespace std;

int main() {
    ShoppingCart cart;  // Shopping cart object
    int choice;

    do {
        cout << "\n===== Online Shopping Cart =====\n";
        cout << "1. Add Item\n";
        cout << "2. View Cart\n";
        cout << "3. Save Cart\n";
        cout << "4. Load Cart\n";
        cout << "5. Exit\n";
        cout << "Enter your choice: ";
        cin >> choice;

        switch (choice) {
            case 1:
                cart.addItem();  // Add an item to the cart
                break;
            case 2:
                cart.viewCart();  // View the items in the cart
                break;
            case 3:
                saveCart(cart);  // Save the cart to a file
                break;
            case 4:
            loadCart(cart);  // Load the cart from a file
                break;
            case 5:
                cout << "Exiting program. Goodbye!\n";
                break;
            default:
                invalidInput();  // Handle invalid input
        }
    } while (choice != 5);

    return 0;
}