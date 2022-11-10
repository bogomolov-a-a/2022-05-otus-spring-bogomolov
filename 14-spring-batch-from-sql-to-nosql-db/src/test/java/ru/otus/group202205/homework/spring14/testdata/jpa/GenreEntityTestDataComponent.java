package ru.otus.group202205.homework.spring14.testdata.jpa;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring14.model.jpa.GenreEntity;

@Component
public class GenreEntityTestDataComponent {

  public GenreEntity getMangaGenre() {
    GenreEntity result = new GenreEntity();
    result.setName("Manga");
    return result;
  }

  public GenreEntity getNovellGenre() {
    GenreEntity result = new GenreEntity();
    result.setId(1L);
    result.setName("Novell");
    return result;
  }

  public GenreEntity getScienceFictionGenre() {
    GenreEntity result = new GenreEntity();
    result.setId(2L);
    result.setName("Science fiction");
    return result;
  }

  public List<GenreEntity> getAllExistingGenres() {
    return List.of(getNovellGenre(),
        getScienceFictionGenre());
  }

  public GenreEntity getLiteNovellGenre() {
    GenreEntity result = new GenreEntity();
    result.setName("Lite novell");
    return result;
  }

}
