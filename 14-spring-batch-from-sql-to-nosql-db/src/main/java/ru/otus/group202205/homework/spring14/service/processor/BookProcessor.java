package ru.otus.group202205.homework.spring14.service.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring14.model.jpa.BookEntity;
import ru.otus.group202205.homework.spring14.model.mongo.AuthorDocument;
import ru.otus.group202205.homework.spring14.model.mongo.BookDocument;
import ru.otus.group202205.homework.spring14.model.mongo.GenreDocument;

@Service
public class BookProcessor implements ItemProcessor<BookEntity, BookDocument> {

  @Override
  public BookDocument process(BookEntity item) {
    BookDocument result = new BookDocument();
    result.setId(item
        .getId()
        .toString());
    result.setIsbn(item.getIsbn());
    result.setTitle(item.getTitle());
    AuthorDocument resultAuthor = new AuthorDocument();
    result.setAuthor(resultAuthor);
    resultAuthor.setId(item
        .getAuthor()
        .getId()
        .toString());
    GenreDocument resultGenre = new GenreDocument();
    result.setGenre(resultGenre);
    resultGenre.setId(item
        .getGenre()
        .getId()
        .toString());
    return result;
  }

}
