#include <iostream>

// Include all other .cpp files directly
#include "DecisionMaking.cpp"
#include "Loops.cpp"
#include "FileHandler.cpp"
#include "Functions.cpp"
#include "Classes.cpp"

using namespace std;

int main() {
    ShoppingCart cart; // Make cart non-const to allow modifications
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
                cart.addItem(); // Modify cart
                break;
            case 2:
                cart.viewCart(); // View cart
                break;
            case 3:
                saveCart(cart); // Save cart
                break;
            case 4:
                loadCart(cart); // Load cart
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
