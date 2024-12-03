#include <iostream>
#include <fstream>
#include <iomanip> // For setprecision
#include <cmath>   // For pow

using namespace std;

// Loan class to handle loan details and calculations
class Loan {
private:
    double principal;
    double annualInterestRate;
    int durationYears;

public:
    // Constructor
    Loan(double p, double rate, int years)
        : principal(p), annualInterestRate(rate), durationYears(years) {}

    // Function to calculate monthly payment
    double calculateMonthlyPayment() const {
        double monthlyRate = annualInterestRate / 12 / 100;
        int totalMonths = durationYears * 12;
        return (principal * monthlyRate * pow(1 + monthlyRate, totalMonths)) /
               (pow(1 + monthlyRate, totalMonths) - 1);
    }

    // Function to calculate total payment
    double calculateTotalPayment() const {
        return calculateMonthlyPayment() * durationYears * 12;
    }

    // Function to save loan details to a file
    void saveToFile(const string &filename) const {
        ofstream file(filename, ios::app); // Append mode
        if (file.fail()) {
            cerr << "Error: Unable to open file '" << filename << "'." << endl;
            return;
        }

        file << fixed << setprecision(2);
        file << "Principal: $" << principal << "\n";
        file << "Annual Interest Rate: " << annualInterestRate << "%\n";
        file << "Duration: " << durationYears << " years\n";
        file << "Monthly Payment: $" << calculateMonthlyPayment() << "\n";
        file << "Total Payment: $" << calculateTotalPayment() << "\n";
        file << "-----------------------------------\n";
        file.close();

        cout << "Loan details saved to '" << filename << "'." << endl;
    }

    // Display loan details
    void displayDetails() const {
        cout << fixed << setprecision(2);
        cout << "Principal: $" << principal << endl;
        cout << "Annual Interest Rate: " << annualInterestRate << "%" << endl;
        cout << "Duration: " << durationYears << " years" << endl;
        cout << "Monthly Payment: $" << calculateMonthlyPayment() << endl;
        cout << "Total Payment: $" << calculateTotalPayment() << endl;
        cout << "-----------------------------------" << endl;
    }
};

int main() {
    double principal;
    double annualInterestRate;
    int durationYears;
    string filename = "loan_data.txt";
    char choice;

    cout << "=== Bank Loan Calculator ===" << endl;

    do {
        // Get loan details from user
        cout << "Enter loan principal amount: $";
        cin >> principal;
        cout << "Enter annual interest rate (in %): ";
        cin >> annualInterestRate;
        cout << "Enter loan duration (in years): ";
        cin >> durationYears;

        // Create a Loan object
        Loan loan(principal, annualInterestRate, durationYears);

        // Display loan details
        loan.displayDetails();

        // Save loan details to a file
        loan.saveToFile(filename);

        cout << "Loan details saved to '" << filename << "'." << endl;

        // Ask if the user wants to calculate another loan
        cout << "Do you want to calculate another loan? (Y/N): ";
        cin >> choice;

        // Convert choice to uppercase for consistency
        choice = toupper(choice);

    } while (choice == 'Y'); // Continue if user enters 'Y'

    cout << "Thank you for using the Bank Loan Calculator!" << endl;
    return 0;
}