#include <iostream>
#include <vector>
#include <string>
using namespace std;

class KeywordSearcher {
private:
    vector<pair<int, string>> results;

public:
    void search(vector<string>lines,  string keyword) {
        results.clear();
        for (size_t i = 0; i < lines.size(); ++i) {
            if (lines[i].find(keyword) != string::npos) {
                results.push_back({static_cast<int>(i + 1), lines[i]});
            }
        }
    }

    vector<string> getResults() {
        vector<string> extractedResults;
        for ( auto res : results) {
            extractedResults.push_back("Line " + to_string(res.first) + ": " + res.second);
        }
        return extractedResults;
    }

    void navigateResults() {
        if (results.empty()) {
            cout << "No occurrences found." << endl;
            return;
        }

        int currentResult = 0;
        char choice = 'n';

        while (true) {
            cout << "Line " << results[currentResult].first << ": " << results[currentResult].second << endl;

            cout << "Options: (n)ext, (p)revious, (q)uit: ";
            cin >> choice;

            if (choice == 'n') {
                if (currentResult < results.size() - 1) {
                    currentResult++;
                } else {
                    cout << "No more results." << endl;
                }
            } else if (choice == 'p') {
                if (currentResult > 0) {
                    currentResult--;
                } else {
                    cout << "No previous results." << endl;
                }
            } else if (choice == 'q') {
                break;
            } else {
                cout << "Invalid option. Please try again." << endl;
            }
        }
    }
};
