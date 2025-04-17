#include "task.h" 
using namespace std; 

Task::Task(const string& title,const string& description, Priority priority, time_t deadline) // use of const signifying it cannot be modified after init 
    : title (title), description(description), priority(priority), deadline (deadline), completed(false){} 

string Task::getPriorityString() const {
    switch (priority) {
        case Priority :: LOW :
            return "Low"; 

        case Priority :: MEDIUM : 
            return "Medium"; 

        case Priority :: HIGH: 
            return "High";
            
        default : return "Unkown Priority"; 
    }
}