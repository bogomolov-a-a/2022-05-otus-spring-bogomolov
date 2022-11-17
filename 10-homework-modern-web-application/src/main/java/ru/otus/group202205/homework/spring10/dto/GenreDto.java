package ru.otus.group202205.homework.spring10.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class GenreDto {

  private Long id;
  @NotBlank
  @Pattern(regexp = "([A-Za-zА-Яа-яёЁ\\s])*")
  private String name;

}
