#ifndef TASK_TYPES_H
#define TASK_TYPES_H

#include <iostream>
#include "main.cpp"
#include "task.h"
using namespace std;

class BasicTask : public Task {
public:
    BasicTask(const string& title, Priority priority) : Task(title, priority) {}

    void display() const override {
        cout << "Basic Task: " << title << " with priority " << static_cast<int>(priority) << std::endl;
    }
};

class PriorityTask : public Task {
public:
    PriorityTask(const string& title, Priority priority) : Task(title, priority) {}

    void display() const override {
        cout << "Priority Task: " << title << " with priority " << static_cast<int>(priority) << std::endl;
    }
};

#endif
