#ifndef CHECKING_ACCOUNT_CLASS_H
#define CHECKING_ACCOUNT_CLASS_H

#include "Account_class.h"

class CheckingAccount : public Account {
public:
    CheckingAccount(const string& accNum, const string& holder, double bal)
        : Account(accNum, holder, bal) {}

    void saveToFile() const override {
        string filename = "CheckingAccount_" + getAccountNumber() + ".txt";
        ofstream file(filename);
        if (file.is_open()) {
            file << "Account Type: Checking Account\n";
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


