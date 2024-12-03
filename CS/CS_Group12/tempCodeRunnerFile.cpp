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

    string getDate()  const {return date;}
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

        void update_totals(string date, double amount){
            string month = date.substr(0, 7);  // (yyyy-mm)
            monthlyTotals[month] += amount;
            annualTotal += amount;
        }



        void addExpenses(string date, Category category, double amount){
            if (amount < 0) throw invalid_argument("Amount cannot be negative");
            expenses.emplace_back(date, category, amount);   
            update_totals(date, amount);
        }

        void displayExpenses() const{
            if (expenses.empty()) {
                cout<< "NO EXPENSE RECORDED.\n";
                return;
            }
            cout<<"\nDate        | Category          | Amount\n";
            cout<<"------------------------------------------\n";
            for (const auto& expense : expenses){
                expense.display();
            }
        }

        void displayTotals() const{
            cout<< "\nMonthly Total\n";
            for(const auto& pair : monthlyTotals){
                cout<< pair.first << ": $"<< fixed << setprecision(2)<< pair.second << endl;
            }
            cout<< "Annual Total: $"<< fixed << setprecision(2)<< annualTotal<< endl;
        }

        void saveToFile(const string& filename)  const{
            ofstream file(filename,ios::trunc );
            if(!file){
                cerr<< "Error, unavle to open file\n";
                return;
            }
            for(const auto& expense : expenses){        // Expenses
                file<< expense.getDate()<< "," <<category_to_string(expense.getCategory())
                    << ","<< expense.getAmount()<< endl; 
            }

            file<< "\nMonthly Totals:\n";
            for(const auto& pair : monthlyTotals){
                file<< pair.first<< ":$"<< annualTotal<< endl;
                file.close();
                cout<<"Saved to'"<< filename<< "'\n";
            }
        }
    };

    Expense generateRandomExpenses(){
        string dates[]= {"2024-04-05", "2024-01-13", "2024-04-25", "2024-12-03"};
        Category categories[]= {FOOD, RENT, FEES, AIRTIME, TRANSPORT, CLOTHES, SHOES, MISCELLANEOUS};
        double amount = (rand()% 100+1)* 10.0;       //(amount b/n 10 and 1000)
        return Expense(dates[rand()%4], categories[rand()%4], amount);
    }


    void displayMenu(){
        cout<<"\nMenu: \n";
        cout<<"1. Add Expense\n";
        cout<<"2. View All Expenses\n";
        cout<<"3. View Totals\n";
        cout<<"4.Random expense\n";
        cout<<"5. Save to file\n";
        cout<<"6. Exit\n";
        cout<<"Enter your choice: ";
    }


    int main() {
    srand(time(0)); // Seed for random number generation
    Expense_tracker tracker;
    int choice;

    do {
        displayMenu();
        cin >> choice;
        cin.ignore();

        switch (choice) {
            case 1: {
                string date;
                int cat;
                double amount;
                cout << "Enter date (YYYY-MM-DD): ";
                cin >> date;
                cout << "Enter category (0: Food, 1: Rent, 2: Travel, 3: Other): ";
                cin >> cat;
                cout << "Enter amount: ";
                cin >> amount;

                try {
                    tracker.addExpenses(date, static_cast<Category>(cat), amount);
                    cout << "Expense added successfully.\n";
                } catch (const exception& e) {
                    cout << "Error: " << e.what() << endl;
                }
                break;
            }
            case 2:
                tracker.displayExpenses();
                break;
            case 3:
                tracker.displayTotals();
                break;
            case 4: {
                Expense randomExpense = generateRandomExpenses();
                tracker.addExpenses(randomExpense.getDate(), randomExpense.getCategory(),
                                   randomExpense.getAmount());
                cout << "Random expense generated and added.\n";
                randomExpense.display();
                break;
            }
            case 5:
                tracker.saveToFile("expenses.txt");
                break;
            case 6:
                cout << "Exiting program. Goodbye!\n";
                break;
            default:
                cout << "Invalid choice. Please try again.\n";
        }
    } while (choice != 6);

    return 0;
}