package ru.otus.group202205.homework.spring07.testdata;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring07.dto.BookFullDto;
import ru.otus.group202205.homework.spring07.dto.BookSimpleDto;
import ru.otus.group202205.homework.spring07.model.Author;
import ru.otus.group202205.homework.spring07.model.Book;
import ru.otus.group202205.homework.spring07.model.Genre;

@Component
@RequiredArgsConstructor
public class BookTestDataComponent {

  private final AuthorTestDataComponent authorTestDataComponent;
  private final GenreTestDataComponent genreTestDataComponent;

  public Book getChildIslandBook() {
    Book result = new Book();
    result.setTitle("Child_Island");
    result.setIsbn("978-5-17-111905-8");
    result.setAuthor(authorTestDataComponent.getBulychevKirAuthor());
    result.setGenre(genreTestDataComponent.getScienceFictionGenre());
    return result;
  }

  public Book getGirlFromEarthBook() {
    Book result = new Book();
    result.setId(1L);
    result.setTitle("Girl from the Earth");
    result.setIsbn("978-5-699-11438-2");
    result.setAuthor(authorTestDataComponent.getBulychevKirAuthor());
    result.setGenre(genreTestDataComponent.getScienceFictionGenre());
    return result;
  }

  public Book getChildhoodBoyhoodYouthBook() {
    Book result = new Book();
    result.setId(2L);
    result.setTitle("Childhood. Boyhood. Youth");
    result.setIsbn("978-5-04-116640-3");
    result.setAuthor(authorTestDataComponent.getTolstoyLevNikAuthor());
    result.setGenre(genreTestDataComponent.getNovellGenre());
    return result;
  }

  public List<Book> getAllExistingBooks() {
    return List.of(getGirlFromEarthBook(),
        getChildhoodBoyhoodYouthBook());
  }

  public Book getBookWithMissingAuthorOrGenre() {
    Author resultAuthor = new Author();
    resultAuthor.setId(4L);
    resultAuthor.setSurname("Hououin");
    resultAuthor.setName("Kyouma");
    resultAuthor.setBirthYear(1991L);
    Genre resulGenre = new Genre();
    resulGenre.setId(4L);
    resulGenre.setName("Fanfiction");
    Book result = new Book();
    result.setAuthor(resultAuthor);
    result.setGenre(resulGenre);
    result.setTitle("How_to_transfer_signals_from_prefrontal_cortex_to_computer.");
    result.setIsbn("978-3-343-34233-1");
    return result;
  }

  public Book getBookWithoutAuthorAndGenre() {
    Book result = new Book();
    result.setTitle("How to transfer signals from prefrontal prefrontal cortex to computer.");
    result.setIsbn("978-3-343-34233-1");
    return result;
  }

  public BookFullDto getGirlFromEarthBookDto() {
    BookFullDto result = new BookFullDto();
    result.setId(1L);
    result.setTitle("Girl from the Earth");
    result.setIsbn("978-5-699-11438-2");
    result.setAuthor(authorTestDataComponent.getBulychevKirAuthorDto());
    result.setGenre(genreTestDataComponent.getScienceFictionGenreDto());
    return result;
  }

  public BookFullDto getBookWithoutAuthorAndGenreDto() {
    BookFullDto result = new BookFullDto();
    result.setTitle("How to transfer signals from prefrontal prefrontal cortex to computer.");
    result.setIsbn("978-3-343-34233-1");
    return result;
  }

  public BookSimpleDto getGirlFromEarthBookSimpleDto() {
    BookSimpleDto result = new BookSimpleDto();
    result.setId(1L);
    return result;
  }

  public Book getGirlFromEarthBookSimple() {
    Book result = new Book();
    result.setId(1L);
    return result;
  }

}
