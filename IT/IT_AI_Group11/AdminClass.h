#include <iostream>
#include <iomanip>
#include "StationeryClass.h"
using namespace std;

class Admin {
private:
    Stationery inventory[5]; 
    int inventorySize = 5;

public:
    Admin() {
        inventory[0] = Stationery("Pen", 100, 1.50, "Writing");
        inventory[1] = Stationery("Pencil", 100, 1.00, "Writing");
        inventory[2] = Stationery("Eraser", 100, 0.50, "Correction");
        inventory[3] = Stationery("Ruler", 100, 2.00, "Measurement");
        inventory[4] = Stationery("MathSet", 100, 5.00, "Drawing");
    }

    void viewInventory() {
        cout << "\n============== Stationery Inventory ==============" << endl;
        cout << setw(20) << left << "Product Name" 
             << setw(10) << left << "Quantity" 
             << setw(10) << "Price" 
             << setw(15) << "Category" << endl;
        cout << "--------------------------------------------------" << endl;
        for (int i = 0; i < inventorySize; i++) {
            inventory[i].display();
        }
        cout << "==================================================" << endl;
    }

    void updateInventory(string productName, int quantity, bool isAdding) {
        for (int i = 0; i < inventorySize; i++) {
            if (inventory[i].getName() == productName) {
                int currentQuantity = inventory[i].getQuantity();
                if (isAdding) {
                    inventory[i].setQuantity(currentQuantity + quantity);
                    cout << "Added " << quantity << " units of " << productName << " to stock." << endl;
                } else {
                    if (currentQuantity < quantity) {
                        cout << "Not enough stock for " << productName << "!" << endl;
                        return;
                    }
                    inventory[i].setQuantity(currentQuantity - quantity);
                    cout << "Updated stock for " << productName << "." << endl;
                }
                return;
            }
        }
        cout << "Product not found!" << endl;
    }

    Stationery getProduct(string productName) {
        for (int i = 0; i < inventorySize; i++) {
            if (inventory[i].getName() == productName) {
                return inventory[i];
            }
        }
        return Stationery();
    }
};
