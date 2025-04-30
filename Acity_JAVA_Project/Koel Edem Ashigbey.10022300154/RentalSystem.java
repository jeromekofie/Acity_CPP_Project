import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class RentalSystem {
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Rental> rentals = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();

    public RentalSystem() {
        DBConnection.initializeDatabase();
        loadFromDatabase();
    }
    
    public void addVehicle() {
        String id = JOptionPane.showInputDialog("Enter vehicle ID:");
        if (id == null || id.trim().isEmpty()) return;

        String brand = JOptionPane.showInputDialog("Enter brand:");
        if (brand == null || brand.trim().isEmpty()) return;

        String model = JOptionPane.showInputDialog("Enter model:");
        if (model == null || model.trim().isEmpty()) return;

        double rate = 0;
        while (true) {
            try {
                String rateInput = JOptionPane.showInputDialog("Enter daily rate:");
                if (rateInput == null) return;
                rate = Double.parseDouble(rateInput);
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid number. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        String color = JOptionPane.showInputDialog("Enter color:");
        if (color == null || color.trim().isEmpty()) return;

        int year = 0;
        while (true) {
            try {
                String yearInput = JOptionPane.showInputDialog("Enter year:");
                if (yearInput == null) return;
                year = Integer.parseInt(yearInput);
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid year. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        int doors = 0;
        while (true) {
            try {
                String doorsInput = JOptionPane.showInputDialog("Enter number of doors:");
                if (doorsInput == null) return;
                doors = Integer.parseInt(doorsInput);
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid number. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        String transmission = JOptionPane.showInputDialog("Enter transmission type (Automatic/Manual):");
        if (transmission == null || transmission.trim().isEmpty()) return;

        String fuelType = JOptionPane.showInputDialog("Enter fuel type:");
        if (fuelType == null || fuelType.trim().isEmpty()) return;

        Vehicle vehicle = new Car(id, brand, model, rate, color, year, doors, transmission, fuelType);
        vehicles.add(vehicle);
        vehicle.saveToDatabase();
        JOptionPane.showMessageDialog(null, "Vehicle added successfully!");
    }
    
    public void displayVehicles() {
        if (vehicles.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No vehicles available.");
            return;
        }

        StringBuilder sb = new StringBuilder("--- Available Vehicles ---\n");
        for (Vehicle vehicle : vehicles) {
            sb.append(vehicle.toString()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }
    
    public void deleteVehicle() {
        if (vehicles.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No vehicles available to delete.");
            return;
        }
        
        StringBuilder sb = new StringBuilder("Select vehicle to delete:\n");
        for (int i = 0; i < vehicles.size(); i++) {
            sb.append((i+1)).append(". ").append(vehicles.get(i).toString()).append("\n");
        }
        
        int vehicleIndex = -1;
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(sb.toString());
                if (input == null) return;
                vehicleIndex = Integer.parseInt(input.trim()) - 1;
                if (vehicleIndex >= 0 && vehicleIndex < vehicles.size()) {
                    break;
                }
                JOptionPane.showMessageDialog(null, "Invalid selection. Please try again.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.");
            }
        }
        
        Vehicle vehicle = vehicles.get(vehicleIndex);
        
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to delete this vehicle?\n" + vehicle.toString(),
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (Vehicle.deleteFromDatabase(vehicle.getId())) {
                vehicles.remove(vehicleIndex);
                JOptionPane.showMessageDialog(null, "Vehicle deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete vehicle.");
            }
        }
    }
    
    public void addCustomer() {
        String id = JOptionPane.showInputDialog("Enter customer ID:");
        if (id == null || id.trim().isEmpty()) return;

        String name = JOptionPane.showInputDialog("Enter customer name:");
        if (name == null || name.trim().isEmpty()) return;

        String license = JOptionPane.showInputDialog("Enter license number:");
        if (license == null || license.trim().isEmpty()) return;

        String phone = JOptionPane.showInputDialog("Enter phone number:");
        String email = JOptionPane.showInputDialog("Enter email address:");

        // Check if customer ID already exists
        if (Customer.findById(id) != null) {
            JOptionPane.showMessageDialog(null, "Customer ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer customer = new Customer(id, name, license, phone, email);
        customers.add(customer);
        customer.saveToDatabase();
        JOptionPane.showMessageDialog(null, "Customer added successfully!");
    }
    
    public void displayCustomers() {
        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No customers available.");
            return;
        }

        StringBuilder sb = new StringBuilder("--- Customers ---\n");
        for (Customer customer : customers) {
            sb.append(customer.toString()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }
    
    public void editCustomer() {
        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No customers available to edit.");
            return;
        }
        
        int index = selectCustomer();
        if (index == -1) return;
        
        Customer customer = customers.get(index);
        
        String name = JOptionPane.showInputDialog("Enter new name:", customer.getName());
        if (name == null || name.trim().isEmpty()) return;
        
        String phone = JOptionPane.showInputDialog("Enter new phone:", customer.getPhone());
        String email = JOptionPane.showInputDialog("Enter new email:", customer.getEmail());
        
        customer.setName(name);
        customer.setPhone(phone);
        customer.setEmail(email);
        customer.saveToDatabase();
        
        JOptionPane.showMessageDialog(null, "Customer updated successfully!");
    }
    
    public void deleteCustomer() {
        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No customers available to delete.");
            return;
        }
        
        int index = selectCustomer();
        if (index == -1) return;
        
        Customer customer = customers.get(index);
        
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to delete this customer?\n" + customer.toString(),
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (Customer.deleteFromDatabase(customer.getId())) {
                customers.remove(index);
                JOptionPane.showMessageDialog(null, "Customer deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete customer.");
            }
        }
    }
    
    private int selectCustomer() {
        StringBuilder sb = new StringBuilder("Select customer:\n");
        for (int i = 0; i < customers.size(); i++) {
            sb.append((i+1)).append(". ").append(customers.get(i).toString()).append("\n");
        }
        
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(sb.toString());
                if (input == null) return -1;
                int index = Integer.parseInt(input.trim()) - 1;
                if (index >= 0 && index < customers.size()) {
                    return index;
                }
                JOptionPane.showMessageDialog(null, "Invalid selection. Please try again.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.");
            }
        }
    }
    
    public void rentVehicle() {
        if (vehicles.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No vehicles available for rent.");
            return;
        }
        
        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No customers available. Please add customers first.");
            return;
        }

        // Show available vehicles
        StringBuilder vehicleSb = new StringBuilder("Available Vehicles:\n");
        int availableCount = 0;
        for (int i = 0; i < vehicles.size(); i++) {
            if (!vehicles.get(i).getIsRented()) {
                vehicleSb.append((i+1)).append(". ").append(vehicles.get(i).toString()).append("\n");
                availableCount++;
            }
        }

        if (availableCount == 0) {
            JOptionPane.showMessageDialog(null, "All vehicles are currently rented.");
            return;
        }

        // Select vehicle
        int vehicleIndex = -1;
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(vehicleSb.toString() + "\nSelect vehicle number:");
                if (input == null) return;
                vehicleIndex = Integer.parseInt(input.trim()) - 1;
                if (vehicleIndex >= 0 && vehicleIndex < vehicles.size() && !vehicles.get(vehicleIndex).getIsRented()) {
                    break;
                }
                JOptionPane.showMessageDialog(null, "Invalid selection. Please try again.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.");
            }
        }

        // Select customer
        int customerIndex = selectCustomer();
        if (customerIndex == -1) return;

        Vehicle selectedVehicle = vehicles.get(vehicleIndex);
        Customer selectedCustomer = customers.get(customerIndex);
        
        Rental rental = new Rental(selectedCustomer.getId(), selectedVehicle.getId(), new Date());
        rentals.add(rental);
        selectedVehicle.setIsRented(true);
        
        rental.saveToDatabase();
        JOptionPane.showMessageDialog(null, "Vehicle rented successfully to " + selectedCustomer.getName() + "!");
    }
    
    public void returnVehicle() {
        if (rentals.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No active rentals to return.");
            return;
        }

        List<Rental> activeRentals = new ArrayList<>();
        StringBuilder sb = new StringBuilder("Active Rentals:\n");
        for (int i = 0; i < rentals.size(); i++) {
            if (rentals.get(i).getEndDate() == null) {
                sb.append((i+1)).append(". ").append(rentals.get(i).toString()).append("\n");
                activeRentals.add(rentals.get(i));
            }
        }

        if (activeRentals.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No active rentals to return.");
            return;
        }

        int rentalIndex = -1;
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(sb.toString() + "\nSelect rental to return:");
                if (input == null) return;
                rentalIndex = Integer.parseInt(input.trim()) - 1;
                if (rentalIndex >= 0 && rentalIndex < activeRentals.size()) {
                    break;
                }
                JOptionPane.showMessageDialog(null, "Invalid selection. Please try again.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.");
            }
        }

        Rental rental = activeRentals.get(rentalIndex);
        rental.returnVehicle();
        
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId().equals(rental.getVehicleId())) {
                vehicle.setIsRented(false);
                break;
            }
        }
        
        JOptionPane.showMessageDialog(null, 
            String.format("Vehicle returned successfully!\nTotal cost: $%.2f", rental.getTotalCost()));
    }
    
    public void displayRentals() {
        if (rentals.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No rentals found.");
            return;
        }

        StringBuilder sb = new StringBuilder("--- All Rentals ---\n");
        for (Rental rental : rentals) {
            sb.append(rental.toString()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }
    
    public void loadFromDatabase() {
        vehicles = Vehicle.loadAllFromDatabase();
        rentals = Rental.loadAllFromDatabase();
        customers = Customer.loadAllFromDatabase();
        
        // Update vehicle rental status based on active rentals
        for (Rental rental : rentals) {
            if (rental.getEndDate() == null) {
                for (Vehicle vehicle : vehicles) {
                    if (vehicle.getId().equals(rental.getVehicleId())) {
                        vehicle.setIsRented(true);
                        break;
                    }
                }
            }
        }
    }
}