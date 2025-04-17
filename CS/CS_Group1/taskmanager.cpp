// Task Manager Implementation

#include "taskmanager.h"
#include <iostream>
#include <fstream>
#include <iomanip>
#include <ctime>

TaskManager::TaskManager()
{
    loadTasks();
};

TaskManager::~TaskManager()
{
    saveTasks();
};

void TaskManager::addTask(const Task &task)
{
    tasks.push_back(task);
};

void TaskManager::markTaskCompleted(size_t index)
{
    if (index < tasks.size())
    {
        tasks[index].setCompleted(true);
    }
};

void TaskManager::displayTasks() const
{
    if (tasks.empty())
    {
        cout << "No Tasks Available. \n"; // escape sequence used
        return;
    }

    cout << "\n Task List: \n";
    cout << setw(5) << "ID" << setw(20) << "TITLE" << endl;
    cout << setw(15) << "Priority" << setw(25) << "Deadline" << endl;
    cout << setw(10) << "Status" << "\n";

    cout << string(75, '-') << "\n";

    for (size_t i = 0; i < tasks.size(); ++i)
    {
        const Task &task = tasks[i];
        const time_t& deadlineTime  = task.getDeadline();
        string deadline = ctime(&deadlineTime);
        deadline = deadline.substr(0, deadline.length() - 1); // To remove the new line

        cout << setw(5) << i
             << setw(20) << task.getTitle()
             << setw(15) << task.getPriorityString()
             << setw(25) << deadline
             << setw(10) << (task.isCompleted() ? "Done" : "Pending")
             << "\n";
    }
};

void TaskManager::saveTasks() const
{
    ofstream file(filename, ios::binary);
    if (!file)
    {
        cerr << "Error: Could not open file for writing\n";
        return;
    }

    size_t size = tasks.size();
    file.write(reinterpret_cast<const char *>(&size), sizeof(size));

    for (const Task &task : tasks)
    {
        string title = task.getTitle();
        string desc = task.getDescription();
        Priority priority = task.getPriority();
        time_t deadline = task.getDeadline();
        bool completed = task.isCompleted();

        size_t titleLen = title.length();
        size_t descLen = desc.length();
        file.write(reinterpret_cast<const char *>(&titleLen), sizeof(titleLen));
        file.write(title.c_str(), titleLen);
        file.write(reinterpret_cast<const char *>(&descLen), sizeof(descLen));
        file.write(desc.c_str(), descLen);
        file.write(reinterpret_cast<const char *>(&priority), sizeof(priority));
        file.write(reinterpret_cast<const char *>(&deadline), sizeof(deadline));
        file.write(reinterpret_cast<const char *>(&completed), sizeof(completed));
    }
};

void TaskManager::loadTasks()
{
    ifstream file(filename, ios::binary);
    if (!file)
    {
        return; // File Doesnt exist yet
        tasks.clear();
        size_t size;
        file.read(reinterpret_cast<char *>(&size), sizeof(size));

        for (size_t i = 0; i < size; ++i)
        {
            size_t titleLen, descLen;
            file.read(reinterpret_cast<char *>(&titleLen), sizeof(titleLen));

            std::string title(titleLen, '\0');
            file.read(&title[0], titleLen);

            file.read(reinterpret_cast<char *>(&descLen), sizeof(descLen));
            std::string desc(descLen, '\0');
            file.read(&desc[0], descLen);

            Priority priority;
            time_t deadline;
            bool completed;

            file.read(reinterpret_cast<char *>(&priority), sizeof(priority));
            file.read(reinterpret_cast<char *>(&deadline), sizeof(deadline));
            file.read(reinterpret_cast<char *>(&completed), sizeof(completed));

            Task task(title, desc, priority, deadline);
            task.setCompleted(completed);
            tasks.push_back(task);
        }
    }
};