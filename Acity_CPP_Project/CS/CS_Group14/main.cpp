/*while
switch
2 classses
functions
setprecission
/n
boolean
break
variable declaration
setw
*/



#include <iostream>
#include <iomanip>
// #include <fstream>
#include "hotel_menu_class_1.cpp"
#include "hotel_menu_class_2.cpp"

using namespace std;


int main() {
    int choice;
    int quantity;
    float total = 0;
    float grandTotal = 0;
    HotelBill bill;
    DisplayBill dBill;

    cout << "WELCOME!" << endl;

    // Display menu initially
    bill.display_menu();

    while (true) {
        cout << "Please select a choice from the menu (1-4 to order, 5 to exit): ";
        cin >> choice;

        if (choice == 5) {
            cout << "Exiting menu" << endl;
            grandTotal += total;
            dBill.display_bill(grandTotal);
            break; // Exit the loop
        }

        switch (choice) {
            case 1:
                cout << "Enter quantity for pizza ($15.99 for each): ";
                cin >> quantity;
                total += quantity * 15.99;
                cout << "The price for " << quantity << " pizzas is: $" << total << endl;
                break;

            case 2:
                cout << "Enter quantity for salad ($5.50 for each): ";
                cin >> quantity;
                total += quantity * 5.50;
                cout << "The price for " << quantity << " salads is: $" << total << endl;
                break;

            case 3:
                cout << "Enter quantity for burger ($8.99 for each): ";
                cin >> quantity;
                total += quantity * 8.99;
                cout << "The price for " << quantity << " burgers is: $" << total << endl;
                break;

            case 4:
                cout << "Enter quantity for water ($2.99 for each): ";
                cin >> quantity;
                total += quantity * 2.99;
                cout << "The price for " << quantity << " bottles of water is: $" << total << endl;
                break;

            default:
                cout << "Invalid choice. Please select an item from 1 to 4 or select 5 to exit." << endl;
                break;
        }
    }

    return 0;
}