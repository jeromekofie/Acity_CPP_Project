import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private int hotelId;
    private String customerName;
    private Date orderDate;
    private double totalAmount;
    private String status;
    private List<OrderItem> items;
    
    public Order(int id, int hotelId, String customerName, Date orderDate, 
                double totalAmount, String status, List<OrderItem> items) {
        this.id = id;
        this.hotelId = hotelId;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public int getHotelId() { return hotelId; }
    public String getCustomerName() { return customerName; }
    public Date getOrderDate() { return orderDate; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public List<OrderItem> getItems() { return items; }
    
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setStatus(String status) { this.status = status; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    
    @Override
    public String toString() {
        return String.format("Order #%d - %s - Total: $%.2f", id, customerName, totalAmount);
    }
}