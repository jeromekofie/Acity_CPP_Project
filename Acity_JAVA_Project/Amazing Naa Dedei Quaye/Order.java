public class Order {
    public String orderId;
    public String customerName;
    public String items;
    public String total;

    public Order(String orderId, String customerName, String items, String total) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = items;
        this.total = total;
    }

    // Getter and setter methods
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    // Converts the Order to a string for file saving (with a pipe delimiter)
    public String toFileString() {
        return orderId + "|" + customerName + "|" + items + "|" + total;
    }

    // Converts a string from file back into an Order object
    public static Order fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length == 4) {
            return new Order(parts[0], parts[1], parts[2], parts[3]);
        }
        return null;
    }

    @Override
    public String toString() {
        return customerName + " - " + items + " - " + total;
    }
}
