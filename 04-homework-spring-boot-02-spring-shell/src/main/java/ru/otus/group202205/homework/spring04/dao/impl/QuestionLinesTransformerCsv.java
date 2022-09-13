package ru.otus.group202205.homework.spring04.dao.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.otus.group202205.homework.spring04.config.properties.LocaleProperties;
import ru.otus.group202205.homework.spring04.dao.QuestionLinesTransformer;
import ru.otus.group202205.homework.spring04.exception.QuestionTransformException;
import ru.otus.group202205.homework.spring04.model.Answer;
import ru.otus.group202205.homework.spring04.model.Question;
import ru.otus.group202205.homework.spring04.model.QuestionType;

@Component
@RequiredArgsConstructor

public class QuestionLinesTransformerCsv implements QuestionLinesTransformer {

  static final String STATEMENT_PART_NAME = "statement";
  static final String TYPE_PART_NAME = "type";
  static final String CORRECT_ANSWER_INDEXES_PART_NAME = "correctAnswerIndexList";
  static final String QUESTION_PART_SEPARATOR = ",";
  static final String QUESTION_PART_INNER_SEPARATOR = ";";
  static final int QUESTION_STATEMENT_PART_INDEX = 0;
  static final int QUESTION_TYPE_PART_INDEX = 1;
  static final int QUESTION_ANSWER_CONTENT_START_PART_INDEX = 3;
  static final int QUESTION_CORRECT_ANSWER_INDEXES_PART_INDEX = 2;
  private final MessageSource messageSource;
  private final LocaleProperties localeProperties;

  @Override
  public List<Question> transformQuestionInputToQuestion(List<String> questionLines) {
    examineQuestionListNotEmpty(questionLines);
    List<Question> result = new ArrayList<>(questionLines.size());
    for (String questionLine : questionLines) {
      result.add(parseQuestion(questionLine));
    }
    return result;
  }

  private void examineQuestionListNotEmpty(List<String> lines) {
    if (lines.isEmpty()) {
      throw new QuestionTransformException(messageSource.getMessage("question-transformer.empty-question-list-error-message",
          null,
          localeProperties.getCurrentLocale()));
    }
  }

  private Question parseQuestion(String questionLine) {
    String[] parts = questionLine.split(QUESTION_PART_SEPARATOR);
    examineMinimumPartNumberForQuestionType(parts,
        QuestionType.DEFAULT);
    examineRequiredQuestionParts(parts);
    Question result = new Question(parts[QUESTION_STATEMENT_PART_INDEX].trim(),
        QuestionType.getValueByString(parts[QUESTION_TYPE_PART_INDEX].trim()));
    fillQuestionAnswersIfAllow(result,
        parts);
    return result;
  }

  private void examineMinimumPartNumberForQuestionType(String[] parts, QuestionType questionType) {
    if (parts.length < questionType.getQuestionLineMinimumPartNumber()) {
      throw new QuestionTransformException(messageSource.getMessage("question-transformer.not-enough-part-number-error-message",
          new Object[]{questionType.getType(), questionType.getQuestionLineMinimumPartNumber(), parts.length},
          localeProperties.getCurrentLocale()));
    }
  }

  private void examineRequiredQuestionParts(String[] parts) {
    if (!StringUtils.hasText(parts[QUESTION_STATEMENT_PART_INDEX])) {
      throw new QuestionTransformException(messageSource.getMessage("question-transformer.not-found-required-part-error-message",
          new Object[]{STATEMENT_PART_NAME, QUESTION_STATEMENT_PART_INDEX},
          localeProperties.getCurrentLocale()));
    }
    if (!StringUtils.hasText(parts[QUESTION_TYPE_PART_INDEX]) || QuestionType.getValueByString(parts[QUESTION_TYPE_PART_INDEX].trim()) == null) {
      throw new QuestionTransformException(messageSource.getMessage("question-transformer.not-found-required-part-error-message",
          new Object[]{TYPE_PART_NAME, QUESTION_TYPE_PART_INDEX},
          localeProperties.getCurrentLocale()));
    }
  }

  private void fillQuestionAnswersIfAllow(Question result, String[] parts) {
    QuestionType questionType = result.getType();
    examineMinimumPartNumberForQuestionType(parts,
        questionType);
    fillQuestionWithAnswers(result,
        parts,
        questionType);
  }


  private void fillQuestionWithAnswers(Question result, String[] parts, QuestionType questionType) {
    String answerIndexesPart = parts[QUESTION_CORRECT_ANSWER_INDEXES_PART_INDEX];
    examineAnswerIndexesQuestionLinePart(answerIndexesPart);
    String[] answerIndexes = answerIndexesPart.split(QUESTION_PART_INNER_SEPARATOR);
    examineMinimumCorrectAnswerNumber(answerIndexes,
        questionType);
    addCorrectAnswerIndexes(result.getCorrectAnswerIndexList(),
        answerIndexes,
        questionType);
    List<Answer> answerList = result.getAnswers();
    for (int i = QUESTION_ANSWER_CONTENT_START_PART_INDEX; i < parts.length; i++) {
      answerList.add(parseQuestionAnswer(parts[i].trim()));
    }
  }

  private void examineAnswerIndexesQuestionLinePart(String answerIndexesPart) {
    if (!StringUtils.hasText(answerIndexesPart)) {
      throw new QuestionTransformException(messageSource.getMessage("question-transformer.not-found-required-part-error-message",
          new Object[]{CORRECT_ANSWER_INDEXES_PART_NAME, QUESTION_CORRECT_ANSWER_INDEXES_PART_INDEX},
          localeProperties.getCurrentLocale()));
    }
  }

  private void examineMinimumCorrectAnswerNumber(String[] answerIndexes, QuestionType questionType) {
    if (answerIndexes.length < questionType.getQuestionMinimumCorrectAnswerListCount()) {
      throw new QuestionTransformException(messageSource.getMessage("question-transformer.not-enough-correct-answer-indexes-error-message",
          new Object[]{questionType.getType(), questionType.getQuestionLineMinimumPartNumber(), answerIndexes.length},
          localeProperties.getCurrentLocale()));
    }
  }

  private void addCorrectAnswerIndexes(List<Long> correctAnswerIndexList, String[] answerIndexes, QuestionType questionType) {
    for (String answerIndex : answerIndexes) {
      try {
        correctAnswerIndexList.add(Long.parseLong(answerIndex.trim()));
      } catch (NumberFormatException e) {
        throw new QuestionTransformException(messageSource.getMessage("question-transformer.answer-indexes-must-be-only-number-error-message",
            new Object[]{questionType.getType(), e.getMessage()},
            localeProperties.getCurrentLocale()));
      }
    }
  }

  private Answer parseQuestionAnswer(String answerStatement) {
    examineAnswerStatement(answerStatement);
    return new Answer(answerStatement);
  }

  private void examineAnswerStatement(String answerStatement) {
    if (!StringUtils.hasText(answerStatement)) {
      throw new QuestionTransformException(messageSource.getMessage("question-transformer.not-found-required-answer-part-error-message",
          new Object[]{STATEMENT_PART_NAME},
          localeProperties.getCurrentLocale()));
    }
  }

}
