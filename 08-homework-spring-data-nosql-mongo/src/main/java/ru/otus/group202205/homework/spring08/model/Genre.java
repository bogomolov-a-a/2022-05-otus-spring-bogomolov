package ru.otus.group202205.homework.spring08.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Document(collection = "genres")
public class Genre {

  @Id
  private String id;
  @Indexed(unique = true, name = "genreNaturalKey")
  private String name;

}
