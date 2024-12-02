#include <iostream>
#include <string>
#include <iomanip>
using namespace std;

class Health_Information {
};
class Extended_Health_Profile : public Health_Information {
private:

    string name; // Variable declarations
    double weight;
    double height; 
    double body_temperature;
    double blood_sugar;


// Calculate BMI
    double BMI() const { 
        return weight / (height * height); 


// Categorised by status
    string bmi_category() {
        double bmi = BMI();
        if (bmi < 18.5) 
        return "Underweight";

        else if (bmi < 24.9) 
        return "Normal weight";

        else if (bmi < 29.9)
        return "Overweight";

        else return "Obese";
    }

    string temperature_status() {
        if (body_temperature < 36.1) 
        return "Low";

        else if (body_temperature <= 37.2) 
        return "Normal";

        else return "High";
    }

    string blood_sugar_status() {
        if (blood_sugar < 70) 
        return "Low";
        else if (blood_sugar <= 140) 
        return "Normal";

        else return "High";
    }



    string generate_feedback() {
        stringstream feedback; 
        feedback << "As of the vital statistics of " << name << " are as follows:\n"
                 << "The Body Mass Index (BMI) is " << fixed << setprecision(2) <<BMI() 
                 << ", which falls into the category of " << bmi_category() << ".\n"
                 << "The body temperature is " << fixed << setprecision(2) << body_temperature 
                 << " C, indicating that the status is " << temperature_status() << ".\n"
                 << "The blood sugar level is " << fixed << setprecision(2) << blood_sugar
                 << " mg/dL, which is considered " << blood_sugar_status() << ".";
        return feedback.str();
    }
    


    void displayProfile(){
        cout << setw(15) << "Name: " << name << "\n"
             << setw(15) << "Weight: " << weight << " kg\n"
             << setw(15) << "Height: " << height << " m\n"
             << setw(15) << "BMI: " << fixed << setprecision(2) << BMI() << " (" << bmi_category() << ")\n"
             << setw(15) << "Temperature: " << body_temperature << " °C (" << temperature_status() << ")\n"
             << setw(15) << "Blood Sugar: " << blood_sugar << " mg/dL (" << blood_sugar_status() << ")\n\n";
    }
};
