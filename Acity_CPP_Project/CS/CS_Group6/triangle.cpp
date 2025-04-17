
#include <iostream>
#include <string>


using namespace std;

class Triangle {
private:
    double base;
    double height;

public:
    Triangle() : base(0), height(0) {}

    void dimensions(double b, double h) {
        base = b;
        height = h;
    }

    double calculate_area() const {
        return 0.5 * base * height;
    }

    void Area() const {
        cout << "\nTriangle Dimensions:\n";
        cout << "\n--------------------------------------\n";
        cout << "Base\t Height\t Area\t";
        cout << "\n---------------------------------------\n";
        cout << "This is the area of the triangle: " << calculate_area() << " square units"<<"\n";
    }
};
