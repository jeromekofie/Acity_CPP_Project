#include <iostream>
#include <vector>
#include <fstream>
#include <string>

using namespace std;

//Book Class
class Book {
public:
    string title;
    string author;
    bool isAvailable;

    Book( string t, string a ) : title(t), author(a), isAvailable(true) {}

    void displayBook() const {
        cout << "Title: " << title << ", Author: " << author
             << ", Status: " << (isAvailable ? "Available" : "Borrowed") << endl;
    }
};

//Base Member Class
class Member {
protected:
    string memberID;
    string name;
    
public:
    Member(string id, string n) : memberID(id) , name(n) {}

    virtual void displayMember() const {
        cout << "ID: " << memberID <<". Name: "<< name << endl;
    }

    string getMemberID() const { return memberID; }
};

// Derived class for Student Members
class StudentMember : public Member{
public:
   StudentMember(string id, string n) : Member(id , n){}

   void displayMember() const override {
        cout << "[Student] ",
        Member::displayMember();
   }

};

// Derived class for Teacher Members
class TeacherMember : public Member{
public:
    TeacherMember( string id, string n) : Member( id , n){}

    void displayMember() const override {
        cout << "[Teacher]" , 
        Member::displayMember();
    }
};