int main() {
    double principal;
    double annualInterestRate;
    int durationYears;

    cout << "=== Bank Loan Calculator ===" << endl;
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
    string filename = "loan_data.txt";
    loan.saveToFile(filename);

    cout << "Loan details saved to '" << filename << "'." << endl;

    return 0;
}