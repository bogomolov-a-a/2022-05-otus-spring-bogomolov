package ru.otus.group202205.homework.spring08.dto;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class BookCommentDto {

  private String id;
  private String text;
  private LocalDateTime created;
  private BookSimpleDto book;

}
