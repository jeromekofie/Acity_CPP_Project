#ifndef DATE_TYPES_H
#define DATE_TYPES_H

#include "date.h"
using namespace std;

class SpecialDate : public Date {
public:
    SpecialDate(int day, int month, int year) : Date(day, month, year) {}

    void display() const {
        cout << "Special Date: " << day << "/" << month << "/" << year << std::endl;
    }
};

#endif
