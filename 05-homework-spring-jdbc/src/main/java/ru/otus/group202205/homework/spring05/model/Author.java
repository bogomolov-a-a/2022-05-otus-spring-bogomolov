package ru.otus.group202205.homework.spring05.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Author {

  public static final String PATRONYMIC_DEFAULT_VALUE = "not set";
  private Long id;
  private String surname;
  private String name;
  private String patronymic = PATRONYMIC_DEFAULT_VALUE;
  private Long birthYear;
  private Long deathYear;

}
