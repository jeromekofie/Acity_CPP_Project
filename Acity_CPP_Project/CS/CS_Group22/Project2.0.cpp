#include <iostream>
#include <fstream>

using namespace std;
int main() {
    string Q1 = "What is the capital city of ghana?";
    string OPS1 ="(a)agbogba(b)Accra(c)Madina(d)Lagos";
    char A1='b';



    string Q2= "Who was the president of nigeria as at january 2024?";
    string OPS2 ="(a)Tinubu(b)Buhari (c)Atiku (d)jonathan";
    char A2 ='a';


    string Q3 ="What is the tallest building in the world?";
    string OPS3 ="(a)Eiffel tower (b)Twin towers (c)Burj khalifa (d)Acity tower";
    char A3 ='c';



    string Q4 ="If 2x+3=5.Find x";
    string OPS4 ="(a)4 (b)1 (c)0 (d)3";
    char A4 = 'b';


    string Q5 ="what year did Nigeria gain independence?";
    string OPS5 ="(a)1945 (b)1932 (c)1958 (d)1960";
    char A5 ='d';

    ofstream outfileQuestions("Palace.txt");

    outfileQuestions<<Q1<<endl;
    outfileQuestions<<OPS1<<endl;
    outfileQuestions<<A1<<endl;

    outfileQuestions<<Q2<<endl;
    outfileQuestions<<OPS2<<endl;
    outfileQuestions<<A2<<endl;


    outfileQuestions<<Q3<<endl;
    outfileQuestions<<OPS3<<endl;
    outfileQuestions<<A3<<endl;


    outfileQuestions<<Q4<<endl;
    outfileQuestions<<OPS4<<endl;
    outfileQuestions<<A4<<endl;


    outfileQuestions<<Q5<<endl;
    outfileQuestions<<OPS5<<endl;
    outfileQuestions<<A5<<endl;

    outfileQuestions.close();
}