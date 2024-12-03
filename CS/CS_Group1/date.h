#ifndef DATE_H 
#define DATE_H 

#include <string> 
#include <iostream>

using namespace std; 

class Date { 
    public: 
        int day; 
        int month; 
        int year; 

        Date (int d, int m, int y) : day(d), month(m), year(y) {}

        string toString() const {
            return to_string(day) + "/" + to_string (month) + "/" + to_string(year);
        }
}; 

#endif // for the DATE_H