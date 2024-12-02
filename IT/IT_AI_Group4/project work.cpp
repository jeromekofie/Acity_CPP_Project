// create a student report generator 
#include<iostream>
#include<iomanip>
#include<fstream>
#include"Classes_for_project.cpp"
using namespace std;


// 2. Student Report Card Generator
// Store and calculate grades for each student.
// Use text files to save scores and generate report cards.
// Implement polymorphism for different grading policies (e.g., Pass/Fail vs. Letter Grades).
int main(){
    string name;
    int roll_number;
    int marks;
    int course;
    string CS;
    string IT ;

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

class CompSci {
private:
    float critical_thinking;
    float programming_in_C;
    float data_structures;
    float logical_reasoning;
    float Total_marks;
public:
    void setcritical_thinking(float ct){
        critical_thinking = ct;
    }    
    
    void setprogramming_in_C(float pc){
        programming_in_C = pc;
    }
    void setdata_stuctures(float ds){
        data_structures = ds;
    }
     void setlogical_reasoning(float lr){
        logical_reasoning = lr;
    }
    
    float getTotalMarks() {
        Total_marks = critical_thinking + programming_in_C + data_structures + logical_reasoning;
        return Total_marks;
    }

    void displayMarks(ofstream &file, GradingPolicy *gp) {
        file << "Critical Thinking      : " << setw(3) << critical_thinking << " | Grade: " << gp->calculateGrade(critical_thinking) << endl;
        file << "Programming in C       : " << setw(3) << programming_in_C << " | Grade: " << gp->calculateGrade(programming_in_C) << endl;
        file << "Data Structures        : " << setw(3) << data_structures << " | Grade: " << gp->calculateGrade(data_structures) << endl;
        file << "Logical Reasoning      : " << setw(3) << logical_reasoning << " | Grade: " << gp->calculateGrade(logical_reasoning) << endl;
        file << "Total Marks            : " << setw(3) << getTotalMarks() << endl;
    }
};

// IT Class
class IT {
private:
    float networking;
    float cybersecurity;
    float web_development;
    float database_management;

public:
    void setMarks(float nw, float cs, float wd, float dm) {
        networking = nw;
        cybersecurity = cs;
        web_development = wd;
        database_management = dm;
    }

    float getTotalMarks() {
        return networking + cybersecurity + web_development + database_management;
    }

    void displayMarks(ofstream &file, GradingPolicy *gp) {
        file << "Networking             : " << setw(3) << networking 
             << " | Grade: " << gp->calculateGrade(networking) << endl;
        file << "Cybersecurity          : " << setw(3) << cybersecurity 
             << " | Grade: " << gp->calculateGrade(cybersecurity) << endl;
        file << "Web Development        : " << setw(3) << web_development 
             << " | Grade: " << gp->calculateGrade(web_development) << endl;
        file << "Database Management    : " << setw(3) << database_management 
             << " | Grade: " << gp->calculateGrade(database_management) << endl;
        file << "Total Marks            : " << setw(3) << getTotalMarks() << endl;
    }
};

    Student s;
    Compsci c;
    cout<<"REPORT CARD GENERATOR"<<endl;

    cout<<"Enter the students name : "<<endl;
    cin>>name;
   
    
    cout<<"Enter the roll_number : "<<endl;
    cin>>roll_number;
    
    cout<<"Enter the course : "<<endl;

    cout<<"COURSES"<<endl;
    cout<<"----------------------"<<endl;
    cout<<"1. CS"<<endl;    
    cout<<"2. IT"<<endl;
    
    
    cin>>course;
        if (course == 1){
            cout<<"Enter the individual marks for the subjects"<<endl;
            cout<<"C++ programming : "<<endl;
            cin>>marks;
            cout<<"Critical Thinking : "<<endl;
            cin>>marks;
            cout<<"Data Structures : "<<endl;
            cin>>marks;
            cout<<"Logical Reasoning : "<<endl;
            cin>>marks;

            sec.setcritical_thinking(critical);

        }else















    return 0;
}