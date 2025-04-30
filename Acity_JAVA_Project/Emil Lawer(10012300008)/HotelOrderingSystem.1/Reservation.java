import java.sql.Date;

public class Reservation {
    private int id;
    private int hotelId;
    private String customerName;
    private Date checkInDate;
    private Date checkOutDate;
    private String roomType;
    private String status;
    
    public Reservation(int id, int hotelId, String customerName, Date checkInDate, 
                      Date checkOutDate, String roomType, String status) {
        this.id = id;
        this.hotelId = hotelId;
        this.customerName = customerName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomType = roomType;
        this.status = status;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public int getHotelId() { return hotelId; }
    public String getCustomerName() { return customerName; }
    public Date getCheckInDate() { return checkInDate; }
    public Date getCheckOutDate() { return checkOutDate; }
    public String getRoomType() { return roomType; }
    public String getStatus() { return status; }
    
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setCheckInDate(Date checkInDate) { this.checkInDate = checkInDate; }
    public void setCheckOutDate(Date checkOutDate) { this.checkOutDate = checkOutDate; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return String.format("Reservation #%d - %s (%s to %s)", 
            id, customerName, checkInDate, checkOutDate);
    }
}