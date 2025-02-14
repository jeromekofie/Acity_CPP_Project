#pragma once
#include <iostream>
using namespace std;

class Product{

private:
    string name;
    int quantity;
    double price;

public:
    Product() : name(""), quantity(0), price(0.0) {
    }
    
    Product(string n, int q, double p) : name(n), quantity(q), price(p) {  
    }
    void setName(string n){
        name = n;
        }
    void setQuantity(int q){
        quantity = q;
        }
    void setPrice(double p){
        price = p;
        }

    string getName(){
        return name;
        }
    int getQuantity(){
        return quantity;
        }
    double getPrice(){
        return price;
        }   

};