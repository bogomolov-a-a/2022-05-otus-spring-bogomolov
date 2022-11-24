package ru.otus.group202205.homework.spring19.zooshop.action.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;
import ru.otus.group202205.homework.spring19.zooshop.action.model.DiscountType;

@Getter
@Setter
@EqualsAndHashCode
@Schema(title = "Action information")
public class ActionDto {

  @Schema(title = "Action id")
  private Long id;
  @NonNull
  @Schema(title = "Action name")
  private String name;
  @Schema(title = "Action description")

  private String description;
  @NonNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Schema(title = "Action start date")
  private LocalDateTime startDate;
  @NonNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Schema(title = "Action end date", description = "may be null, if action no has end date")
  private LocalDateTime endDate;
  @NonNull
  @Schema(title = "Action discount type")
  private DiscountType discountType;
  @NonNull
  @Schema(title = "Action discount value")
  private Float discountValue;
  @NonNull
  @Schema(title = "Good id for the action", description = "N..1, one good - more actions")
  private Long goodId;

}
