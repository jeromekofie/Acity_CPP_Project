#ifndef HEALTHPROFILEMANAGER_H
#define HEALTHPROFILEMANAGER_H

#include <vector>
#include <fstream>
#include <sstream>
#include <iostream>
#include <limits>
#include <iomanip>
#include <algorithm>
#include "r1.cpp"
 using namespace std;
class Health_Profile_Manager {
private:
    vector<Health_Profile> profiles; //A dynamic array that manages the collection of data 

public:
    // Menu
    void menu() {
        int choice;
        do {
            cout << "\n--- Health Profile Manager ---\n";
            cout << "This is necessary for doctors to keep track of patient health profiles." << endl;
            cout << "Please select an option:" << endl;
            cout << "1. Create Profile\n";
            cout << "2. View Profile(s)\n";
            cout << "3. Update Profile\n";
            cout << "4. Save Profiles to File\n";
            cout << "5. Load Profiles from File\n";
            cout << "6. Exit\n";
            cout << "Choose an option to proceed: ";
            cin >> choice;

            if (cin.fail()) {
                cin.clear();
                cin.ignore(numeric_limits<streamsize>::max(), '\n');
                cout << "Invalid input. Please enter a number.\n";
                continue; // use of continue to skip the rest of the loop and start again
            }

            switch (choice) { //use of switch case to handle different menu options
                case 1:
                    create_profile();
                    break;
                case 2:
                    view_profiles();
                    break;
                case 3:
                    update_profile();
                    break;
                case 4:
                    saveToFile();
                    break;
                case 5:
                    loadFromFile();
                    break;
                case 6:
                    cout << "Exiting program...\n";
                    break; //use of break to exit the switch case and the loop
                default:
                    cout << "Invalid choice. Try again.\n";
            }
        } while (choice != 6); //use of do-while loop to keep the menu running until the user chooses to exit
    }

    // Create a Profile
    void create_profile() { //Functions to manage profiles
        string name;
        double weight, height, temperature, sugar;
        int systolic, diastolic;

        cout << "Enter name: ";
        cin >> name;

        cout << "Enter weight (kg): ";
        while (!(cin >> weight) || weight <= 0) {
            cout << "Invalid input. Enter a positive weight (kg): ";
            cin.clear();
            cin.ignore(numeric_limits<streamsize>::max(), '\n'); // When you want to clear any errors and unwanted inputs
        }

        cout << "Enter height (m): ";
        while (!(cin >> height) || height <= 0) { // use of input validations
            cout << "Invalid input. Enter a positive height (m): ";
            cin.clear();
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
        }

        cout << "Enter body temperature (°C): ";
        while (!(cin >> temperature) || temperature <= 0) {
            cout << "Invalid input. Enter a positive body temperature (°C): ";
            cin.clear();
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
        }

        cout << "Enter blood sugar level (mg/dL): ";
        while (!(cin >> sugar) || sugar < 0) {
            cout << "Invalid input. Enter a valid blood sugar level (mg/dL): ";
            cin.clear();
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
        }

        cout << "Enter systolic blood pressure: ";
        while (!(cin >> systolic) || systolic <= 0) {
            cout << "Invalid input. Enter a valid systolic BP: ";
            cin.clear();
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
        }

        cout << "Enter diastolic blood pressure: ";
        while (!(cin >> diastolic) || diastolic <= 0) {
            cout << "Invalid input. Enter a valid diastolic BP: ";
            cin.clear();
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
        }

        profiles.emplace_back(name, weight, height, temperature, sugar, systolic, diastolic); // Add the profile to the vector
        cout << "Profile created successfully!\n";
    }

    // View Profiles
    void view_profiles() {
        string name;
        cout << "Enter the name(s) of profiles to view (comma-separated for multiple): ";
        cin.ignore();
        getline(cin, name); //to read the whole line

        vector<string> selected_profiles; //here the vector is named as selected_profiles
        stringstream ss(name); //to facilitate the splitting of the string into a vector of strings
        string token; //to store each token (name) from the string

        while (getline(ss, token, ',')) {
            selected_profiles.push_back(token);
        }

        for (const auto& profile_name : selected_profiles) {
            auto it = find_if(profiles.begin(), profiles.end(),
                              [&](const Health_Profile& profile) { return profile.get_name() == profile_name; });

            if (it != profiles.end()) {
                it->display_profile();
            } else {
                cout << "Profile with name '" << profile_name <<"Profile with name '" << profile_name << "' not found.\n";
            }
        }
    }

    // Update a Profile
    void update_profile() {
        string name;
        cout << "Enter the name of the profile to update: ";
        cin >> name;

        auto it = find_if(profiles.begin(), profiles.end(),
                          [&](const Health_Profile& profile) { return profile.get_name() == name; });

        if (it != profiles.end()) {
            double weight, height, temperature, sugar;
            int systolic, diastolic;

            cout << "Enter new weight (kg): ";
            while (!(cin >> weight) || weight <= 0) {
                cout << "Invalid input. Enter a positive weight (kg): ";
                cin.clear();
                cin.ignore(numeric_limits<streamsize>::max(), '\n');
            }

            cout << "Enter new height (m): ";
            while (!(cin >> height) || height <= 0) {
                cout << "Invalid input. Enter a positive height (m): ";
                cin.clear();
                cin.ignore(numeric_limits<streamsize>::max(), '\n');
            }

            cout << "Enter new body temperature (°C): ";
            while (!(cin >> temperature) || temperature <= 0) {
                cout << "Invalid input. Enter a positive body temperature (°C): ";
                cin.clear();
                cin.ignore(numeric_limits<streamsize>::max(), '\n');
            }

            cout << "Enter new blood sugar level (mg/dL): ";
            while (!(cin >> sugar) || sugar < 0) {
                cout << "Invalid input. Enter a valid blood sugar level (mg/dL): ";
                cin.clear();
                cin.ignore(numeric_limits<streamsize>::max(), '\n');
            }

            cout << "Enter new systolic blood pressure: ";
            while (!(cin >> systolic) || systolic <= 0) {
                cout << "Invalid input. Enter a valid systolic BP: ";
                cin.clear();
                cin.ignore(numeric_limits<streamsize>::max(), '\n');
            }

            cout << "Enter new diastolic blood pressure: ";
            while (!(cin >> diastolic) || diastolic <= 0) {
                cout << "Invalid input. Enter a valid diastolic BP: ";
                cin.clear();
                cin.ignore(numeric_limits<streamsize>::max(), '\n');
            }

            it->set_weight(weight);
            it->set_height(height);
            it->set_temperature(temperature);
            it->set_blood_sugar(sugar);
            it->set_blood_pressure(systolic, diastolic);

            cout << "Profile updated successfully!\n";
        } else {
            cout << "Profile with name '" << name << "' not found.\n";
        }
    }

    // Save Profiles to File
    void saveToFile() {
        ofstream file("health_profiles.txt");
        if (!file) {
            cerr << "Error opening file for saving.\n";
            return;
        }
//writing to file
        for (const auto& profile : profiles) {
            file << "Name: " << profile.get_name() << "\n";
            file << "Weight: " << profile.get_weight() << "Kg\n";
            file << "Height: " << profile.get_height() << " m\n";
            file << "Body Temperature: " << profile.get_temperature()<< " oC\n";
            file << "Blood Sugar Level: " << profile.get_blood_sugar() << " mg/dL\n";
            file << "Blood Pressure: " << profile.get_systolic_BP() << "/" << profile.get_diastolic_BP() << "\n";
            file << "BMI: " << profile.calculate_BMI() << " (" << profile.bmi_category() << ")\n";
            file << "---------------------------------\n";
        }

        cout << "Profiles saved to 'health_profiles.txt'.\n";
    }

    // Load Profiles from File
    void loadFromFile() {
        ifstream file("health_profiles.txt");
        if (!file) {
            cerr << "Error opening file for loading.\n";
            return;
        }

        profiles.clear();

        string name, line;
        double weight, height, temperature, sugar;
        int systolic, diastolic;
//what's special in our code
//line by line explained
//read from file
        while (getline(file, line)) {//getline function reads the line and stores in a variable called line
            if (line.find("Name: ") == 0) {//checks if the line starts with a string
                name = line.substr(6);//converts the weight from string to a double
                getline(file, line);//reads the next line
                weight = stod(line.substr(8));//the substr(8) skips the first 8 characters
                getline(file, line);
                height = stod(line.substr(8));
                getline(file, line);
                temperature = stod(line.substr(18));
                getline(file, line);
                sugar = stod(line.substr(18));
                getline(file, line);
                sscanf(line.c_str(), "Blood Pressure: %d/%d", &systolic, &diastolic);//the sscanf function parses the string and stores the values in the variables
                //creates a new profile and adds it to the vector
                profiles.emplace_back(name, weight, height, temperature, sugar, systolic, diastolic);
                getline(file, line); // Skip separator line
            }
        }

        cout << "Profiles loaded from 'health_profiles.txt'.\n";
    }
};

#endif

