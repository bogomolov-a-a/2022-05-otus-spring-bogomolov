package ru.otus.group202205.homework.spring14.testdata.jpa;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring14.model.jpa.AuthorEntity;

@Component
public class AuthorEntityTestDataComponent {

  public AuthorEntity getBulychevKirAuthor() {
    AuthorEntity result = new AuthorEntity();
    result.setId(1L);
    result.setSurname("Bulychev");
    result.setName("Kir");
    result.setBirthYear(1934L);
    result.setDeathYear(2003L);
    return result;
  }

  public AuthorEntity getTolstoyLevNikAuthor() {
    AuthorEntity result = new AuthorEntity();
    result.setId(2L);
    result.setSurname("Tolstoy");
    result.setName("Lev");
    result.setPatronymic("Nikolayevich");
    result.setBirthYear(1828L);
    result.setDeathYear(1910L);
    return result;
  }

  public List<AuthorEntity> getAllExistingAuthors() {
    return List.of(getBulychevKirAuthor(),
        getTolstoyLevNikAuthor());
  }

}
