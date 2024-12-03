#include "ShoppingCart.cpp"  // Include the ShoppingCart class definition directly
#include <iostream>
#include <fstream>
#include <string>

// Save the cart to a file
void saveCart(const ShoppingCart& cart) {
    std::ofstream outFile("cart.txt");  // Open a file for writing
    if (!outFile) {
        std::cout << "Failed to open file for saving.\n";
        return;
    }

    // Assuming ShoppingCart has a method to retrieve items
    for (const auto& item : cart.getItems()) {
        outFile << item << std::endl;
    }
    std::cout << "Cart saved successfully.\n";
}

// Load the cart from a file
void loadCart(ShoppingCart& cart) {
    std::ifstream inFile("cart.txt");  // Open a file for reading
    if (!inFile) {
        std::cout << "No saved cart found.\n";
        return;
    }
}
