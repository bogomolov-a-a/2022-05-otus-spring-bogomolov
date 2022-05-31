package ru.otus.group202205.homework.spring02.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.group202205.homework.spring02.dao.QuestionDao;
import ru.otus.group202205.homework.spring02.service.impl.QuestionServiceImpl;
import ru.otus.group202205.homework.spring02.service.impl.SimpleStudentTestServiceCsv;
import ru.otus.group202205.homework.spring02.util.impl.ConsoleUtil;

@ComponentScan()
@Configuration
public class ServiceConfig {

  @Autowired
  private AnswerService answerService;

  @Bean
  public StudentTestService simpleStudentTestServiceCsv(QuestionService questionService,
      ConsoleUtil consoleUtil,
      @Value("${test.question-list.location}") String testQuestionsCsvLocation,
      @Value("${test.correct-answer-number.suite}") int correctAnswerForTestNumber,
      @Value("${test.attempt.limit}") int attemptLimit) {
    return new SimpleStudentTestServiceCsv(questionService,
        consoleUtil,
        answerService,
        testQuestionsCsvLocation,
        correctAnswerForTestNumber,
        attemptLimit);
  }

  @Bean
  public QuestionService questionService(QuestionDao questionDao,
      ConsoleUtil consoleUtil,
      @Value("${test.correct-answer-number.multi-select-question}")
          float multiselectCorrectAnswerLevel) {
    return new QuestionServiceImpl(questionDao,
        answerService,
        consoleUtil,
        multiselectCorrectAnswerLevel);
  }
}
