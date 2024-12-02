#include "ShoppingCart.cpp"  // Include the ShoppingCart class definition directly
#include <iostream>
#include <fstream>
#include <string>

// Save the cart to a file
void saveCart(const ShoppingCart& cart) {
    std::ofstream outFile("cart.txt");  // Open a file for writing
    if (!outFile) {
        std::cout << "Open file for saving: Failed.\n";
        return;
    }

    // Assuming ShoppingCart has a method to retrieve items
    for (const auto& item : cart.getItems()) {
        outFile << item << std::endl;
    }
    std::cout << "Cart saved successfully.\n";
}

void loadCart(ShoppingCart& cart) {
    std::ifstream inputFile("cart.txt"); // Open the file for reading
    if (!inputFile.is_open()) {
        std::cout << "No saved cart found.\n";
        return;
    }

    std::string item;
    while (getline(inFile, item)) {
        cart.addItem(item);  // Assuming ShoppingCart has an addItem method that accepts a string
    }
    std::cout << "Cart loaded successfully.\n";
}
