#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <iomanip>
#include <stdexcept>

using namespace std;

// Enum for attendance status
enum AttendanceStatus { Present, Absent };

// Class to represent a student's attendance
class StudentAttendance {
private:
    string name;
    vector<AttendanceStatus> monthlyAttendance;

public:
    // Constructor
    StudentAttendance(const string& studentName, int daysInMonth)
        : name(studentName), monthlyAttendance(daysInMonth, Absent) {}

    // Get student name
    string getName() const {
        return name;
    }

    // Record attendance for a specific day
    void recordAttendance(int day, AttendanceStatus status, int daysInMonth) {
        if (day < 1 || day > daysInMonth) {
            throw out_of_range("Day must be within the valid range for the month.");
        }
        monthlyAttendance[day - 1] = status;
    }

    // View attendance for the month
    void viewMonthlyAttendance(int daysInMonth) const {
        cout << "\nAttendance for " << name << ":\n";
        cout << setw(5) << "Day" << setw(10) << "Status\n";
        for (int i = 0; i < daysInMonth; ++i) {
            cout << setw(5) << (i + 1)
                 << setw(10) << (monthlyAttendance[i] == Present ? "✅" : "❌") << endl;
        }
    }

    // Save student attendance to file
    void saveToFile(ofstream& outFile) const {
        outFile << name;
        for (const auto& status : monthlyAttendance) {
            outFile << " " << (status == Present ? "1" : "0");
        }
        outFile << endl;
    }

    // Load student attendance from file
    void loadFromFile(ifstream& inFile, int daysInMonth) {
        for (int i = 0; i < daysInMonth; ++i) {
            int status;
            inFile >> status;
            monthlyAttendance[i] = (status == 1 ? Present : Absent);
        }
    }
};

// Function to display menu
void displayMenu() {
    cout << "\nAttendance Tracker Menu\n";
    cout << "1. Add a Student\n";
    cout << "2. Remove a Student\n";
    cout << "3. Record Attendance for a Day\n";
    cout << "4. View Attendance for a Student\n";
    cout << "5. Save and Exit\n";
    cout << "Enter your choice: ";
}

// Function to get number of days in a month
int getDaysInMonth(int month) {
    if (month == 2) return 28; // Simplified to non-leap years for now
    return (month == 4 || month == 6 || month == 9 || month == 11) ? 30 : 31;
}

// Function to add a student
void addStudent(vector<StudentAttendance>& students, int daysInMonth) {
    string name;
    cout << "Enter the student's name: ";
    cin.ignore();
    getline(cin, name);
    students.emplace_back(name, daysInMonth);
    cout << name << " has been added to the list.\n";
}

// Function to remove a student
void removeStudent(vector<StudentAttendance>& students) {
    cout << "Select a student to remove:\n";
    for (size_t i = 0; i < students.size(); ++i) {
        cout << (i + 1) << ". " << students[i].getName() << endl;
    }
    int choice;
    cin >> choice;
    if (choice >= 1 && choice <= students.size()) {
        cout << students[choice - 1].getName() << " has been removed.\n";
        students.erase(students.begin() + (choice - 1));
    } else {
        cout << "Invalid choice. No student removed.\n";
    }
}

// Function to save attendance records to file
void saveAttendance(const vector<StudentAttendance>& students, int daysInMonth) {
    ofstream outFile("attendance_month.txt");
    if (!outFile) {
        cout << "Error saving attendance to file.\n";
        return;
    }
    outFile << students.size() << " " << daysInMonth << endl;
    for (const auto& student : students) {
        student.saveToFile(outFile);
    }
    outFile.close();
    cout << "Attendance saved successfully.\n";
}

// Function to load attendance records from file
void loadAttendance(vector<StudentAttendance>& students, int& daysInMonth) {
    ifstream inFile("attendance_month.txt");
    if (!inFile) {
        cout << "No attendance records found. Starting fresh.\n";
        return;
    }
    size_t numStudents;
    inFile >> numStudents >> daysInMonth;
    students.clear();
    for (size_t i = 0; i < numStudents; ++i) {
        string name;
        inFile >> name;
        students.emplace_back(name, daysInMonth);
        students.back().loadFromFile(inFile, daysInMonth);
    }
    inFile.close();
    cout << "Attendance records loaded successfully.\n";
}

int main() {
    vector<StudentAttendance> students;
    int daysInMonth, month;

    // Input month and determine the number of days
    cout << "Enter the month (1-12): ";
    cin >> month;
    daysInMonth = getDaysInMonth(month);

    // Load attendance data if available
    loadAttendance(students, daysInMonth);

    int choice;
    do {
        displayMenu();
        cin >> choice;

        switch (choice) {
            case 1: // Add a student
                addStudent(students, daysInMonth);
                break;

            case 2: // Remove a student
                removeStudent(students);
                break;

            case 3: { // Record attendance
                int day, statusChoice;
                cout << "Enter day of the month (1-" << daysInMonth << "): ";
                cin >> day;
                for (auto& student : students) {
                    cout << "Record attendance for " << student.getName() << " (1 for Present, 0 for Absent): ";
                    cin >> statusChoice;
                    AttendanceStatus status = (statusChoice == 1 ? Present : Absent);
                    student.recordAttendance(day, status, daysInMonth);
                }
                saveAttendance(students, daysInMonth);
                cout << "Attendance for day " << day << " recorded successfully and saved.\n";
                break;
            }

           

            case 5: // Save and exit
                saveAttendance(students, daysInMonth);
                cout << "Exiting program.\n";
                break;

            default:
                cout << "Invalid choice. Please try again.\n";
        }
    } while (choice != 5);

    return 0;
}
