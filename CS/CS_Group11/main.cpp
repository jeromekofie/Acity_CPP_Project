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

       
    } while (choice != 5);

    return 0;
}
