#include <string>
#include <iostream>
using namespace std;

class Profile {
private:
    string name;
    double weight;
    double height;
    double body_temperature;
    double blood_sugar;

public:
    Profile(string name, double weight, double height, double body_temperature, double blood_sugar) 
        : name(name), weight(weight), height(height), body_temperature(body_temperature), blood_sugar(blood_sugar) {}

    string getName() { 
        return name;
    }
    double getWeight() {
        return weight; 
    }
    double getHeight() { 
        return height; 
    }
    double getBodyTemperature() { 
        return body_temperature; 
    }
    double getBloodSugarLevel() { 
        return blood_sugar; 
    }

    void setWeight(double newWeight) { weight = newWeight; }
    void setHeight(double newHeight) { height = newHeight; }
    void setBodyTemperature(double newBodyTemperature) { body_temperature = newBodyTemperature; }
    void setBloodSugarLevel(double newBloodSugarLevel) { blood_sugar = newBloodSugarLevel; }

    void displayProfile() const {
        cout << "Name: " << name << ", Weight: " << weight << "kg, Height: " << height 
             << "m, Body Temperature: " << body_temperature << "°C, Blood Sugar: " << blood_sugar << "mg/dL\n";
    }

    // Dummy methods for feedback and status
    string generateFeedback() const { return "Feedback based on health data."; }
};