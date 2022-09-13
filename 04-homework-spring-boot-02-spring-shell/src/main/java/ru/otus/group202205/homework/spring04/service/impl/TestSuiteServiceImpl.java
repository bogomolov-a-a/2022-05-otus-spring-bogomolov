package ru.otus.group202205.homework.spring04.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring04.config.properties.LocaleProperties;
import ru.otus.group202205.homework.spring04.config.properties.TestSuiteProperties;
import ru.otus.group202205.homework.spring04.exception.TestFailedException;
import ru.otus.group202205.homework.spring04.model.Question;
import ru.otus.group202205.homework.spring04.service.AnswerConverter;
import ru.otus.group202205.homework.spring04.service.AnswerService;
import ru.otus.group202205.homework.spring04.service.IoService;
import ru.otus.group202205.homework.spring04.service.QuestionConverter;
import ru.otus.group202205.homework.spring04.service.QuestionService;
import ru.otus.group202205.homework.spring04.service.TestSuiteService;

@Service
@RequiredArgsConstructor
public class TestSuiteServiceImpl implements TestSuiteService {

  private static final String STUDENT_FULL_NAME_REGEXP = "([A-Z]([A-z])*\\s){0,5}(([A-z])*)";
  private static final Pattern STUDENT_FULL_NAME_PATTERN = Pattern.compile(STUDENT_FULL_NAME_REGEXP);

  private final QuestionService questionService;
  private final IoService ioService;
  private final AnswerService answerService;
  private final AnswerConverter answerConverter;
  private final QuestionConverter questionConverter;
  private final TestSuiteProperties testSuiteProperties;
  private final MessageSource messageSource;
  private final LocaleProperties localeProperties;

  @Override
  public void performStudentTesting() {
    List<Question> questions = questionService.getQuestions();
    Long attemptNumber = testSuiteProperties.getAttemptNumber();
    Long correctAnswerNumber = testSuiteProperties.getCorrectAnswerNumber();
    for (int attemptCount = 1; attemptCount <= attemptNumber; attemptCount++) {
      ioService.outputMessage(messageSource.getMessage("test-suite-service.welcome-text",
          new Object[]{questions.size(), correctAnswerNumber, attemptNumber, attemptCount},
          localeProperties.getCurrentLocale()));
      try {
        if (processTestQuestions(questions)) {
          ioService.outputMessage(messageSource.getMessage("test-suite-service.test-success-message",
              new Object[]{attemptCount, attemptNumber},
              localeProperties.getCurrentLocale()));
          return;
        }
      } catch (TestFailedException e) {
        ioService.outputMessage(messageSource.getMessage("test-suite-service.test-attempt-failed-message",
            new Object[]{attemptCount, e.getMessage()},
            localeProperties.getCurrentLocale()));
      }
    }
    String message = messageSource.getMessage("test-suite-service.test-failed-message",
        new Object[]{attemptNumber},
        localeProperties.getCurrentLocale());
    ioService.outputMessage(message);
    throw new TestFailedException(message);
  }

  private boolean processTestQuestions(List<Question> questions) {
    ioService.outputMessage(messageSource.getMessage("test-suite-service.student-ful-name-welcome-text",
        null,
        localeProperties.getCurrentLocale()));
    String studentName = ioService.readUserInput();
    validateStudentName(studentName);
    int questionCount = 1;
    int questionAnswerSuccess = 0;
    for (Question question : questions) {
      if (processOneQuestion(question,
          questionCount++)) {
        questionAnswerSuccess++;
      }
    }
    return questionAnswerSuccess >= testSuiteProperties.getCorrectAnswerNumber();
  }

  private boolean processOneQuestion(Question question, int questionCount) {
    ioService.outputMessage(questionConverter.convertToString(question,
        questionCount));
    ioService.outputMessage(answerConverter.convertToString(question.getAnswers()));
    return handleStudentAnswer(question);
  }

  private boolean handleStudentAnswer(Question question) {
    String userInputLine = ioService.readUserInput();
    String[] answerIndexes = userInputLine.split(",");
    List<Long> studentAnswerIndexes = Arrays
        .stream(answerIndexes)
        .map(x -> {
          try {
            return Long.parseLong(x.trim());
          } catch (NumberFormatException e) {
            throw new TestFailedException(e.getMessage());
          }
        })
        .sorted()
        .collect(Collectors.toList());
    return answerService.examineQuestionAnswers(question.getType(),
        studentAnswerIndexes,
        question.getCorrectAnswerIndexList());
  }

  private void validateStudentName(String studentName) {
    if (STUDENT_FULL_NAME_PATTERN
        .matcher(studentName)
        .matches()) {
      return;
    }
    throw new TestFailedException(messageSource.getMessage("test-suite-service.student-name-wrong-input",
        new Object[]{STUDENT_FULL_NAME_REGEXP},
        localeProperties.getCurrentLocale()));
  }

}
