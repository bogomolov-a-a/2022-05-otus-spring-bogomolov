package ru.otus.group202205.homework.spring05.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.group202205.homework.spring05.model.Author;
import ru.otus.group202205.homework.spring05.model.Book;
import ru.otus.group202205.homework.spring05.model.Genre;
import ru.otus.group202205.homework.spring05.service.BookConverter;

@SpringBootTest(classes = {BookConverterImpl.class, AuthorConverterImpl.class, GenreConverterImpl.class})
class BookConverterImplTest {

  @Autowired
  private BookConverter bookConverter;

  @Test
  void shouldBeConvertGenreWithFullInfoToString() {
    Book book = new Book();
    book.setId(2L);
    book.setTitle("Childhood. Boyhood. Youth");
    book.setIsbn("978-5-04-116640-3");
    Author author = new Author();
    author.setId(2L);
    author.setSurname("Tolstoy");
    author.setName("Lev");
    author.setPatronymic("Nikolayevich");
    author.setBirthYear(1828L);
    author.setDeathYear(1910L);
    Genre genre = new Genre();
    genre.setId(1L);
    genre.setName("Novell");
    book.setGenre(genre);
    book.setAuthor(author);
    String actualBookOutput = bookConverter.convertBook(book);
    assertThat(actualBookOutput)
        .isNotNull()
        .isEqualTo("Book id: 2" + System.lineSeparator() + "title: Childhood. Boyhood. Youth" + System.lineSeparator() + "isbn: 978-5-04-116640-3"
            + System.lineSeparator() + "written by: Author id: 2" + System.lineSeparator() + "surname: Tolstoy" + System.lineSeparator() + "name: Lev"
            + System.lineSeparator() + "patronymic: Nikolayevich" + System.lineSeparator() + "birth year: 1828" + System.lineSeparator()
            + "death year: 1910 in genre: Genre id: 1" + System.lineSeparator() + "name: Novell"
            + System.lineSeparator());
  }

  @Test
  void shouldBeConvertGenresWithFullInfoToString() {
    Book firstBook = new Book();
    firstBook.setId(1L);
    firstBook.setTitle("Girl from the Earth");
    firstBook.setIsbn("978-5-699-11438-2");
    Author firstBookAuthor = new Author();
    firstBookAuthor.setId(1L);
    firstBookAuthor.setSurname("Bulychev");
    firstBookAuthor.setName("Kir");
    firstBookAuthor.setBirthYear(1934L);
    firstBookAuthor.setDeathYear(2003L);
    Genre firstBookGenre = new Genre();
    firstBookGenre.setId(2L);
    firstBookGenre.setName("Science fiction");
    firstBook.setGenre(firstBookGenre);
    firstBook.setAuthor(firstBookAuthor);
    Book secondBook = new Book();
    secondBook.setId(2L);
    secondBook.setTitle("Childhood. Boyhood. Youth");
    secondBook.setIsbn("978-5-04-116640-3");
    Author secondBookAuthor = new Author();
    secondBookAuthor.setId(2L);
    secondBookAuthor.setSurname("Tolstoy");
    secondBookAuthor.setName("Lev");
    secondBookAuthor.setPatronymic("Nikolayevich");
    secondBookAuthor.setBirthYear(1828L);
    secondBookAuthor.setDeathYear(1910L);
    Genre secondBookGenre = new Genre();
    secondBookGenre.setId(1L);
    secondBookGenre.setName("Novell");
    secondBook.setGenre(secondBookGenre);
    secondBook.setAuthor(secondBookAuthor);
    String actualGenreOutput = bookConverter.convertBooks(List.of(firstBook,
        secondBook));
    assertThat(actualGenreOutput)
        .isNotNull()
        .isEqualTo("Book list" + System.lineSeparator() + "Book id: 1" + System.lineSeparator() + "title: Girl from the Earth" + System.lineSeparator()
            + "isbn: 978-5-699-11438-2" + System.lineSeparator() + "written by: Author id: 1" + System.lineSeparator() + "surname: Bulychev"
            + System.lineSeparator() + "name: Kir" + System.lineSeparator() + "patronymic: not set" + System.lineSeparator() + "birth year: 1934"
            + System.lineSeparator() + "death year: 2003 in genre: Genre id: 2" + System.lineSeparator() + "name: Science fiction"
            + System.lineSeparator() + "Book id: 2" + System.lineSeparator() + "title: Childhood. Boyhood. Youth"
            + System.lineSeparator() + "isbn: 978-5-04-116640-3" + System.lineSeparator() + "written by: Author id: 2" + System.lineSeparator()
            + "surname: Tolstoy" + System.lineSeparator() + "name: Lev" + System.lineSeparator() + "patronymic: Nikolayevich" + System.lineSeparator()
            + "birth year: 1828" + System.lineSeparator() + "death year: 1910 in genre: Genre id: 1"
            + System.lineSeparator() + "name: Novell" + System.lineSeparator());
  }

}