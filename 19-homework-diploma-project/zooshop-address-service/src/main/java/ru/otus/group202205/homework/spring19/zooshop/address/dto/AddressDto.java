package ru.otus.group202205.homework.spring19.zooshop.address.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@EqualsAndHashCode
@Schema(title = "Address information")
public class AddressDto {

  private Long id;
  @NonNull
  @Schema
  private String postalCode;
  @NonNull
  private String country;
  private String state;
  @NonNull
  private String city;
  private String locality;
  private String district;
  private String street;
  private String house;
  private Integer room;
  private String specificPart;

}
