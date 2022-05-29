package ru.otus.group202205.homework.spring02.service;

import java.util.List;
import ru.otus.group202205.homework.spring02.model.Question;

public interface QuestionService {

  List<Question> getQuestionsFromCsv(String csvLocation);

  void printQuestionStatementWithAnswers(Question question, int questionCount);

  float getMultiselectCorrectAnswerLevel();
}
