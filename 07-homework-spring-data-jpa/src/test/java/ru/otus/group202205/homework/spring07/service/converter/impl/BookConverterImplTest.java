package ru.otus.group202205.homework.spring07.service.converter.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.group202205.homework.spring07.dto.BookFullDto;
import ru.otus.group202205.homework.spring07.service.converter.BookConverter;
import ru.otus.group202205.homework.spring07.service.mapper.BookMapper;
import ru.otus.group202205.homework.spring07.service.mapper.impl.BookMapperImpl;
import ru.otus.group202205.homework.spring07.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring07.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring07.testdata.GenreTestDataComponent;

@SpringBootTest(
    classes = {BookConverterImpl.class, AuthorConverterImpl.class, GenreConverterImpl.class, BookTestDataComponent.class, AuthorTestDataComponent.class,
        GenreTestDataComponent.class, BookMapperImpl.class})
class BookConverterImplTest {

  @Autowired
  private BookConverter bookConverter;
  @Autowired
  private BookTestDataComponent bookTestDataComponent;
  @Autowired
  private BookMapper bookMapper;

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