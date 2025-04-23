public abstract class Vehicle {
    protected int id;
    protected String model;
    protected String numberPlate;
    protected double ratePerDay;

    public Vehicle(int id, String model, String numberPlate, double ratePerDay) {
        this.id = id;
        this.model = model;
        this.numberPlate = numberPlate;
        this.ratePerDay = ratePerDay;
    }

    public abstract String getType();

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public double getRatePerDay() {
        return ratePerDay;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public void setRatePerDay(double ratePerDay) {
        this.ratePerDay = ratePerDay;
    }
}
