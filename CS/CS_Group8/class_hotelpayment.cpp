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

    double getamount(){
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
