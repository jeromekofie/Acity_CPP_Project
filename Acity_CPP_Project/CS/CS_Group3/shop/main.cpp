#include "cart.cpp" // Include the combined implementation file

int main()
{
    vector<CartItem> cart; // Vector to store cart items
    int choice;            // Variable for menu choice

    do
    {
        // Display menu
        cout << "\n==================================";
        cout << "\n===== Welcome to Ackahmanns =====";
        cout << "\n=================================="<<endl;
        cout << "Enter your choice: "<< endl;
        cout << "1. Add Item\n";
        cout << "2. View Cart\n";
        cout << "3. Calculate Total\n";
        cout << "4. Remove from Cart\n";
        cout << "5. Add to Cart\n";
        cout << "6. Exit\n";
        cin >> choice;

        switch (choice)
        {
        case 1:
        {
            string name;
            double price;
            int quantity;
            cout << "Enter item name: ";
            cin >> name;
            cout << "Enter item price: ";
            cin >> price;
            cout << "Enter item quantity: ";
            cin >> quantity;
            addItemToCart(cart, name, price, quantity);
            break;
        }
        case 2:
            displayCart(cart);
            break;
        case 3:
            calculateTotal(cart);
            break;
        case 4:
        {
            string name;
            cout << "Enter item name to remove: ";
            cin >> name;
            removeItemFromCart(cart, name);
            break;
        }
        case 5:
        {
            string name;
            int quantity;
            cout << "Enter item name to update: ";
            cin >> name;
            cout << "Enter new quantity: ";
            cin >> quantity;
            updateItemQuantity(cart, name, quantity);
            break;
        }
        case 6:
            cout << "Exiting the program. Thank you for using the shopping cart!" << endl;
            break;
        default:
            cout << "Invalid choice! Please try again." << endl;
        }
    } while (choice != 6); // Loop until the user chooses to exit

    return 0;
}