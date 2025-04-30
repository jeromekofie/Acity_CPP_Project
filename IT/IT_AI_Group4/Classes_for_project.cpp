#include<iostream>
#include<fstream>
#include<iomanip>

using namespace std;
class feedback {
private:
    string feedback_text;

public:
    void setfeedback(float marks) {
        if (marks >= 90) {
            feedback_text = "Excellent performance.";
        } else if (marks >= 75) {
            feedback_text = "Good.";
        } else if (marks >= 50) {
            feedback_text = "More room for improvement.";
        } else {
            feedback_text = "Unfortunately, you failed.";
        }
    }
    string getfeedback() {
        return feedback_text;
    }
};



class PassFailPolicy {
public:
    string calculateGrade(float marks) {
        return (marks >= 50) ? "Pass" : "Fail";
    }
};

class GradingPolicy {
public:
    virtual char calculateGrade(float marks) = 0; // Pure virtual function
};

class LetterGrading : public GradingPolicy {
public:
    char calculateGrade(float marks) {
        if (marks >= 90) return 'A';
        else if (marks >= 75) return 'B';
        else if (marks >= 50) return 'C';
        else return 'D';
    }
    
    
};

// Student details Class
class Student {
private:
    string name;
    int roll_number;
    int course;

public:
    void setname(string n) {
        name = n;
    }
    string getname() {
        return name;
    }

    void setRollNumber(int r) {
        roll_number = r;
    }
    int getRollNumber() {
        return roll_number;
    }

    void setCourse(int c) {
        course = c;
    }
    int getCourse() {
        return course;
    }
};

// CS Class
class CompSci {
private:
    float critical_thinking;
    float programming_in_C;
    float data_structures;
    float logical_reasoning;
    float Total_marks;

public:
    void setcritical_thinking(float ct) {
        critical_thinking = ct;
    }

    void setprogramming_in_C(float pc) {
        programming_in_C = pc;
    }
    void setdata_structures(float ds) {
        data_structures = ds;
    }
    void setlogical_reasoning(float lr) {
        logical_reasoning = lr;
    }

    float getTotalMarks() {
        Total_marks = critical_thinking + programming_in_C + data_structures + logical_reasoning;
        return Total_marks;
    }

    void displayMarks(ofstream& file, GradingPolicy* gp) {
        file << "---------------------------------------------------------------------------------------------\n";
        file << "| Subject                 | Marks                      | Grade                               |\n";
        file << "---------------------------------------------------------------------------------------------\n";
        file << "Critical Thinking          : " << setw(3) << critical_thinking <<  "                       " " | Grade: " << gp->calculateGrade(critical_thinking) << endl;
        file <<"---------------------------------------------------------------------------------------------\n";
        file << "Programming in C           : " << setw(3) << programming_in_C <<   "                       " "| Grade: " << gp->calculateGrade(programming_in_C) << endl;
        file <<"---------------------------------------------------------------------------------------------\n";
        file << "Data Structures            : " << setw(3) << data_structures <<    "                       " " | Grade: " << gp->calculateGrade(data_structures) << endl;
        file <<"---------------------------------------------------------------------------------------------\n";
        file << "Logical Reasoning          : " << setw(3) << logical_reasoning <<  "                       " " | Grade: " << gp->calculateGrade(logical_reasoning) << endl;
        file <<"---------------------------------------------------------------------------------------------\n";
        file <<"---------------------------------------------------------------------------------------------\n";
        file << "Total Marks                : "                                                          <<  setw(3) << getTotalMarks() << endl;
        file << "---------------------------------------------------------------------------------------------\n";
        fb.setfeedback(getTotalMarks());
        file << "Teacher's Feedback         : " << fb.getfeedback() << endl;
    }

};

// IT Class
class Info_tech {
private:
    float networking;
    float cybersecurity;
    float web_development;
    float database_management;

public:
    void setnetworking(float nw) {
        networking = nw;
    }
    void setcybersecurity(float cs) {
        cybersecurity = cs;
    }
    void setweb_development(float wd) {
        web_development = wd;
    }
    void setdatabase_management(float dm) {
        database_management = dm;
    }

    float getTotalMarks() {
        float totalmarks = networking + cybersecurity + web_development + database_management;
        return totalmarks;
    }

    void displayMarks(ofstream& file, GradingPolicy* gp) {
        file << "-------------------------------------------------------------------------------------\n";
        file << "| Subject                 | Marks                          | Grade                  |\n";
        file << "-------------------------------------------------------------------------------------\n";
        file << "Networking             : "    "         "<< setw(3) << networking <<"              " " | Grade: " << gp->calculateGrade(networking) << endl;
        file << "-------------------------------------------------------------------------------------\n";
        file << "Cybersecurity          : "    "         " << setw(3) << cybersecurity << "         " " | Grade: " << gp->calculateGrade(cybersecurity) << endl;
        file << "-------------------------------------------------------------------------------------\n";
        file << "Web Development        : "    "         " << setw(3) << web_development <<"         " " | Grade: " << gp->calculateGrade(web_development) << endl;
        file << "-------------------------------------------------------------------------------------\n";
        file << "Database Management    : "    "         " << setw(3) << database_management << "         "" | Grade: " << gp->calculateGrade(database_management) << endl;
        file << "-------------------------------------------------------------------------------------\n";
        file << "Total Marks            : "    "         " << setw(3) << getTotalMarks() << endl;

        fb.setfeedback(getTotalMarks());
        file << "Teacher's Feedback         : " << fb.getfeedback() << endl;
    }
};

