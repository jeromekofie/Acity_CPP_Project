#include<iostream>
#include <iomanip>
using namespace std;

class HotelBill{
    public:
    void display_menu()
{
    cout <<setw(10)<< "Hotel Menu" << endl;
    cout << "-------------------------" << endl;
    cout << "1. Pizza - $15.99" << endl;
    cout << "2. Salad - $5.50" << endl;
    cout << "3. Burger - $8.99" << endl;
    cout << "4. Water - $2.99" << endl;
    cout << "5. Exit menu" << endl;

}
};
