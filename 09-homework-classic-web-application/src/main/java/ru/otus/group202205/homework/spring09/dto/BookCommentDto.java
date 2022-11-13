package ru.otus.group202205.homework.spring09.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@EqualsAndHashCode
public class BookCommentDto {

  private Long id;
  @NotBlank
  private String text;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime created = LocalDateTime.now();
  private BookSimpleDto book;

}
