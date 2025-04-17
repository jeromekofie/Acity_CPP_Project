// THIS IS THE TASK MANAGER CLASS HEADER FILE 
#ifndef TASKMANAGER_H // checks if taskmanager_h is not defined
#define TASKMANAGER_H // defines taskmanager_h

#include <vector> 
#include <string> 
#include "task.h"

using namespace std; 

class TaskManager { 
    private : 
        vector<Task> tasks; 
        const string filename = "tasks.txt"; 

    public : 
        TaskManager(); // constructuer for the class 
        ~TaskManager(); // Destructor for the class 

        void addTask(const Task& task) ; 
        void removeTask(size_t index); 
        void markTaskCompleted(size_t index); 
        void displayTasks() const; 
        void saveTasks() const; 
        void loadTasks(); 

        const vector<Task>& getTasks() const {
            return tasks;
        }

    #endif
    
}; 