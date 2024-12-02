#include <iostream>
#include "utility.h"
using namespace std; 



class task {
private : 
    string name; 
    bool status; 
    unsigned int id; 

public : 
    task (const string &name_var) {
        this->name = name_var;
        this->status = false; 
        id = task_id++; 
    }

    void setName(const string &name_var) {
        this->name = name_var; 
    }

    void setStatus (const bool &status_var){
        this->status = status_var;
    }

    string getName() {
        return name; 
    }

    bool getStatus(){
        return status; 
    }

    unsigned int getTaskId() {
        return id;
    }

    ~task() {
        cout << "\n In task constructor"; 
    }



};