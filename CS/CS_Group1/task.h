#ifndef TASK_H
#define TASK_H

#include <string> 
#include <ctime> 

using namespace std; 

enum class Priority {
    LOW, 
    MEDIUM, 
    HIGH 
}; 

class Task { 
    private : 
        string title; 
        string description;
        Priority priority; 
        time_t deadline ; // From the ctime function 
        bool completed; // true or false for if its completed or not 

    public: 

    Task(const string& title,const string& description, Priority priority, time_t deadline);
//GETTERS
    string getTitle() const {
        return title;
    }
    string getDescription() const  {
        return description;
    }
    Priority getPriority() const  {
        return priority; 
    }
    time_t getDeadline() const  {
        return deadline; 
    }
    bool isCompleted() const  {
        return completed; 
    }

//SETTERS 
    void setTitle ( const string title) {
        this->title = title; // this is used to assign the parameter to that member variable (title)
    }
    void setDescription( const string description) {
        this->description = description; 
    }
    void setPriority(Priority priority) {
        this->priority = priority;
    }
    void setDeadline (time_t deadline) {
        this->deadline = deadline; 
    }
    void setCompleted (bool completed) {
        this ->completed = completed; 
    }

    string getPriorityString() const ; 
    
}; 

#endif