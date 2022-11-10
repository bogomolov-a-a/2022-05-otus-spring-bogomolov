package ru.otus.group202205.homework.spring14.testdata.mongo;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring14.model.mongo.GenreDocument;

@Component
public class GenreDocumentTestDataComponent {

  public GenreDocument getNovellGenre() {
    GenreDocument result = new GenreDocument();
    result.setId("1");
    result.setName("Novell");
    return result;
  }

  public GenreDocument getScienceFictionGenre() {
    GenreDocument result = new GenreDocument();
    result.setId("2");
    result.setName("Science fiction");
    return result;
  }

  public List<GenreDocument> getAllExistingGenres() {
    return List.of(getNovellGenre(),
        getScienceFictionGenre());
  }

}
