#include <iostream>
#include <fstream>
#include <string>
#include <cstdlib>
#include <ctime>
#include <vector>
#include <iomanip>
using namespace std;

int main()
{
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


    string Q6 ="Who won the 2022 world cup?";
    string OPS6= "(a)France (b)England (c)Spain (d)Argetina";
    char A6 = 'd';


    string Q7 = "who is the president of the united states of america?";
    string OPS7 ="(a)Kamala Harris (b)Donald Trump (c)Barack Obama (d)Joe Biden";
    char A7 = 'b';


    string Q8 ="In what year was Google invented?";
    string OPS8 ="(a)2003 (b)1954 (c)1998 (d)2005";
    char A8='c';



    string Q9 ="Who invented c++?";
    string OPS9 = "(a)Kelly (b)Julius (c)Elon musk (d)Bjarne Stroustrup";
    char A9 ='d';



    string Q10="how many secods are there in a day?";
    string OPS10 ="(a)86400 (b)93680 (c)24800 (d)12460";
    char A10 = 'a';

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


    outfileQuestions<<Q6<<endl;
    outfileQuestions<<OPS6<<endl;
    outfileQuestions<<A6<<endl;


    outfileQuestions<<Q7<<endl;
    outfileQuestions<<OPS7<<endl;
    outfileQuestions<<A7<<endl;

    outfileQuestions<<Q8<<endl;
    outfileQuestions<<OPS8<<endl;
    outfileQuestions<<A8<<endl;

    outfileQuestions<<Q9<<endl;
    outfileQuestions<<OPS9<<endl;
    outfileQuestions<<A9<<endl;


    outfileQuestions<<Q10<<endl;
    outfileQuestions<<OPS10<<endl;
    outfileQuestions<<A10<<endl;

    outfileQuestions.close();

















    ifstream chefileQuestions("Palace.txt");
    if(!chefileQuestions.is_open()){
        cout<<"Can't open file right now"<<endl;

        return 1;
   }
   vector <string> Questions,Options;
   vector<char>Answers;

   string Question,option;
   char answer;

   while(getline(chefileQuestions,Question)){
       getline(chefileQuestions,option);
       chefileQuestions>>answer;
       chefileQuestions.ignore();

       Questions.push_back(Question);
       Options.push_back(option);
       Answers.push_back(answer);


   }

    chefileQuestions.close();

    srand(time(0));
    int score=0,total_questions=10;
    float percentage;


    cout<<"Your quiz starts now!\n";

    for(int i=0;i<total_questions;++i){

        int randomIndex =rand() % Questions.size();

        cout<<"Question"<<(i+1)<<":"<<Questions[randomIndex]<<endl;
        cout<<Options[randomIndex]<<endl;

        char user_answer;
        cout<<"Your answer(a-d):";
        cin>>user_answer;

        if(user_answer == Answers[randomIndex]){
                cout<<"CORRECT!"<<endl;
                score++;
        }else{
            cout<<"INCORRECT!! Correct answer:"<<Answers[randomIndex]<<endl;
        }
}


    cout<<"The quiz is now Over"<<endl;
    cout<<score<<"/"<<total_questions<<endl;
     if(score>(0.5*total_questions) && score!=total_questions){
        cout<<"You tried"<<endl;
    }else if(score<(0.5*total_questions)){
        cout<<"Poor"<<endl;

    }else {
        cout<<"Excellent work"<<endl;
    }

    percentage=(score * 100) / total_questions;
    cout << "Percentage: " << percentage<< setprecision(2) << "%" << endl;




}

