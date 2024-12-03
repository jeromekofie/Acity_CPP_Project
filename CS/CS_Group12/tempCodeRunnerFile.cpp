#include <iostream>
#include <fstream>
#include <vector>
#include <iomanip>     
#include <cmath>        
#include <cstdlib>      
#include <ctime>       
#include <string>
#include <map>
#include <stdexcept>    // For exceptions (throw)

using namespace std;


enum Category { FOOD, RENT, TRAVEL, OTHER };

// converting enum types to strings
string categoryToString(Category category) {
    switch (category) {
        case FOOD: return "Food";
        case RENT: return "Rent";
        case TRAVEL: return "Travel";
        case OTHER: return "Other";
    }
    return "Unknown";
}


class Expense {
private:
    string date;
    Category category;
    double amount;

public:
    // Constructor for enum
    Expense(string d, Category c, double a) : date(d), category(c), amount(a) {}

   
    string getDate() const { return date; }
    Category getCategory() const { return category; }
    double getAmount() const { return amount; }

    
    void setDate(string d) { date = d; }
    void setCategory(Category c) { category = c; }
    void setAmount(double a) { amount = a; }

    
    void display() const {
        cout << setw(10) << date << " | " << setw(10) << categoryToString(category)
             << " | " << fixed << setprecision(2) << setw(10) << amount << endl;
    }
};


class ExpenseTracker {
private:
    vector<Expense> expenses;  // Vector to store expenses
    double annualTotal;        // Total expenses for the year
    map<string, double> monthlyTotals; // Map for monthly totals(month, amount)

public:
   
    ExpenseTracker() : annualTotal(0) {}

    
    void addExpense(string date, Category category, double amount) {
        if (amount < 0) throw invalid_argument("Amount cannot be negative."); // Exception 
        expenses.emplace_back(date, category, amount);
        updateTotals(date, amount);
    }

    
    void updateTotals(string date, double amount) {
        string month = date.substr(0, 7); // Extract YYYY-MM
        monthlyTotals[month] += amount;
        annualTotal += amount;
    }

    
    void displayExpenses() const {
        if (expenses.empty()) {
            cout << "No expenses recorded.\n";
            return;
        }
        cout << "\nDate       | Category   | Amount\n";
        cout << "-----------------------------------\n";
        for (const auto& expense : expenses) {
            expense.display();
        }
    }

    
    void displayTotals() const {
        cout << "\nMonthly Totals:\n";
        for (const auto& pair : monthlyTotals) {
            cout << pair.first << ": $" << fixed << setprecision(2) << pair.second << endl;
        }
        cout << "Annual Total: $" << fixed << setprecision(2) << annualTotal << endl;
    }

   
    void saveToFile(const string& filename) const {
        ofstream file(filename, ios::trunc);
        if (!file) {
            cerr << "Error: Unable to open file for saving.\n";
            return;
        }

      
        for (const auto& expense : expenses) {
            file << expense.getDate() << "," << categoryToString(expense.getCategory())
                 << "," << expense.getAmount() << endl;
        }

       
        file << "\nMonthly Totals:\n";
        for (const auto& pair : monthlyTotals) {
            file << pair.first << ": $" << pair.second << endl;
        }
        file << "Annual Total: $" << annualTotal << endl;
        file.close();
        cout << "Data saved to '" << filename << "'.\n";
    }
};


Expense generateRandomExpense() {
    string dates[] = {"2024-01-15", "2024-02-10", "2024-03-05", "2024-11-27"};
    Category categories[] = {FOOD, RENT, TRAVEL, OTHER};
    double amount = (rand() % 100 + 1) * 10.0; // between $10 and $1000

    return Expense(dates[rand() % 4], categories[rand() % 4], amount);
}


void displayMenu() {
    cout << "\nExpense Tracker Menu:\n";
    cout << "1. Add Expense\n";
    cout << "2. View All Expenses\n";
    cout << "3. View Totals\n";
    cout << "4. Generate Random Expense\n";
    cout << "5. Save to File\n";
    cout << "6. Exit\n";
    cout << "Enter your choice: ";
}

int main() {
    srand(time(0)); // Seed 
    ExpenseTracker tracker;
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
                    tracker.addExpense(date, static_cast<Category>(cat), amount);
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
                Expense randomExpense = generateRandomExpense();
                tracker.addExpense(randomExpense.getDate(), randomExpense.getCategory(),
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