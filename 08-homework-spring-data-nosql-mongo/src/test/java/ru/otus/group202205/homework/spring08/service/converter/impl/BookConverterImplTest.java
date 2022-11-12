package ru.otus.group202205.homework.spring08.service.converter.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.group202205.homework.spring08.dto.AuthorDto;
import ru.otus.group202205.homework.spring08.dto.BookFullDto;
import ru.otus.group202205.homework.spring08.dto.GenreDto;
import ru.otus.group202205.homework.spring08.model.Author;
import ru.otus.group202205.homework.spring08.model.Book;
import ru.otus.group202205.homework.spring08.model.Genre;
import ru.otus.group202205.homework.spring08.service.converter.AuthorConverter;
import ru.otus.group202205.homework.spring08.service.converter.BookConverter;
import ru.otus.group202205.homework.spring08.service.converter.GenreConverter;
import ru.otus.group202205.homework.spring08.service.mapper.BookMapper;
import ru.otus.group202205.homework.spring08.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring08.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring08.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {BookConverterImpl.class, BookTestDataComponent.class, AuthorTestDataComponent.class, GenreTestDataComponent.class})
class BookConverterImplTest {

  @Autowired
  private BookConverter bookConverter;
  @Autowired
  private BookTestDataComponent bookTestDataComponent;
  @MockBean
  private BookMapper bookMapper;
  @MockBean
  private AuthorConverter authorConverter;
  @MockBean
  private GenreConverter genreConverter;

  @BeforeEach
  void init() {
    Mockito.reset(bookMapper);
    Mockito.reset(authorConverter);
    Mockito.reset(genreConverter);
    Mockito
        .doAnswer(invocation -> {
          BookFullDto bookDto = invocation.getArgument(0);
          Book book = new Book();
          book.setId(bookDto.getId());
          book.setTitle(bookDto.getTitle());
          book.setIsbn(bookDto.getIsbn());
          AuthorDto authorDto = bookDto.getAuthor();
          if (authorDto != null) {
            Author author = new Author();
            author.setId(authorDto.getId());
            author.setSurname(authorDto.getSurname());
            author.setName(authorDto.getName());
            author.setPatronymic(authorDto.getPatronymic());
            author.setBirthYear(authorDto.getBirthYear());
            author.setDeathYear(authorDto.getDeathYear());
            book.setAuthor(author);
          }
          GenreDto genreDto = bookDto.getGenre();
          if (genreDto != null) {
            Genre genre = new Genre();
            genre.setId(genreDto.getId());
            genre.setName(genreDto.getName());
            book.setGenre(genre);
          }
          return book;
        })
        .when(bookMapper)
        .toEntityFromFull(any());
    Mockito
        .doAnswer(invocation -> {
          Book book = invocation.getArgument(0);
          BookFullDto bookDto = new BookFullDto();
          bookDto.setId(book.getId());
          bookDto.setTitle(book.getTitle());
          bookDto.setIsbn(book.getIsbn());
          Author author = book.getAuthor();
          if (author != null) {
            AuthorDto authorDto = new AuthorDto();
            authorDto.setId(author.getId());
            authorDto.setSurname(author.getSurname());
            authorDto.setName(author.getName());
            authorDto.setPatronymic(author.getPatronymic());
            authorDto.setBirthYear(author.getBirthYear());
            authorDto.setDeathYear(author.getDeathYear());
            bookDto.setAuthor(authorDto);
          }
          Genre genre = book.getGenre();
          if (genre != null) {
            GenreDto genreDto = new GenreDto();
            genreDto.setId(genre.getId());
            genreDto.setName(genre.getName());
            bookDto.setGenre(genreDto);
          }
          return bookDto;
        })
        .when(bookMapper)
        .toFullDto(any());
    Mockito
        .doAnswer(invocation -> {
          AuthorDto author = invocation.getArgument(0);
          return String.format("Author id: %s%ssurname: %s%sname: %s%spatronymic: %s%sbirth year: %d%sdeath year: %s%s",
              author.getId(),
              System.lineSeparator(),
              author.getSurname(),
              System.lineSeparator(),
              author.getName(),
              System.lineSeparator(),
              author.getPatronymic(),
              System.lineSeparator(),
              author.getBirthYear(),
              System.lineSeparator(),
              author.getDeathYear(),
              System.lineSeparator());
        })
        .when(authorConverter)
        .convertAuthor(any());
    Mockito
        .doAnswer(invocation -> {
          List<AuthorDto> authors = invocation.getArgument(0);
          StringBuilder result = new StringBuilder("Author list").append(System.lineSeparator());
          authors.forEach(author -> result.append(authorConverter.convertAuthor(author)));
          return result.toString();
        })
        .when(authorConverter)
        .convertAuthors(anyList());
    Mockito
        .doAnswer(invocation -> {
          GenreDto genre = invocation.getArgument(0);
          return String.format("Genre id: %s%sname: %s%s",
              genre.getId(),
              System.lineSeparator(),
              genre.getName(),
              System.lineSeparator());
        })
        .when(genreConverter)
        .convertGenre(any());
    Mockito
        .doAnswer(invocation -> {
          List<GenreDto> genres = invocation.getArgument(0);
          StringBuilder result = new StringBuilder("Genre list").append(System.lineSeparator());
          genres.forEach(genre -> result.append(genreConverter.convertGenre(genre)));
          return result.toString();
        })
        .when(genreConverter)
        .convertGenres(anyList());
  }

  @Test
  void shouldBeConvertBookWithFullInfoToString() {
    BookFullDto bookDto = bookMapper.toFullDto(bookTestDataComponent.getGirlFromEarthBook());
    String actualBookOutput = bookConverter.convertBook(bookDto);
    assertThat(actualBookOutput)
        .isNotNull()
        .isEqualTo(
            "Book id: 1" + System.lineSeparator() + "title: Girl from the Earth" + System.lineSeparator() + "isbn: 978-5-699-11438-2" + System.lineSeparator()
                + "written by: Author id: 1" + System.lineSeparator() + "surname: Bulychev" + System.lineSeparator() + "name: Kir" + System.lineSeparator()
                + "patronymic: not set" + System.lineSeparator() + "birth year: 1934" + System.lineSeparator() + "death year: 2003 in genre: Genre id: 2"
                + System.lineSeparator() + "name: Science fiction" + System.lineSeparator());
  }

  @Test
  void shouldBeConvertBooksWithFullInfoToString() {
    List<BookFullDto> bookDtos = bookTestDataComponent
        .getAllExistingBooks()
        .stream()
        .map(bookMapper::toFullDto)
        .collect(Collectors.toList());
    String actualBookOutput = bookConverter.convertBooks(bookDtos);
    assertThat(actualBookOutput)
        .isNotNull()
        .isEqualTo("Book list" + System.lineSeparator() + "Book id: 1" + System.lineSeparator() + "title: Girl from the Earth" + System.lineSeparator()
            + "isbn: 978-5-699-11438-2" + System.lineSeparator() + "written by: Author id: 1" + System.lineSeparator() + "surname: Bulychev"
            + System.lineSeparator() + "name: Kir" + System.lineSeparator() + "patronymic: not set" + System.lineSeparator() + "birth year: 1934"
            + System.lineSeparator() + "death year: 2003 in genre: Genre id: 2" + System.lineSeparator() + "name: Science fiction" + System.lineSeparator()
            + "Book id: 2" + System.lineSeparator() + "title: Childhood. Boyhood. Youth" + System.lineSeparator() + "isbn: 978-5-04-116640-3"
            + System.lineSeparator() + "written by: Author id: 2" + System.lineSeparator() + "surname: Tolstoy" + System.lineSeparator() + "name: Lev"
            + System.lineSeparator() + "patronymic: Nikolayevich" + System.lineSeparator() + "birth year: 1828" + System.lineSeparator()
            + "death year: 1910 in genre: Genre id: 1" + System.lineSeparator() + "name: Novell" + System.lineSeparator());
  }

}