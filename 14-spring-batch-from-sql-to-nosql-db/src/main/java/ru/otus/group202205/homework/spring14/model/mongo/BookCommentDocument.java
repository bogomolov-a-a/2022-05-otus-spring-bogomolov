package ru.otus.group202205.homework.spring14.model.mongo;

import java.time.LocalDateTime;
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
@EqualsAndHashCode(exclude = "book")
@Document(collection = "book_comments")
@CompoundIndex(def = "{'text':1,'created':1,'book.id':1}", unique = true, name = "bookCommentNaturalKey")
public class BookCommentDocument {

  @Id
  private String id;
  private String text;
  private LocalDateTime created = LocalDateTime.now();
  @DocumentReference
  private BookDocument book;

}
