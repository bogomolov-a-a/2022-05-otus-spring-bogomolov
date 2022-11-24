package ru.otus.group202205.homework.spring19.zooshop.address.model;

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
@Table(name = "addresses")
@Getter
@Setter
@EqualsAndHashCode
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NonNull
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
