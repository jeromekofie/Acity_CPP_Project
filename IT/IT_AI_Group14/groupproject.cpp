#include <iostream>
#include <time.h>
#include <string>
#include <ctime>
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
        }
    } while (choice != 4);

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
    for (int i = 0; i < studentCount; i++) {
        int status;
        cout << "Roll " << rollNumbers[i] << " (" << studentNames[i] << "): ";
        cin >> status;
        attendance[i][attendanceCount] = status;
    }

    attendanceCount++;
    cout << "Attendance recorded successfully!\n";
}
void viewAttendance() {
    if (studentCount == 0) {
        cout << "No students available. Please add students first.\n";
        return;
    }

    cout << "\nAttendance Report:\n";
    for (int i = 0; i < studentCount; i++) {
        cout << "Roll: " << rollNumbers[i] << ", Name: " << studentNames[i] << "\n";
        for (int j = 0; j < attendanceCount; j++) {
            cout << "  Date: " << attendanceDates[j] << ", Status: "
                 << (attendance[i][j] ? "Present" : "Absent") << "\n";
        }
    }


void saveToFile() {
    ofstream file("attendance_data.txt");
    if (!file) {
        cout << "Error saving data to file.\n";
        return;
    }

    // Save the number of students and attendance days
    file << studentCount << " " << attendanceCount << "\n";

    // Save student details
    for (int i = 0; i < studentCount; i++) {
        file << rollNumbers[i] << " " << studentNames[i] << "\n";
    }

    // Save attendance records
    for (int i = 0; i < attendanceCount; i++) {
        file << attendanceDates[i] << "\n";
        for (int j = 0; j < studentCount; j++) {
            file << attendance[j][i] << " ";
        }
        file << "\n";
    }

    file.close();
}

void loadFromFile() {
    ifstream file("attendance_data.txt");
    if (!file) {
        return; // No file to load
    }

    // Load the number of students and attendance days
    file >> studentCount >> attendanceCount;
    file.ignore(); // Clear the newline character

    // Load student details
    for (int i = 0; i < studentCount; i++) {
        file >> rollNumbers[i];
        file.ignore(); // Skip space
        file.getline(studentNames[i], 100);
    }

    // Load attendance records
    for (int i = 0; i < attendanceCount; i++) {
        file.getline(attendanceDates[i], 31);
        for (int j = 0; j < studentCount; j++) {
            file >> attendance[j][i];
        }
        file.ignore(); // Clear the newline character
    }

    file.close();
}

bool timeLimit() {
    time_t now = time(0);
    tm* localTime = localtime(&now);
    int hour = localTime->tm_hour;
    return hour >= 7 && hour < 12; // Attendance is allowed between 7:00 AM and 11:59 AM
}

void file() {
    saveToFile();
}
