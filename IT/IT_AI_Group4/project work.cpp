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
    //variables for student details
    string name;
    int roll_number;
    int marks;
    int course;
    string CS;
    string IT ;
    
    
    //variables for computer science
    float critical_thinking;
    float programming_in_C;
    float data_structures;
    float logical_reasoning;
    float Total_marks;
    

    //variables for IT 
    float networking;
    float cybersecurity;
    float web_development;
    float database_management;
    float total_marks ;

    Student s;
    CompSci c;
    Info_tech t;
    GradingPolicy gp;



    ofstream file ("report card.txt");
   
    cout<<"______________________REPORT CARD GENERATOR_____________________________"<<endl;

    cout<<"Enter the students name : "<<endl;
    cin>>name;
    s.setname(name);

    
    cout<<"Enter the roll_number : "<<endl;
    cin>>roll_number;
    s.setRollNumber(roll_number);

    cout<<"Enter the course : "<<endl;

    cout<<"COURSES"<<endl;
    cout<<"----------------------"<<endl;
    cout<<"1. CS"<<endl;    
    cout<<"2. IT"<<endl;
    cin>>course;
    s.setCourse(course);



        if (course == 1){
            cout<<"\nEnter the individual marks for the subjects\n"<<endl;
            cout<<"C++ programming : "<<endl;
            cin>>marks;
            c.setprogramming_in_C(programming_in_C);

            cout<<"Critical Thinking : "<<endl;
            cin>>marks;
            c.setcritical_thinking(critical_thinking);
           
            cout<<"Data Structures : "<<endl;
            cin>>marks;
            c. setdata_structures(data_structures);

            cout<<"Logical Reasoning : "<<endl;
            cin>>marks;
            c.setlogical_reasoning(logical_reasoning);
           
           // c.displayMarks();


        }
        
         else if (course==2){
            s.setCourse(course);

            // Input marks for Information Technology
        
            cout << "\nEnter marks for Information Technology subjects :\n";

            cout << "Networking: ";
                cin >> networking;
            t.setnetworking(networking);
        
        
            cout << "Cybersecurity: ";
            cin >> cybersecurity;
            t.setcybersecurity(cybersecurity);
        
            cout << "Web Development: ";
            cin >> web_development;
            t.setweb_development(web_development);
        
            cout << "Database Management: ";
            cin >> database_management;
            t.setdatabase_management(database_management);

            //t.displayMarks();

            }  
            else {
            cout << "Invalid course choice! Exiting program.\n";
            return 1;
        }

        cout << "\nReport card generated successfully in 'reportcard.txt'.\n";
        file.close();
        
        return 0;
}    