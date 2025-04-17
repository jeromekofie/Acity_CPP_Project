// #ifndef DATE_H 
// #define DATE_H 

// #include <string> 
// #include <iostream>

// using namespace std; 

// class Date { 
//     public: 
//         int day; 
//         int month; 
//         int year; 

//         Date (int d, int m, int y) : day(d), month(m), year(y) {}

//         string toString() const {
//             return to_string(day) + "/" + to_string (month) + "/" + to_string(year);
//         }
// }; 

// #endif // for the DATE_H




#ifndef DATE_WITH_GETTERS_SETTERS_H
#define DATE_WITH_GETTERS_SETTERS_H

#include <string>
#include <iostream>

class Date {
public:
    int day;
    int month;
    int year;

    Date(int day, int month, int year) : day(day), month(month), year(year) {}

    // Setters
    void setDay(int newDay) { day = newDay; }
    void setMonth(int newMonth) { month = newMonth; }
    void setYear(int newYear) { year = newYear; }

    // Getters
    int getDay() const { return day; }
    int getMonth() const { return month; }
    int getYear() const { return year; }
};

#endif
