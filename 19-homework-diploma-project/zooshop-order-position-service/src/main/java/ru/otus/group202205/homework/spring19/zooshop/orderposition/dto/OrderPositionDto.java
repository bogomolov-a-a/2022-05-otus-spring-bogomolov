package ru.otus.group202205.homework.spring19.zooshop.orderposition.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@EqualsAndHashCode
@Schema(title = "Order position information")
public class OrderPositionDto {

  private Long id;
  @NonNull
  private String name;
  @NonNull
  private Long goodId;
  @NonNull
  private Long orderId;
  @NonNull
  private Long actionId;
  @NonNull
  private Float quantity;

}
