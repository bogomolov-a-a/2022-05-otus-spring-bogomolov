package ru.otus.group202205.homework.spring02.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import ru.otus.group202205.homework.spring02.exception.TestFailedException;
import ru.otus.group202205.homework.spring02.model.Question;
import ru.otus.group202205.homework.spring02.service.AnswerService;
import ru.otus.group202205.homework.spring02.service.QuestionService;
import ru.otus.group202205.homework.spring02.service.StudentTestService;
import ru.otus.group202205.homework.spring02.util.impl.ConsoleUtil;

public class SimpleStudentTestServiceCsv implements StudentTestService {

  private static final Pattern STUDENT_FULL_NAME_PATTERN =
      Pattern.compile("([A-Z]([A-z])*\\s){0,5}(([A-z])*)");
  private static final String WELCOME_TEXT =
      "This test contains %d questions. For test passing you must answer correct on %d questions."
          + "Question with MULTISELECT must have above %d percent correct answer for question will"
          + " be correct answered.\nYou have only %d attempts."
          + " Your current attempt number %d.\nAnswer must be only number or comma separated"
          + " number string(For example: '2' or '2,3' or '2, 3')";
  private static final String TEST_ATTEMPT_FAILED_MESSAGE =
      "You can't passed test attempt number %d, because '%s'";
  private static final String PASSING_TEST_TEXT =
      "Congratulations! You passed this test! You success attempt number %d of %d.";
  private static final String FAILED_TEST_TEXT =
      "You can't passed test for %d attempts.";
  private static final String STUDENT_FULL_NAME_QUESTION_MESSAGE =
      "Dear student, please type your full name.";
  private static final String WRONG_STUDENT_NAME_MESSAGE = "Wrong student name!";

  private final QuestionService questionService;
  private final ConsoleUtil consoleUtil;
  private final AnswerService answerService;
  private final String testQuestionsCsvLocation;
  private final int correctAnswerForTestNumber;
  private final int attemptLimit;

  public SimpleStudentTestServiceCsv(QuestionService questionService,
      ConsoleUtil consoleUtil,
      AnswerService answerService,
      String testQuestionsCsvLocation,
      int correctAnswerForTestNumber,
      int attemptLimit) {
    this.questionService = questionService;
    this.consoleUtil = consoleUtil;
    this.answerService = answerService;
    this.testQuestionsCsvLocation = testQuestionsCsvLocation;
    this.correctAnswerForTestNumber = correctAnswerForTestNumber;
    this.attemptLimit = attemptLimit;
  }

  @Override
  public void performStudentTesting() {
    List<Question> questions = questionService.getQuestionsFromCsv(testQuestionsCsvLocation);
    for (int attemptCount = 1; attemptCount <= attemptLimit; attemptCount++) {
      consoleUtil.printMessageToConsole(String.format(WELCOME_TEXT, questions.size(),
          correctAnswerForTestNumber,
          Math.round(questionService.getMultiselectCorrectAnswerLevel() * 100),
          attemptLimit,
          attemptCount));
      try {
        if (processTestQuestions(questions)) {
          consoleUtil.printMessageToConsole(String.format(PASSING_TEST_TEXT,
              attemptCount,
              attemptLimit));
          return;
        }
      } catch (TestFailedException e) {
        consoleUtil.printMessageToConsole(String.format(TEST_ATTEMPT_FAILED_MESSAGE,
            attemptCount,
            e.getMessage()));
      }
    }
    String message = String.format(FAILED_TEST_TEXT, attemptLimit);
    consoleUtil.printMessageToConsole(message);
    throw new TestFailedException(message);
  }

  private boolean processTestQuestions(List<Question> questions) {
    consoleUtil.printMessageToConsole(STUDENT_FULL_NAME_QUESTION_MESSAGE);
    String studentName = consoleUtil.readNextLineFromConsole();
    validateStudentName(studentName);
    int questionCount = 1;
    int questionAnswerSuccess = 0;
    for (Question question : questions) {
      if (processOneQuestion(question, questionCount++)) {
        questionAnswerSuccess++;
      }
    }
    return questionAnswerSuccess >= correctAnswerForTestNumber;
  }

  private boolean processOneQuestion(Question question, int questionCount) {
    questionService.printQuestionStatementWithAnswers(question, questionCount);
    return handleStudentAnswer(question);
  }

  private boolean handleStudentAnswer(Question question) {
    String userInputLine = consoleUtil.readNextLineFromConsole();
    String[] answerIndexes = userInputLine.split(",");
    List<Long> studentAnswerIndexes = Arrays.stream(answerIndexes)
        .map(x -> {
          try {
            return Long.parseLong(x.trim());
          } catch (NumberFormatException e) {
            throw new TestFailedException(e.getMessage());
          }
        }).sorted().collect(Collectors.toList());
    return answerService.examineQuestionAnswers(question.getType(),
        studentAnswerIndexes,
        question.getCorrectAnswerIndexList(),
        questionService.getMultiselectCorrectAnswerLevel());
  }

  private void validateStudentName(String studentName) {
    if (!STUDENT_FULL_NAME_PATTERN.matcher(studentName).matches()) {
      throw new TestFailedException(WRONG_STUDENT_NAME_MESSAGE);
    }
  }

}
