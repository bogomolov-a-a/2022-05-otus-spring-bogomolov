package ru.otus.group202205.homework.spring07.testdata;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring07.dto.AuthorDto;
import ru.otus.group202205.homework.spring07.model.Author;

@Component
public class AuthorTestDataComponent {

  public Author getMakiseKurisuAuthor() {
    Author result = new Author();
    result.setSurname("Makise");
    result.setName("Kurisu");
    result.setBirthYear(1992L);
    return result;
  }

  public Author getBulychevKirAuthor() {
    Author result = new Author();
    result.setId(1L);
    result.setSurname("Bulychev");
    result.setName("Kir");
    result.setBirthYear(1934L);
    result.setDeathYear(2003L);
    return result;
  }

  public Author getPushkinAlexSergAuthor() {
    Author result = new Author();
    result.setSurname("Pushkin");
    result.setName("Alexandr");
    result.setPatronymic("Sergeevich");
    result.setBirthYear(1799L);
    result.setDeathYear(1837L);
    return result;
  }

  public Author getTolstoyLevNikAuthor() {
    Author result = new Author();
    result.setId(2L);
    result.setSurname("Tolstoy");
    result.setName("Lev");
    result.setPatronymic("Nikolayevich");
    result.setBirthYear(1828L);
    result.setDeathYear(1910L);
    return result;
  }

  public List<Author> getAllExistingAuthors() {
    return List.of(getBulychevKirAuthor(),
        getTolstoyLevNikAuthor());
  }

  public Author getUncompletedAuthorForInsertOperation() {
    Author result = new Author();
    result.setSurname("Hououin");
    result.setName("Kyouma");
    return result;
  }

  public AuthorDto getBulychevKirAuthorDto() {
    AuthorDto result = new AuthorDto();
    result.setId(1L);
    result.setSurname("Bulychev");
    result.setName("Kir");
    result.setBirthYear(1934L);
    result.setDeathYear(2003L);
    return result;
  }

}
