#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <cstdlib>
#include <ctime>
#include <iomanip>
using namespace std;

class QuizBase {
protected:
    vector<string> Questions;
    vector<string> Options;
    vector<char> Answers;
    int total_questions;

public:
    QuizBase(int total = 10) : total_questions(total) {}
    void loadQuestions(const string& filename);
    void displayResults(int score) const;
};

class QuizGame : public QuizBase {
public:
    QuizGame(int total = 10) : QuizBase(total) {}
    void startQuiz();
};

void QuizBase::loadQuestions(const string& filename) {
    ifstream file(filename);
    if (!file.is_open()) {
        cerr << "Can't open file: " << filename << endl;
        exit(1);
    }

    string question, option;
    char answer;
    while (getline(file, question)) {
        getline(file, option);
        file >> answer;
        file.ignore();

        Questions.push_back(question);
        Options.push_back(option);
        Answers.push_back(answer);
    }
    file.close();
}

void QuizBase::displayResults(int score) const {
    cout << "\nThe quiz is now over.\n";
    cout << "Score: " << score << "/" << total_questions << endl;

    if (score > (0.5 * total_questions) && score != total_questions) {
        cout << "You tried." << endl;
    } else if (score < (0.5 * total_questions)) {
        cout << "Poor." << endl;
    } else {
        cout << "Excellent work!" << endl;
    }

    float percentage = (score * 100.0) / total_questions;
    cout << "Percentage: " << fixed << setprecision(2) << percentage << "%" << endl;
}

void QuizGame::startQuiz() {
    srand(time(0));
    int score = 0;

    cout << "Your quiz starts now!\n";

    for (int i = 0; i < total_questions; ++i) {
        int randomIndex = rand() % Questions.size();
        cout << "\nQuestion " << (i + 1) << ": " << Questions[randomIndex] << endl;
        cout << Options[randomIndex] << endl;

        char user_answer;
        cout << "Your answer (a-d): ";
        cin >> user_answer;

        if (tolower(user_answer) == Answers[randomIndex]) {
            cout << "CORRECT!" << endl;
            ++score;
        } else {
            cout << "INCORRECT! Correct answer: " << Answers[randomIndex] << endl;
        }
    }

    displayResults(score);
}

void writeQuestionsToFile(const string& filename) {
    ofstream outfile(filename);
    if (!outfile.is_open()) {
        cerr << "Error opening file: " << filename << endl;
        exit(1);
    }

    vector<string> questions = {
        "What is the capital city of Ghana?",
        "Who was the president of Nigeria as of January 2024?",
        "What is the tallest building in the world?",
        "If 2x+3=5, find x.",
        "What year did Nigeria gain independence?",
        "Who won the 2022 World Cup?",
        "Who is the president of the United States of America?",
        "In what year was Google invented?",
        "Who invented C++?",
        "How many seconds are there in a day?"
    };

    vector<string> options = {
        "(a) Agbogba (b) Accra (c) Madina (d) Lagos",
        "(a) Tinubu (b) Buhari (c) Atiku (d) Jonathan",
        "(a) Eiffel Tower (b) Twin Towers (c) Burj Khalifa (d) Acity Tower",
        "(a) 4 (b) 1 (c) 0 (d) 3",
        "(a) 1945 (b) 1932 (c) 1958 (d) 1960",
        "(a) France (b) England (c) Spain (d) Argentina",
        "(a) Kamala Harris (b) Donald Trump (c) Barack Obama (d) Joe Biden",
        "(a) 2003 (b) 1954 (c) 1998 (d) 2005",
        "(a) Kelly (b) Julius (c) Elon Musk (d) Bjarne Stroustrup",
        "(a) 86400 (b) 93680 (c) 24800 (d) 12460"
    };

    vector<char> answers = { 'b', 'a', 'c', 'b', 'd', 'd', 'd', 'c', 'd', 'a' };

    for (size_t i = 0; i < questions.size(); ++i) {
        outfile << questions[i] << endl;
        outfile << options[i] << endl;
        outfile << answers[i] << endl;
    }

    outfile.close();
}

int main() {
    string filename = "Palace.txt";

    writeQuestionsToFile(filename);

    QuizGame game(10);
    game.loadQuestions(filename);
    game.startQuiz();

    return 0;
}
