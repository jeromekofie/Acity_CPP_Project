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
    for (const auto& item : cart.getItems()) {  // Ensure getItems() returns a collection of strings or objects with a valid stream operator
        outFile << item << std::endl;
    }
    std::cout << "Cart saved successfully.\n";
}

// Load the cart from a file
void loadCart(ShoppingCart& cart) {
    std::ifstream inputFile("cart.txt"); // Open the file for reading
    if (!inputFile.is_open()) {
        std::cout << "No saved cart found.\n";
        return;
    }

    std::string item;
    while (std::getline(inputFile, item)) {  // Corrected variable name from 'inFile' to 'inputFile'
        cart.addItem(item);  // Assuming ShoppingCart has an addItem method that accepts a string
    }
    std::cout << "Cart loaded successfully.\n";
}
