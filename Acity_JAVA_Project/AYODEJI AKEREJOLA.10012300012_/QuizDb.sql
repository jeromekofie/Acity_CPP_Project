create database QuizDb;
use QuizDb;
CREATE TABLE IF NOT EXISTS questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question TEXT NOT NULL,
    option1 TEXT NOT NULL,
    option2 TEXT NOT NULL,
    option3 TEXT NOT NULL,
    option4 TEXT NOT NULL,
    correct_answer TEXT NOT NULL
);
drop table questions;
INSERT INTO questions (question, option1, option2, option3, option4, correct_answer) VALUES
('What is the capital city of Ghana?', 'Agbogba', 'Accra', 'Madina', 'Lagos', 'Accra'),
('Who is the President of Nigeria?', 'Tinubu', 'Buhari', 'Atiku', 'Jonathan', 'Tinubu'),
('What is the tallest building in the world?', 'Eiffel Tower', 'Twin Towers', 'Burj Khalifa', 'Acity Tower', 'Burj Khalifa'),
('If 2x+3=5, what is X?', '4', '1', '0', '3', '1'),
('What year did Nigeria gain independence?', '1945', '1932', '1958', '1960', '1960');
/*use myDb;
create table Student_table(
Student_name varchar(60),
Roll_number varchar(30),
Course varchar(30),
Student_level int
);

select * from Student_table;
drop table student_table;*/