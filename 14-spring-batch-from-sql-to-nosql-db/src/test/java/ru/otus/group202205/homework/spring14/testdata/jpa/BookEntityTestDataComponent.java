package ru.otus.group202205.homework.spring14.testdata.jpa;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring14.model.jpa.AuthorEntity;
import ru.otus.group202205.homework.spring14.model.jpa.BookEntity;
import ru.otus.group202205.homework.spring14.model.jpa.GenreEntity;

@Component
@RequiredArgsConstructor
public class BookEntityTestDataComponent {

  private final AuthorEntityTestDataComponent authorTestDataComponent;
  private final GenreEntityTestDataComponent genreTestDataComponent;

  public BookEntity getChildIslandBook() {
    BookEntity result = new BookEntity();
    result.setTitle("Child_Island");
    result.setIsbn("978-5-17-111905-8");
    result.setAuthor(authorTestDataComponent.getBulychevKirAuthor());
    result.setGenre(genreTestDataComponent.getScienceFictionGenre());
    return result;
  }

  public BookEntity getGirlFromEarthBook() {
    BookEntity result = new BookEntity();
    result.setId(1L);
    result.setTitle("Girl from the Earth");
    result.setIsbn("978-5-699-11438-2");
    result.setAuthor(authorTestDataComponent.getBulychevKirAuthor());
    result.setGenre(genreTestDataComponent.getScienceFictionGenre());
    return result;
  }

  public BookEntity getChildhoodBoyhoodYouthBook() {
    BookEntity result = new BookEntity();
    result.setId(2L);
    result.setTitle("Childhood. Boyhood. Youth");
    result.setIsbn("978-5-04-116640-3");
    result.setAuthor(authorTestDataComponent.getTolstoyLevNikAuthor());
    result.setGenre(genreTestDataComponent.getNovellGenre());
    return result;
  }

  public List<BookEntity> getAllExistingBooks() {
    return List.of(getGirlFromEarthBook(),
        getChildhoodBoyhoodYouthBook());
  }

  public BookEntity getBookWithoutAuthorAndGenre() {
    BookEntity result = new BookEntity();
    result.setTitle("How to transfer signals from prefrontal prefrontal cortex to computer.");
    result.setIsbn("978-3-343-34233-1");
    return result;
  }

  public BookEntity getBookWithMissingAuthorOrGenre() {
    AuthorEntity resultAuthor = new AuthorEntity();
    resultAuthor.setId(4L);
    resultAuthor.setSurname("Hououin");
    resultAuthor.setName("Kyouma");
    resultAuthor.setBirthYear(1991L);
    GenreEntity resulGenre = new GenreEntity();
    resulGenre.setId(4L);
    resulGenre.setName("Fanfiction");
    BookEntity result = new BookEntity();
    result.setAuthor(resultAuthor);
    result.setGenre(resulGenre);
    result.setTitle("How_to_transfer_signals_from_prefrontal_cortex_to_computer.");
    result.setIsbn("978-3-343-34233-1");
    return result;
  }

}
