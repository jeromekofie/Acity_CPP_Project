#include "RentalSystem.cpp"

int main() {
    RentalSystem system;
    system.loadFromFile();

    int choice;
    do {
        cout << "\n=== Vehicle Rental System ===\n";
        cout << "1. Add Vehicle\n2. Display Vehicles\n3. Rent Vehicle\n4. Display Rentals\n5. Exit\n";
        cout << "Enter your choice: ";
        cin >> choice;

        if (choice == 1) {
            system.addVehicle();
        } else if (choice == 2) {
            system.displayVehicles();
        } else if (choice == 3) {
            system.rentVehicle();
        } else if (choice == 4) {
            system.displayRentals();
        } else if (choice == 5) {
            system.saveToFile();
            cout << "Exiting program. Goodbye!" << endl;
        } else {
            cout << "Invalid choice. Please try again." << endl;
        }
    } while (choice != 5);

    return 0;
}
