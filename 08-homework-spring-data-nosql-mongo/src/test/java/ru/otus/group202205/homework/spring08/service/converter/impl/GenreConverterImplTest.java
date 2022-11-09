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
import ru.otus.group202205.homework.spring08.dto.GenreDto;
import ru.otus.group202205.homework.spring08.model.Genre;
import ru.otus.group202205.homework.spring08.service.converter.GenreConverter;
import ru.otus.group202205.homework.spring08.service.mapper.GenreMapper;
import ru.otus.group202205.homework.spring08.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {GenreConverterImpl.class, GenreTestDataComponent.class})
class GenreConverterImplTest {

  @Autowired
  private GenreConverter genreConverter;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;
  @MockBean
  private GenreMapper genreMapper;

  @BeforeEach
  void init() {
    Mockito.reset(genreMapper);
    Mockito
        .doAnswer(invocation -> {
          Genre genre = invocation.getArgument(0);
          GenreDto result = new GenreDto();
          result.setId(genre.getId());
          result.setName(genre.getName());
          return result;
        })
        .when(genreMapper)
        .toDto(any());
    Mockito
        .doAnswer(invocation -> {
          GenreDto genre = invocation.getArgument(0);
          Genre result = new Genre();
          result.setId(genre.getId());
          result.setName(genre.getName());
          return result;
        })
        .when(genreMapper)
        .toEntity(any());
  }

  @Test
  void shouldBeConvertGenreWithFullInfoToString() {
    GenreDto genreDto = genreTestDataComponent.getScienceFictionGenreDto();
    String actualGenreOutput = genreConverter.convertGenre(genreDto);
    assertThat(actualGenreOutput)
        .isNotNull()
        .isEqualTo("Genre id: 2" + System.lineSeparator() + "name: Science fiction" + System.lineSeparator());
  }

  @Test
  void shouldBeConvertGenresWithFullInfoToString() {
    List<GenreDto> genreDtos = genreTestDataComponent
        .getAllExistingGenres()
        .stream()
        .map(genreMapper::toDto)
        .collect(Collectors.toList());
    String actualGenreOutput = genreConverter.convertGenres(genreDtos);
    assertThat(actualGenreOutput)
        .isNotNull()
        .isEqualTo("Genre list" + System.lineSeparator() + "Genre id: 1" + System.lineSeparator() + "name: Novell" + System.lineSeparator() + "Genre id: 2"
            + System.lineSeparator() + "name: Science fiction" + System.lineSeparator());
  }

}