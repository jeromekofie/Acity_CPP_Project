import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private List<OrderItem> items;

    public OrderManager() {
        items = new ArrayList<>();
    }

    public void addItem(String name, double price) {
        items.add(new OrderItem(name, price));
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public double getTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getPrice();
        }
        return total;
    }

    public void clear() {
        items.clear();
    }
}