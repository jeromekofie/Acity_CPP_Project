#include <iostream>
#include <time.h>
#include <string>
#include <ctime>
#include <fstream>
using namespace std;

const int MAXSTUDENTS = 100;  
const int MAXDAYS = 31;     

 // void returning functions for the code
 void addStudent();
void markAttendance();
void viewAttendance( );
void file( );
void loadFromFile();
bool timeLimit();
char studentNames[MAXSTUDENTS][100];
int rollNumbers[MAXSTUDENTS];
int attendance[MAXSTUDENTS][MAXDAYS];
char attendanceDates[MAXDAYS][31];
int studentCount = 0;
int attendanceCount = 0;

 
int main() {
    cout<<"WELCOME TO ATTENDIFY ATTENDANCE TRACKER\n";
     cout<<"Please a number from the menu\n";

    loadFromFile(); 
  

//creation of menu
    int choice;
    do {
        cout << "Attendance Tracker Menu:\n";
        cout << "1. Add Student\n";
        cout << "2. Mark Attendance\n";
        cout << "3. View Attendance\n";
        cout << "4. Exit\n";
        cout << "Enter your choice: ";
        cin >> choice;

        switch (choice) {
            case 1:
                addStudent();
                break;
            case 2:
                if (timeLimit()) {
                    markAttendance();
                } else {
                     cout << "Attendance can only be marked between 7AM and 12 PM.\n";
                }
                break;
            case 3:
                viewAttendance();
                break;
            case 4:
                file();
                cout << "Exiting and saving data\n";
                break;
            default:
                cout << "Invalid choice!.\n";
                break;
        }
    } 
    while (choice!=4);


    return 0;
}
void addStudent() {
    if (studentCount >= MAXSTUDENTS) {
        cout << "Cannot add more students. Maximum limit reached!\n";
        return;
    }

    cin.ignore();
    cout << "Enter student name: ";
    cin.getline(studentNames[studentCount], 100);
    cout << "Enter roll number: ";
    cin >> rollNumbers[studentCount];

     studentCount++;
    cout << "Student added successfully!\n";
}
void markAttendance() {
    if (attendanceCount >= MAXDAYS) {
        cout << "Cannot record attendance. Maximum days reached!\n";
        return;
    }

 time_t now = time(0);
    tm* localTime = localtime(&now);
    sprintf(attendanceDates[attendanceCount], "%d-%02d-%02d",
            localTime->tm_year + 1900, localTime->tm_mon + 1, localTime->tm_mday);

    cout << "Mark attendance for " << attendanceDates[attendanceCount] << ":\n";
    for (int stu = 0; stu< studentCount; stu++) {
        int status;
        cout << "Roll " << rollNumbers[stu] << " (" << studentNames[stu] << "): ";
        cin >> status;
        attendance[stu][attendanceCount] = status;
    }

    attendanceCount++;
    cout << "Attendance recorded successfully!\n";
}
void viewAttendance() {
    if (studentCount == 0) {
        cout << "No students available. Please add students first.\n";
        return;
    }

    cout << "Attendance Report:\n";
    for (int i = 0; i < studentCount; i++) {
        cout << "Roll: " << rollNumbers[i] << ", Name: " << studentNames[i] << "\n";
        for (int j = 0; j < attendanceCount; j++) {
            cout << "  Date: " << attendanceDates[j] << ", Status: "
                 << (attendance[i][j] ? "Present" : "Absent") << "\n";
        }
    }
}

void saveToFile() {
    ofstream file("attendance_data.txt");
    if (!file) {
        cout << "Error saving data to file.\n";
        return;
    }

    
    file << studentCount << " " << attendanceCount << "\n";

    // Save student details
    for (int stu= 0; stu < studentCount; stu++) {
        file << rollNumbers[stu] << " " << studentNames[stu] << "\n";
    }

    // Save attendance records
    for (int stu= 0; stu < attendanceCount; stu++) {
        file << attendanceDates[stu] << "\n";
        for (int attend= 0; attend < studentCount; attend++) {
            file << attendance[attend][stu] << " ";
        }
        file << "\n";
    }

    file.close();
}

void loadFromFile() {
    ifstream file("attendance_data.txt");
    if (!file) {
        return; 
    }

    
    file >> studentCount >> attendanceCount;
    file.ignore(); 

    for (int stu= 0; stu< studentCount; stu++) {
        file >> rollNumbers[stu];
        file.ignore(); 
        file.getline(studentNames[stu], 100);
    }

    
    for (int stu= 0; stu < attendanceCount; stu++) {
        file.getline(attendanceDates[stu], 31);
        for (int attend= 0; attend < studentCount; attend++) {
            file >> attendance[attend][stu];
        }
        file.ignore(); 
    }

 file.close();
}

bool timeLimit() {
    time_t now = time(0);
    tm* localTime =  localtime(&now);
    int hour = localTime->tm_hour;
     return hour >= 7 && hour < 12; 
}

void file() {
    saveToFile();
}
