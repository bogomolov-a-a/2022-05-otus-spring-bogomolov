package ru.otus.group202205.homework.spring08.service.converter.impl;

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
import ru.otus.group202205.homework.spring08.dto.AuthorDto;
import ru.otus.group202205.homework.spring08.model.Author;
import ru.otus.group202205.homework.spring08.service.converter.AuthorConverter;
import ru.otus.group202205.homework.spring08.service.mapper.AuthorMapper;
import ru.otus.group202205.homework.spring08.testdata.AuthorTestDataComponent;

@SpringBootTest(classes = {AuthorConverterImpl.class, AuthorTestDataComponent.class})
class AuthorConverterImplTest {

  @Autowired
  private AuthorConverter authorConverter;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;
  @MockBean
  private AuthorMapper authorMapper;

  @BeforeEach
  void init() {
    Mockito.reset(authorMapper);
    Mockito
        .doAnswer(invocation -> {
          Author author = invocation.getArgument(0);
          AuthorDto result = new AuthorDto();
          result.setId(author.getId());
          result.setName(author.getName());
          result.setSurname(author.getSurname());
          result.setPatronymic(author.getPatronymic());
          result.setBirthYear(author.getBirthYear());
          result.setDeathYear(author.getDeathYear());
          return result;
        })
        .when(authorMapper)
        .toDto(any());
    Mockito
        .doAnswer(invocation -> {
          AuthorDto authorDto = invocation.getArgument(0);
          Author result = new Author();
          result.setId(authorDto.getId());
          result.setName(authorDto.getName());
          result.setSurname(authorDto.getSurname());
          result.setPatronymic(authorDto.getPatronymic());
          result.setBirthYear(authorDto.getBirthYear());
          result.setDeathYear(authorDto.getDeathYear());
          return result;
        })
        .when(authorMapper)
        .toEntity(any());
  }

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