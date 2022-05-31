package ru.otus.group202205.homework.spring02.dao.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring02.dao.QuestionDao;
import ru.otus.group202205.homework.spring02.model.Question;
import ru.otus.group202205.homework.spring02.util.QuestionLinesReader;
import ru.otus.group202205.homework.spring02.util.QuestionLinesTransformer;

@Service
public class QuestionDaoCsv implements QuestionDao {

  private final QuestionLinesReader questionLinesReader;
  private final QuestionLinesTransformer questionLinesTransformer;

  public QuestionDaoCsv(QuestionLinesReader questionLinesReader,
      QuestionLinesTransformer questionLinesTransformer) {
    this.questionLinesReader = questionLinesReader;
    this.questionLinesTransformer = questionLinesTransformer;
  }

  @Override
  public List<Question> getQuestionsFromCsv(String csvLocation) {
    return questionLinesTransformer.transformQuestionLinesToQuestion(
        questionLinesReader.readQuestionLineFromCsv(csvLocation));
  }


}
