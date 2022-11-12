package ru.otus.group202205.homework.spring14.testdata.jpa;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring14.model.jpa.BookCommentEntity;

@Component
@RequiredArgsConstructor
public class BookCommentEntityTestDataComponent {

  private final BookEntityTestDataComponent bookTestDataComponent;

  public BookCommentEntity getNiceBookCommentForGirlFromTheEarthBook() {
    BookCommentEntity result = new BookCommentEntity();
    result.setId(1L);
    result.setText("Nice book!");
    result.setCreated(LocalDateTime.parse("2022-10-26T22:35:31"));
    result.setBook(bookTestDataComponent.getGirlFromEarthBook());
    return result;
  }

  public BookCommentEntity getMeLikeThisAuthorCommentForGirlFromTheEarthBook() {
    BookCommentEntity result = new BookCommentEntity();
    result.setId(2L);
    result.setText("I like this author!");
    result.setCreated(LocalDateTime.parse("2022-10-26T22:35:32"));
    result.setBook(bookTestDataComponent.getGirlFromEarthBook());
    return result;
  }

  public BookCommentEntity getComplicatedBookCommentForgetChildhoodBoyhoodYouthBook() {
    BookCommentEntity result = new BookCommentEntity();
    result.setId(3L);
    result.setText("Complicated book!");
    result.setCreated(LocalDateTime.parse("2022-10-26T22:35:32"));
    result.setBook(bookTestDataComponent.getChildhoodBoyhoodYouthBook());
    return result;
  }

  public List<BookCommentEntity> getAllBookComments() {
    return List.of(getNiceBookCommentForGirlFromTheEarthBook(),
        getMeLikeThisAuthorCommentForGirlFromTheEarthBook(),
        getComplicatedBookCommentForgetChildhoodBoyhoodYouthBook());
  }

}
