package ru.otus.group202205.homework.spring19.zooshop.producer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "producers")
@Getter
@Setter
@EqualsAndHashCode
public class Producer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NonNull
  @Pattern(regexp = "(A-Za-zА-Яа-яЁё\\-\\s)*")
  private String name;
  @NonNull
  private Long addressId;

}
