#include <iostream>

using namespace std;
class Fruit{
    public:
        void fruit(){
                cout << "Fruits have colors" <<endl;
        }

};
class Apple : public Fruit{

    public:
    void apple(){
        cout<<"Apple is red"<< endl;
    }
};

int main() {
    Fruit f;
    Apple a;

    a.fruit();
    a.apple();

    return 0;
}