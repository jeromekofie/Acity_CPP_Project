#include <iostream>
#include <string>
#include <cmath>
#include <iomanip>
#include <sstream>
using namespace std;

// Base Class: Health_Information
class Health_Information {
protected:
    string name; // Variable declarations
    double weight;
    double height;
    double body_temperature;
    double blood_sugar;

public:
    Health_Information(string name, double weight, double height, double body_temperature = 0.0, double blood_sugar = 0.0)
        : name(name), weight(weight), height(height), body_temperature(body_temperature), blood_sugar(blood_sugar) {}

    void set_weight(double w) { weight = w; }
    void set_height(double h) { height = h; }
    void set_body_temperature(double temp) { body_temperature = temp; }
    void set_blood_sugar(double level) { blood_sugar = level; }

    string get_name() { return name; }
    double get_weight() { return weight; }
    double get_height() { return height; }
    double get_body_temperature() { return body_temperature; }
    double get_blood_sugar() { return blood_sugar; }
    double BMI() const { return weight / (height * height); }
};

// Derived Class: Extended_Health_Profile
class Extended_Health_Profile : public Health_Information {
public:
    Extended_Health_Profile(string name, double weight, double height, double body_temperature, double blood_sugar)
        : Health_Information(name, weight, height, body_temperature, blood_sugar) {}

    string bmi_category() const {
        double bmi = BMI();
        if (bmi < 18.5)
            return "Underweight";
        else if (bmi < 24.9)
            return "Normal weight";
        else if (bmi < 29.9)
            return "Overweight";
        else
            return "Obese";
    }

    string temperature_status() const {
        if (body_temperature < 36.1)
            return "Low";
        else if (body_temperature <= 37.2)
            return "Normal";
        else
            return "High";
    }

    string blood_sugar_status() const {
        if (blood_sugar < 70)
            return "Low";
        else if (blood_sugar <= 140)
            return "Normal";
        else
            return "High";
    }

    void display_profile() const {
        cout << setw(15) << "Name: " << name << "\n"
             << setw(15) << "Weight: " << weight << " kg\n"
             << setw(15) << "Height: " << height << " m\n"
             << setw(15) << "BMI: " << fixed << setprecision(2) << BMI() << " (" << bmi_category() << ")\n"
             << setw(15) << "Temperature: " << body_temperature << " °C (" << temperature_status() << ")\n"
             << setw(15) << "Blood Sugar: " << blood_sugar << " mg/dL (" << blood_sugar_status() << ")\n\n";
    }
};

int main() {
    // Create an Extended_Health_Profile object
    Extended_Health_Profile profile("John Doe", 70.0, 1.75, 37.0, 120.0);

    // Display the health profile
    profile.display_profile();

    // Modify some attributes
    profile.set_weight(75.0);
    profile.set_blood_sugar(160.0);

    cout << "\nUpdated Health Profile:\n";
    profile.display_profile();

    return 0;
}
