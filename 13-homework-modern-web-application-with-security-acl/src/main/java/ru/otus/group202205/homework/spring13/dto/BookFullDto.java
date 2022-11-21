package ru.otus.group202205.homework.spring13.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class BookFullDto {

  private Long id;
  @NotBlank
  @Pattern(regexp = "([A-Za-zА-Яа-яёЁ\\s])*")
  private String title;
  @NotBlank
  @Pattern(regexp = "([0-9\\-])*")
  private String isbn;
  private AuthorDto author;
  private GenreDto genre;

}
