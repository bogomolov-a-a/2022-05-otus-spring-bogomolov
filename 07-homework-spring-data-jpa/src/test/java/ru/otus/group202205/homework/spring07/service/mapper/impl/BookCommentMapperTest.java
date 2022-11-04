package ru.otus.group202205.homework.spring07.service.mapper.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.group202205.homework.spring07.dto.BookCommentDto;
import ru.otus.group202205.homework.spring07.dto.BookSimpleDto;
import ru.otus.group202205.homework.spring07.model.Book;
import ru.otus.group202205.homework.spring07.model.BookComment;
import ru.otus.group202205.homework.spring07.service.mapper.BookCommentMapper;
import ru.otus.group202205.homework.spring07.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring07.testdata.BookCommentTestDataComponent;
import ru.otus.group202205.homework.spring07.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring07.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {BookCommentMapperImpl.class, BookCommentTestDataComponent.class, BookTestDataComponent.class, AuthorTestDataComponent.class,
    GenreTestDataComponent.class})
class BookCommentMapperTest {

  @Autowired
  private BookCommentMapper bookCommentMapper;
  @Autowired
  private BookCommentTestDataComponent bookCommentTestDataComponent;
  @Autowired
  private BookTestDataComponent bookTestDataComponent;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;

  @Test
  void shouldBeConvertToDtoFromEntity() {
    BookComment bookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBookSimple();
    Book exceptedBook = bookTestDataComponent.getGirlFromEarthBookSimple();
    assertThat(exceptedBook.getComments())
        .isNotNull()
        .isEmpty();
    assertThat(exceptedBook.getAuthor()).isNull();
    assertThat(exceptedBook.getGenre()).isNull();
    assertThat(exceptedBook.getTitle()).isNull();
    assertThat(exceptedBook.getIsbn()).isNull();
    assertThat(exceptedBook.getId()).isNotNull();
    BookCommentDto exceptedBookCommentDto = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBookSimpleDto();
    BookCommentDto actualBookCommentDto = bookCommentMapper.toDto(bookComment);
    assertThat(actualBookCommentDto)
        .isNotNull()
        .isEqualTo(exceptedBookCommentDto);
    BookSimpleDto actualBook = actualBookCommentDto.getBook();
    assertThat(actualBook)
        .isNotNull()
        .isEqualTo(exceptedBookCommentDto.getBook());
  }

  @Test
  void shouldBeConvertToEntityFromDto() {

    BookCommentDto bookCommentDto = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBookSimpleDto();
    Book exceptedBook = bookTestDataComponent.getGirlFromEarthBookSimple();
    BookComment expectedBookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBookSimple();
    BookComment actualBookComment = bookCommentMapper.toEntity(bookCommentDto);
    assertThat(actualBookComment)
        .isNotNull()
        .isEqualTo(expectedBookComment);
    Book actualBook = actualBookComment.getBook();
    assertThat(actualBook)
        .isNotNull()
        .isEqualTo(exceptedBook);
    assertThat(exceptedBook.getAuthor()).isNull();
    assertThat(exceptedBook.getGenre()).isNull();
    assertThat(exceptedBook.getTitle()).isNull();
    assertThat(exceptedBook.getIsbn()).isNull();
    assertThat(exceptedBook.getId()).isNotNull();
  }

  @Test
  void shouldReturnNullEntityFromNullDto() {
    BookComment actualBookComment = bookCommentMapper.toEntity(null);
    assertThat(actualBookComment).isNull();
  }

  @Test
  void shouldReturnNullDtoFromNullEntity() {
    BookCommentDto actualBookCommentDto = bookCommentMapper.toDto(null);
    assertThat(actualBookCommentDto).isNull();
  }

}