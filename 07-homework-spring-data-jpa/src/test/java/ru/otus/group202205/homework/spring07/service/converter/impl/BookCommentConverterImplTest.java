package ru.otus.group202205.homework.spring07.service.converter.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.group202205.homework.spring07.dto.BookCommentDto;
import ru.otus.group202205.homework.spring07.dto.BookSimpleDto;
import ru.otus.group202205.homework.spring07.model.Book;
import ru.otus.group202205.homework.spring07.model.BookComment;
import ru.otus.group202205.homework.spring07.service.converter.BookCommentConverter;
import ru.otus.group202205.homework.spring07.service.mapper.BookCommentMapper;
import ru.otus.group202205.homework.spring07.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring07.testdata.BookCommentTestDataComponent;
import ru.otus.group202205.homework.spring07.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring07.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {BookCommentConverterImpl.class, BookCommentTestDataComponent.class, BookTestDataComponent.class, GenreTestDataComponent.class,
    AuthorTestDataComponent.class})
class BookCommentConverterImplTest {

  @Autowired
  private BookCommentConverter bookCommentConverter;
  @Autowired
  private BookCommentTestDataComponent bookCommentTestDataComponent;
  @MockBean
  private BookCommentMapper bookCommentMapper;

  @BeforeEach
  void init() {
    Mockito.reset(bookCommentMapper);
    Mockito
        .doAnswer(invocation -> {
          BookComment bookComment = invocation.getArgument(0);
          BookCommentDto result = new BookCommentDto();
          result.setId(bookComment.getId());
          result.setText(bookComment.getText());
          result.setCreated(bookComment.getCreated());
          Book book = bookComment.getBook();
          if (book != null) {
            BookSimpleDto bookDto = new BookSimpleDto();
            bookDto.setId(book.getId());
            result.setBook(bookDto);
          }
          return result;
        })
        .when(bookCommentMapper)
        .toDto(any());
    Mockito
        .doAnswer(invocation -> {
          BookCommentDto bookCommentDto = invocation.getArgument(0);
          BookComment result = new BookComment();
          result.setId(bookCommentDto.getId());
          result.setText(bookCommentDto.getText());
          result.setCreated(bookCommentDto.getCreated());
          BookSimpleDto bookDto = bookCommentDto.getBook();
          if (bookDto != null) {
            Book book = new Book();
            book.setId(bookDto.getId());
            result.setBook(book);
          }
          return result;
        })
        .when(bookCommentMapper)
        .toEntity(any());
  }

  @Test
  void shouldBeConvertBookCommentWithFullInfoToString() {
    BookCommentDto bookCommentDto = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBookSimpleDto();

    String actualBookOutput = bookCommentConverter.convertBookComment(bookCommentDto);
    assertThat(actualBookOutput)
        .isNotNull()
        .isEqualTo("Comment id 1 book with id 1 at 2022-10-26T22:35:31." + System.lineSeparator() + " with text: 'Nice book!'");
  }

  @Test
  void shouldBeConvertBookCommentsWithFullInfoToString() {
    List<BookCommentDto> bookCommentDtos = bookCommentTestDataComponent
        .getAllBookComments()
        .stream()
        .map(bookCommentMapper::toDto)
        .collect(Collectors.toList());

    String actualBookOutput = bookCommentConverter.convertBookComments(bookCommentDtos);
    assertThat(actualBookOutput)
        .isNotNull()
        .isEqualTo("Comment list:" + System.lineSeparator() + "Comment id 3 book with id 2 at 2022-10-26T22:35:32." + System.lineSeparator()
            + " with text: 'Complicated book!'" + System.lineSeparator() + "Comment id 2 book with id 1 at 2022-10-26T22:35:32." + System.lineSeparator()
            + " with text: 'I like this author!'" + System.lineSeparator() + "Comment id 1 book with id 1 at 2022-10-26T22:35:31." + System.lineSeparator()
            + " with text: 'Nice book!'" + System.lineSeparator());
  }

}