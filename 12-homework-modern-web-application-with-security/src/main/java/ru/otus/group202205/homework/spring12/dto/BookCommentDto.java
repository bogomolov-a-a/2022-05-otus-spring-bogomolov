package ru.otus.group202205.homework.spring12.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime created = LocalDateTime.now();
  private BookSimpleDto book;

}
