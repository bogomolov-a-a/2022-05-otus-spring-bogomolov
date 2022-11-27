package ru.otus.group202205.homework.spring19.zooshop.good.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "goods")
@Getter
@Setter
@EqualsAndHashCode
public class Good {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NonNull
  @Pattern(regexp = "(A-Za-zА-Яа-яЁё\\-\\s)*")
  private String name;
  @Pattern(regexp = "(A-Za-zА-Яа-яЁё\\-\\s\\.\\(\\))")
  private String description;
  @NonNull
  @Positive
  private Float price;
  @NonNull
  @Positive
  private Float quantity;
  @Enumerated(EnumType.STRING)
  private GoodUnit goodUnit;
  @NonNull
  private Long producerId;
  @NonNull
  private Long categoryId;

}
