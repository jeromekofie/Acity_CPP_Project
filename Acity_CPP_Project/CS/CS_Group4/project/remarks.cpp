#include <string> 
using namespace std; 

class Remarks {
private:
    string remark;

public:
    void setRemark(float average) {
        if (average >= 90) {
            remark = "Excellent performance. More vim.";
        } else if (average >= 75) {
            remark = "Good job. You force.";
        } else if (average >= 50) {
            remark = "You try. There's room for improvement.";
        } else {
            remark = "Needs improvement. Don't give up.";
        }
    }

    string getRemark() {
        return remark;
    }
};
