public class OrderItem {
    private int menuItemId;
    private String name;
    private int quantity;
    private double price;
    
    public OrderItem(int menuItemId, String name, int quantity, double price) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
    
    // Getters and setters
    public int getMenuItemId() { return menuItemId; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getSubtotal() { return quantity * price; }
    
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    @Override
    public String toString() {
        return String.format("%d x %s @ $%.2f = $%.2f", 
            quantity, name, price, getSubtotal());
    }
}