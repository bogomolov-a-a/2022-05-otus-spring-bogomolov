package ru.otus.group202205.homework.spring14.testdata.mongo;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring14.model.mongo.BookCommentDocument;

@Component
@RequiredArgsConstructor
public class BookCommentDocumentTestDataComponent {

  private final BookDocumentTestDataComponent bookTestDataComponent;

  public BookCommentDocument getNiceBookCommentForGirlFromTheEarthBook() {
    BookCommentDocument result = new BookCommentDocument();
    result.setId("1");
    result.setText("Nice book!");
    result.setCreated(LocalDateTime.parse("2022-10-26T22:35:31"));
    result.setBook(bookTestDataComponent.getGirlFromEarthBook());
    return result;
  }

  public BookCommentDocument getMeLikeThisAuthorCommentForGirlFromTheEarthBook() {
    BookCommentDocument result = new BookCommentDocument();
    result.setId("2");
    result.setText("I like this author!");
    result.setCreated(LocalDateTime.parse("2022-10-26T22:35:32"));
    result.setBook(bookTestDataComponent.getGirlFromEarthBook());
    return result;
  }

  public BookCommentDocument getComplicatedBookCommentForgetChildhoodBoyhoodYouthBook() {
    BookCommentDocument result = new BookCommentDocument();
    result.setId("3");
    result.setText("Complicated book!");
    result.setCreated(LocalDateTime.parse("2022-10-26T22:35:32"));
    result.setBook(bookTestDataComponent.getChildhoodBoyhoodYouthBook());
    return result;
  }

  public List<BookCommentDocument> getAllBookComments() {
    return List.of(getNiceBookCommentForGirlFromTheEarthBook(),
        getMeLikeThisAuthorCommentForGirlFromTheEarthBook(),
        getComplicatedBookCommentForgetChildhoodBoyhoodYouthBook());
  }

}
