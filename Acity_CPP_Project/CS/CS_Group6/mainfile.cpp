#include <iostream>
#include "classhealthprofile.cpp"
#include "health_manager.cpp"
using namespace std;


int main(){
Health_Information info;
    string name; // Variable declarations
    double weight;
    double height; 
    double body_temperature;
    double blood_sugar;

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
        if (bloodSugarLevel < 70) 
        return "Low";
        else if (blood_sugar <= 140) 
        return "Normal";

        else return "High";
    }

    string get_current_date() {
        time_t t = time(0);
        struct tm * now = localtime(&t);
        string date = to_string(now->tm_year + 1900) + "-" + to_string(now->tm_mon + 1) + "-" + to_string(now->tm_mday);
        return date;
    }

    string generate_feedback() {
        stringstream feedback;
        feedback << "As of " << get_current_date() << ", the vital statistics of " << name << " are as follows:\n"
                 << "The Body Mass Index (BMI) is " << fixed << setprecision(2) <<BMI() 
                 << ", which falls into the category of " << bmi_category() << ".\n"
                 << "The body temperature is " << fixed << setprecision(2) << body_temperature 
                 << " C, indicating that the status is " << temperature_status() << ".\n"
                 << "The blood sugar level is " << fixed << setprecision(2) << blood_sugar
                 << " mg/dL, which is considered " << blood_sugar_status() << ".";
        return feedback.str();
    }
    
    void store_record() {
        string feedback = generate_feedback();
        weeklyRecord.push_back(feedback);
        if (weeklyRecord.size() > 7) {
            weeklyRecord.erase(weeklyRecord.begin());
        }
    }

    void displayWeeklyRecords() {
        cout << "Weekly records for " << name << ":\n";
        for (const string& record : weeklyRecord) {
            cout << record << "\n";
        }
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