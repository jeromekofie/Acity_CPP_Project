#ifndef TASK_H
#define TASK_H

#include "priority.h"
#include "date.h"
#include <string>

using namespace std; 

class Task {
protected: // Change from private to protected
    string title;
    string description;
    bool isDone;
    Priority priority;
    Date dueDate;

public:
    Task(const std::string &_title, const std::string &_description, Priority _priority, const Date &_dueDate)
        : title(_title), description(_description), isDone(false), priority(_priority), dueDate(_dueDate) {}

    // Setters
    void setTitle(const std::string& newTitle) { title = newTitle; }
    void setDescription(const std::string& newDescription) { description = newDescription; }
    void setIsDone(bool done) { isDone = done; }
    void setPriority(Priority newPriority) { priority = newPriority; }
    void setDueDate(const Date& newDueDate) { dueDate = newDueDate; }

    // Getters
    string getTitle() const { return title; }
    string getDescription() const { return description; }
    bool getIsDone() const { return isDone; }
    Priority getPriority() const { return priority; }
    Date getDueDate() const { return dueDate; }

    // Display function
    virtual void display() const {
        cout << "Task: " << title << ", Description: " << description 
                  << ", Status: " << (isDone ? "Done" : "Not Done") 
                  << ", Priority: " << static_cast<int>(priority) 
                  << ", Due Date: " << dueDate.getDay() << "/" << dueDate.getMonth() << "/" << dueDate.getYear() << std::endl;
    }
};

#endif
