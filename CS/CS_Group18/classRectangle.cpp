// If anything inside the class is private then other classes cannot access it 
// vice versa for public 
// class Rectangle
// {
//      private:
//            int 

//        public: 
//            void name(){

//            }


// };

// 1. class - keyword
// 2. name - Upper First letter 
// 3. {}
// 4. ;

/*using the concept of classes and objects, write a program that will calculate the area of the rectangle, taking input from the user.

private: length, width, area
public: getLength, getWidth, getArea, setLength, setWidth 
*/





#include<iostream>
#include "secondRectangleClass.cpp"

using namespace std;

int main(){

    Rectangle shape;

    int length;
    int width;

    cout << "Enter the length and the width to find area" <<endl;
    
    cout << "Length: ";
    cin >> length;

    cout << "\n Width: ";
    cin >> width;

    shape.setLength(length);
    shape.setWidth(width);

    cout << "Length\tWidth\tArea" <<endl;
    cout << "---------------------------" <<endl;
    cout << shape.getLength() << "\t" << shape.getWidth() << "\t" << shape.getArea();

    return 0;
}