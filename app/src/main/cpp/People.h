//
// Created by Administrator on 2018/8/23.
//
#include <string.h>
#include <string>

#ifndef OPENCVSTUDY_PEOPLE_H
#define OPENCVSTUDY_PEOPLE_H
class People {
private:
    int age;
public:
    People(int age);
    std::string say();
};
#endif //OPENCVSTUDY_PEOPLE_H
