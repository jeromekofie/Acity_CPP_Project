public class Hotel {
    private int id;
    private String name;
    private String address;
    private String phone;
    private double rating;
    
    public Hotel(int id, String name, String address, String phone, double rating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.rating = rating;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public double getRating() { return rating; }
    
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setRating(double rating) { this.rating = rating; }
    
    @Override
    public String toString() {
        return name + " (" + rating + "★) - " + address;
    }
}