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