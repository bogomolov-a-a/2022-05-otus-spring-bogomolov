package ru.otus.group202205.homework.spring10.service.mapper.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.group202205.homework.spring10.dto.BookFullDto;
import ru.otus.group202205.homework.spring10.dto.BookSimpleDto;
import ru.otus.group202205.homework.spring10.model.Book;
import ru.otus.group202205.homework.spring10.service.mapper.BookMapper;
import ru.otus.group202205.homework.spring10.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring10.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring10.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {BookMapperImpl.class, BookTestDataComponent.class, AuthorTestDataComponent.class, GenreTestDataComponent.class})
class BookMapperTest {

  private static final long EXISTING_BOOK_ID_VALUE = 1L;
  @Autowired
  private BookMapper bookMapper;
  @Autowired
  private BookTestDataComponent bookTestDataComponent;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;

  @Test
  void shouldBeConvertToDtoFromEntity() {
    Book book = bookTestDataComponent.getGirlFromEarthBook();
    Assertions
        .assertThat(book.getComments())
        .isNotNull()
        .isEmpty();
    assertThat(book.getAuthor())
        .isNotNull()
        .isEqualTo(authorTestDataComponent.getBulychevKirAuthor());
    assertThat(book.getGenre())
        .isNotNull()
        .isEqualTo(genreTestDataComponent.getScienceFictionGenre());
    BookFullDto exceptedBookDto = bookTestDataComponent.getGirlFromEarthBookDto();
    BookFullDto actualBookDto = bookMapper.toFullDto(book);
    assertThat(actualBookDto)
        .isNotNull()
        .isEqualTo(exceptedBookDto);
    assertThat(actualBookDto.getAuthor())
        .isNotNull()
        .isEqualTo(authorTestDataComponent.getBulychevKirAuthorDto());
    assertThat(actualBookDto.getGenre())
        .isNotNull()
        .isEqualTo(genreTestDataComponent.getScienceFictionGenreDto());
  }

  @Test
  void shouldBeConvertToEntityFromDto() {
    BookFullDto bookDto = bookTestDataComponent.getGirlFromEarthBookDto();
    assertThat(bookDto.getAuthor())
        .isNotNull()
        .isEqualTo(authorTestDataComponent.getBulychevKirAuthorDto());
    assertThat(bookDto.getGenre())
        .isNotNull()
        .isEqualTo(genreTestDataComponent.getScienceFictionGenreDto());
    Book exceptedBook = bookTestDataComponent.getGirlFromEarthBook();
    Book actualBook = bookMapper.toEntityFromFull(bookDto);
    assertThat(actualBook)
        .isNotNull()
        .isEqualTo(exceptedBook);
    Assertions
        .assertThat(actualBook.getComments())
        .isNotNull()
        .isEmpty();
    assertThat(actualBook.getAuthor())
        .isNotNull()
        .isEqualTo(authorTestDataComponent.getBulychevKirAuthor());
    assertThat(actualBook.getGenre())
        .isNotNull()
        .isEqualTo(genreTestDataComponent.getScienceFictionGenre());
  }

  @Test
  void shouldBeConvertToDtoFromEntityWithEmptyAuthorAndGenre() {
    Book book = bookTestDataComponent.getBookWithoutAuthorAndGenre();
    Assertions
        .assertThat(book.getComments())
        .isNotNull()
        .isEmpty();
    assertThat(book.getAuthor()).isNull();
    assertThat(book.getGenre()).isNull();
    BookFullDto exceptedBookDto = bookTestDataComponent.getBookWithoutAuthorAndGenreDto();
    BookFullDto actualBookDto = bookMapper.toFullDto(book);
    assertThat(actualBookDto)
        .isNotNull()
        .isEqualTo(exceptedBookDto);
    assertThat(actualBookDto.getAuthor()).isNull();
    assertThat(actualBookDto.getGenre()).isNull();
  }

  @Test
  void shouldBeConvertToEntityFromDtoEmptyAuthorAndGenre() {
    BookFullDto bookDto = bookTestDataComponent.getBookWithoutAuthorAndGenreDto();
    assertThat(bookDto.getAuthor()).isNull();
    assertThat(bookDto.getGenre()).isNull();
    Book exceptedBook = bookTestDataComponent.getBookWithoutAuthorAndGenre();
    Book actualBook = bookMapper.toEntityFromFull(bookDto);
    assertThat(actualBook)
        .isNotNull()
        .isEqualTo(exceptedBook);
    Assertions
        .assertThat(actualBook.getComments())
        .isNotNull()
        .isEmpty();
    assertThat(actualBook.getAuthor()).isNull();
    assertThat(actualBook.getGenre()).isNull();
  }

  @Test
  void shouldBeConvertToDtoSimpleFromEntity() {
    Book book = bookTestDataComponent.getGirlFromEarthBookSimple();
    Assertions
        .assertThat(book.getComments())
        .isNotNull()
        .isEmpty();
    assertThat(book.getAuthor()).isNull();
    assertThat(book.getGenre()).isNull();
    assertThat(book.getTitle()).isNotNull();
    assertThat(book.getIsbn()).isNull();
    assertThat(book.getId())
        .isNotNull()
        .isEqualTo(EXISTING_BOOK_ID_VALUE);
    BookSimpleDto exceptedBookDto = bookTestDataComponent.getGirlFromEarthBookSimpleDto();
    BookSimpleDto actualBookDto = bookMapper.toSimpleDto(book);
    assertThat(actualBookDto)
        .isNotNull()
        .isEqualTo(exceptedBookDto);
  }

  @Test
  void shouldBeConvertToEntityFromSimpleDto() {
    BookSimpleDto bookDto = bookTestDataComponent.getGirlFromEarthBookSimpleDto();
    Book exceptedBook = bookTestDataComponent.getGirlFromEarthBookSimple();
    Book actualBook = bookMapper.toEntityFromSimple(bookDto);
    assertThat(actualBook)
        .isNotNull()
        .isEqualTo(exceptedBook);
    Assertions
        .assertThat(actualBook.getComments())
        .isNotNull()
        .isEmpty();
    assertThat(actualBook.getAuthor()).isNull();
    assertThat(actualBook.getGenre()).isNull();
    assertThat(actualBook.getTitle()).isNotNull();
    assertThat(actualBook.getIsbn()).isNull();
    assertThat(actualBook.getId())
        .isNotNull()
        .isEqualTo(EXISTING_BOOK_ID_VALUE);
  }

  @Test
  void shouldReturnNullEntityFromNullSimpleDto() {
    Book actualBook = bookMapper.toEntityFromSimple(null);
    assertThat(actualBook).isNull();
  }

  @Test
  void shouldReturnNullDtoSimpleFromNullEntity() {
    BookSimpleDto actualBookSimpleDto = bookMapper.toSimpleDto(null);
    assertThat(actualBookSimpleDto).isNull();
  }

  @Test
  void shouldReturnNullEntityFromNullDto() {
    Book actualBook = bookMapper.toEntityFromFull(null);
    assertThat(actualBook).isNull();
  }

  @Test
  void shouldReturnNullDtoFromNullEntity() {
    BookFullDto actualBookFullDto = bookMapper.toFullDto(null);
    assertThat(actualBookFullDto).isNull();
  }

}