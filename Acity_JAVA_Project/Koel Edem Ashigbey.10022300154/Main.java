import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        RentalSystem system = new RentalSystem();
        int choice;
        do {
            String menu = """
                === Vehicle Rental System ===
                1. Add Vehicle
                2. Display Vehicles
                3. Delete Vehicle
                4. Add Customer
                5. Display Customers
                6. Edit Customer
                7. Delete Customer
                8. Rent Vehicle
                9. Return Vehicle
                10. Display Rentals
                11. Exit
                Enter your choice (1-11):""";

            try {
                String input = JOptionPane.showInputDialog(null, menu);
                
                if (input == null) {
                    choice = 11; // Treat as exit
                } else {
                    choice = Integer.parseInt(input.trim());
                }

                switch (choice) {
                    case 1:
                        system.addVehicle();
                        break;
                    case 2:
                        system.displayVehicles();
                        break;
                    case 3:
                        system.deleteVehicle();
                        break;
                    case 4:
                        system.addCustomer();
                        break;
                    case 5:
                        system.displayCustomers();
                        break;
                    case 6:
                        system.editCustomer();
                        break;
                    case 7:
                        system.deleteCustomer();
                        break;
                    case 8:
                        system.rentVehicle();
                        break;
                    case 9:
                        system.returnVehicle();
                        break;
                    case 10:
                        system.displayRentals();
                        break;
                    case 11:
                        JOptionPane.showMessageDialog(null, "Exiting program. Goodbye!");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, 
                            "Invalid choice. Please enter a number between 1-11.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, 
                    "Please enter a valid number (1-11).",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
                choice = 0;
            }
        } while (choice != 11);
    }
}