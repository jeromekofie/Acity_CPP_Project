#include<iostream>
#include<iomanip>   //setw()
#include<fstream>   // ofstream
#include<cstdlib>  // rand
#include<ctime>    // seed
#include<cmath>    // pow()
#include<vector>   // stores collections of data  [vector<string> _ _ _ = {" "};]
#include<map> 
#include<string>
#include<stdexcept> // exceptions (throw)

using namespace std;

enum Category{ FOOD, RENT, TRANSPORT, CLOTHES, SHOES, FEES, AIRTIME, MISCELLANEOUS};

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

// Classes

class Expense{
    private:
        string date;
        Category category;
        double amount;
    
    public:
    Expense (string d, Category c, double a) : date(d), category(c), amount(a) {}

    string gateDate()  const {return date;}
    Category getCategory() const{return category;}
    double getAmount()  const{return amount;}

    void setDate(string d)  {date= d;}
    void setCategory(Category c)  {category= c;}
    void setAmount(double a)  {amount = a;}

    void display() const{
        cout<< setw(10) << " | "<< setw(10) << category_to_string(category)
            << " | "<< fixed << setprecision(2) << amount << endl;
    }

};

class Expense_tracker{
    private:
        vector<Expense> expenses;  // store expenses
        double annualTotal;
        map<string, double> monthlyTotals;  // maps monthly totals

    public:
        Expense_tracker() : annualTotal(0) {}

        void addExpenses(string date, Category category, double amount){
            if (amount < 0) throw invalid_argument("Amount cannot be negative");   }
}




