#include <iostream>
#include "descisionmaking.cpp"
#include "loops.cpp"
#include "filehandler.cpp"
#include "fuctions.cpp"
#include "classes.cpp"

using namespace std;

int main() {
    ShoppingCart cart;
    int choice;
    int cart;

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
                cart.addItem();
                break;
            case 2:
                cart.viewCart();
                break;
            case 3:
                saveCart(cart);
                break;
            case 4:
                loadCart(cart);
                break;
            case 5:
                cout << "Exiting program. Goodbye!\n";
                break;
            default:
                invalidInput();
        }
    } while (choice != 5);

    return 0;
}
