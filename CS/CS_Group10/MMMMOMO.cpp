#include <iostream>

using namespace std;

int main() {
    int choice;
    string code="*170#";
    string usercode;
    float amount;
    int momoPin;
    int momochoice;
    int numberA;
    int numberB;
    int momoAmount;
    string reference;

    cout<< "Please enter the code:"<<endl;
    cin>>usercode;
    if(code==usercode){
        cout<<" Welcome to MTN Mobile Money"<<endl;
        cout<< "1. Transfer Money"<<endl;
        cout<< "2. MomoPay"<<endl;
        cout<< "3. Buy Airtime & Bundle"<<endl;
        cout<< "4. Exit"<<endl;

        cout<<"Enter Choice:";
        cin>>choice;   

        switch(choice){
            case 1:
                cout<< "Transfer Money"<<endl;
                 cout << "1. Momo User"<<endl;
                cout << "2. Non-Momo User"<<endl;
                cout << "3. Other Network"<<endl;
                cout << "4. Bank Account"<<endl;
                cout << "5. Back"<<endl;

                cout << "Enter choice:";
                cin>> momochoice;

                if(choice==1){

                    cout << "\nEnter mobile number:";
                    cin>> numberA;
                         
                    cout<< "\nConfirm mobile number:";
                    cin>>numberB;

                        if(numberA==numberB){

                            cout << "\nEnter amount:";
                            cin>>momoAmount;

                            cout << "\nConfirm momo pin:"<<endl;
                            cin>>momoPin;

                             cout << "\nReference:"<<endl;
                            cin>>reference;


                            cout << "You have successfully made payment of GHS "<<momoAmount<<" to " <<numberA <<" with reference "<< reference<<endl;

                            }
                            else {
                                cout << "Numbers do not match. Please start process"<<endl;
                            }

                }else{
                    cout<<"Under construction"<<endl;
                }
                break;
            case 2:
                cout<< " Momo Pay"<<endl;
                cout<<" Under Construction"<<endl;
                break;
            case 3:
                cout<< "Buy Airtime"<<endl;
                cout<<"\n";

                cout<<"1. Airtime"<<endl;
                cout<<"2. Bundle"<<endl;

                cout<<"\nChoice:";
                cin>> choice;

                if(choice==1){
                    cout<< "Enter airtime amount"<<endl;
                    cout<<"\nAmount:";
                    cin>>amount;

                    cout<<"Enter your MOMO pin"<<endl;
                    cin>> momoPin;

                    cout<< "Airtime of Ghc "<<amount<<" purchased, enjoy your calls";


                }else{
                    cout<<"Under construction";
                }
                
                   
            
        }
    }
    else{
        cout<< "Invalid USSD Code";
    }

    

    return 0;
}