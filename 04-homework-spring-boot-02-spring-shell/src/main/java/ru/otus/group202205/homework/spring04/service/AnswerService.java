package ru.otus.group202205.homework.spring04.service;

import java.util.List;
import ru.otus.group202205.homework.spring04.model.QuestionType;


public interface AnswerService {

  boolean examineQuestionAnswers(QuestionType questionType,
      List<Long> studentAnswerIndexes,
      List<Long> exceptedAnswerIndexes);

}
