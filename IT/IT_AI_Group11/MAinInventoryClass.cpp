#include <iostream>
#include "AdminClass.h"   
#include "CustomerClass.h" 
using namespace std;

int main() {
    Admin admin;       
    Customer customer; 
    int choice;        
    string username, password;

    while (true) {
        
        cout << "\n========== Joojo & Wuni's Stationery Shop ==========" << endl;
        cout << "1. Admin Login" << endl;
        cout << "2. Customer Menu" << endl;
        cout << "3. Exit" << endl;
        cout << "Enter your choice: ";
        cin >> choice;

        switch (choice) {
        case 1: { 
            cout << "\nEnter Admin Username: ";
            cin >> username;
            cout << "Enter Admin Password: ";
            cin >> password;

            if (username == "admin" && password == "admin123") {
                int adminChoice;
                while (true) {
                    
                    cout << "\n========== Admin Menu ==========" << endl;
                    cout << "1. View Inventory" << endl;
                    cout << "2. Update Inventory" << endl;
                    cout << "3. Logout" << endl;
                    cout << "Enter your choice: ";
                    cin >> adminChoice;

                    if (adminChoice == 1) {
                        admin.viewInventory();
                    } else if (adminChoice == 2) {
                        string productName;
                        int quantity;
                        bool isAdding;

                        cout << "\nEnter product name to update: ";
                        cin >> productName;
                        cout << "Enter quantity: ";
                        cin >> quantity;
                        cout << "Are you adding to stock? (1 for Yes, 0 for No): ";
                        cin >> isAdding;

                        admin.updateInventory(productName, quantity, isAdding);
                    } else if (adminChoice == 3) {
                        cout << "Logging out of Admin Menu..." << endl;
                        break;
                    } else {
                        cout << "Invalid choice! Please try again." << endl;
                    }
                }
            } else {
                cout << "Invalid credentials! Access denied." << endl;
            }
            break;
        }
        case 2: { 
            string customerName;
            cout << "\nEnter your name: ";
            cin.ignore();
            getline(cin, customerName);
            customer.setCustomerName(customerName);

            int customerChoice;
            while (true) {
                
                cout << "\nCustomer Menu " << endl;
                cout << "1. View Inventory" << endl;
                cout << "2. Add to Cart" << endl;
                cout << "3. View Cart" << endl;
                cout << "4. Checkout" << endl;
                cout << "5. Exit" << endl;
                cout << "Enter your choice: ";
                cin >> customerChoice;

                if (customerChoice == 1) {
                    admin.viewInventory();
                } else if (customerChoice == 2) {
                    string productName;
                    int quantity;
                    cout << "\nEnter product name to add to cart: ";
                    cin >> productName;
                    cout << "Enter quantity: ";
                    cin >> quantity;

                    Stationery selectedProduct = admin.getProduct(productName);
                    if (selectedProduct.getName() != "" && selectedProduct.getQuantity() >= quantity) {
                        customer.addToCart(selectedProduct, quantity);
                        admin.updateInventory(productName, quantity, false); 
                        cout << "Product added to cart!" << endl;
                    } else {
                        cout << "Product not found or insufficient stock!" << endl;
                    }
                } else if (customerChoice == 3) {
                    customer.viewCart();
                } else if (customerChoice == 4) {
                    customer.checkout();
                } else if (customerChoice == 5) {
                    cout << "Exiting Customer Menu..." << endl;
                    break;
                } else {
                    cout << "Invalid choice! Please try again." << endl;
                }
            }
            break;
        }
        case 3: {
            cout << "Exiting shop. Goodbye!" << endl;
            return 0;
        }
        default: {
            cout << "Invalid choice. Please try again." << endl;
        }
        }
    }

    return 0;
}
