#include <iostream>
#include <vector>
#include <string>
#pragma once

using namespace std;

// Define enumerators for task priority
enum class Priority { Low, Medium, High };

class Task {
protected:
    int id;
    string description;
    string deadline;
    bool isCompleted;
    Priority priority; // Add priority member

public:
    Task(int id, const string& desc, const string& deadline, Priority priority, bool completed = false)
        : id(id), description(desc), deadline(deadline), priority(priority), isCompleted(completed) {}

    virtual ~Task() {}

    virtual void display() const {
        cout << "ID: " << id
             << " | Description: " << description
             << " | Deadline: " << deadline
             << " | Status: " << (isCompleted ? "Completed" : "Pending")
             << " | Priority: " << static_cast<int>(priority) << '\n'; 
    }

    int getId() const { return id; }
    bool markAsCompleted() { return isCompleted = true; }

    // New method to return a string representation of the task
    string getDetails() const {
        return to_string(id) + "\n" + description + "\n" + deadline + "\n" + (isCompleted ? "Completed" : "Pending") + "\nPriority: " + to_string(static_cast<int>(priority));
    }
};
