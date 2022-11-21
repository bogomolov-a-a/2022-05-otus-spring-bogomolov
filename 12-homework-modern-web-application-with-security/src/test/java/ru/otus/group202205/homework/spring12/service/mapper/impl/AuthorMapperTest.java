package ru.otus.group202205.homework.spring12.service.mapper.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.group202205.homework.spring12.dto.AuthorDto;
import ru.otus.group202205.homework.spring12.model.Author;
import ru.otus.group202205.homework.spring12.service.mapper.AuthorMapper;
import ru.otus.group202205.homework.spring12.testdata.AuthorTestDataComponent;

@SpringBootTest(classes = {AuthorTestDataComponent.class, AuthorMapperImpl.class})
class AuthorMapperTest {

  @Autowired
  private AuthorMapper authorMapper;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;

  @Test
  void shouldBeConvertToDtoFromEntity() {
    Author author = authorTestDataComponent.getBulychevKirAuthor();
    AuthorDto exceptedAuthorDto = authorTestDataComponent.getBulychevKirAuthorDto();
    AuthorDto actualAuthorDto = authorMapper.toDto(author);
    assertThat(actualAuthorDto)
        .isNotNull()
        .isEqualTo(exceptedAuthorDto);
  }

  @Test
  void shouldBeConvertToEntityFromDto() {
    AuthorDto authorDto = authorTestDataComponent.getBulychevKirAuthorDto();
    Author exceptedAuthor = authorTestDataComponent.getBulychevKirAuthor();
    Author actualAuthor = authorMapper.toEntity(authorDto);
    assertThat(actualAuthor)
        .isNotNull()
        .isEqualTo(exceptedAuthor);
  }

  @Test
  void shouldReturnNullEntityFromNullDto() {
    Author actualAuthor = authorMapper.toEntity(null);
    assertThat(actualAuthor).isNull();
  }

  @Test
  void shouldReturnNullDtoFromNullEntity() {
    AuthorDto actualAuthorDto = authorMapper.toDto(null);
    assertThat(actualAuthorDto).isNull();
  }

}