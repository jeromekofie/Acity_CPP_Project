#include <iostream>
#include <vector>
#include <fstream>
#include "Account_class.h"
#include "SavingsAccount_class.h"
#include "CheckingAccount_class.h"

using namespace std;

void displayMenu() {
    cout << "\n-------------------------------\n";
    cout << "--- Bank Account Management ---\n";
    cout << "-------------------------------\n";
    cout << "1. Create Savings Account\n";
    cout << "2. Create Checking Account\n";
    cout << "3. Transfer Money\n";
    cout << "4. View Transaction History\n";
    cout << "5. Check Account Balance\n";
    cout << "6. Exit\n";
}

Account* findAccount(const vector<Account*>& accounts, const string& accountNumber) {
    for (auto account : accounts) {
        if (account->getAccountNumber() == accountNumber) {
            return account;
        }
    }
    return nullptr;
}

void transferMoney(vector<Account*>& accounts) {
    string senderAccNum, receiverAccNum;
    double amount;

    cout << "Enter Sender's Account Number: ";
    cin >> senderAccNum;
    Account* senderAccount = findAccount(accounts, senderAccNum);
    if (!senderAccount) {
        cout << "Sender account not found!\n";
        return;
    }

    cout << "Enter Receiver's Account Number: ";
    cin >> receiverAccNum;
    Account* receiverAccount = findAccount(accounts, receiverAccNum);
    if (!receiverAccount) {
        cout << "Receiver account not found!\n";
        return;
    }

    cout << "Enter amount to transfer: ";
    cin >> amount;

    if (senderAccount->getBalance() >= amount) {
        senderAccount->withdraw(amount);
        receiverAccount->deposit(amount);

        // Log the transaction
        Account::logTransaction(senderAccNum, receiverAccNum, amount);
        cout << "Transfer successful!\n";
    } else {
        cout << "Insufficient funds for transfer!\n";
    }
}

void CheckAccountBalance(const vector<Account*>& accounts) {
    string accNum;
    cout << "Enter Account Number to access: ";
    cin >> accNum;

    Account* account = findAccount(accounts, accNum);
    if (account) {
        account->checkBalance();
    } else {
        cout << "Account not found!\n";
    }
}

void viewTransactionHistory() {
    ifstream logFile("transaction_history.txt");
    if (logFile.is_open()) {
        string line;
        cout << "\nTransaction History:\n";
        while (getline(logFile, line)) {
            cout << line << endl;
        }
        logFile.close();
    } else {
        cout << "Error opening transaction history file.\n";
    }
}

int main() {
    vector<Account*> accounts;
    int choice;

    do {
        displayMenu();
        cout << "Enter your choice: ";
        cin >> choice;

        string accNum, holder;
        double initialBalance;
        Account* account = nullptr;

        switch (choice) {
        case 1: {
            cout << "Create Savings Account:\n";
            cout << "Enter Account Number: ";
            cin >> accNum;
            cout << "Enter Account Holder Name: ";
            cin.ignore();
            getline(cin, holder);
            cout << "Enter Initial Balance: ";
            cin >> initialBalance;
            account = new SavingsAccount(accNum, holder, initialBalance);
            accounts.push_back(account);
            account->saveToFile();
            cout << "Savings Account created successfully!\n";
            break;
        }
        case 2: {
            cout << "Create Checking Account:\n";
            cout << "Enter Account Number: ";
            cin >> accNum;
            cout << "Enter Account Holder Name: ";
            cin.ignore();
            getline(cin, holder);
            cout << "Enter Initial Balance: ";
            cin >> initialBalance;
            account = new CheckingAccount(accNum, holder, initialBalance);
            accounts.push_back(account);
            account->saveToFile();
            cout << "Checking Account created successfully!\n";
            break;
        }
        case 3: {
            transferMoney(accounts);
            break;
        }
        case 4: {
            viewTransactionHistory();
            break;
        }
        case 5: {
            CheckAccountBalance(accounts);
            break;
        }
        case 6:
            cout << "Exiting program.\n";
            break;
        default:
            cout << "Invalid choice! Try again.\n";
        }
    } while (choice != 6);

    // Clean up allocated memory
    for (auto acc : accounts) {
        delete acc;
    }

    return 0;
}

