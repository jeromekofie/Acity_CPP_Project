#include <iostream>
using namespace std;

int main() {
    // Personal information
    string name = "Kekeli Atiase";      
    int age = 20;                     
    float height = 5.97;               
    bool isStudent = true;           
    double expectedSalary = 55000;    
    char gender = 'M';               

    
    cout << "My name is " << name << "." << endl;
    cout << "I am " << age << " years old." << endl;
    cout << "My height is " << height << " feet." << endl;
    cout << "Is it true that I am a student? " << (isStudent ? "True" : "False") << "." << endl;
    cout << "My expected salary is $" << expectedSalary << "." << endl;
    cout << "My gender is " << gender << "." << endl;

    return 0;
}
