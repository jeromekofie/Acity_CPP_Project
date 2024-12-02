#include<iostream>

using namespace std;


class student{
    private:
        string name;
        int roll_number;
        int marks;
        string course;

    public:
        void setname(string n){
            name = n;
        }
        string getname(){
            return name;
        }

        void setroll_number(int r){
            roll_number = r;
        }
        int getroll_number(){
            return roll_number;
        }

        void setmarks(int m){
            marks = m;
        }
        int getmarks(){
            return marks;
        }


        void setcourse(string c){
            course = c;
        }
        string getcourse(){
            return course;
        }

};




class Grading_requirements:{
    private:
    

};



