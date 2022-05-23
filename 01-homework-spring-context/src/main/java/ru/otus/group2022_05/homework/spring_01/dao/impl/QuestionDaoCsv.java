package ru.otus.group2022_05.homework.spring_01.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;
import ru.otus.group2022_05.homework.spring_01.dao.QuestionDao;
import ru.otus.group2022_05.homework.spring_01.exception.QuestionFormatException;
import ru.otus.group2022_05.homework.spring_01.exception.QuestionListException;
import ru.otus.group2022_05.homework.spring_01.model.Answer;
import ru.otus.group2022_05.homework.spring_01.model.Question;
import ru.otus.group2022_05.homework.spring_01.model.QuestionType;

public class QuestionDaoCsv implements QuestionDao {

  private static final String NOT_FOUND_RESOURCE_MESSAGE_TEMPLATE = "Question list not found at '%s'. Please check resource name and try again!";
  private static final String DURING_READ_QUESTION_LIST_ERROR_MESSAGE_TEMPLATE = "During read question list occurred exception. Cause: '%s'.";
  private static final String QUESTION_LIST_EMPTY_MESSAGE_TEMPLATE = "Question list is empty!";
  private static final String QUESTION_WRONG_FORMAT_MESSAGE_TEMPLATE = "Wrong question format. For type '%s' must be ";
  private static final String QUESTION_WRONG_FORMAT_PART_COUNT_MESSAGE_TEMPLATE =
      QUESTION_WRONG_FORMAT_MESSAGE_TEMPLATE + "minimum %d parts, but get %d parts.";
  private static final String QUESTION_WRONG_FORMAT_CORRECT_ANSWER_INDEXES_COUNT_MESSAGE_TEMPLATE =
      QUESTION_WRONG_FORMAT_MESSAGE_TEMPLATE + " minimum %d correct answer indexes, but get %d correct answer indexes.";
  private static final String QUESTION_WRONG_FORMAT_CORRECT_ANSWER_INDEX_FORMAT_ERROR_MESSAGE_TEMPLATE =
      QUESTION_WRONG_FORMAT_MESSAGE_TEMPLATE + " only number index for question answer,but get another. Cause: '%s'.";
  private static final String QUESTION_WRONG_FORMAT_REQUIRED_PART_MISSED_MESSAGE_TEMPLATE =
      "Wrong question format. Required part called '%s' with index %d missed in question line, empty or incorrect.";
  private static final String QUESTION_WRONG_FORMAT_ANSWER_REQUIRED_PART_MISSED_MESSAGE_TEMPLATE =
      "Wrong question format. Answer required part called '%s' missed in question line or empty.";

  private final List<Question> questionList = new ArrayList<>();

  public QuestionDaoCsv(String questionListFileName) {
    try (InputStream questionListInputStream = QuestionDaoCsv.class.getClassLoader().getResourceAsStream(questionListFileName)) {
      if (questionListInputStream == null) {
        throw new IOException(String.format(NOT_FOUND_RESOURCE_MESSAGE_TEMPLATE, questionListFileName));
      }
      readQuestions(questionListInputStream);
    } catch (IOException | QuestionListException | QuestionFormatException e) {
      e.printStackTrace();
      throw new IllegalStateException(String.format(DURING_READ_QUESTION_LIST_ERROR_MESSAGE_TEMPLATE, e.getMessage()));
    }
  }

  private void readQuestions(InputStream questionListInputStream) throws IOException, QuestionListException, QuestionFormatException {
    try (InputStreamReader questionListInputStreamReader = new InputStreamReader(questionListInputStream);
        BufferedReader questionListInputStreamBufferedReader = new BufferedReader(questionListInputStreamReader)) {
      List<String> questionLines = questionListInputStreamBufferedReader.lines().collect(Collectors.toUnmodifiableList());
      examineQuestionListNotEmpty(questionLines);
      for (String questionLine : questionLines) {
        System.out.println("parsing line '" + questionLine + "'");
        questionList.add(parseQuestion(questionLine));
        System.out.println("line '" + questionLine + "' parsed");
      }
    }
  }

  private void examineQuestionListNotEmpty(List<String> lines) throws QuestionListException {
    if (lines.isEmpty()) {
      throw new QuestionListException(QUESTION_LIST_EMPTY_MESSAGE_TEMPLATE);
    }
  }

  private Question parseQuestion(String questionLine) throws QuestionFormatException {
    String[] parts = questionLine.split(Question.QUESTION_PART_SEPARATOR);
    examineMinimumPartNumberForQuestionType(parts, QuestionType.DEFAULT);
    examineRequiredQuestionParts(parts);
    Question result = new Question(parts[Question.QUESTION_STATEMENT_PART_INDEX].trim(),
        QuestionType.getValueByString(parts[Question.QUESTION_TYPE_PART_INDEX].trim()));
    fillQuestionAnswersIfAllow(result, parts);
    return result;
  }

  private void examineRequiredQuestionParts(String[] parts) throws QuestionFormatException {
    if (!StringUtils.hasText(parts[Question.QUESTION_STATEMENT_PART_INDEX])) {
      throw new QuestionFormatException(
          String.format(QUESTION_WRONG_FORMAT_REQUIRED_PART_MISSED_MESSAGE_TEMPLATE, Question.STATEMENT_PART_NAME, Question.QUESTION_STATEMENT_PART_INDEX));
    }
    if (!StringUtils.hasText(parts[Question.QUESTION_TYPE_PART_INDEX])
        || QuestionType.getValueByString(parts[Question.QUESTION_TYPE_PART_INDEX].trim()) == null) {
      throw new QuestionFormatException(
          String.format(QUESTION_WRONG_FORMAT_REQUIRED_PART_MISSED_MESSAGE_TEMPLATE, Question.TYPE_PART_NAME, Question.QUESTION_TYPE_PART_INDEX));
    }
  }

  private void fillQuestionAnswersIfAllow(Question result, String[] parts) throws QuestionFormatException {
    QuestionType questionType = result.getType();
    examineMinimumPartNumberForQuestionType(parts, questionType);
    switch (questionType) {
      case INPUT: {
        return;
      }
      case REGULAR:
      case MULTISELECT: {
        fillQuestionWithAnswers(result, parts, questionType);

      }
    }
  }

  private void fillQuestionWithAnswers(Question result, String[] parts, QuestionType questionType) throws QuestionFormatException {
    String answerIndexesPart = parts[Question.QUESTION_CORRECT_ANSWER_INDEXES_PART_INDEX];
    examineAnswerIndexesQuestionLinePart(answerIndexesPart);
    String[] answerIndexes = answerIndexesPart.split(Question.QUESTION_PART_INNER_SEPARATOR);
    examineMinimumCorrectAnswerNumber(answerIndexes, questionType);
    addCorrectAnswerIndexes(result.getCorrectAnswerIndexList(), answerIndexes, questionType);
    List<Answer> answerList = result.getAnswers();
    for (int i = Question.QUESTION_ANSWER_CONTENT_START_PART_INDEX; i < parts.length; i++) {
      answerList.add(parseQuestionAnswer(parts[i].trim()));
    }
  }

  private void examineAnswerIndexesQuestionLinePart(String answerIndexesPart) throws QuestionFormatException {
    if (!StringUtils.hasText(answerIndexesPart)) {
      throw new QuestionFormatException(
          String.format(QUESTION_WRONG_FORMAT_REQUIRED_PART_MISSED_MESSAGE_TEMPLATE, Question.CORRECT_ANSWER_INDEXES_PART_NAME,
              Question.QUESTION_CORRECT_ANSWER_INDEXES_PART_INDEX));
    }
  }

  private Answer parseQuestionAnswer(String answerStatement) throws QuestionFormatException {
    examineAnswerStatement(answerStatement);
    return new Answer(answerStatement);
  }

  private void examineAnswerStatement(String answerStatement) throws QuestionFormatException {
    if (!StringUtils.hasText(answerStatement)) {
      throw new QuestionFormatException(
          String.format(QUESTION_WRONG_FORMAT_ANSWER_REQUIRED_PART_MISSED_MESSAGE_TEMPLATE, Answer.STATEMENT_PART_NAME));
    }
  }

  private void addCorrectAnswerIndexes(List<Long> correctAnswerIndexList, String[] answerIndexes,
      QuestionType questionType) throws QuestionFormatException {
    for (String answerIndex : answerIndexes) {
      try {
        correctAnswerIndexList.add(Long.parseLong(answerIndex.trim()));
      } catch (NumberFormatException e) {
        throw new QuestionFormatException(String.format(QUESTION_WRONG_FORMAT_CORRECT_ANSWER_INDEX_FORMAT_ERROR_MESSAGE_TEMPLATE,
            questionType.getType(), e.getMessage()));
      }
    }
  }

  private void examineMinimumCorrectAnswerNumber(String[] answerIndexes, QuestionType questionType) throws QuestionFormatException {
    if (answerIndexes.length < questionType.getQuestionMinimumCorrectAnswerListCount()) {
      throw new QuestionFormatException(
          String.format(QUESTION_WRONG_FORMAT_CORRECT_ANSWER_INDEXES_COUNT_MESSAGE_TEMPLATE, questionType.getType(),
              questionType.getQuestionLineMinimumPartNumber(),
              answerIndexes.length));
    }
  }

  private void examineMinimumPartNumberForQuestionType(String[] parts, QuestionType questionType) throws QuestionFormatException {
    if (parts.length < questionType.getQuestionLineMinimumPartNumber()) {
      throw new QuestionFormatException(
          String.format(QUESTION_WRONG_FORMAT_PART_COUNT_MESSAGE_TEMPLATE, questionType.getType(), questionType.getQuestionLineMinimumPartNumber(),
              parts.length));
    }
  }

  @Override
  public List<Question> getQuestions() {
    return questionList;
  }
}
