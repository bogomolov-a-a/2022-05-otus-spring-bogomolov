package ru.otus.group202205.homework.spring06.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class BookFullDto {

  private Long id;
  private String title;
  private String isbn;
  private AuthorDto author;
  private GenreDto genre;

}
