public class Bike extends Vehicle {
    public Bike(int id, String model, String numberPlate, double ratePerDay) {
        super(id, model, numberPlate, ratePerDay);
    }

    @Override
    public String getType() {
        return "Bike";
    }
}
