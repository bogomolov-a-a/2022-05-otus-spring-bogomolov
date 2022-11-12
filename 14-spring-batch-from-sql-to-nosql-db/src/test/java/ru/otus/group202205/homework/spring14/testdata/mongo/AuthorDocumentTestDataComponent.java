package ru.otus.group202205.homework.spring14.testdata.mongo;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring14.model.mongo.AuthorDocument;

@Component
public class AuthorDocumentTestDataComponent {

  public AuthorDocument getBulychevKirAuthor() {
    AuthorDocument result = new AuthorDocument();
    result.setId("1");
    result.setSurname("Bulychev");
    result.setName("Kir");
    result.setBirthYear(1934L);
    result.setDeathYear(2003L);
    return result;
  }

  public AuthorDocument getTolstoyLevNikAuthor() {
    AuthorDocument result = new AuthorDocument();
    result.setId("2");
    result.setSurname("Tolstoy");
    result.setName("Lev");
    result.setPatronymic("Nikolayevich");
    result.setBirthYear(1828L);
    result.setDeathYear(1910L);
    return result;
  }

  public List<AuthorDocument> getAllExistingAuthors() {
    return List.of(getBulychevKirAuthor(),
        getTolstoyLevNikAuthor());
  }

}
