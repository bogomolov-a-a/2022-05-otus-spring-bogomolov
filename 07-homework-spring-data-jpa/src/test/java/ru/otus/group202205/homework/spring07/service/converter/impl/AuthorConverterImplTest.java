package ru.otus.group202205.homework.spring07.service.converter.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.group202205.homework.spring07.dto.AuthorDto;
import ru.otus.group202205.homework.spring07.service.converter.AuthorConverter;
import ru.otus.group202205.homework.spring07.service.mapper.AuthorMapper;
import ru.otus.group202205.homework.spring07.service.mapper.impl.AuthorMapperImpl;
import ru.otus.group202205.homework.spring07.testdata.AuthorTestDataComponent;

@SpringBootTest(classes = {AuthorConverterImpl.class, AuthorTestDataComponent.class, AuthorMapperImpl.class})
class AuthorConverterImplTest {

  @Autowired
  private AuthorConverter authorConverter;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;
  @Autowired
  private AuthorMapper authorMapper;

  @Test
  void shouldBeConvertAuthorWithFullInfoToString() {
    AuthorDto authorDto = authorMapper.toDto(authorTestDataComponent.getMakiseKurisuAuthor());
    String actualAuthorOutput = authorConverter.convertAuthor(authorDto);
    assertThat(actualAuthorOutput)
        .isNotNull()
        .isEqualTo("Author id: null" + System.lineSeparator() + "surname: Makise" + System.lineSeparator() + "name: Kurisu" + System.lineSeparator()
            + "patronymic: not set" + System.lineSeparator() + "birth year: 1992" + System.lineSeparator() + "death year: null" + System.lineSeparator());
  }

  @Test
  void shouldBeConvertAuthorsWithFullInfoToString() {

    List<AuthorDto> authorDtos = authorTestDataComponent
        .getAllExistingAuthors()
        .stream()
        .map(authorMapper::toDto)
        .collect(Collectors.toList());
    String actualAuthorOutput = authorConverter.convertAuthors(authorDtos);
    assertThat(actualAuthorOutput)
        .isNotNull()
        .isEqualTo("Author list" + System.lineSeparator() + "Author id: 1" + System.lineSeparator() + "surname: Bulychev" + System.lineSeparator() + "name: Kir"
            + System.lineSeparator() + "patronymic: not set" + System.lineSeparator() + "birth year: 1934" + System.lineSeparator() + "death year: 2003"
            + System.lineSeparator() + "Author id: 2" + System.lineSeparator() + "surname: Tolstoy" + System.lineSeparator() + "name: Lev"
            + System.lineSeparator() + "patronymic: Nikolayevich" + System.lineSeparator() + "birth year: 1828" + System.lineSeparator() + "death year: 1910"
            + System.lineSeparator());
  }

}