package ru.otus.group202205.homework.spring02.service;

import java.util.List;
import ru.otus.group202205.homework.spring02.model.Answer;
import ru.otus.group202205.homework.spring02.model.QuestionType;


public interface AnswerService {

  void printPossibleAnswers(List<Answer> answers);

  boolean examineQuestionAnswers(QuestionType questionType,
      List<Long> studentAnswerIndexes,
      List<Long> exceptedAnswerIndexes,
      float multiselectCorrectAnswerLevel);

}
