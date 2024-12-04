#include <iostream>
#include <string>
#include "Authentication.h"
#include "Menu.h"
#include "Owner.h"

using namespace std;

// Function for customer menu
void customerMenu(Menu& menu) {
    unordered_map<int, int> order;  // Store the order in an unordered_map (item number, quantity)
    char sectionChoice;
    int itemNumber, quantity;

    cout << "\n=====================================================\n";
    cout << "              Welcome, Valued Customer!              \n";
    cout << "      Thank you for choosing our restaurant!         \n";
    cout << "=====================================================\n";
    cout << "  Select a menu section:\n";
    cout << "    - Press 'S' for Starters\n";
    cout << "    - Press 'M' for Main Course\n";
    cout << "    - Press 'D' for Desserts\n";
    cout << "    - Press 'Q' to finish ordering and view your bill\n";
    cout << "=====================================================\n";

    // Allow the user to select menu sections and add items until they decide to quit
    while (true) {
        cout << "\nChoose a section (S/M/D) or 'Q' to finish ordering: ";
        cin >> sectionChoice;
        sectionChoice = toupper(sectionChoice);

        if (sectionChoice == 'Q') {
            break;  // Exit if the user presses 'Q'
        }

        // Display selected section and handle item selection
        switch (sectionChoice) {
            case 'S':
                cout << "\n--- Starters Menu ---\n";
                menu.displayStarterMenu();
                break;
            case 'M':
                cout << "\n--- Main Course Menu ---\n";
                menu.displayMainCourseMenu();
                break;
            case 'D':
                cout << "\n--- Desserts Menu ---\n";
                menu.displayDessertMenu();
                break;
            default:
                cout << "Invalid section choice. Please try again.\n";
                continue;
        }

        // Allow ordering items within the selected section
        while (true) {
            cout << "\nEnter the item number to add to your order (or '0' to return to sections): ";
            cin >> itemNumber;

            if (itemNumber == 0) {
                break;  // Go back to section menu if user enters 0
            }

            cout << "Enter quantity for item " << itemNumber << ": ";
            cin >> quantity;

            // Add the item to the order or update quantity if already exists
            if (order.find(itemNumber) != order.end()) {
                order[itemNumber] += quantity;
            } else {
                order[itemNumber] = quantity;
            }
        }
    }

    // Show the final combined bill
    menu.calculateBill(order);
}

// Updated function to handle the owner's menu with better structure
void ownerMainMenu(Owner& owner) {
    int choice;

    // Owner menu loop
    do {
        cout << "\n=====================================================\n";
        cout << "                Welcome, Owner!\n";
        cout << "       Manage orders and restaurant information\n";
        cout << "=====================================================\n";
        cout << "  Options:\n";
        cout << "    1. View and Edit Orders\n";
        cout << "    2. Edit Menu\n";  // New option for editing menu
        cout << "    3. Exit\n";
        cout << "=====================================================\n";
        cout << "Select an option: ";
        cin >> choice;

        switch (choice) {
            case 1:
                owner.viewOrderHistory();  // Correct function call to view order history
                break;
            
            case 2:
                owner.editMenu();  // Correct function to allow the owner to edit the menu
                break;
            case 3:
                cout << "Exiting the Owner Menu.\n";
                return;
            default:
                cout << "Invalid option. Please try again.\n";
                break;
        }
    } while (choice != 3);
}

int main() {
    Authentication auth;  // Create an instance of the Authentication class
    Menu menu;  // Create an instance of the Menu class
    Owner owner;  // Create an instance of the Owner class

    string username, password;
    int userType, actionChoice;

    cout << "=====================================================\n";
    cout << "        Welcome to the Hotel Management System       \n";
    cout << "=====================================================\n";

    // Main loop to allow repeated registration and sign-in
    while (true) {
        // User Authentication: Ask if the user is a customer or an owner
        cout << "\nAre you a customer (1) or an owner (2)? ";
        cin >> userType;

        // Validate input
        if (userType != 1 && userType != 2) {
            cout << "Invalid choice. Exiting...\n";
            return 0;
        }

        // Ask the user whether they want to sign in or register
        cout << "Do you want to (1) Sign In or (2) Register? ";
        cin >> actionChoice;

        if (actionChoice == 1) {  // Sign In Flow
            cout << "\n--- Sign In ---\n";
            cout << "Enter your username: ";
            cin >> username;
            cout << "Enter your password: ";
            cin >> password;

            if (userType == 1) {  // Customer Sign In
                if (auth.validateCustomer(username, password)) {
                    customerMenu(menu);  // Show customer menu and process order
                } else {
                    cout << "Invalid username or password. Please try again.\n";
                }
            } else if (userType == 2) {  // Owner Sign In
                if (auth.validateOwner(username, password)) {
                    ownerMainMenu(owner);  // Show owner menu
                } else {
                    cout << "Invalid username or password. Please try again.\n";
                }
            }
        } else if (actionChoice == 2) {  // Register Flow
            cout << "\n--- Register ---\n";
            cout << "Enter your username: ";
            cin >> username;
            cout << "Enter your password: ";
            cin >> password;

            if (userType == 1) {  // Customer Register
                if (auth.registerCustomer(username, password)) {
                    cout << "Customer registration successful. You can now sign in.\n";
                } else {
                    cout << "Registration failed. Username may already exist.\n";
                }
            } else if (userType == 2) {  // Owner Register
                if (auth.registerOwner(username, password)) {
                    cout << "Owner registration successful. You can now sign in.\n";
                } else {
                    cout << "Registration failed. Username may already exist.\n";
                }
            }
        } else {
            cout << "Invalid choice. Please try again.\n";
        }

        // Ask if the user wants to go back to the main menu or exit
        cout << "\nPress 1 to go back to the main menu or any other key to exit: ";
        int continueChoice;
        cin >> continueChoice;
        if (continueChoice != 1) {
            cout << "\nExiting program. Goodbye!\n";
            break;
        }
    }

    return 0;
}