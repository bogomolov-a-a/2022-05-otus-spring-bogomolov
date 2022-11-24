package ru.otus.group202205.homework.spring13.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "authors")
public class Author {

  public static final String PATRONYMIC_DEFAULT_VALUE = "not set";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String surname;
  private String name;
  private String patronymic = PATRONYMIC_DEFAULT_VALUE;
  private Long birthYear;
  private Long deathYear;

}
