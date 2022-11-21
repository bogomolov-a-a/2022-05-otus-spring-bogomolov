package ru.otus.group202205.homework.spring13.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class BookSimpleDto {

  private Long id;
  private String title;

}
