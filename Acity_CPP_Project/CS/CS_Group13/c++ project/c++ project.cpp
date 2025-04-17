#include <iostream>
#include <string>
using namespace std;

// BankAccount class definition
class BankAccount {
private:
    string accountHolder;
    int accountNumber;
    double balance;

public:
    // Constructor to initialize account
    BankAccount(string holder, int number, double initialBalance)
        : accountHolder(holder), accountNumber(number), balance(initialBalance) {}

    // Function to deposit money
    void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            cout << "Deposit successful. New balance: " << balance << endl;
        } else {
            cout << "Invalid deposit amount!" << endl;
        }
    }

    // Function to withdraw money
    void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            cout << "Withdrawal successful. New balance: " << balance << endl;
        } else {
            cout << "Insufficient funds or invalid withdrawal amount!" << endl;
        }
    }

    // Function to display account details
    void displayDetails() const {
        cout << "Account Holder: " << accountHolder << endl;
        cout << "Account Number: " << accountNumber << endl;
        cout << "Balance: " << balance << endl;
    }

    // Function to get the current balance
    double getBalance() const {
        return balance;
    }
};

// Main function
int main() {
    // Creating two bank accounts
    BankAccount account1("Alice Johnson", 1001, 5000.00);
    BankAccount account2("Bob Smith", 1002, 3000.00);

    int choice, accountChoice;
    double amount;

    do {
        // Menu display
        cout << "\n--- Bank Management System ---\n";
        cout << "1. Deposit\n";
        cout << "2. Withdraw\n";
        cout << "3. Check Balance\n";
        cout << "4. Account Details\n";
        cout << "5. Exit\n";
        cout << "Enter your choice: ";
        cin >> choice;

        if (choice == 5) break;

        // Choose account
        cout << "Select account (1 or 2): ";
        cin >> accountChoice;

        BankAccount* selectedAccount = (accountChoice == 1) ? &account1 : &account2;

        switch (choice) {
            case 1: // Deposit
                cout << "Enter amount to deposit: ";
                cin >> amount;
                selectedAccount->deposit(amount);
                break;

            case 2: // Withdraw
                cout << "Enter amount to withdraw: ";
                cin >> amount;
                selectedAccount->withdraw(amount);
                break;

            case 3: // Check Balance
                cout << "Current balance: " << selectedAccount->getBalance() << endl;
                break;

            case 4: // Account Details
                selectedAccount->displayDetails();
                break;

            default:
                cout << "Invalid choice!" << endl;
        }
    } while (choice != 5);

    cout << "Thank you for using the Bank Management System!" << endl;
    return 0;
}
