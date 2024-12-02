#pragma once
#include <iostream>
#include <string>
using namespace std;

class LibraryEntity {
protected:
    string id;
    string name;

public:
    void setID(const string& entityID) { id = entityID; }
    string getID() const { return id; }
    void setName(const string& entityName) { name = entityName; }
    string getName() const { return name; }
    virtual void display() const {
        cout << "ID: " << id << ", Name: " << name << endl;
    }
};
