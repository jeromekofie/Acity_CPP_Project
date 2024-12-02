// TASK MANAGEMENT SYSTEM
/*
18. Task Management System
Manage to-do lists with task priority and deadlines.
Use enums for task priorities (e.g., Low, Medium, High).
Store tasks in files, use for and do-while loops for task management.

*/

#include <iostream>
#include <limits>
#include <ctime>
#include "taskmanager.h"

using namespace std;

void clearInput()
{
    cin.clear();
    cin.ignore();
}

Priority getPriorityFromUser()
{
    int choice;
    do
    {
        cout << "Select Priority: \n"
             << "1. Low\n"
             << "2. Medium\n"
             << "3. High\n"
             << "Choice: ";
        cin >> choice;
        clearInput();
    } while (choice < 1 || choice > 3);

    switch (choice)
    {
    case 1:
        return Priority::LOW;
    case 2:
        return Priority::MEDIUM;
    default:
        return Priority::HIGH;
    }
}

time_t getDeadlineFromUser()
{
    struct tm deadline = {0};
    cout << "Enter Deadline: \n";

    do
    {
        cout << "Year (e.g., 2024): ";
        cin >> deadline.tm_year;
        deadline.tm_year -= 1900; // to adjust the year
    } while (deadline.tm_year + 1900 < 2024);

    do
    {
        cout << "Month (1-12) : ";
        cin >> deadline.tm_mon;
        deadline.tm_mon -= 1; // Adjust month
    } while (deadline.tm_mon < 0 || deadline.tm_mon > 11);

    do
    {
        cout << "Day (1-31): ";
        cin >> deadline.tm_mday;
    } while (deadline.tm_mday < 1 || deadline.tm_mday > 31);

    clearInput();
    return mktime(&deadline);
}

void addNewTask(TaskManager &manager)
{
    string title, description;

    cout << "Enter task title: ";
    getline(cin, title);

    cout << "Enter task description: ";
    getline(cin, description);

    Priority priority = getPriorityFromUser();
    time_t deadline = getDeadlineFromUser();

    // adding a new task to the list
    Task newTask(title, description, priority, deadline);
    manager.addTask(newTask);
    cout << "Task added successfully! \n";
}

int main()
{
    TaskManager manager;
    int choice;

    do
    {
        cout << "\n TASK MANAGEMENT SYSTEM\n"
             << "1. Add Task\n"
             << "2. View Tasks\n"
             << "3. Mark Task as completed\n"
             << "4. Remove Task\n"
             << "5. Exit\n"
             << "Enter your choice: ";

        cin >> choice;
        clearInput();

        switch (choice)
        {
        case 1:
            addNewTask(manager);
            break;

        case 2:
            manager.displayTasks();
            break;

        case 3:
        {
            manager.displayTasks();
            cout << "Enter Task ID to mark as completed: ";
            size_t index;
            cin >> index;

            manager.markTaskCompleted(index);
            cout << "Task marked as completed! \n";
            break;
        }

        case 4:
        {
            manager.displayTasks();
            cout << "Enter Task ID to remove: ";
            size_t index;
            cin >> index;
            manager.removeTask(index);
            cout << "Task removed!\n ";
            break;
        }

        case 5:
            cout << "Goodbye!\n";
            break;

        default:
            cout << "Invalid Choice. Please try again.\n";
        }
    } while (choice != 5);
    return 0; 
}