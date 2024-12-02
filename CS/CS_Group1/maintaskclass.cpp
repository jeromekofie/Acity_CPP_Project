#include <iostream>
#include <fstream>
#include <vector>
#include <string>

using namespace std;


struct Task {
    string name;
    string description;
};

////VECTOR FOT STORING////
vector<Task> tasks;

////DISPLAY TASK//// 
void displayTask(const vector<Task>& tasks) {
    for (const auto& task : tasks) {
        cout << "Name: " << task.name << endl;
        cout << "Description: " << task.description << endl;
    }
}

////ADD TASK////
void addTask(vector<Task>& tasks) {
    Task newTask;
    cout << "Enter task name: ";
    cin >> newTask.name;
    cout << "Enter task description: ";
    cin >> newTask.description;
    tasks.push_back(newTask);
}

////DELETE TASK////
void deleteTask(vector<Task>& tasks) {
    int index;
    cout << "Enter task index to delete: ";
    cin >> index;
    if (index >= 0 && index < tasks.size()) {
        tasks.erase(tasks.begin() + index);
    } else {
        cout << "Invalid index." << endl;
    }
}

////SAVE TASK TO FILE////
void saveTask(const string& filename, const vector<Task>& tasks) {
    ofstream file("tasks.txt");
    if (file.is_open()) {
        for (const auto& task : tasks) {
            file << task.name << endl;
            file << task.description << endl;
        }
        file.close();
    } else {
        cout << "Unable to open file." << endl;
    }
}

int main() {
    while (true) {
        cout << "Main Menu" << endl;
        cout << "1. View Task" << endl;
        cout << "2. Add Task" << endl;
        cout << "3. Delete Task" << endl;
        cout << "4. Exit" << endl;

        int choice;
        cin >> choice;
        cin.ignore();!

        switch (choice) {
            case 1:
                displayTask(tasks);
                break;
            case 2:
                addTask(tasks);
                break;
            case 3:
                deleteTask(tasks);
                break;
            case 4:
                saveTask("tasks.txt", tasks);
                cout << "Exiting menu" << endl;
                return 0;
            default:
                cout << "Invalid choice." << endl;
        }
    }
    return 0;
}