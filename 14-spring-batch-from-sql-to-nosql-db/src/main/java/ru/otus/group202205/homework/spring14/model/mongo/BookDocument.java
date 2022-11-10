package ru.otus.group202205.homework.spring14.model.mongo;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"author", "genre", "comments"})
@Document(collection = "books")
@CompoundIndex(def = "{'title':1,'isbn':1,'author.id':1,'genre.id':1}", unique = true, name = "bookNaturalKey")
public class BookDocument {

  @Id
  private String id;
  private String title;
  private String isbn;
  @DocumentReference
  private AuthorDocument author;
  @DocumentReference
  private GenreDocument genre;
  @DocumentReference(lazy = true)
  private List<BookCommentDocument> comments = List.of();

}
