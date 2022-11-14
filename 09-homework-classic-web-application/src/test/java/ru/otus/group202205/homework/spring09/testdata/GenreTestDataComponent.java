package ru.otus.group202205.homework.spring09.testdata;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring09.dto.GenreDto;
import ru.otus.group202205.homework.spring09.model.Genre;

@Component
public class GenreTestDataComponent {

  public Genre getMangaGenre() {
    Genre result = new Genre();
    result.setName("Manga");
    return result;
  }

  public Genre getNovellGenre() {
    Genre result = new Genre();
    result.setId(1L);
    result.setName("Novell");
    return result;
  }

  public Genre getScienceFictionGenre() {
    Genre result = new Genre();
    result.setId(2L);
    result.setName("Science fiction");
    return result;
  }

  public List<Genre> getAllExistingGenres() {
    return List.of(getNovellGenre(),
        getScienceFictionGenre());
  }

  public Genre getLiteNovellGenre() {
    Genre result = new Genre();
    result.setName("Lite novell");
    return result;
  }

  public GenreDto getNovellGenreDto() {
    GenreDto result = new GenreDto();
    result.setId(1L);
    result.setName("Novell");
    return result;
  }

  public GenreDto getScienceFictionGenreDto() {
    GenreDto result = new GenreDto();
    result.setId(2L);
    result.setName("Science fiction");
    return result;
  }

}
