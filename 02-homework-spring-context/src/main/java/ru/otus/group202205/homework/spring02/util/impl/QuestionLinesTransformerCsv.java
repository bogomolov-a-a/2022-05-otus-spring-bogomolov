package ru.otus.group202205.homework.spring02.util.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.otus.group202205.homework.spring02.exception.QuestionTransformException;
import ru.otus.group202205.homework.spring02.model.Answer;
import ru.otus.group202205.homework.spring02.model.Question;
import ru.otus.group202205.homework.spring02.model.QuestionType;
import ru.otus.group202205.homework.spring02.util.QuestionLinesTransformer;

@Component
public class QuestionLinesTransformerCsv implements QuestionLinesTransformer {

  private static final String QUESTION_LIST_EMPTY_MESSAGE_TEMPLATE = "Question list is empty!";
  private static final String QUESTION_WRONG_FORMAT_MESSAGE_TEMPLATE =
      "Wrong question format. For type '%s' must be ";
  private static final String QUESTION_WRONG_FORMAT_PART_COUNT_MESSAGE_TEMPLATE =
      QUESTION_WRONG_FORMAT_MESSAGE_TEMPLATE + "minimum %d parts, but get %d parts.";
  private static final String QUESTION_WRONG_FORMAT_CORRECT_ANSWER_INDEXES_COUNT_MESSAGE_TEMPLATE =
      QUESTION_WRONG_FORMAT_MESSAGE_TEMPLATE
          + " minimum %d correct answer indexes, but get %d correct answer indexes.";
  private static final String QUESTION_WRONG_FORMAT_CORRECT_ANSWER_INDEX_ERROR_MESSAGE_TEMPLATE =
      QUESTION_WRONG_FORMAT_MESSAGE_TEMPLATE
          + " only number index for question answer,but get another. Cause: '%s'.";
  private static final String QUESTION_WRONG_FORMAT_REQUIRED_PART_MISSED_MESSAGE_TEMPLATE =
      "Wrong question format. Required part called '%s' with index %d"
          + " missed in question line, empty or incorrect.";
  private static final String QUESTION_WRONG_FORMAT_ANSWER_REQUIRED_PART_MISSED_MESSAGE_TEMPLATE =
      "Wrong question format. Answer required part called '%s' "
          + "missed in question line or empty.";

  @Override
  public List<Question> transformQuestionLinesToQuestion(List<String> questionLines) {
    examineQuestionListNotEmpty(questionLines);
    List<Question> result = new ArrayList<>(questionLines.size());
    for (String questionLine : questionLines) {
      result.add(parseQuestion(questionLine));
    }
    return result;
  }

  private void examineQuestionListNotEmpty(List<String> lines) {
    if (lines.isEmpty()) {
      throw new QuestionTransformException(QUESTION_LIST_EMPTY_MESSAGE_TEMPLATE);
    }
  }

  private Question parseQuestion(String questionLine) {
    String[] parts = questionLine.split(Question.QUESTION_PART_SEPARATOR);
    examineMinimumPartNumberForQuestionType(parts, QuestionType.DEFAULT);
    examineRequiredQuestionParts(parts);
    Question result = new Question(parts[Question.QUESTION_STATEMENT_PART_INDEX].trim(),
        QuestionType.getValueByString(parts[Question.QUESTION_TYPE_PART_INDEX].trim()));
    fillQuestionAnswersIfAllow(result, parts);
    return result;
  }

  private void examineRequiredQuestionParts(String[] parts) {
    if (!StringUtils.hasText(parts[Question.QUESTION_STATEMENT_PART_INDEX])) {
      throw new QuestionTransformException(
          String.format(QUESTION_WRONG_FORMAT_REQUIRED_PART_MISSED_MESSAGE_TEMPLATE,
              Question.STATEMENT_PART_NAME, Question.QUESTION_STATEMENT_PART_INDEX));
    }
    if (!StringUtils.hasText(parts[Question.QUESTION_TYPE_PART_INDEX])
        || QuestionType.getValueByString(parts[Question.QUESTION_TYPE_PART_INDEX].trim()) == null) {
      throw new QuestionTransformException(
          String.format(QUESTION_WRONG_FORMAT_REQUIRED_PART_MISSED_MESSAGE_TEMPLATE,
              Question.TYPE_PART_NAME, Question.QUESTION_TYPE_PART_INDEX));
    }
  }

  private void fillQuestionAnswersIfAllow(Question result, String[] parts) {
    QuestionType questionType = result.getType();
    examineMinimumPartNumberForQuestionType(parts, questionType);
    fillQuestionWithAnswers(result, parts, questionType);
  }

  private void examineMinimumPartNumberForQuestionType(String[] parts, QuestionType questionType) {
    if (parts.length < questionType.getQuestionLineMinimumPartNumber()) {
      throw new QuestionTransformException(
          String.format(QUESTION_WRONG_FORMAT_PART_COUNT_MESSAGE_TEMPLATE,
              questionType.getType(),
              questionType.getQuestionLineMinimumPartNumber(),
              parts.length));
    }
  }

  private void fillQuestionWithAnswers(Question result, String[] parts, QuestionType questionType) {
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

  private void examineAnswerIndexesQuestionLinePart(String answerIndexesPart) {
    if (!StringUtils.hasText(answerIndexesPart)) {
      throw new QuestionTransformException(
          String.format(QUESTION_WRONG_FORMAT_REQUIRED_PART_MISSED_MESSAGE_TEMPLATE,
              Question.CORRECT_ANSWER_INDEXES_PART_NAME,
              Question.QUESTION_CORRECT_ANSWER_INDEXES_PART_INDEX));
    }
  }

  private void examineMinimumCorrectAnswerNumber(String[] answerIndexes,
      QuestionType questionType) {
    if (answerIndexes.length < questionType.getQuestionMinimumCorrectAnswerListCount()) {
      throw new QuestionTransformException(
          String.format(QUESTION_WRONG_FORMAT_CORRECT_ANSWER_INDEXES_COUNT_MESSAGE_TEMPLATE,
              questionType.getType(),
              questionType.getQuestionLineMinimumPartNumber(),
              answerIndexes.length));
    }
  }

  private void addCorrectAnswerIndexes(List<Long> correctAnswerIndexList, String[] answerIndexes,
      QuestionType questionType) {
    for (String answerIndex : answerIndexes) {
      try {
        correctAnswerIndexList.add(Long.parseLong(answerIndex.trim()));
      } catch (NumberFormatException e) {
        throw new QuestionTransformException(
            String.format(QUESTION_WRONG_FORMAT_CORRECT_ANSWER_INDEX_ERROR_MESSAGE_TEMPLATE,
                questionType.getType(),
                e.getMessage()));
      }
    }
  }

  private Answer parseQuestionAnswer(String answerStatement) {
    examineAnswerStatement(answerStatement);
    return new Answer(answerStatement);
  }

  private void examineAnswerStatement(String answerStatement) {
    if (!StringUtils.hasText(answerStatement)) {
      throw new QuestionTransformException(
          String.format(QUESTION_WRONG_FORMAT_ANSWER_REQUIRED_PART_MISSED_MESSAGE_TEMPLATE,
              Answer.STATEMENT_PART_NAME));
    }
  }

}
