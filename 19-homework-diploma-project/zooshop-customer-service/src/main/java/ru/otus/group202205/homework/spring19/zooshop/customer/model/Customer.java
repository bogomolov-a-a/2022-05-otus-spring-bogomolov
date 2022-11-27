package ru.otus.group202205.homework.spring19.zooshop.customer.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
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
@Table(name = "customers")
@Getter
@Setter
@EqualsAndHashCode
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NonNull
  private String name;
  @NonNull
  private String surname;
  private String patronymic;
  @Column(columnDefinition = "TIMESTAMP")
  private LocalDateTime birthDate;
  private String phone;
  private String email;
  @NonNull
  private Long addressId;

}
