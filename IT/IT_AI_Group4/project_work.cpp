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
    
    // Variables for student details
    string name;
    int roll_number;
    int marks;
    int course;

    // Variables for computer science
    float critical_thinking;
    float programming_in_C;
    float data_structures;
    float logical_reasoning;

    // Variables for IT
    float networking;
    float cybersecurity;
    float web_development;
    float database_management;

    //classes 
    Student s;
    CompSci c;
    Info_tech t;
    feedback fb;
    LetterGrading lg;
    GradingPolicy* gp = &lg; // Assign the grading policy

    ofstream file("report card.txt",ios::app);

    feedback response;


    fb.setfeedback(marks);

    string feedback = fb.getfeedback();

    if (!file) {
        cerr << "Error opening file for writing.\n";
        return 1;
    }

    cout << "______________________REPORT CARD GENERATOR_____________________________" << endl;

    cout << "Enter the student's name: ";
    cin >> name;
    s.setname(name);

    cout << "Enter the roll number: "<<endl;
    cin >> roll_number;
    s.setRollNumber(roll_number);

    cout << "COURSES\n";
    cout << "1. CS\n";
    cout << "2. IT\n";
    cout << "Enter the course: ";
    cin >> course;
    s.setCourse(course);

    if (course == 1) {
        cout << "Enter marks for Critical Thinking: ";
        cin >> critical_thinking;
        c.setcritical_thinking(critical_thinking);

        cout << "Enter marks for Programming in C: ";
        cin >> programming_in_C;
        c.setprogramming_in_C(programming_in_C);

        cout << "Enter marks for Data Structures: ";
        cin >> data_structures;
        c.setdata_structures(data_structures);

        cout << "Enter marks for Logical Reasoning: ";
        cin >> logical_reasoning;
        c.setlogical_reasoning(logical_reasoning);

        file << "Report Card for " << name << endl;
        file<<" (Roll No: " << roll_number << ")\n";
        file << "Course: Computer Science\n";
        c.displayMarks(file, gp);






    } else if (course == 2) {
        cout << "Enter marks for Networking: ";
        cin >> networking;
        t.setnetworking(networking);

        cout << "Enter marks for Cybersecurity: ";
        cin >> cybersecurity;
        t.setcybersecurity(cybersecurity);

        cout << "Enter marks for Web Development: ";
        cin >> web_development;
        t.setweb_development(web_development);

        cout << "Enter marks for Database Management: ";
        cin >> database_management;
        t.setdatabase_management(database_management);

        file << "Report Card for " << name << endl;
        file<<" Roll No: " << roll_number << "\n";
        file << "Course: Information Technology\n";
        t.displayMarks(file, gp);



    } else {
        cout << "Invalid course choice! Exiting program.\n";
        file.close();
        return 1;
    }

    cout << "Report card generated successfully in 'report card.txt'.\n";
    file.close();
    return 0;
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
   