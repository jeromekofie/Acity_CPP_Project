#pragma once
#include <iostream>
#include <iomanip>
#include "ProductClass.h"
using namespace std;

class Stationery : public Product {
private:
    string category;

public:
    Stationery() : Product(), category("") {

    }
    Stationery(string n, int q, double p, string c) : Product(n, q, p), category(c) {}

    string getCategory() {
         return category; 
         }
    void setCategory(string c) { 
        category = c; 
        }

    void display() {
        cout << setw(20) << left << getName()
             << setw(10) << left << getQuantity()
             << "$" << setw(10) << left << fixed << setprecision(2) << getPrice()
             << setw(15) << left << category << endl;
    }
};
