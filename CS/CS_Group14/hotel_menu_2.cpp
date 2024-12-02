/*
Hotel Menu Ordering System
Allow customers to place orders from a menu.
Use switch for different menu options and file operations to save orders.
Format bills with setprecision and store order history in a file.

create at least 2 functions
one function to display the menu
the other to calculate the bill
using switch, allow the user to enter the quantity of food
calculate the total of the order
create an option to leave the program

Step 1: Define the Project Structure
Identify Components: Determine the main components of your system, such as displaying the menu, taking orders, calculating totals, and saving order history.
Choose a Programming Language: Since you mentioned C++, ensure you have a suitable development environment set up for C++ programming.
Step 2: Design the Menu
List Menu Items: Create a list of items that will be available for customers to order, including their prices.
Format the Menu: Decide how you want to present the menu to the user, including how to display item names and prices.
Step 3: Implement User Interaction
Display the Menu: Write a function to display the menu to the user.
Get User Input: Implement a method to capture the user’s selection (order) using standard input.
Step 4: Handle Orders
Use Switch Statements: Create a switch statement to handle different menu options based on user input. Each case should correspond to an item on the menu.
Calculate Total: Maintain a running total of the order cost as the user adds items.
Step 5: Format the Bill
Use setprecision: When displaying the total bill, ensure you format the output to show two decimal places for currency.
Display Order Summary: Create a summary of the order, including itemized costs and the total.
Step 6: Save Order History
File Operations: Learn how to open, write to, and close files in C++. You will need to create or append to a file to save order history.
Store Order Details: Write the order summary and total to a file, ensuring you include information like the date and time of the order for better tracking.
Step 7: Test the Program
Run Test Cases: Test the program with different inputs to ensure it behaves as expected. Check for valid and invalid menu selections.
Check File Output: Verify that the order history is correctly saved to the file and formatted properly.
Step 8: Refine and Enhance
Error Handling: Implement error handling for invalid inputs and file operations.
User Experience: Consider ways to improve user experience, such as adding prompts for reordering or exiting the program.
Add Features: Think about additional features you might want to include, such as discounts, multiple orders, or a more detailed menu.
Step 9: Document Your Code
Add Comments: Write comments in your code to explain the purpose of different functions and sections.
Create Documentation: Consider creating a user manual or README file that explains how to use your program.
Step 10: Prepare for Submission
Final Review: Go through your code and documentation to ensure everything is complete and functioning.
Package Your Project: If required, package your project files neatly, including source code, documentation, and any additional resources.

create a function prototyp for the printing o then final bill
use if else statement in the function
 */

#include <iostream>
#include <iomanip>
#include <fstream>
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

void display_bill()
{
    int choice;
    int quantity;
    float total=0;
    float grandTotal=0;

    cout<<setw(14)<<"BILL"<<endl;
    cout << "-------------------------" << endl;
    cout<<""<<endl;
    while (choice == 5){
        
    if (choice == 1){
        cout<<quantity<<setw(2)<<"Pizza"<<setw(5)<< "$"<<total<<endl;
        grandTotal+=total;
    }
    else if (choice == 2){
        cout<<quantity<<setw(2)<<"Salad"<<setw(5)<<"$"<<total<<endl;
        grandTotal+=total;
    }
    else if (choice == 3){
        cout<<quantity<<setw(2)<<"Burger"<<setw(5)<<"$"<<total<<endl;
        grandTotal+=total;
    }
    else if (choice == 4){
        cout<<quantity<<setw(2)<<"Water"<<setw(5)<<"$"<<total<<endl;
        grandTotal+=total;
    }
    else if (choice == 5){
        cout<<"Your total is "<<setw(5)<<"$ "<<grandTotal;
    }
    
    }
};




int main(){

    int choice ;
    int quantity;
    float total = 0;
    float grandTtoal=0;
    string order;
    float order_price;
    
    cout<<"WELCOME!"<<endl;
    cout<<""<<endl;
    display_menu();
    cout<<""<<endl;

    //cout<<"Place you order by selecting an item from 1 to 4. Select option 5 to exit the menu."<<endl;
    

    while (choice != 5){
        cout<<"Place you order by selecting an item from 1 to 4. Select option 5 to exit the menu."<<endl;
        cin >> choice;
        cout<<""<<endl;

        switch (choice)
    {
         case 1:
        cout << "Enter quantity for pizza ($15.99 for each)" << endl;
        cin >> quantity;
        total += quantity * 15.99;
        cout<<"The price for "<<quantity<<" pizzas is: $"<< total<<endl;
        cout<<""<<endl;

        break;

    case 2:
        cout << "Enter qunatity of salad ($5.50 for each)" << endl;
        cin >> quantity;
        total += quantity * 5.50;
        cout<<"The price for "<<quantity<<" salads is: $"<< total<<endl;
        cout<<""<<endl;

        break;

    case 3:
        cout << "Enter quantity for burger ($8.99 for each)" << endl;
        cin >> quantity;
        total += quantity * 8.99;
        cout<<"The price for "<<quantity<<" burgers is: $"<< total<<endl;
        cout<<""<<endl;

        break;

    case 4:
        cout << "Enter quantity for water ($2.99 for each)" << endl;
        cin >> quantity;
        total += quantity * 2.99;
        cout<<"The price for "<<quantity<<" bottles of water is: $"<< total<<endl;
        cout<<""<<endl;

        break;

    case 5:
        cout << "Exiting menu" << endl;
        grandTtoal+=total;
        cout<<""<<endl;
        //cout<<"Your grand total is :$"<<fixed <<setprecision(2) <<grandTtoal <<endl;
        //cout<<""<<endl;
        display_bill();
        break;
        
    default:
        cout << "Invalid choice. PLease select an item from 1 to 4 or select 5 to exit." << endl;
       break;
    }
    
    }
    

    return 0;

}

hi