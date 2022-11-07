package ru.otus.group202205.homework.spring07.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class AuthorDto {

  private Long id;
  private String surname;
  private String name;
  private String patronymic = "not set";
  private Long birthYear;
  private Long deathYear;

}
