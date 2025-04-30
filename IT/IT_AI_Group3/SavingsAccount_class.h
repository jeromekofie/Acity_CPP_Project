#ifndef SAVINGS_ACCOUNT_CLASS_H
#define SAVINGS_ACCOUNT_CLASS_H

#include "Account_class.h"

class SavingsAccount : public Account {
public:
    SavingsAccount(const string& accNum, const string& holder, double bal)
        : Account(accNum, holder, bal) {}

    void saveToFile() const override {
        string filename = "SavingsAccount_" + getAccountNumber() + ".txt";
        ofstream file(filename);
        if (file.is_open()) {
            file << "Account Type: Savings Account\n";
            file << "Account Number: " << getAccountNumber() << "\n";
            file << "Account Holder: " << getHolderName() << "\n";
            file << "Balance: " << getBalance() << "\n";
            file.close();
            cout << "Account successfully saved to " << filename << ".\n";
        } else {
            cout << "Error saving account to file.\n";
        }
    }
};

#endif

