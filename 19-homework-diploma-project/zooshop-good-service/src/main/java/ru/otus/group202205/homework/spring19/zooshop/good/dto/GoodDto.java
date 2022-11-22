package ru.otus.group202205.homework.spring19.zooshop.good.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import ru.otus.group202205.homework.spring19.zooshop.good.model.GoodUnit;

@Getter
@Setter
@EqualsAndHashCode
@Schema(title = "Good information")
public class GoodDto {

  @Schema(title = "Good id")
  private Long id;
  @NonNull
  @Schema(title = "Good name, part of natural key", required = true, pattern = "(A-Za-zА-Яа-яЁё\\-\\s)*", maxLength = 1024)
  private String name;
  @Schema(title = "Good description", pattern = "(A-Za-zА-Яа-яЁё\\-\\s\\.\\(\\))", maxLength = 8192)
  private String description;
  @NonNull
  @Positive
  @Schema(title = "price for one unit good", minimum = "0.01")
  private Float price;
  @NonNull
  @Positive
  @Schema(title = "unit good quantity", minimum = "0.01")
  private Float quantity;
  @Schema(title = "unit of good")
  @Enumerated(EnumType.STRING)
  private GoodUnit goodUnit;
  @NonNull
  @Schema(title = "name of good producer", pattern = "(A-Za-zА-Яа-яЁё\\-\\s\\.\\(\\))")
  private String producerName;
  @NonNull
  @Schema(title = "name of good category", pattern = "(A-Za-zА-Яа-яЁё\\-\\s\\.\\(\\))")
  private String categoryName;

}
