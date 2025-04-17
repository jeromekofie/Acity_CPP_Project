#pragma once
#include <string> 
using namespace std;
class Hotelroom{
    private:
    int roomnumber;
    string roomtype;
    bool availability;
    double price;

    public:
    void setroomnumber(int N){
        roomnumber = N;
    }

    int getroomnumber(){
        return roomnumber;
    }

    void setroomtype(string T){
        roomtype = T;
    }

    string getroomtype(){
        return roomtype;
    }

    void setavailability(bool A){
        availability = A;
    }

    bool getavailability(){
        return availability;
    }

    void setprice(double P){
        price = P;
    }

    double getprice(){
        return price;
    }

    
};



