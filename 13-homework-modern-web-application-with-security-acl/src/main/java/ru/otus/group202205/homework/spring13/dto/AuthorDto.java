package ru.otus.group202205.homework.spring13.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class AuthorDto {

  private Long id;
  @NotBlank
  @Pattern(regexp = "([A-Za-zА-Яа-я\\-])*", message = "wrong surname")
  private String surname;
  @NotBlank
  @Pattern(regexp = "([A-Za-zА-Яа-я\\-])*", message = "wrong name")
  private String name;
  @Pattern(regexp = "([A-Za-zА-Яа-я\\-])*|(not set)", message = "wrong patronymic")
  private String patronymic = "not set";
  @NotNull
  private Long birthYear;
  @NotNull
  private Long deathYear;

}
