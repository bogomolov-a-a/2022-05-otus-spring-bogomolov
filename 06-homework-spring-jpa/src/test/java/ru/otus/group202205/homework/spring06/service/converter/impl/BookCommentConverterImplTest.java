package ru.otus.group202205.homework.spring06.service.converter.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.group202205.homework.spring06.dto.BookCommentDto;
import ru.otus.group202205.homework.spring06.service.converter.BookCommentConverter;
import ru.otus.group202205.homework.spring06.service.mapper.BookCommentMapper;
import ru.otus.group202205.homework.spring06.service.mapper.impl.BookCommentMapperImpl;
import ru.otus.group202205.homework.spring06.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring06.testdata.BookCommentTestDataComponent;
import ru.otus.group202205.homework.spring06.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring06.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {BookCommentConverterImpl.class, BookCommentMapperImpl.class, BookCommentTestDataComponent.class, BookTestDataComponent.class,
    GenreTestDataComponent.class, AuthorTestDataComponent.class})
class BookCommentConverterImplTest {

  @Autowired
  private BookCommentConverter bookCommentConverter;
  @Autowired
  private BookCommentTestDataComponent bookCommentTestDataComponent;
  @Autowired
  private BookCommentMapper bookCommentMapper;

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