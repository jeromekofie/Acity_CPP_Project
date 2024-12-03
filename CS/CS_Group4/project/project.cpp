#include <iostream>
#include <iomanip>
#include <fstream>
using namespace std;

class Remarks {
private:
    string remark;

public:
    void setRemark(float average) {
        if (average >= 90) {
            remark = "Excellent performance. More vim.";
        } else if (average >= 75) {
            remark = "Good job. You force.";
        } else if (average >= 50) {
            remark = "You try. There's room for improvement.";
        } else {
            remark = "Needs improvement. Don't give up.";
        }
    }

    string getRemark() {
        return remark;
    }
};

char calculateGrade(float subjectTotal) {
    if (subjectTotal >= 90) {
        return 'A';
    } else if (subjectTotal >= 75) {
        return 'B';
    } else if (subjectTotal >= 50) {
        return 'C';
    } else {
        return 'D';
    }
}

int main() {
    string name, course;
    int rollNumber, choice;
    float total = 0, average;

    const int MAX_SUBJECTS = 8;
    string subjects[MAX_SUBJECTS];
    float classScores[MAX_SUBJECTS], examScores[MAX_SUBJECTS];

    ofstream outToFile("reportcard.txt", ios::app);

    cout << "Enter student's name: ";
    cin.ignore();
    getline(cin, name);

    cout << "Enter roll number: ";
    cin >> rollNumber;

   
    cout << "Choose a course:" << endl;
    cout << "1. Computer Science" << endl;
    cout << "2. Information Technology" << endl;
    cout << "3. Business Administration" << endl;
    cout << "Enter your choice: ";
    cin >> choice;

    switch (choice) {
        case 1:
            course = "Computer Science";
            subjects[0] = "Programming in C++ ";
            subjects[1] = "Data Structures    ";
            subjects[2] = "Database Systems   ";
            subjects[3] = "Operations Research";
            subjects[4] = "Calculus & Matlab  ";
            subjects[5] = "Leadership Seminar ";
            subjects[6] = "Logic and Design   ";
            subjects[7] = "F. I .E            ";
            break;
        case 2:
            course = "Information Technology";
            subjects[0] = "Business in Africa   ";
            subjects[1] = "Web Development      ";
            subjects[2] = "System Administration";
            subjects[3] = "Database Systems     ";
            subjects[4] = "Cybersecurity        ";
            subjects[5] = "Leadership Seminar   ";
            subjects[6] = "Data Structures      ";
            subjects[7] = "F. I .E              ";
            break;
        case 3:
            course = "Business Administration";
            subjects[0] = "Management Principles";
            subjects[1] = "Marketing            ";
            subjects[2] = "Accounting           ";
            subjects[3] = "Business Law         ";
            subjects[4] = "Economics            ";
            subjects[5] = "Leadership Seminar   ";
            subjects[6] = "I. P. Law            ";
            subjects[7] = "F. I .E              ";
            break;
        default:
            cout << "Invalid choice! Exiting program.";
            return 1;
    }

    outToFile << "\n--------- Report Card ---------\n";
    outToFile << "Name       : " << name << endl;
    outToFile << "Roll Number: " << rollNumber << endl;
    outToFile << "Course     : " << course << endl << endl;

    for (int i = 0; i < MAX_SUBJECTS; i++) {
        float classScore, examScore, subjectTotal;

        cout << "Enter class score for " << subjects[i] << " (out of 40): ";
        cin >> classScore;
        while (classScore < 0 || classScore > 40) {
            cout << "Invalid score! Please enter a value between 0 and 40: ";
            cin >> classScore;
        }

        cout << "Enter exam score for " << subjects[i] << " (out of 60): ";
        cin >> examScore;
        while (examScore < 0 || examScore > 60) {
            cout << "Invalid score! Please enter a value between 0 and 60: ";
            cin >> examScore;
        }

        classScores[i] = classScore;
        examScores[i] = examScore;
        subjectTotal = classScore + examScore;
        total += subjectTotal;

        
        char grade = calculateGrade(subjectTotal);

        outToFile << subjects[i] << "\t: " << subjectTotal << " | Grade: \t\t " << grade << endl;
    }

    average = total / MAX_SUBJECTS;

   
    Remarks remarkHandler;
    remarkHandler.setRemark(average);
    string remarks = remarkHandler.getRemark();

    outToFile << "\nTotal Marks: " << total << endl;
    outToFile << "CWA        : " << average << endl;
    outToFile << "Remarks    : " << remarks << endl;

    cout << "\n--- Summary ---\n";
    cout << "Total Marks: " << total << endl;
    cout << "CWA        : " << average << endl;
    cout << "Remarks    : " << remarks << endl;

    outToFile.close();
    return 0;
}
