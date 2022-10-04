package ru.otus.group202205.homework.spring05.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Book {

  private Long id;
  private String title;
  private String isbn;
  private Author author;
  private Genre genre;

}
