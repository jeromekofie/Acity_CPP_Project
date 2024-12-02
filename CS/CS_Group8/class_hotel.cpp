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


class Reservation{

    private:
    int reservationid;
    string customername;
    int roomnumber;
    string checkindate;
    string checkoutdate;

    public:
    void setreservationid(int I){
        reservationid = I;
    }

    int getreservationid(){
        return reservationid;
    }

    void setcustomername(string C){
        customername = C;
    }

    string getcustomername(){
        return customername;
    }

    void setroomnumber(string R){
        customername = R;
    }

    string getroomnumber(){
        return roomnumber;
    }

    void checkindate(string D){
        checkindate = D;
    }

    string getcheckindate(){
        return checkindate;
    }

    void setcheckoutdate(string O){
        checkoutdate = O;
    }

    string getoutcheckoutdate(){
        return checkoutdate;
    }
    
};

class Payment{

    private:
    int reservationid;
    double amount;
    string paymentdate;
    string paymentmethod;

    public:
    void setreservationid (int S){
        reservationid = S;
    }

    int getreservationid(){
        return reservationid;
    }

    void setamount (double M){
        amount = M;
    }

    string getamount(){
        return amount;
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