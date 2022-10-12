package ru.otus.group202205.homework.spring05.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.group202205.homework.spring05.model.Genre;
import ru.otus.group202205.homework.spring05.service.GenreConverter;

@SpringBootTest(classes = GenreConverterImpl.class)
class GenreConverterImplTest {

  @Autowired
  private GenreConverter genreConverter;

  @Test
  void shouldBeConvertGenreWithFullInfoToString() {
    Genre genre = new Genre();
    genre.setName("Manga");
    genre.setId(3L);
    String actualGenreOutput = genreConverter.convertGenre(genre);
    assertThat(actualGenreOutput)
        .isNotNull()
        .isEqualTo("Genre id: 3" + System.lineSeparator() + "name: Manga" + System.lineSeparator());
  }

  @Test
  void shouldBeConvertGenresWithFullInfoToString() {
    Genre firstGenre = new Genre();
    firstGenre.setName("Manga");
    firstGenre.setId(3L);
    Genre secondGenre = new Genre();
    secondGenre.setName("Lite novell");
    secondGenre.setId(4L);
    String actualGenreOutput = genreConverter.convertGenres(List.of(firstGenre,
        secondGenre));
    assertThat(actualGenreOutput)
        .isNotNull()
        .isEqualTo(
            "Genre list" + System.lineSeparator()
                + "Genre id: 3" + System.lineSeparator() + "name: Manga" + System.lineSeparator()
                + "Genre id: 4" + System.lineSeparator() + "name: Lite novell" + System.lineSeparator());
  }

}