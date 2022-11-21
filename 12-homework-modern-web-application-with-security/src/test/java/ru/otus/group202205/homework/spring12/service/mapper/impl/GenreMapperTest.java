package ru.otus.group202205.homework.spring12.service.mapper.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.group202205.homework.spring12.dto.GenreDto;
import ru.otus.group202205.homework.spring12.model.Genre;
import ru.otus.group202205.homework.spring12.service.mapper.GenreMapper;
import ru.otus.group202205.homework.spring12.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {GenreMapperImpl.class, GenreTestDataComponent.class})
class GenreMapperTest {

  @Autowired
  private GenreMapper genreMapper;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;

  @Test
  void shouldBeConvertToDtoFromEntity() {
    Genre genre = genreTestDataComponent.getNovellGenre();
    GenreDto exceptedGenreDto = genreTestDataComponent.getNovellGenreDto();
    GenreDto actualGenreDto = genreMapper.toDto(genre);
    assertThat(actualGenreDto)
        .isNotNull()
        .isEqualTo(exceptedGenreDto);
  }

  @Test
  void shouldBeConvertToEntityFromDto() {
    GenreDto genreDto = genreTestDataComponent.getNovellGenreDto();
    Genre exceptedGenre = genreTestDataComponent.getNovellGenre();
    Genre actualGenre = genreMapper.toEntity(genreDto);
    assertThat(actualGenre)
        .isNotNull()
        .isEqualTo(exceptedGenre);
  }

  @Test
  void shouldReturnNullEntityFromNullDto() {
    Genre actualGenre = genreMapper.toEntity(null);
    assertThat(actualGenre).isNull();
  }

  @Test
  void shouldReturnNullDtoFromNullEntity() {
    GenreDto actualGenreDto = genreMapper.toDto(null);
    assertThat(actualGenreDto).isNull();
  }

}