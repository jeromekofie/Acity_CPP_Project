#include <iostream>
using namespace std;
int main(){

    int sharesofstock;
    float price;
    // bought @ $21.77
    price = 21.77;
    cout <<"Enter number of shares of stock:";
    cin >> sharesofstock;
    cout <<"The total amount paid for the stock is:$" << sharesofstock*price <<endl;

    float saleprice = 16.44;
    cout <<"The total amount she received from selling:$ " << sharesofstock*saleprice <<endl;


    float lost;
    cout <<"Kathryn lost:$ " << (sharesofstock*price)-(sharesofstock*saleprice) << endl;
    return 0;
    
 
}