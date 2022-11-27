package ru.otus.group202205.homework.spring19.zooshop.producer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@EqualsAndHashCode
@Schema(title = "Producer information")
public class ProducerDto {

  @Schema(title = "Producer id")
  private Long id;
  @NonNull
  @Schema(title = "Producer name, part of natural key", required = true, pattern = "(A-Za-zА-Яа-яЁё\\-\\s)*", maxLength = 1024)
  private String name;
  @NonNull
  @Schema(title = "id of producer address, part of natural key")
  private Long addressId;

}
