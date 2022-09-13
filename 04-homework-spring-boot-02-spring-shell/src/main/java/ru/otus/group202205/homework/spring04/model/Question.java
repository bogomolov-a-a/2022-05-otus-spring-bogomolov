package ru.otus.group202205.homework.spring04.model;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Question {

  private final String statement;
  private final QuestionType type;
  private final List<Answer> answers = new ArrayList<>();
  private final List<Long> correctAnswerIndexList = new ArrayList<>();

}
