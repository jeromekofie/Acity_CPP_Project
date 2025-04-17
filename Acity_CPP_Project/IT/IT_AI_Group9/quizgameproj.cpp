#include <iostream>
#include <vector>
#include <cstdlib>
#include <ctime>
#include <fstream>
#include <string>
#include <algorithm>
#include <iomanip>

using namespace std;

class Quiz {
public:
    string question;
    vector<string> options;
    string correctAnswer;

    // Constructor
    Quiz(const string& q, const vector<string>& option, const string& ans)
        : question(q), options(option), correctAnswer(ans) {}

    // Display the question and options
    void display() {
        cout << "\n" << question << endl;
        for (size_t i = 0; i < options.size(); ++i) {
            cout << options[i] << endl;
        }
    }

    // Validate the user's answer 
    bool isCorrect(string answer) {
        // Convert both user answer and correct answer to uppercase for comparison
        transform(answer.begin(), answer.end(), answer.begin(), ::toupper);
        string correct = correctAnswer;
        transform(correct.begin(), correct.end(), correct.begin(), ::toupper);
        return answer == correct;
    }
};

class User {
public:
    string username;
    int score;

    // Constructor
    User(string name) : username(name), score(0) {}

    // Function to update the score
    void updateScore(int points) {
        score += points;
    }

    // Function to display the final score
    void displayScore() {
        cout << "\n" << username << ", your total score is: " << score << endl;
    }
};

// Function to generate a random question
Quiz generateRandomQuestions() {
    int randques = rand() % 10;
    if (randques == 0) {
        return Quiz("What is 2 to the power 3?",
                    {"A. 6", "B. 7", "C. 8", "D. 9"}, "C");
    } else if (randques == 1) {
        return Quiz("What is the capital of France?",
                    {"A. Berlin", "B. Paris", "C. London", "D. Rome"}, "B");
    } else if (randques == 2) {
        return Quiz("What is the square root of 25?",
                    {"A. 4", "B. 32", "C. 5", "D. 2"}, "C");
    } else if (randques == 3) {
        return Quiz("What is the largest planet in our solar system?",
                    {"A. Earth", "B. Saturn", "C. Jupiter", "D. Mars"}, "C");
    } else if (randques == 4) {
        return Quiz("What is the smallest country in the world?",
                    {"A. Vatican City", "B. Monaco", "C. Nauru", "D. Tuvalu"}, "A");
    } else if (randques == 5) {
        return Quiz("What is the largest mammal?",
                    {"A. Blue whale", "B. Elephant", "C. Hippopotamus", "D. Rhino"}, "A");
    } else if (randques == 6) {
        return Quiz("Who wrote 'Romeo and Juliet'?",
                    {"A. Charles Dickens", "B. William Shakespeare", "C. J.K. Rowling", "D. Mark Twain"}, "B");
    } else if (randques == 7) {
        return Quiz("Which element has the chemical symbol O?",
                    {"A. Oxygen", "B. Osmium", "C. Oganesson", "D. Orlon"}, "A");
    } else if (randques == 8) {
        return Quiz("What is the boiling point of water at sea level?",
                    {"A. 90°C", "B. 100°C", "C. 120°C", "D. 80°C"}, "B" );
    } else {
        return Quiz("Which country is known as the Land of the Rising Sun?",
                    {"A. China", "B. Japan", "C. South Korea", "D. Thailand"}, "B");
    }
}

// Function to display the menu
void DisplayMenu() {
    cout << "\nQuiz Game Menu\n";
    cout << "1. Start Quiz\n";
    cout << "2. Exit\n";
    cout << "Enter your choice: ";
}

// Function to save the score to a file
void saveScoreToFile(const User& user) {
    ofstream file("scores.txt", ios::app);
    if (file.is_open()) {
        file << left << setw(20) << "Username" << "Score\n";
        file << string(30, '-') << "\n";
        file << left << setw(20) << user.username << user.score << "\n";
        file.close();
    } else {
        cerr << "Unable to open file for saving scores.\n";
    }
}

// Function to save answers to a separate file
void saveAnswersToFile(const vector<Quiz>& questions) {
    ofstream file("answers.txt", ios::app);
    if (file.is_open()) {
        file << left << setw(50) << "Question" << "Correct Answer\n";
        file << string(70, '-') << "\n";
        for (const auto& question : questions) {
            file << left << setw(50) << question.question
                 << question.correctAnswer << "\n";
        }
        file.close();
    } else {
        cerr << "Unable to open file for saving answers.\n";
    }
}

int main() {
    srand(static_cast<unsigned>(time(0)));
    int choice;
    do {
        DisplayMenu();
        cin >> choice;

        if (choice == 1) {
            string name;
            cout << "\nEnter your name: ";
            cin >> name;

            cout << "\nWelcome " << name << " to the quiz game\n";
            cout << "You will be asked 10 questions\n";
            cout << "Take Your time and answer them!\n";
            cout << "Good luck!\n";

            User user(name);
            vector<Quiz> questions;

            for (int i = 0; i < 10; i++) {
                Quiz quiz = generateRandomQuestions();
                questions.push_back(quiz);
                quiz.display();

                string answer;
                cout << "Your Answer: ";
                cin >> answer;

                if (quiz.isCorrect(answer)) {
                    cout << "Correct!\n";
                    user.updateScore(10);
                } else {
                    cout << "Incorrect.\n";
                }
            }

            user.displayScore();
            saveScoreToFile(user);

            // Display all correct answers
            cout << "\nHere are the correct answers:\n";
            for (const auto& question : questions) {
                cout << question.question << " - Correct Answer: " << question.correctAnswer << "\n";
            }

            // Save answers to a file
            saveAnswersToFile(questions);

            // Exit the program after completing the quiz
            break;
        }
    } while (choice != 2);

    cout << "\nThank you for playing the quiz game. Goodbye!\n";
    return 0;
}
