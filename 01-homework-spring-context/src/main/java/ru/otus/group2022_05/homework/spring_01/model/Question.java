package ru.otus.group2022_05.homework.spring_01.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Question {

  public static final String STATEMENT_PART_NAME = "statement";
  public static final String TYPE_PART_NAME = "type";
  public static final String CORRECT_ANSWER_INDEXES_PART_NAME = "correctAnswerIndexList";

  public static final String QUESTION_PART_SEPARATOR = ",";
  public static final String QUESTION_PART_INNER_SEPARATOR = ";";
  public static final int QUESTION_STATEMENT_PART_INDEX = 0;
  public static final int QUESTION_TYPE_PART_INDEX = 1;
  public static final int QUESTION_CORRECT_ANSWER_INDEXES_PART_INDEX = 2;
  public static final int QUESTION_ANSWER_CONTENT_START_PART_INDEX = 3;

  private final String statement;
  private final QuestionType type;
  private final List<Answer> answers = new ArrayList<>();
  private final List<Long> correctAnswerIndexList = new ArrayList<>();

  @Override
  public String toString() {
    return "Question has " +
        "statement '" + statement + "' with type" +
        " '" + type.getType() + "'" +
        " and answers " + answers +
        ". Correct answer indexes list " + correctAnswerIndexList;
  }
}
