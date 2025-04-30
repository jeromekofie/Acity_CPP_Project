#ifndef STUDENT_H
#define STUDENT_H

#include <string>
using namespace std;

class Student {
private:
    int id;
    string name;

public:
    Student(int id, const string &name): id(id), name(name){};
    int getId() const{
        return id;
    };
    string getName() const{
        return name;
    };
};
#endif
