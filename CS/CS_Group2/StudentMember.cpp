#include <iostream>
#include <fstream>
#include <vector> //for precised arrays.
#include <iomanip>
#include <string>
#include <cstdlib>
#include <limits>
#include <algorithm> // to find the value in range
#include"Member.cpp"
#pragma once
using namespace std;

class StudentMember : public Member {
public:
    StudentMember(string n, int id) : Member(n, id) {}
};

class TeacherMember : public Member {
public:
    TeacherMember(string n, int id) : Member(n, id) {}
};
