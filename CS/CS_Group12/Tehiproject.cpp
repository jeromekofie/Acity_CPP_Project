#include<iostream>
#include<iomanip>   //setw()
#include<fstream>   // ofstream
#include<cstdlib>  // rand
#include<ctime>    // seed
#include<cmath>    // pow()
#include<vector>   // stores collections of data  [vector<string> _ _ _ = {" "};]
#include<map> 
#include<string>
#include<stdexcept> // exceptions

using namespace std;

enum Category{ FOOD,RENT, TRANSPORT, CLOTHES, SHOES, FEES, AIRTIME, MISCELLANEOUS};

string category_to_string(Category category){
    switch(category){
        case FOOD : return "Food";
        case RENT: return "Rent";
        case TRANSPORT: return "Transport";
        case CLOTHES: return "Clothes";
        case SHOES: return "Shoes";
        case FEES: return "Fees";
        case AIRTIME: return "Airtime";
        case MISCELLANEOUS: return "Miscellaneous";
    }
    return "Unknown";
}


int main(){

    
    return 0;
}