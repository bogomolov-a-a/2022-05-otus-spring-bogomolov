package ru.otus.group202205.homework.spring14.model.mongo;

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
public class GenreDocument {

  @Id
  private String id;
  @Indexed(unique = true, name = "genreNaturalKey")
  private String name;

}
