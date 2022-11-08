package ru.otus.group202205.homework.spring19.zooshop.good.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
