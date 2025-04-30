#include "Employee.h"

// Implementation of Employee struct methods
void Employee::display() const {
    cout << "ID: " << setw(3) << setfill('0') << id << endl // setw
         << "Name: " << name << endl
         << "Position: " << position << endl
         << "Department: " << department << endl
         << "Salary Type: " << salaryType << endl
         << "Salary: $" << fixed << setprecision(2) << salary << endl;
}

// Implementation of EmployeeManager class methods
EmployeeManager::EmployeeManager() {
    ifstream inFile(filename);
    if (!inFile) return; // File doesn't exist yet

    Employee emp;
    string line;
    while (getline(inFile, line)) {
        if (line.find("ID:") != string::npos) {
            emp.id = stoi(line.substr(4));
            getline(inFile, emp.name);
            getline(inFile, emp.position);
            getline(inFile, emp.department);
            getline(inFile, emp.salaryType);
            getline(inFile, line); // Read salary line
            emp.salary = stod(line.substr(9)); // Parse salary
            employees.push_back(emp);
            getline(inFile, line); // Read the extra blank line between records
        }
    }
}

void EmployeeManager::saveToFile() { //function
    ofstream outFile(filename);
    if (!outFile) throw runtime_error("Unable to open file for saving!"); // Exceptions

    for (const auto& emp : employees) {
        outFile << "ID: " << setw(3) << setfill('0') << emp.id << endl
                << "Name: " << emp.name << endl
                << "Position: " << emp.position << endl
                << "Department: " << emp.department << endl
                << "Salary Type: " << emp.salaryType << endl
                << "Salary: $" << fixed << setprecision(2) << emp.salary << endl;
    }
}

void EmployeeManager::addEmployee() {
    Employee emp;
    cout << "Enter Employee Details" << endl;
    cout << "ID: ";
    cin >> emp.id;
    cin.ignore(); // Ignore leftover newline from previous input
    cout << "Name: ";
    getline(cin, emp.name);
    cout << "Position: ";
    getline(cin, emp.position);
    cout << "Department: ";
    getline(cin, emp.department);
    cout << "Salary Type (Salaried/Hourly): ";
    cin >> emp.salaryType;
    cout << "Salary: ";
    cin >> emp.salary;

    employees.push_back(emp);
    saveToFile();
    cout << "Employee added successfully!" << endl;
}

void EmployeeManager::displayAllEmployees() const {
    for (const auto& emp : employees) {
        emp.display();
    }
}

void EmployeeManager::displayEmployeeById() const {
    int id;
    cout << "Enter Employee ID: ";
    cin >> id;

    auto it = find_if(employees.begin(), employees.end(), // bool functions
                      [id](const Employee& emp) { return emp.id == id; });

    if (it != employees.end()) {
        it->display();
    } else {
        cout << "Employee not found." << endl;
    }
}

void EmployeeManager::editEmployee() {
    int id; // local variable
    cout << "Enter Employee ID to edit: ";
    cin >> id;

    auto it = find_if(employees.begin(), employees.end(),
                      [id](const Employee& emp) { return emp.id == id; });

    if (it != employees.end()) {
        cout << "Editing Employee ID " << id << endl;

        char choice;
        bool editDone = false;

        do { // menu driven
            cout << "\nSelect field to edit:\n";
            cout << "1. Name" << endl;
            cout << "2. Department" << endl;
            cout << "3. Position" << endl;
            cout << "4. Salary Type" << endl;
            cout << "5. Salary" << endl;
            cout << "6. Done editing" << endl;
            cout << "Enter your choice: ";
            cin >> choice;

            switch (choice) {
                case '1':
                    cout << "Enter new Name: ";
                    cin.ignore();
                    getline(cin, it->name);
                    editDone = true;
                    break; // break concepts

                case '2':
                    cout << "Enter new Department: ";
                    cin.ignore();
                    getline(cin, it->department);
                    editDone = true;
                    break;

                case '3':
                    cout << "Enter new Position: ";
                    cin.ignore();
                    getline(cin, it->position);
                    editDone = true;
                    break;

                case '4':
                    cout << "Enter new Salary Type (Salaried/Hourly): ";
                    cin >> it->salaryType;
                    editDone = true;
                    break;

                case '5':
                    cout << "Enter new Salary: ";
                    cin >> it->salary;
                    editDone = true;
                    break;

                case '6':
                    cout << "Done editing Employee ID " << id << endl;
                    break;

                default:
                    cout << "Invalid choice, please try again." << endl;
            }
        } while (choice != '6');

        if (editDone) {
            saveToFile();
            cout << "Employee updated successfully." << endl;
        } else {
            cout << "No changes were made." << endl;
        }
    } else {
        cout << "Employee not found." << endl;
    }
}

void EmployeeManager::deleteEmployee() {
    int id;
    cout << "Enter Employee ID to delete: ";
    cin >> id;

    auto it = remove_if(employees.begin(), employees.end(),
                        [id](const Employee& emp) { return emp.id == id; });

    if (it != employees.end()) {
        employees.erase(it, employees.end());
        saveToFile();
        cout << "Employee deleted successfully." << endl;
    } else {
        cout << "Employee not found." << endl;
    }
}

const vector<Employee>& EmployeeManager::getEmployees() const {
    return employees;
}