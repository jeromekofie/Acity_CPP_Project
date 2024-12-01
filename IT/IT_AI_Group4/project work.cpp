// create a student report generator 
#include<iostream>
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


        }














    return 0;
}