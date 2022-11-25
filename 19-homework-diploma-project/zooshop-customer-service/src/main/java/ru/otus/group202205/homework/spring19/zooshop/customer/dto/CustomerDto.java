package ru.otus.group202205.homework.spring19.zooshop.customer.dto;

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

@Getter
@Setter
@EqualsAndHashCode
@Schema(title = "Customer information")
public class CustomerDto {

  @Schema(title = "Customer id")
  private Long id;
  @NonNull
  private String name;
  @NonNull
  private String surname;
  private String patronymic;
  @NonNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime birthDate;
  private String phone;
  private String email;
  @NonNull
  @Schema()
  private Long addressId;

}
