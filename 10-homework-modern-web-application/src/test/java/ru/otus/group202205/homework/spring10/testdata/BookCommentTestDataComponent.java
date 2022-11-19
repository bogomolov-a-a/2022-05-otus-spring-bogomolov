package ru.otus.group202205.homework.spring10.testdata;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring10.dto.BookCommentDto;
import ru.otus.group202205.homework.spring10.model.Book;
import ru.otus.group202205.homework.spring10.model.BookComment;

@Component
@RequiredArgsConstructor
public class BookCommentTestDataComponent {

  private final BookTestDataComponent bookTestDataComponent;

  public BookComment getNiceBookCommentForGirlFromTheEarthBook() {
    BookComment result = new BookComment();
    result.setId(1L);
    result.setText("Nice book!");
    result.setCreated(LocalDateTime.parse("2022-10-26T22:35:31"));
    result.setBook(bookTestDataComponent.getGirlFromEarthBook());
    return result;
  }

  public BookComment getMeLikeThisAuthorCommentForGirlFromTheEarthBook() {
    BookComment result = new BookComment();
    result.setId(2L);
    result.setText("I like this author!");
    result.setCreated(LocalDateTime.parse("2022-10-26T22:35:32"));
    result.setBook(bookTestDataComponent.getGirlFromEarthBook());
    return result;
  }

  public BookComment getComplicatedBookCommentForgetChildhoodBoyhoodYouthBook() {
    BookComment result = new BookComment();
    result.setId(3L);
    result.setText("Complicated book!");
    result.setCreated(LocalDateTime.parse("2022-10-26T22:35:32"));
    result.setBook(bookTestDataComponent.getChildhoodBoyhoodYouthBook());
    return result;
  }

  public List<BookComment> getAllBookComments() {
    return List.of(getComplicatedBookCommentForgetChildhoodBoyhoodYouthBook(),
        getMeLikeThisAuthorCommentForGirlFromTheEarthBook(),
        getNiceBookCommentForGirlFromTheEarthBook());
  }

  public BookComment getAliceMielophonJokeCommentForGirlFromTheEarthBook() {
    BookComment result = new BookComment();
    result.setText("Alice! I have a mielophone!");
    result.setCreated(LocalDateTime.parse("2022-10-27T21:10:00"));
    result.setBook(bookTestDataComponent.getGirlFromEarthBook());
    return result;
  }

  public BookComment getConstraintViolationBookComment() {
    return new BookComment();
  }

  public BookComment getNiceBookCommentForGirlFromTheEarthBookSimple() {
    BookComment result = new BookComment();
    result.setId(1L);
    result.setText("Nice book!");
    result.setCreated(LocalDateTime.parse("2022-10-26T22:35:31"));
    result.setBook(bookTestDataComponent.getGirlFromEarthBookSimple());
    return result;
  }

  public BookCommentDto getNiceBookCommentForGirlFromTheEarthBookSimpleDto() {
    BookCommentDto result = new BookCommentDto();
    result.setId(1L);
    result.setText("Nice book!");
    result.setCreated(LocalDateTime.parse("2022-10-26T22:35:31"));
    result.setBook(bookTestDataComponent.getGirlFromEarthBookSimpleDto());
    return result;
  }

  public List<BookComment> getAllBookCommentsByExistingBookId() {
    return List.of(getMeLikeThisAuthorCommentForGirlFromTheEarthBook(),
        getNiceBookCommentForGirlFromTheEarthBook());
  }

  public Book getExistingBookWithComments() {
    Book result = bookTestDataComponent.getGirlFromEarthBook();
    result
        .setComments(getAllBookCommentsByExistingBookId());
    return result;
  }

}
