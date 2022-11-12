package ru.otus.group202205.homework.spring14.model.mongo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Document(collection = "authors")
@CompoundIndex(def = "{'surname': 1,'name': 1,'patronymic': 1,'birthYear': 1}", unique = true, name = "authorNaturalKey")
public class AuthorDocument {

  public static final String PATRONYMIC_DEFAULT_VALUE = "not set";
  @Id
  private String id;
  private String surname;
  private String name;
  private String patronymic = PATRONYMIC_DEFAULT_VALUE;
  private Long birthYear;
  private Long deathYear;

}
