package onlineshoppingcart;

import java.sql.*;
import java.util.*;

public class CartManager {
    private Map<Integer, Integer> cart = new HashMap<>();

    public void addToCart(int itemId, int quantity) {
        cart.put(itemId, cart.getOrDefault(itemId, 0) + quantity);
    }

    public void removeFromCart(int itemId) {
        cart.remove(itemId);
    }

    public Map<Integer, Integer> getCart() {
        return cart;
    }

    public List<Item> getItemsFromDatabase() {
        List<Item> items = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM items");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(new Item(rs.getInt("id"), rs.getString("name"), rs.getDouble("price")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public double calculateTotal() {
        double total = 0;
        try {
            Connection conn = DBConnection.getConnection();
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                int itemId = entry.getKey();
                int quantity = entry.getValue();
                PreparedStatement stmt = conn.prepareStatement("SELECT price FROM items WHERE id = ?");
                stmt.setInt(1, itemId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    total += rs.getDouble("price") * quantity;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    void clearCart() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
