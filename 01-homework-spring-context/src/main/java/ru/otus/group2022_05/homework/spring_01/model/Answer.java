package ru.otus.group2022_05.homework.spring_01.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Answer {

  public static final String STATEMENT_PART_NAME = "statement";
  private final String statement;

  @Override
  public String toString() {
    return "'" + statement + "'";
  }
}
