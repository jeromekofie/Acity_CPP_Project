public class MenuItem {
    private int id;
    private int hotelId;
    private String name;
    private String description;
    private double price;
    private String category;
    
    public MenuItem(int id, int hotelId, String name, String description, double price, String category) {
        this.id = id;
        this.hotelId = hotelId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public int getHotelId() { return hotelId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
    
    @Override
    public String toString() {
        return String.format("%s - $%.2f (%s)", name, price, category);
    }
}