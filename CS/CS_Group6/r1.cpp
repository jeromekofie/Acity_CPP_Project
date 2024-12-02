#ifndef HEALTHPROFILE_H
#define HEALTHPROFILE_H //header guards 
                        //prevent the repetition of files

#include <string>      //Needed libraries included
#include <iostream>
#include <iomanip>
using namespace std;

class Health_Profile {
protected:
    string name;   //Variable declaration
    double weight;
    double height;
    double body_temperature;
    double blood_sugar;
    int systolic_BP;
    int diastolic_BP;

public:
    //constructors
    //The primary purpose of a constructor is to initialize the object's properties or fields
    //Constructors can take parameters, allowing you to pass values when creating an instance of the class
    
    Health_Profile(string name, double weight, double height, double body_temperature = 0.0, 
                  double blood_sugar = 0.0, int systolic_BP = 120, int diastolic_BP = 80)
        : name(name), weight(weight), height(height), body_temperature(body_temperature),
          blood_sugar(blood_sugar), systolic_BP(systolic_BP), diastolic_BP(diastolic_BP) {}

    // Setters
    void set_weight(double w) { 
        weight = w;
         }
    void set_height(double h) { 
        height = h;
         }
    void set_temperature(double temp) { 
        body_temperature = temp;
         }
    void set_blood_sugar(double sugar) { 
        blood_sugar = sugar;
         }
    void set_blood_pressure(int sys, int dia) { 
        systolic_BP = sys; diastolic_BP = dia;
         }

    // Getters
    string get_name() const { 
        return name; 
        }
    double get_weight() const { 
        return weight; 
        }
    double get_height() const { 
        return height; 
        }
    double get_temperature() const { 
        return body_temperature; 
        }
    double get_blood_sugar() const { 
        return blood_sugar; 
        }
    int get_systolic_BP() const { 
        return systolic_BP; 
        }
    int get_diastolic_BP() const { 
        return diastolic_BP; 
        }

    // Calculate BMI
    double calculate_BMI() const {
        return weight / (height * height);
    }

    // BMI Category
    string bmi_category() const {
        double bmi = calculate_BMI();
        if (bmi < 18.5) 
        return "Underweight";

        else if (bmi < 24.9) 
        return "Normal weight";

        else if (bmi < 29.9) 
        return "Overweight";

        else return "Obese";
    }

    // Temperature Status
    string temperature_status() const {
        if (body_temperature < 36.1) 
        return "Low";

        else if (body_temperature <= 37.2) 
        return "Normal";

        else return "High";
    }

    // Blood Sugar Status
    string blood_sugar_status() const {
        if (blood_sugar < 70) 
        return "Low";

        else if (blood_sugar <= 140) 
        return "Normal";

        else return "High";
    }

    // Blood Pressure Status
    string blood_pressure_status() const {
        if (systolic_BP < 120 && diastolic_BP < 80) 
        return "Normal";

        else if (systolic_BP < 130 && diastolic_BP < 80) 
        return "Elevated";

        else if (systolic_BP <= 139 || diastolic_BP <= 89) 
        return "Hypertension Stage 1";
        
        else return "Hypertension Stage 2";
    }

    // Display Profile
    void display_profile() const {
        cout << fixed << setprecision(2);// use of set precision to display 2 decimal places
        cout << "---------------------------------\n";
        cout << "Name            : " << name << "\n";
        cout << "Weight          : " << weight << " kg\n";
        cout << "Height          : " << height << " m\n";
        cout << "Body Temperature: " << body_temperature << " °C (" << temperature_status() << ")\n";
        cout << "Blood Sugar     : " << blood_sugar << " mg/dL (" << blood_sugar_status() << ")\n";
        cout << "Blood Pressure  : " << systolic_BP << "/" << diastolic_BP << " (" << blood_pressure_status() << ")\n";
        cout << "BMI             : " << calculate_BMI() << " (" << bmi_category() << ")\n";
        cout << "---------------------------------\n";
    }
};

#endif
