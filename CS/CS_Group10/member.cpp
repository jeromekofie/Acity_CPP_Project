#pragma once
#include "LibraryEntity.cpp"

//derived class for library members...adds more details about members 
class Member : public LibraryEntity {
public:
    void display() const override {
        cout << "Member - ";
        LibraryEntity::display();
    }
};
