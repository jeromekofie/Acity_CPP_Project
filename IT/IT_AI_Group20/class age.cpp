#include <iostream> 

using namespace std;
class School{
public:     
    void school(){
    cout<<"howard university"<<endl;
    }
};
class Fruits: public School{
    public:
        void fruits (){
        cout<<"apple"<< endl;
        }
};

int main () {
    Fruits a;

    a.school();
    a.fruits();
}