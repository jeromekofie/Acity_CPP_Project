#include <iostream>
#include <vector>
#include <string>

class ShoppingCart {
public:
    void addItem(const std::string& item);  // Overloaded to accept a string
    void addItem();  // Existing method to add an item interactively
    void viewCart() const;
    const std::vector<std::string>& getItems() const;  // Getter for items

private:
    std::vector<std::string> items;
};

void ShoppingCart::addItem(const std::string& item) {
    items.push_back(item);
}

void ShoppingCart::addItem() {
    std::string item;
    std::cout << "Enter item name: ";
    std::cin >> item;
    items.push_back(item);
    std::cout << item << " added to your cart.\n";
}

void ShoppingCart::viewCart() const {
    if (items.empty()) {
        std::cout << "Your cart is empty.\n";
    } else {
        std::cout << "Items in your cart:\n";
        for (const auto& item : items) {
            std::cout << item << "\n";
        }
    }
}

const std::vector<std::string>& ShoppingCart::getItems() const {
    return items;
}
