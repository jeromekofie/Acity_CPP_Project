#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include "taskclass.cpp"
#include "taskmangerclass.cpp"
#include "filesclass.cpp"

using namespace std;

int main() {
    TaskManager manager;
    const string filename = "tasks.txt";
    int choice;

    do {
        cout << "\n--- Task Manager ---\n";
        cout << "1. Add Task\n";
        cout << "2. View Tasks\n";
        cout << "3. Mark Task as Completed\n";
        cout << "4. Save Tasks to File\n"; 
        cout << "5. Exit\n";
        cout << "Enter your choice: ";
        cin >> choice;
        cin.ignore(); 

        if (choice == 1) {
            string desc, deadline;
            int priorityChoice;
            cout << "Enter task description: ";
            getline(cin, desc);
            cout << "Enter deadline (YYYY-MM-DD): ";
            getline(cin, deadline);
            cout << "Select priority (0: Low, 1: Medium, 2: High): ";
            cin >> priorityChoice;
            Priority priority = static_cast<Priority>(priorityChoice);
            manager.addTask(desc, deadline, priority); 

        } else if (choice == 2) {
            manager.viewTasks();

        } else if (choice == 3) {
            int id;
            cout << "Enter task ID to mark as completed: ";
            cin >> id;
            manager.markTaskAsCompleted(id);

        } else if (choice == 4) {
            
            vector<Task> tasks = manager.getTasks(); 
            FileHandler::saveTasksToFile(tasks, filename);
            cout << "Tasks have been saved to " << filename << endl;

        } else if (choice == 5) {
            cout << "Exiting...\n";
        } else {
            cout << "Invalid choice. Try again.\n";
        }
    } while (choice != 5);

    return 0;
}
