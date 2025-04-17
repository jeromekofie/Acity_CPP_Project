#include<iostream>
#include<fstream>
#include<iomanip>
using namespace std;

void display_menu()
{
    cout << "*****Hotel Menu*****" << endl;
    cout << "-------------------------" << endl;
    cout << "1. Pizza - $15.99" << endl;
    cout << "2. Salad - $5.50" << endl;
    cout << "3. Burger - $8.99" << endl;
    cout << "4. Water - $2.99" << endl;
    cout << "5. Exit menu" << endl;
};

float menu(){
    int choice;
    int quantity;
    float total = 0;

    cout << "Place your order by selecting an item from 1 to 4. Select 5 to exit: " << endl;
    cin >> choice;

    switch (choice){
        case 1:
            cout<<"Enter quantity for pizza ($15.99 for each): "<<endl;
            cin>>quantity;
            total=+quantity*15.99;
            cout<<total;
            break;

        case 2:
            cout<<"Enter quantity for water ($15.99 for each): "<<endl;
            cin>>quantity;
            total=+quantity*15.99;
            cout<<total;
            break;
        
        case 3:
            cout<<"Enter quantity for salad ($5.50 for each): "<<endl;
            cin>>quantity;
            total=+quantity*5.50;
            cout<<total;
            break;

        case 4:
            cout<<"Enter quantity for burger ($8.99 for each): "<<endl;
            cin>>quantity;
            total=+quantity*8.99;
            cout<<total;
            break;

        case 5:
            cout << "Exiting menu." << endl;
            return 0;
            break;

        default:
            cout<<"Invalid choice pleae try again: "<<endl;
            return 0;
            
    
    }
    return total;
}

int main(){
    return menu();

}