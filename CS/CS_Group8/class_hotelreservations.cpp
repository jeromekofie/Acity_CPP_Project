#include <string>
using namespace std;
class Reservation : public Payment {

    private:
    string reservationid;
    string customername;
    int roomnumber;
    string checkindate;
    string checkoutdate;

    public:
    void setreservationid(string I){
        reservationid = I;
    }

    string getreservationid(){
        return reservationid;
    }

    void setcustomername(string C){
        customername = C;
    }

    string getcustomername(){
        return customername;
    }

    void setroomnumber(int R){
       roomnumber = R;
    }

    int getroomnumber(){
        return roomnumber;
    }

    void setcheckindate(string D){
        checkindate = D;
    }


    string getcheckindate(){
        return checkindate;
    }

    void setcheckoutdate(string O){
        checkoutdate = O;
    }

    string getcheckoutdate(){
        return checkoutdate;
    }
    
};
