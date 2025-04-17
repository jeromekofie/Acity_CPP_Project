#include <iostream>
#include <cstdlib>
#include "HotelBill.cpp" // Include the base class

using namespace std;

class AdvancedHotelBill : public HotelBill {
private:
    string customer_name;
    int customer_id;

public:
    AdvancedHotelBill(string name, int id) : HotelBill(), customer_name(name), customer_id(id) {}

    void display_menu() override {
        HotelBill::display_menu();
        cout << "Special Offers: " << endl;
        cout << "6. Random Discount on your total bill!\n";
    }

    float apply_discount() {
        float discount = (rand() % 10) + 5; // Random discount between 5% and 15%
        return total - (total * (discount / 100));
    }

    float calculate_grand_total() {
        const float e_levy = 20;
        const float covid_tax = 10;
        const float vat = 8;
        return total + e_levy + covid_tax + vat;
    }

    string get_customer_name() const {
        return customer_name;
    }

    int get_customer_id() const {
        return customer_id;
    }
};
