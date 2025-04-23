public class Truck extends Vehicle {
    public Truck(int id, String model, String numberPlate, double ratePerDay) {
        super(id, model, numberPlate, ratePerDay);
    }

    @Override
    public String getType() {
        return "Truck";
    }
}
