#pragma once
#include <string>
using namespace std;
class Payment{

    private:
    int reservationid;
    string paymentdate;
    string paymentmethod;

    public:
    void setreservationid (int S){
        reservationid = S;
    }

    int getreservationid(){
        return reservationid;
    }

    void setpaymentdate(string E){
        paymentdate = E;
    }

    string getpaymentdate(){
        return paymentdate;
    }

    void setpaymentmethod(string H){
        paymentmethod = H;
    }

    string getpaymentmethod(){
        return paymentmethod;
    }
};
