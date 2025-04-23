                                                                /*GROUP 18*/

                                                              /*CHAPTER TWO*/
                                                           /*QUESTION FOURTEEN*/
/*BASKETBALL PLAYER HEIGHT*/
/*The star player of a high school basketball team is 73 inches tall. Write a program to compute and display the height in feet and in inches form.*/ 
/*Hint: Try using the modulus and integer divide operations.*/

#include<iostream>

using namespace std;

int main(){
    /*INITIALIZATION*/
    /*HEIGHT(IN INCHES)*/
    int total_inches = 73; 

    /*WE NEED TO FIND THE LEFTOVER INCHES USING THE MODULUS OPERATION*/
    int inches = total_inches % 12; 
    
    /*CONVERSION OF INCHES TO FEET USING INTEGER DIVIDE OPERATION*/
    int feet = total_inches / 12;

    /*DISPLAY OF RESULT*/
    cout << "The player's height is " << inches << " inche(s) and " << feet << " feet." << endl;
    
    return 0;
}


                                                                 /*CHAPTER THREE*/
                                                                /*QUESTION SEVEN*/
                                                              /*AVERAGE RAINFALL*/
/* Write a program that calculates the average monthly rainfall for three months.The program should ask the user to enter the name of each month,such as June or July,and the amount of rain (in inches) that fell that month.The program should display a message similar to the following:*/
/*The average monthly rainfall for June, July, and August was 6.72 inches.*/

#include <iostream>
#include <iomanip> 

using namespace std;

int main(){
    /*DECLARATION*/
    string first_month, second_month, third_month;
    float first_month_rain, second_month_rain, third_month_rain;

    /*RECEIVING INPUT FROM THE USER ABOUT THE NAMES OF THE MONTHS AND THEIR CORRESPONDING AMOUNT OF RAIN(IN INCHES)*/
    cout << "Enter the name of the first month: ";
    cin >> first_month;
    cout << "Enter the amount of rain for " << first_month << ": ";
    cin >> first_month_rain;

    cout << "Enter the name of the second month: ";
    cin >> second_month;
    cout << "Enter the amount of rain for " << second_month << ": ";
    cin >> second_month_rain;

    cout << "Enter the name of the third month: ";
    cin >> third_month;
    cout << "Enter the amount of rain for " << third_month << ": ";
    cin >> third_month_rain;

    /*CALCULATION OF AVERAGE RAINFALL*/
    float averageRainfall = (first_month_rain + second_month_rain + third_month_rain) / 3.0;

    /*DISPLAY OF RESULT*/
    cout << fixed << setprecision(2); 
    cout << "The average monthly rainfall for " << first_month << ", " << second_month << ", and " << third_month << " was " << averageRainfall << " inches." << endl;

    return 0;
}


/*CHAPTER FOUR*/
/*QUESTION THIRTEEN*/
/*PERSONAL BEST*/
/*Write a program that asks for the name of a pole vaulter and the dates and vault heights(in meters) of the athlete’s three best vaults.It should then report in height order (best first),the date on which each vault was made, and its height.*/
/*Input Validation:Only allow the program to accept values between 2.0 and 5.0 for the heights.*/




/*CHAPTER FIVE*/
/*QUESTION NINE*/
/*MEMBERSHIP FEES INCREASE*/
/*A country club, which currently charges $2500 per year for membership,has announced it will increase its membership fee by 4% each year for the next six years.Write a program that uses a loop to display the projected rates for the next six years*/
