package ru.otus.group202205.homework.spring02.service.impl;

import java.util.List;
import lombok.Getter;
import ru.otus.group202205.homework.spring02.dao.QuestionDao;
import ru.otus.group202205.homework.spring02.model.Question;
import ru.otus.group202205.homework.spring02.service.AnswerService;
import ru.otus.group202205.homework.spring02.service.QuestionService;
import ru.otus.group202205.homework.spring02.util.impl.ConsoleUtil;

public class QuestionServiceImpl implements QuestionService {

  private static final String QUESTION_STATEMENT = "Question number %d (%s): %s\n";

  private final QuestionDao questionDao;
  private final ConsoleUtil consoleUtil;
  @Getter
  private final float multiselectCorrectAnswerLevel;
  private final AnswerService answerService;

  public QuestionServiceImpl(QuestionDao questionDao,
      AnswerService answerService,
      ConsoleUtil consoleUtil,
      float multiselectCorrectAnswerLevel) {
    this.questionDao = questionDao;
    this.answerService = answerService;
    this.consoleUtil = consoleUtil;
    this.multiselectCorrectAnswerLevel = multiselectCorrectAnswerLevel;
  }

  @Override
  public List<Question> getQuestionsFromCsv(String csvLocation) {
    return questionDao.getQuestionsFromCsv(csvLocation);
  }

  @Override
  public void printQuestionStatementWithAnswers(Question question, int questionCount) {
    consoleUtil.printMessageToConsole(String.format(QUESTION_STATEMENT,
        questionCount,
        question.getType(),
        question.getStatement()));
    answerService.printPossibleAnswers(question.getAnswers());
  }

}
