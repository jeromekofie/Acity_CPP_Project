public class Car extends Vehicle {
    public Car(int id, String model, String numberPlate, double ratePerDay) {
        super(id, model, numberPlate, ratePerDay);
    }

    @Override
    public String getType() {
        return "Car";
    }
}
