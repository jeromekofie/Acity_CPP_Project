#ifndef TASK_WITH_GETTERS_SETTERS_H
#define TASK_WITH_GETTERS_SETTERS_H

#include "priority.h"
#include <string>
using namespace std; 

class Task {
protected:
    string title;
    Priority priority;

public:
    Task(const string& title, Priority priority) : title(title), priority(priority) {}

    virtual void display() const = 0; // Pure virtual function

    // Setters
    void setTitle(const string& newTitle) { title = newTitle; }
    void setPriority(Priority newPriority) { priority = newPriority; }

    // Getters
    string getTitle() const { return title; }
    Priority getPriority() const { return priority; }
};

#endif
