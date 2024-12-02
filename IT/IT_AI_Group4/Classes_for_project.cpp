#include<iostream>


using namespace std;


class Student{
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
            roll_number=r ;
        }
        int getroll_number(){
            return roll_number;
        }



        void setcourse(string c){
            course = c;
        }
        string getcourse(){
            return course;
        }

};




class Grading_requirements{
    private:
    

};

class Compsci{
    private:
        const int MAX_SUBJECTS ;
        float critical_thinking;
        float programming_in_C;
        float Data_structures;
        float logical_reasoning;

    double total_marks;

    public:

        void setcritical_thinking(float c){
            critical_thinking = c;
        }
        float getcritical_thinking(){
            return critical_thinking;
        }

        void setprogramming_in_C(float p){
            programming_in_C = p;
        }
        float getprogramming_in_C(){
            return programming_in_C;
        }

        void setData_structures(float d){
            Data_structures= d;
        }
        float getData_structures(){
            return Data_structures;
        }

        void setlogical_reasoning(float l){
                logical_reasoning = l;
        }
        float getlogical_reasoning(){
            return logical_reasoning;
        }


        double total_marks(double tm){
            double total_marks= critical_thinking + programming_in_C + Data_structures + logical_reasoning;
            return total_marks;
        } 
};



