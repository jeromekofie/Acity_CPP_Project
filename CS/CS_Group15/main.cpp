#include "Course.cpp"
#include "Student.cpp"
#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>
using namespace std;

// Function to save courses to a file
void saveToFile(const vector<Course> &courses) {
    ofstream file("courses_and_students.txt");
    if (file.is_open()) {
        if (courses.empty()) {
            file << "No courses available.\n";
        } else {
            for (const auto &course : courses) {
                course.saveCourseData(file); // Save course and student data
            }
        }
        file.close();
        cout << "Courses and student data saved to 'courses_and_students.txt'.\n";
    } else {
        cout << "Error opening file for saving.\n";
    }
}

// Function to display the menu
void displayMenu() {
    cout << "\nStudent Course Registration System\n";
    cout << "1. Add Course\n";
    cout << "2. Enroll Student in a Course\n";
    cout << "3. List Students in a Course\n";
    cout << "4. Save and Exit\n";
    cout << "Enter your choice: ";
}

int main() {
    vector<Course> courses;
    int choice;

    do {
        displayMenu();
        cin >> choice;

        if (choice == 1) {
            // Add a new course
            cout << "\n...................................................................."<<endl;
            string courseName;
            int courseLimit;
            cout << "Enter course name: ";
            cin.ignore();
            getline(cin, courseName);
            cout << "Enter course student limit: ";
            cin >> courseLimit;
            courses.emplace_back(courseName, courseLimit);
            cout << "Course added successfully.\n";

        } else if (choice == 2) {
            // Enroll a student in a course
            if (courses.empty()) {
                cout << "No courses available. Add a course first.\n";
                continue;
            }

            string courseName;
            int studentId;
            string studentName;

            cout << "Enter course name: ";
            cin.ignore();
            getline(cin, courseName);

            auto it = find_if(courses.begin(), courses.end(), [&courseName](const Course &course) {
                return course.getName() == courseName;
            });

            if (it != courses.end()) {
                cout << "Enter student ID: ";
                cin >> studentId;
                cin.ignore();
                cout << "Enter student name: ";
                getline(cin, studentName);

                if (it->enrollStudent(Student(studentId, studentName))) {
                    cout << "Student enrolled successfully.\n";
                } else {
                    cout << "Course is full.\n";
                }
            } else {
                cout << "Course not found.\n";
            }

        } else if (choice == 3) {
            // List students in a course
            if (courses.empty()) {
                cout << "No courses available. Add a course first.\n";
                continue;
            }

            string courseName;
            cout << "Enter course name: ";
            cin.ignore();
            getline(cin, courseName);

            auto it = find_if(courses.begin(), courses.end(), [&courseName](const Course &course) {
                return course.getName() == courseName;
            });

            if (it != courses.end()) {
                it->listStudents();
            } else {
                cout << "Course not found.\n";
            }

        } else if (choice == 4) {
            // Save and exit
            saveToFile(courses);
            cout << "Exiting program. Goodbye!\n";
        } else {
            cout << "Invalid choice. Please try again.\n";
        }

    } while (choice != 4);

    return 0;
}
