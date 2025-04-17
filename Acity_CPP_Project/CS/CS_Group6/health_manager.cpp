#include <string>
#include <iostream>
#include <vector>
#include <fstream>
#include <limits>
#include "profile.cpp"
using namespace std;


class Health_manager : private profile {
private:
string profiles;

public:
    void create_profile(string name, double weight, double height, double body_temperature, double blood_sugar) {
        profiles.emplace_back(name, weight, height, body_temperature, blood_sugar);
        cout << "Profile for " << name << " created successfully.\n";
    }

    void readProfile(string name) const {
        for (const auto& profile : profiles) {
            if (profile.getName() == name) {
                profile.displayProfile();
                cout << profile.generateFeedback() << endl;
                return;
            }
        }
        cout << "Profile not found.\n";
    }

    void updateProfile(string name, double new_weight, double new_height, double new_body_temperature, double new_blood_sugar) {
        for (auto& profile : profiles) {
            if (profile.getName() == name) {
                profile.setWeight(new_weight);
                profile.setHeight(new_height);
                profile.setBodyTemperature(new_body_temperature);
                profile.setBloodSugarLevel(new_blood_sugar);
                cout << "Profile updated.\n";
                profile.storeRecord();  
                return;
            }
        }
        cout << "Profile not found.\n";
    }

    void deleteProfile(string name) {
        for (auto it = profiles.begin(); it != profiles.end(); ++it) {
            if (it->getName() == name) {
                profiles.erase(it);
                cout << "Profile deleted.\n";
                return;
            }
        }
        cout << "Profile not found.\n";
    }

    void save_to_file() {
        ofstream file("health_profiles.txt");
        for (auto& profile : profiles) {
            file << profile.get_name() << " " << profile.getWeight() << " " 
                 << profile.get_height() << " " << profile.getBodyTemperature() << " "
                 << profile.get_blood_sugar() << " " << profile.calculateBMI() << " " 
                 << profile.bmiCategory() << " " << profile.temperatureStatus() << " "
                 << profile.bloodSugarStatus() << "\n";
        }
        cout << "Profiles saved to file.\n";
    }

    void load_from_file() {
        ifstream file("health_profiles.txt");
        string name, bmiCategory, tempStatus, sugarStatus;
        double weight, height, bodyTemperature, bloodSugarLevel, bmi;
        profiles.clear();
        while (file >> name >> weight >> height >> bodyTemperature >> bloodSugarLevel >> bmi >> bmiCategory >> tempStatus >> sugarStatus) {
            profiles.emplace_back(name, weight, height, bodyTemperature, bloodSugarLevel);
        }
        cout << "Profiles loaded from file.\n";
    }

    void displayAllProfiles() const {
        for (const auto& profile : profiles) {
            profile.displayProfile();
        }
    }

    void menu() {
        int choice;
        do {
            cout << "\nHealth Profile Manager\n";
            cout << "1. Create Profile\n";
            cout << "2. View Profile\n";
            cout << "3. Update Profile\n";
            cout << "4. Delete Profile\n";
            cout << "5. Save Profiles to File\n";
            cout << "6. Load Profiles from File\n";
            cout << "7. Display All Profiles\n";
            cout << "8. Display Weekly Records\n";
            cout << "9. Exit\n";
            cout << "Choose an option: ";
            cin >> choice;

            if (cin.fail()) {
                cin.clear();
                cin.ignore(numeric_limits<streamsize>::max(), '\n');
                cout << "Invalid input. Please enter a number between 1 and 9.\n";
                continue;
            }

            string name;
            double weight, height, body_temperature, blood_sugar;

            switch (choice) {
                case 1:
                    cout << "Enter name: ";
                    cin >> name;
                    cout << "Enter weight (kg): ";
                    cin >> weight;
                    cout << "Enter height (m): ";
                    cin >> height;
                    cout << "Enter body temperature (°C): ";
                    cin >> body_temperature;
                    cout << "Enter blood sugar level (mg/dL): ";
                    cin >> blood_sugar;
                    createProfile(name, weight, height, body_temperature, blood_sugar);
                    break;
                case 2:
                    cout << "Enter name: ";
                    cin >> name;
                    readProfile(name);
                    break;
                case 3:
                    cout << "Enter name: ";
                    cin >> name;
                    cout << "Enter new weight (kg): ";
                    cin >> weight;
                    cout << "Enter new height (m): ";
                    cin >> height;
                    cout << "Enter new body temperature (°C): ";
                    cin >> body_temperature;
                    cout << "Enter new blood sugar level (mg/dL): ";
                    cin >> blood_sugar;
                    updateProfile(name, weight, height, body_temperature, blood_sugar);
                    break;
                case 4:
                    cout << "Enter name: ";
                    cin >> name;
                    deleteProfile(name);
                    break;
                case 5:
                    save_to_file();
                    break;
                case 6:
                    load_from_file();
                    break;
                case 7:
                    displayAllProfiles();
                    break;
                case 8:
                    cout << "Enter name to display weekly records: ";
                    cin >> name;
                    for (const auto& profile : profiles) {
                        if (profile.getName() == name) {
                            break;
                        }
                    }
                    break;
                case 9:
                    cout << "Exiting\n";
                    break;
                default:
                    cout << "Invalid choice. Please try again.\n";
            }
        } while (choice != 9);
    }
};

int main() {
    Health_manager manager;
    manager.menu();
    return 0;
}
