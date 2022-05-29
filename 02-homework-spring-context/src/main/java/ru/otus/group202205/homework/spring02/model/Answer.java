package ru.otus.group202205.homework.spring02.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Answer {

  public static final String STATEMENT_PART_NAME = "statement";
  private final String statement;

}
