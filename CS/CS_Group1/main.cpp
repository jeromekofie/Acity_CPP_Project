#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <sstream>

// #include "task_types.h"
// #include "task_with_getters_setters.h"
// #include "task.h"
#include "priority.h"
#include "date.h"


using namespace std;

class Task
{
public:
    string title;
    string description;
    bool isDone;
    Priority priority;
    Date dueDate;

    Task(const string &_title, const string &_description, Priority _priority, const Date &_dueDate)
        : title(_title), description(_description), isDone(false), priority(_priority), dueDate(_dueDate) {}
};

class TaskManager
{
private:
    vector<Task> tasks;
    const string filename = "tasks.txt";

public:
    void addTask(const string &title, const string &description, Priority priority, const Date &dueDate)
    {
        tasks.emplace_back(title, description, priority, dueDate);
    }

    void deleteTask(int index)
    {
        if (index >= 0 && index < tasks.size())
        {
            tasks.erase(tasks.begin() + index);
        }
        else
        {
            cerr << "Invalid task index.\n";
        }
    }

    void markTaskAsDone(int index)
    {
        if (index >= 0 && index < tasks.size())
        {
            tasks[index].isDone = true;
        }
        else
        {
            cerr << "Invalid task index.\n";
        }
    }

    void exportTasks()
    {
        ofstream file(filename);
        if (file.is_open())
        {
            for (const auto &task : tasks)
            {
                // file << task.title << ','
                //      << task.description << ','
                //      << task.isDone << ','
                //      << static_cast<int>(task.priority) << ','
                //      << task.dueDate.day << '/' << task.dueDate.month << '/' << task.dueDate.year << '\n';



                // file << "TITLE : " << task.title << endl;
                // file << "DESCRIPTION : " << task.description << endl;
                // file << "PRIORITY : " << static_cast<int>(task.priority) << endl;
                // file << "DUE DATE : " << task.dueDate.day << "/" << task.dueDate.month << "/" << task.dueDate.year << endl;
                // file << "\n"; 
                // file << "_____________________________" << endl;

                file << "\n";
                file << "TITLE : " << task.title << endl;
                file << "DESCRIPTION : " << task.description << endl;
                file << "PRIORITY : " << static_cast<int>(task.priority) << endl;
                file << "DUE DATE : " << task.dueDate.day << "/" << task.dueDate.month << "/" << task.dueDate.year << endl;
                file << "Status: " << (task.isDone ? "Done" : "Not Done") << '\n';
                file << "\n"; 
                file << "_____________________________" << endl;
                file << "\n";


            }

            file.close();
            cout << "Tasks exported successfully.\n";
        }
        else
        {
            cerr << "Failed to export tasks.\n";
        }
    }

    void importTasks()
    {
        tasks.clear();
        ifstream file(filename);
        if (file.is_open())
        {
            string line;
            while (getline(file, line))
            {
                stringstream ss(line);
                string title, description, isDoneStr, priorityStr, dueDateStr;
                getline(ss, title, ',');
                getline(ss, description, ',');
                getline(ss, isDoneStr, ',');
                getline(ss, priorityStr, ',');
                getline(ss, dueDateStr, ',');

                bool isDone = (isDoneStr == "1");
                try
                {
                    Priority priority = static_cast<Priority>(stoi(priorityStr));
                    int day, month, year;
                    if (sscanf(dueDateStr.c_str(), "%d/%d/%d", &day, &month, &year) != 3)
                    {
                        throw std::invalid_argument("Invalid date format");
                    }
                    Date dueDate(day, month, year);
                    tasks.emplace_back(title, description, priority, dueDate);
                    tasks.back().isDone = isDone;
                }
                catch (const std::invalid_argument &e)
                {
                    cerr << "Error converting priority or due date: " << e.what() << endl;
                }
            }
            file.close();
            cout << "Tasks imported successfully.\n";
        }
        else
        {
            cerr << "Failed to import tasks.\n";
        }
    }

    void listTasks()
    {
        int index = 0;
        for (const auto &task : tasks)
        {
                cout << "\n";
                cout << index << ". ";
                cout << "TITLE : " << task.title << endl;
                cout << "DESCRIPTION : " << task.description << endl;
                cout << "PRIORITY : " << static_cast<int>(task.priority) << endl;
                cout << "DUE DATE : " << task.dueDate.day << "/" << task.dueDate.month << "/" << task.dueDate.year << endl;
                cout << "Status: " << (task.isDone ? "Done" : "Not Done") << '\n';
                index++;
                cout << "\n"; 
                cout << "_____________________________" << endl;
                cout << "\n";

        }
    }
};

enum MenuOptions
{
    ADD_TASK = 1,
    DELETE_TASK,
    MARK_TASK_DONE,
    LIST_TASKS,
    EXPORT_TASKS,
    EXIT
};

int main()
{
    TaskManager taskManager;
    taskManager.importTasks();

    int choice;
    string title, description;
    int priorityChoice, day, month, year;
    do
    {
        cout << "Task Management Application\n";
        cout << ADD_TASK << ". Add Task\n";
        cout << DELETE_TASK << ". Delete Task\n";
        cout << MARK_TASK_DONE << ". Mark Task as Done\n";
        cout << LIST_TASKS << ". List Tasks\n";
        cout << EXPORT_TASKS << ". Export Tasks\n";
        cout << EXIT << ". Exit\n";
        cout << "Enter your choice: ";
        cin >> choice;

        switch (choice)
        {
        case ADD_TASK:
            cout << "Enter task title: ";
            cin.ignore();
            getline(cin, title);
            cout << "Enter task description: ";
            getline(cin, description);
            cout << "Enter task priority (0 for Low, 1 for Medium, 2 for High): ";
            cin >> priorityChoice;
            cout << "Enter due date (day month year): ";
            cin >> day >> month >> year;
            taskManager.addTask(title, description, static_cast<Priority>(priorityChoice), Date(day, month, year));
            break;

        case DELETE_TASK:
            int deleteIndex;
            cout << "Enter task index to delete: ";
            cin >> deleteIndex;
            taskManager.deleteTask(deleteIndex);
            break;

        case MARK_TASK_DONE:
            int markIndex;
            cout << "Enter task index to mark as done: ";
            cin >> markIndex;
            taskManager.markTaskAsDone(markIndex);
            break;

        case LIST_TASKS:
            taskManager.listTasks();
            break;

        case EXPORT_TASKS:
            taskManager.exportTasks();
            break;

        case EXIT:
            break;

        default:
            cout << "Invalid choice. Please try again.\n";
            break;
        }
    } while (choice != EXIT);

    return 0;
}
