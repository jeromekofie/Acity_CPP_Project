#include "class_hotelpayment.cpp"
#include "class_hotelreservations.cpp"
#include "class_hotelroom.cpp"
#include <fstream>

class FileWriter{

    private:
        Reservation reservation;
        Hotelroom *room;
        Payment payment;

    public: 

        FileWriter(Reservation r, Payment p, Hotelroom *selectedRoom) {
        reservation = r;
        payment = p;
        room = selectedRoom;
    }
        void file(){
            ofstream OutToFile("project.txt");
            OutToFile<< "Your name: "<<reservation.getcustomername()<<endl;
            OutToFile<< "Your reservation is: "<<reservation.getreservationid()<<endl;
            OutToFile<< "Your reserved room number: "<<reservation.getroomnumber()<<endl;
            OutToFile<< "Your check-in date: "<<reservation.getcheckindate()<<endl;
            OutToFile<< "Your check-out date: "<<reservation.getcheckoutdate()<<endl;

            OutToFile << "Your room type: " << room->getroomtype() << endl;
            OutToFile << "Your total price: " << room->getprice() << endl;

           
           OutToFile<< "Your reservation id: "<<payment.getreservationid()<<endl;
           OutToFile<< "Your payment date: "<<payment.getpaymentdate()<<endl;
           OutToFile<< "Your payment method: "<<payment.getpaymentmethod()<<endl;

           OutToFile.close();

        }    

};

