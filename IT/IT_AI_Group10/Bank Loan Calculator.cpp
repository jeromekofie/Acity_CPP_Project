#include <iostream>
#include <cmath> // For pow
#include <iomanip> // For set precision. The decimal place.
#include <fstream> // Save to File


using namespace std;


int main(){

    cout <<" Bank Loan Calculator (Know Your Debts!!)"<< endl;
    cout << "****************************************" << endl; 

}

class Loan{
	private: // Only accessible to the loan class.
		double principal;   // What you borrowed        
    	double annualInterestRate; //Annual charge on what you borrowed in percentage
    	int duration;         // Time in years             
    	double monthlyPayment;  //What to pay monthly to cover the loan
    
    
    public: // Accessible anywhere in the code //Constructor
    	Loan(double p, double r, int d)
    	:principal(p), annualInterestRate(r) , duration(d), monthlyPayment(0.0)// Explicitly initializing monthly payment to avoid errors during calculations.
    	// Sets up basic info about the loan
    	//Takes the values for the loan and fills in the details.
    	
    	
    	 /*
     	 calculateMonthlyPayment
      	Calculates the fixed monthly payment using the formula for an annuity.
      	Formula: M = P * (r(1+r)^n) / ((1+r)^n - 1), where:
      	- M = Monthly Payment
     	 - P = Principal
     	 - r = Monthly interest rate
    	  - n = Total number of payments (months)
    	 */
    	 
    void calculateMonthlyPayment() {
        double monthlyRate = annualInterestRate / 12 / 100; //From years to months
        int totalPayments = duration * 12;                 // Total number of payments in mothns

        if (monthlyRate == 0) {
            // When the interest rate is 0
            monthlyPayment = principal / totalPayments;
        } else {
            // When the loan comes with interest
            monthlyPayment = principal * (monthlyRate * pow(1 + monthlyRate, totalPayments)) 
                            / (pow(1 + monthlyRate, totalPayments) - 1);
        }
    }
    

     // displayLoanDetails
    
    void displayLoanDetails() const { // Does not change your loan details
        cout << fixed << setprecision(2); // 2 decimal places
        cout << "Your Loan Details" << endl;
        cout << "*****************" << endl;
        cout << "Principal: $" << principal << endl;
        cout << "Annual Interest Rate: " << annualInterestRate << "%" << endl;
        cout << "Duration: " << duration << " years" << endl;
        cout << "Monthly Payment: $" << monthlyPayment << endl;
    }

    	
    
    	

		
};
