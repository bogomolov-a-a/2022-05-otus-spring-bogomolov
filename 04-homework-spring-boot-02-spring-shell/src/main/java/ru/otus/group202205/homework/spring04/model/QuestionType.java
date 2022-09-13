package ru.otus.group202205.homework.spring04.model;

import lombok.Getter;

public enum QuestionType {
  /**
   * Question statement,question type, list of allowed answers, minimum 2 answers (1 may be wrong and 1 answer may be selected as correct).
   */
  REGULAR("regular",
      5,
      1),
  /**
   * Question statement,question type, list of allowed answers, minimum 3 answers (1 may be wrong and 2 answer may be selected as correct).
   */
  MULTISELECT("multiselect",
      6,
      2),
  /**
   * for all questions: Question statement,question type.
   */
  DEFAULT("all questions",
      2,
      0);


  public static QuestionType getValueByString(String type) {
    for (QuestionType value : QuestionType.values()) {
      if (value
          .getType()
          .equals(type)) {
        return value;
      }
    }
    return null;
  }

  @Getter
  private final String type;
  @Getter
  private final int questionLineMinimumPartNumber;
  @Getter
  private final int questionMinimumCorrectAnswerListCount;

  QuestionType(String type,
      int questionLineMinimumPartNumber,
      int questionMinimumCorrectAnswerListCount) {
    this.type = type;
    this.questionLineMinimumPartNumber = questionLineMinimumPartNumber;
    this.questionMinimumCorrectAnswerListCount = questionMinimumCorrectAnswerListCount;
  }
}
