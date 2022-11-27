package ru.otus.group202205.homework.spring19.zooshop.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;
import ru.otus.group202205.homework.spring19.zooshop.order.model.OrderStatus;

@Getter
@Setter
@EqualsAndHashCode
@Schema(title = "Order  information")
public class OrderDto {

  private Long id;
  @Schema(description = "User who order created")
  @NonNull
  private Long customerId;
  @Schema(description = "Date when order created")
  @NonNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime orderDate;
  @Schema(description = "Address whereto order must be delivered")
  private Long deliveredAddressId;
  private String description;
  @Schema(description = "Date when order delivered")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDate deliveredDate;
  @NonNull
  private OrderStatus orderStatus = OrderStatus.CREATED;

}
