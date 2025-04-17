//C++ project group 18 :SIMPLE HOTEL BOOKING SYSTEM
//InstaStay: Emphasizes quick and efficient bookings

#include <iostream>
#include <fstream>
#include <vector>
#include <string>

using namespace std;

// BaseEntity Class
class BaseEntity {
protected:
    int id;

public:
    BaseEntity(int id) : id(id) {}
    int getId() const { return id; }
    void setId(int id) { this->id = id; }
};

// Person Class
class Person {
protected:
    string name;
    char gender;
    string contact;

public:
    Person(string n, char g, string c) : name(n), gender(g), contact(c) {}

    string getName() const { return name; }
    void setName(const string &n) { name = n; }

    char getGender() const { return gender; }
    void setGender(char g) { gender = g; }

    string getContact() const { return contact; }
    void setContact(const string &c) { contact = c; }
};

// Room Class inherits from BaseEntity
class Room : public BaseEntity {
private:
    bool isBooked;

public:
    Room(int number) : BaseEntity(number), isBooked(false) {}

    bool getIsBooked() const { return isBooked; }
    void setIsBooked(bool booked) { isBooked = booked; }
};

// Customer Class inherits from Person
class Customer : public Person {
private:
    int roomNumber;
    string checkInDate;
    string checkOutDate;

public:
    Customer(string n, char g, string c, int rn, string ci, string co)
        : Person(n, g, c), roomNumber(rn), checkInDate(ci), checkOutDate(co) {}

    int getRoomNumber() const { return roomNumber; }
    void setRoomNumber(int rn) { roomNumber = rn; }

    string getCheckInDate() const { return checkInDate; }
    void setCheckInDate(const string &ci) { checkInDate = ci; }

    string getCheckOutDate() const { return checkOutDate; }
    void setCheckOutDate(const string &co) { checkOutDate = co; }
};

// Hotel Class
class Hotel {
public:
    vector<Room> rooms;
    vector<Customer> customers;

    Hotel() {
        // Initializing some rooms
        for (int i = 1; i <= 20; ++i) {
            rooms.push_back(Room(i));
        }
    }

    void addReservation() {
        string name, contact, checkInDate, checkOutDate;
        int roomNumber;
        char gender;

        cout << "Enter customer name: ";
        cin >> name;
        cout << "Enter Gender (M/F): ";
        cin >> gender;
        cout << "Enter customer contact: ";
        cin >> contact;

        // Input and validate room number
        cout << "Enter room number (1-20): ";
        cin >> roomNumber;
        if (roomNumber < 1 || roomNumber > rooms.size()) {
            cout << "Invalid room number! Please try again." << endl;
            return;
        }

        cout << "Enter check-in date (YYYY-MM-DD): ";
        cin >> checkInDate;
        cout << "Enter check-out date (YYYY-MM-DD): ";
        cin >> checkOutDate;

        if (rooms[roomNumber - 1].getIsBooked()) {
            cout << "Room " << roomNumber << " is already booked!" << endl;
        } else {
            rooms[roomNumber - 1].setIsBooked(true);
            Customer newCustomer(name, gender, contact, roomNumber, checkInDate, checkOutDate);
            customers.push_back(newCustomer);
            saveToFile();
            cout << "Reservation added successfully!" << endl;
        }
    }

    void viewReservations() {
        for (const auto& customer : customers) {
            cout << "\nCustomer: " << customer.getName() << "\nRoom: " << customer.getRoomNumber()
                 << "\nGender: " << customer.getGender() << "\nContact: " << customer.getContact()
                 << "\nCheck-in: " << customer.getCheckInDate()
                 << "\nCheck-out: " << customer.getCheckOutDate() << endl;
        }
    }

    void saveToFile() {
        ofstream outFile("reservations.txt");
        for (const auto& customer : customers) {
            outFile << "Name: " << customer.getName() << endl;
            outFile << "Gender: " << customer.getGender() << endl;
            outFile << "Contact: " << customer.getContact() << endl;
            outFile << "Room Number: " << customer.getRoomNumber() << endl;
            outFile << "Check-in date: " << customer.getCheckInDate() << endl;
            outFile << "Check-out date: " << customer.getCheckOutDate() << endl;
        }
        outFile.close();
        cout << "Data successfully written to reservations.txt" << endl;
    }

    void loadFromFile() {
        ifstream inFile("reservations.txt");
        char gender;
        string name, contact, checkInDate, checkOutDate;
        int roomNumber;

        while (inFile >> name >> gender >> contact >> roomNumber >> checkInDate >> checkOutDate) {
            Customer customer(name, gender, contact, roomNumber, checkInDate, checkOutDate);
            customers.push_back(customer);
            rooms[roomNumber - 1].setIsBooked(true);
        }
        inFile.close();
    }
};

int main() {
    Hotel hotel;
    hotel.loadFromFile();
    int choice;

    while (true) {
        cout << "**Welcome to InstaStay the Hotel Booking System**" << endl;
        cout << "1. Add Reservation" << endl;
        cout << "2. View Reservations" << endl;
        cout << "3. Exit" << endl;
        cout << "Enter choice: ";
        cin >> choice;

        switch (choice) {
        case 1:
            hotel.addReservation();
            break;
        case 2:
            hotel.viewReservations();
            break;
        case 3:
            cout << "Thanks for using InstaStay the Hotel Booking System!" << endl;
            return 0;
        default:
            cout << "Invalid choice, please try again." << endl;
        }
    }
}
