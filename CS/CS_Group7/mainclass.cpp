#include <iostream>

using namespace std;


int main() {
   
    //ContactManager manager;
    int choice;

    do {
        cout << "\nContact Management System\n";
        cout << "1. Add Contact\n";
        cout << "2. Search Contact\n";
        cout << "3. Delete Contact\n";
        cout << "4. Sort Contacts by Name\n";
        cout << "6. Display All Contacts\n";
        cout << "7. Exit\n";
        cout << "Enter your choice: ";
        cin >> choice;

  
    }while(choice=!7);
    cout<<"Wrong input\n";
    
    return 0;
}
