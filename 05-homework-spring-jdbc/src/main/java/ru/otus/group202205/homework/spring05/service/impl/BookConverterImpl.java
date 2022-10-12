package ru.otus.group202205.homework.spring05.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring05.model.Book;
import ru.otus.group202205.homework.spring05.service.AuthorConverter;
import ru.otus.group202205.homework.spring05.service.BookConverter;
import ru.otus.group202205.homework.spring05.service.GenreConverter;

@Service
@RequiredArgsConstructor
public class BookConverterImpl implements BookConverter {

  private final AuthorConverter authorConverter;

  private final GenreConverter genreConverter;

  @Override
  public String convertBook(Book book) {
    return String.format("Book id: %d%stitle: %s%sisbn: %s%swritten by: %s in genre: %s%s",
        book.getId(),
        System.lineSeparator(),
        book.getTitle(),
        System.lineSeparator(),
        book.getIsbn(),
        System.lineSeparator(),
        authorConverter
            .convertAuthor(book.getAuthor())
            .trim(),
        genreConverter
            .convertGenre(book.getGenre())
            .trim(),
        System.lineSeparator());
  }

  @Override
  public String convertBooks(List<Book> books) {
    StringBuilder result = new StringBuilder("Book list").append(System.lineSeparator());
    books.forEach(book -> result.append(convertBook(book)));
    return result.toString();
  }

}
