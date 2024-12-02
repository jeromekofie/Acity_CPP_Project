#include <iostream>
#include <fstream>
#include <cmath>
#include "class_hotelfile.cpp"
#include "class_hotelroom.cpp"
#include "class_hotelpayment.cpp"
#include "class_hotelreservations.cpp"

#include <cmath>

void splitDate(const string &date, int &day, int &month, int &year) {
    sscanf(date.c_str(), "%d/%d/%d", &day, &month, &year);
}

int calculateDays(const string &checkindate, const string &checkoutdate) {
    int day1, month1, year1, day2, month2, year2;
    splitDate(checkindate, day1, month1, year1);
    splitDate(checkoutdate, day2, month2, year2);

    int daysInMonth[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    int totalDays1 = year1 * 365 + day1;
    int totalDays2 = year2 * 365 + day2;

    for (int i = 0; i < month1 - 1; ++i) {
        totalDays1 += daysInMonth[i];
    }
    for (int i = 0; i < month2 - 1; ++i) {
        totalDays2 += daysInMonth[i];
    }

    return totalDays2 - totalDays1; 
}


using namespace std;

int main(){

    Hotelroom standardRooms[3];
    Hotelroom deluxeRooms[3];
    Hotelroom executiveRooms[3];

    
    // Standard rooms
    for (int i = 0; i < 3; i++) {
        standardRooms[i].setroomnumber(101 + i);
        standardRooms[i].setroomtype("Standard");
        standardRooms[i].setavailability(true);
        standardRooms[i].setprice(250.0);
    }

    // Deluxe rooms
    for (int i = 0; i < 3; i++) {
        deluxeRooms[i].setroomnumber(201 + i);
        deluxeRooms[i].setroomtype("Deluxe");
        deluxeRooms[i].setavailability(true);
        deluxeRooms[i].setprice(550.0);
    }

    // Executive rooms
    for (int i = 0; i < 3; i++) {
        executiveRooms[i].setroomnumber(301 + i);
        executiveRooms[i].setroomtype("Executive");
        executiveRooms[i].setavailability(true);
        executiveRooms[i].setprice(1000.0);
    }

    
    int roomtypeChoice;
    int roomnumber = -1;
    double price = 0;

    cout << "Choose your room type:" << endl;
    cout << "1. Standard (GHC 250 per night)" << endl;
    cout << "2. Deluxe (GHC 550 per night)" << endl;
    cout << "3. Executive (GHC 1,000 per night)" << endl;
    cout << "Enter your choice (1/2/3): ";
    cin >> roomtypeChoice;

    if (roomtypeChoice == 1) {
        for (int i = 0; i < 3; i++) {
            if (standardRooms[i].getavailability()) {
                roomnumber = standardRooms[i].getroomnumber();
                standardRooms[i].setavailability(false);
                price = standardRooms[i].getprice();
                cout << "Room assigned: " << roomnumber << " at GHC " << price << " per night." << endl;
                break;
            }
        }
        if (roomnumber == -1) {
            cout << "No Standard rooms are available." << endl;
            return 1;
        }
    } else if (roomtypeChoice == 2) {
        for (int i = 0; i < 3; i++) {
            if (deluxeRooms[i].getavailability()) {
                roomnumber = deluxeRooms[i].getroomnumber();
                deluxeRooms[i].setavailability(false);
                price = deluxeRooms[i].getprice();
                cout << "Room assigned: " << roomnumber << " at GHC " << price << " per night." << endl;
                break;
            }
        }
        if (roomnumber == -1) {
            cout << "No Deluxe rooms are available." << endl;
            return 1;
        }
    } else if (roomtypeChoice == 3) {
        for (int i = 0; i < 3; i++) {
            if (executiveRooms[i].getavailability()) {
                roomnumber = executiveRooms[i].getroomnumber();
                executiveRooms[i].setavailability(false);
                price = executiveRooms[i].getprice();
                cout << "Room assigned: " << roomnumber << " at GHC " << price << " per night." << endl;
                break;
            }
        }
        if (roomnumber == -1) {
            cout << "No Executive rooms are available." << endl;
            return 1;
        }
    } else {
        cout << "Invalid room type selected!" << endl;
        return 1;
    }

    Reservation reservation;

        string reservationID = "RES-" + to_string(roomnumber); 
        reservation.setreservationid(reservationID); 
        reservation.setroomnumber(roomnumber);        

        string customername, checkindate, checkoutdate;
        cin.ignore();
        cout << "Enter your name: ";
        getline(cin,customername);
        reservation.setcustomername(customername);

        cout << "Enter your check-in date (DD/MM/YYYY): ";
        cin >> checkindate;
        reservation.setcheckindate(checkindate);

        cout << "Enter your check-out date (DD/MM/YYYY): ";
        cin >> checkoutdate;
        reservation.setcheckoutdate(checkoutdate);

        int days = calculateDays(checkindate, checkoutdate);
        if (days <= 0) {
            cout << "Invalid dates. Check-out date must be after check-in date." << endl;
            return 1;
}

        double totalCost = price * days;

        
        cout << "\nReservation Summary:" << endl;
        cout << "Reservation ID: " << reservation.getreservationid() << endl;
        cout << "Customer Name: " << reservation.getcustomername() << endl;
        cout << "Room Number: " << reservation.getroomnumber() << endl;
        cout << "Check-in Date: " << reservation.getcheckindate() << endl;
        cout << "Check-out Date: " << reservation.getcheckoutdate() << endl;
        cout << "Total number of nights: " << days << endl;  // Display number of nights
        cout << "Total cost for your stay: GHC " << totalCost << endl;

       

    Payment payment;

    payment.setreservationid(roomnumber);  

    

    string paymentdate;
    cout << "Enter payment date (DD/MM/YYYY): ";
    cin >> paymentdate;
    payment.setpaymentdate(paymentdate);

    string paymentMethod;
    cout << "Enter payment method (Cash/Card/Online): ";
    cin >> paymentMethod;
    payment.setpaymentmethod(paymentMethod);
    
     Hotelroom *selectedRoom;
    if (roomtypeChoice == 1) selectedRoom = &standardRooms[0];
    else if (roomtypeChoice == 2) selectedRoom = &deluxeRooms[0];
    else if (roomtypeChoice == 3) selectedRoom = &executiveRooms[0];

    

    return 0;
}

       