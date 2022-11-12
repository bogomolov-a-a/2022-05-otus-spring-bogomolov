package ru.otus.group202205.homework.spring14.testdata.mongo;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring14.model.mongo.BookDocument;

@Component
@RequiredArgsConstructor
public class BookDocumentTestDataComponent {

  private final AuthorDocumentTestDataComponent authorTestDataComponent;
  private final GenreDocumentTestDataComponent genreTestDataComponent;

  public BookDocument getChildIslandBook() {
    BookDocument result = new BookDocument();
    result.setTitle("Child_Island");
    result.setIsbn("978-5-17-111905-8");
    result.setAuthor(authorTestDataComponent.getBulychevKirAuthor());
    result.setGenre(genreTestDataComponent.getScienceFictionGenre());
    return result;
  }

  public BookDocument getGirlFromEarthBook() {
    BookDocument result = new BookDocument();
    result.setId("1");
    result.setTitle("Girl from the Earth");
    result.setIsbn("978-5-699-11438-2");
    result.setAuthor(authorTestDataComponent.getBulychevKirAuthor());
    result.setGenre(genreTestDataComponent.getScienceFictionGenre());
    return result;
  }

  public BookDocument getChildhoodBoyhoodYouthBook() {
    BookDocument result = new BookDocument();
    result.setId("2");
    result.setTitle("Childhood. Boyhood. Youth");
    result.setIsbn("978-5-04-116640-3");
    result.setAuthor(authorTestDataComponent.getTolstoyLevNikAuthor());
    result.setGenre(genreTestDataComponent.getNovellGenre());
    return result;
  }

  public List<BookDocument> getAllExistingBooks() {
    return List.of(getGirlFromEarthBook(),
        getChildhoodBoyhoodYouthBook());
  }

}
