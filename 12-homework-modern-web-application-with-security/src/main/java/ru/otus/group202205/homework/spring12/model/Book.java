package ru.otus.group202205.homework.spring12.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"author", "genre", "comments"})
@Entity
@Table(name = "books")
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String isbn;
  @ManyToOne(optional = false)
  @JoinColumn(name = "author_id", nullable = false)
  private Author author;
  @ManyToOne(optional = false)
  @JoinColumn(name = "genre_id", nullable = false)
  private Genre genre;
  @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "book")
  private List<BookComment> comments = List.of();

}
