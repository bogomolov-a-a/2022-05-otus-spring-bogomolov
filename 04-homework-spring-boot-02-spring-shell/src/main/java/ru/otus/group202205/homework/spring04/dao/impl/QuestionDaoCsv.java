package ru.otus.group202205.homework.spring04.dao.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring04.dao.QuestionDao;
import ru.otus.group202205.homework.spring04.dao.QuestionLinesReader;
import ru.otus.group202205.homework.spring04.dao.QuestionLinesTransformer;
import ru.otus.group202205.homework.spring04.model.Question;

@Service
@RequiredArgsConstructor
public class QuestionDaoCsv implements QuestionDao {

  private final QuestionLinesReader questionLinesReader;
  private final QuestionLinesTransformer questionLinesTransformer;

  @Override
  public List<Question> getQuestions() {
    return questionLinesTransformer.transformQuestionInputToQuestion(questionLinesReader.readQuestionLines());
  }

}
