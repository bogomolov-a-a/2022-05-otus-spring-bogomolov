package ru.otus.group202205.homework.spring06.service.converter.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.group202205.homework.spring06.dto.GenreDto;
import ru.otus.group202205.homework.spring06.service.converter.GenreConverter;
import ru.otus.group202205.homework.spring06.service.mapper.GenreMapper;
import ru.otus.group202205.homework.spring06.service.mapper.impl.GenreMapperImpl;
import ru.otus.group202205.homework.spring06.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {GenreConverterImpl.class, GenreTestDataComponent.class, GenreMapperImpl.class})
class GenreConverterImplTest {

  @Autowired
  private GenreConverter genreConverter;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;
  @Autowired
  private GenreMapper genreMapper;

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