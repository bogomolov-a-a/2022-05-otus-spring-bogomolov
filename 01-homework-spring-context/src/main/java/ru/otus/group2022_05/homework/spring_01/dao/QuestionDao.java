package ru.otus.group2022_05.homework.spring_01.dao;

import java.util.List;
import ru.otus.group2022_05.homework.spring_01.model.Question;

public interface QuestionDao {
  List<Question> getQuestions();
}
