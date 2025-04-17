#ifndef ACCOUNT_CLASS_H
#define ACCOUNT_CLASS_H

#include <iostream>
#include <fstream>
#include <string>

using namespace std;

class Account {
private:
    string accountNumber;
    string HolderName;
    double Balance;

public:
    Account(const string& accNum, const string& holder, double bal)
        : accountNumber(accNum), HolderName(holder), Balance(bal) {}

    virtual ~Account() {} // Virtual destructor for polymorphism

    string getAccountNumber() const { return accountNumber; }
    double getBalance() const { return Balance; }
    string getHolderName() const { return HolderName; }

    void deposit(double amount) {
        Balance += amount;
        cout << "Deposit successful! New Balance: " << Balance << endl;
    }

    void withdraw(double amount) {
        if (amount > Balance) {
            cout << "Insufficient balance!" << endl;
        } else {
            Balance -= amount;
            cout << "Withdrawal successful! New Balance: " << Balance << endl;
        }
    }

    void checkBalance() const {
        cout << "Current Balance: " << Balance << endl;
    }

    virtual void saveToFile() const = 0; // Pure virtual for polymorphism

    // Log transaction in the transaction history file
    static void logTransaction(const string& sender, const string& receiver, double amount) {
        ofstream logFile("transaction_history.txt", ios::app); // Append to file
        if (logFile.is_open()) {
            logFile << "Sender: " << sender << ", Receiver: " << receiver << ", Amount: " << amount << endl;
            logFile.close();
            cout << "Transaction logged successfully." << endl;
        } else {
            cout << "Error logging transaction." << endl;
        }
    }
};

#endif
