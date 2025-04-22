import java.sql.Date;

public class RentalRecord {
    private int id;
    private int vehicleId;
    private String customerName;
    private Date rentDate;
    private Date returnDate;
    private double totalAmount;

    public RentalRecord(int id, int vehicleId, String customerName, Date rentDate, Date returnDate, double totalAmount) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.customerName = customerName;
        this.rentDate = rentDate;
        this.returnDate = returnDate;
        this.totalAmount = totalAmount;
    }

    public int getId() {
        return id;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Date getRentDate() {
        return rentDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
