#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include "taskclass.cpp" 
using namespace std;

class TaskManager {
private:
    vector<Task*> tasks;
    int nextId = 1;

public:
    ~TaskManager() {
        for (auto task : tasks) {
            delete task; // Free allocated memory
        }
    }

    void addTask(const string& desc, const string& deadline, Priority priority) {
        tasks.push_back(new Task(nextId++, desc, deadline, priority)); 
        cout << "Task added.\n";
    }

    void viewTasks() const {
        if (tasks.empty()) {
            cout << "No tasks available.\n";
        } else {
            for (const auto& task : tasks) {
                task->display();
            }
        }
    }

    void markTaskAsCompleted(int id) {
        for (auto& task : tasks) {
            if (task->getId() == id) {
                task->markAsCompleted();
                cout << "Task marked as completed.\n";
                return;
            }
        }
        cout << "Task not found.\n";
    }

    vector<Task> getTasks() const {
        vector<Task> taskList;
        for (const auto& task : tasks) {
            taskList.push_back(*task); 
        }
        return taskList;
    }
};
