package ru.otus.group202205.homework.spring07.dto;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class BookCommentDto {

  private Long id;
  private String text;
  private LocalDateTime created;
  private BookSimpleDto book;

}
