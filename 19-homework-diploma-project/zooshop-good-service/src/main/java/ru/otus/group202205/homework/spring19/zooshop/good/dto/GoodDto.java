package ru.otus.group202205.homework.spring19.zooshop.good.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@EqualsAndHashCode
public class GoodDto {

  private Long id;
  @NonNull
  private String name;
  private String description;
  @NonNull
  private Float price;
  @NonNull
  private Float quantity;
  @NonNull
  private String producerName;
  @NonNull
  private String categoryName;

}
