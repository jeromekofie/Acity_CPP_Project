#include <iostream>
#include <fstream>
#include <iomanip>
#include <vector>
#include "AdvancedHotelBill.cpp" // Include the derived class

using namespace std;

class FileManager {
public:
    void save_customer_data(const AdvancedHotelBill& customer, float grandTotal) {
        string filename = "Customer_" + to_string(customer.get_customer_id()) + ".txt";
        ofstream customerFile(filename);

        if (customerFile.is_open()) {
            customerFile << "Hotel DIOR - Receipt\n";
            customerFile << "====================\n";
            customerFile << "Customer Name: " << customer.get_customer_name() << endl;
            customerFile << "Customer ID: " << customer.get_customer_id() << endl;
            customerFile << "\nPurchased Items:\n";

            // Access items using the getter function
            for (const auto& item : customer.get_items()) {
                customerFile << item.first << " - $" << fixed << setprecision(2) << item.second << endl;
            }
            customerFile << "-------------------\n";
            customerFile << "Subtotal: $" << fixed << setprecision(2) << customer.get_total() << endl;
            customerFile << "E-levy: $20" << endl;
            customerFile << "Covid Tax: $10" << endl;
            customerFile << "VAT: $8" << endl;
            customerFile << "====================\n";
            customerFile << "Grand Total: $" << fixed << setprecision(2) << grandTotal << endl;
            customerFile.close();
            cout << "Data saved to file: " << filename << endl;
        } else {
            cout << "Error: Unable to save customer data to file." << endl;
        }
    }

    void view_customer_data(int customer_id) {
        string filename = "Customer_" + to_string(customer_id) + ".txt";
        ifstream customerFile(filename);

        if (customerFile.is_open()) {
            cout << "Data for Customer ID " << customer_id << ":" << endl;
            string line;
            while (getline(customerFile, line)) {
                cout << line << endl;
            }
            customerFile.close();
            // Create a separate file for requested data
            string outputFile = "Specific_Customer_" + to_string(customer_id) + ".txt";
            ofstream outFile(outputFile);
            ifstream inputFile(filename);
            outFile << inputFile.rdbuf();
            outFile.close();
            cout << "Specific customer data saved to: " << outputFile << endl;
        } else {
            cout << "Error: No data found for Customer ID " << customer_id << endl;
        }
    }

    void view_all_customers(const vector<AdvancedHotelBill>& customers) {
        if (customers.empty()) {
            cout << "No customers found." << endl;
            return;
        }

        cout << "All Customers Data:" << endl;
        for (const auto& customer : customers) {
            view_customer_data(customer.get_customer_id());
        }
    }
};

int main() {
    vector<AdvancedHotelBill> customers; // Store all customers
    FileManager fileManager;

    while (true) {
        int menuChoice;
        cout << "\nMenu:\n";
        cout << "1. Add new customer and process their order\n";
        cout << "2. View specific customer data\n";
        cout << "3. View all customers data\n";
        cout << "4. Exit\n";
        cout << "Enter your choice: ";
        cin >> menuChoice;

        if (menuChoice == 4) {
            cout << "Exiting application. Goodbye!" << endl;
            break;
        }

        switch (menuChoice) {
            case 1: {
                string name;
                int id, choice, quantity;

                cout << "Enter customer name: ";
                cin.ignore();
                getline(cin, name);
                cout << "Enter customer ID: ";
                cin >> id;

                AdvancedHotelBill customer(name, id);
                customer.display_menu();

                while (true) {
                    cout << "Please select a choice from the menu (1-6 to order, 5 to exit): ";
                    cin >> choice;

                    if (choice == 5) {
                        cout << "Exiting menu for this customer..." << endl;
                        float grandTotal = customer.calculate_grand_total();
                        fileManager.save_customer_data(customer, grandTotal);
                        customers.push_back(customer); // Add customer to the list
                        break;
                    }

                    switch (choice) {
                        case 1:
                            cout << "Enter quantity for pizza ($15.99 for each): ";
                            cin >> quantity;
                            customer.add_item("Pizza", customer.calculate_total(15.99, quantity));
                            break;

                        case 2:
                            cout << "Enter quantity for salad ($5.50 for each): ";
                            cin >> quantity;
                            customer.add_item("Salad", customer.calculate_total(5.50, quantity));
                            break;

                        case 3:
                            cout << "Enter quantity for burger ($8.99 for each): ";
                            cin >> quantity;
                            customer.add_item("Burger", customer.calculate_total(8.99, quantity));
                            break;

                        case 4:
                            cout << "Enter quantity for water ($2.99 for each): ";
                            cin >> quantity;
                            customer.add_item("Water", customer.calculate_total(2.99, quantity));
                            break;

                        case 6:
                            customer.add_item("Random Discount", customer.apply_discount());
                            cout << "A random discount has been applied! New total: $" << customer.get_total() << endl;
                            break;

                        default:
                            cout << "Invalid choice. Please select a valid option." << endl;
                            break;
                    }
                }
                break;
            }

            case 2: {
                int search_id;
                cout << "Enter Customer ID to view their data: ";
                cin >> search_id;
                fileManager.view_customer_data(search_id);
                break;
            }

            case 3: {
                fileManager.view_all_customers(customers);
                break;
            }

            default:
                cout << "Invalid choice. Please select a valid option." << endl;
                break;
        }
    }

    return 0;
}
