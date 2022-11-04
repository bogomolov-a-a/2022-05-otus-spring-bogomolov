package ru.otus.group202205.homework.spring06.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Optional;
import javax.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import ru.otus.group202205.homework.spring06.dao.BookCommentRepository;
import ru.otus.group202205.homework.spring06.model.Book;
import ru.otus.group202205.homework.spring06.model.BookComment;
import ru.otus.group202205.homework.spring06.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring06.testdata.BookCommentTestDataComponent;
import ru.otus.group202205.homework.spring06.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring06.testdata.GenreTestDataComponent;

@DataJpaTest
@Import({BookCommentRepositoryJpa.class, BookCommentTestDataComponent.class, BookTestDataComponent.class, AuthorTestDataComponent.class,
    GenreTestDataComponent.class})
class BookCommentRepositoryJpaTest {

  private static final Long INSERTED_BOOK_COMMENT_ID_VALUE = 4L;
  private static final Long EXISTING_BOOK_COMMENT_ID_VALUE = 1L;
  private static final String RUSSIAN_COMMENT_TEXT = "Классная книга!";
  @Autowired
  private BookCommentRepository bookCommentRepository;
  @Autowired
  private TestEntityManager testEntityManager;
  @Autowired
  private BookCommentTestDataComponent bookCommentTestDataComponent;
  @Autowired
  private BookTestDataComponent bookTestDataComponent;

  //region create
  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeInsertNewBookComment() {
    assertThat(testEntityManager.find(BookComment.class,
        INSERTED_BOOK_COMMENT_ID_VALUE)).isNull();
    BookComment exceptedBookComment = bookCommentTestDataComponent.getAliceMielophonJokeCommentForGirlFromTheEarthBook();
    Book exceptedBook = exceptedBookComment.getBook();
    assertThat(exceptedBook).isNotNull();
    assertThat(exceptedBook.getId()).isNotNull();
    exceptedBookComment = bookCommentRepository.saveOrUpdate(exceptedBookComment);
    assertThat(exceptedBookComment.getId())
        .isNotNull()
        .isEqualTo(INSERTED_BOOK_COMMENT_ID_VALUE);
    BookComment actualBookComment = testEntityManager.find(BookComment.class,
        INSERTED_BOOK_COMMENT_ID_VALUE);
    assertThat(actualBookComment)
        .isNotNull()
        .isEqualTo(exceptedBookComment);
    assertThat(exceptedBookComment.getBook())
        .isNotNull()
        .isEqualTo(bookTestDataComponent.getGirlFromEarthBook());
  }

  //fool guard for saveOrUpdate method
  @Test
  void shouldBeNotThrowPersistentObjectExceptionForDetachedEntity() {
    BookComment existingBookComment = testEntityManager.find(BookComment.class,
        EXISTING_BOOK_COMMENT_ID_VALUE);
    testEntityManager.detach(existingBookComment);
    assertThatCode(() -> bookCommentRepository.saveOrUpdate(existingBookComment)).isNull();
  }

  @Test
  void shouldBeThrowDataIntegrityConstraintViolationForEmptyComment() {
    BookComment expectedBookComment = bookCommentTestDataComponent.getConstraintViolationBookComment();
    assertThatCode(() -> bookCommentRepository.saveOrUpdate(expectedBookComment))
        .isNotNull()
        .isInstanceOf(PersistenceException.class)
        .hasCauseInstanceOf(ConstraintViolationException.class);
  }
  //endregion

  //region read
  @Test
  void shouldBeGetAllInsertedBookComments() {
    assertThat(bookCommentRepository.findAll())
        .isNotNull()
        .isNotEmpty()
        .isEqualTo(bookCommentTestDataComponent.getAllBookComments());
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateInsertedBookComment() {
    BookComment existingBookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    assertThat(existingBookComment).isNotNull();
    assertThat(existingBookComment.getId()).isEqualTo(EXISTING_BOOK_COMMENT_ID_VALUE);
    BookComment exceptedBookComment = testEntityManager.find(BookComment.class,
        EXISTING_BOOK_COMMENT_ID_VALUE);
    assertThat(exceptedBookComment)
        .isNotNull()
        .isEqualTo(existingBookComment);
    assertThat(exceptedBookComment.getBook())
        .isNotNull()
        .isEqualTo(bookTestDataComponent.getGirlFromEarthBook());
    BookComment exceptedUpdatedBookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    exceptedUpdatedBookComment.setText(RUSSIAN_COMMENT_TEXT);
    exceptedUpdatedBookComment = bookCommentRepository.saveOrUpdate(exceptedUpdatedBookComment);
    BookComment actualUpdatedBookComment = testEntityManager.find(BookComment.class,
        EXISTING_BOOK_COMMENT_ID_VALUE);
    assertThat(actualUpdatedBookComment)
        .isNotNull()
        .isEqualTo(exceptedUpdatedBookComment);
    assertThat(exceptedBookComment.getBook())
        .isNotNull()
        .isEqualTo(existingBookComment.getBook());
  }

  @Test
  void shouldBeUpdateBookWithOnlyIdInsertedBookComment() {
    BookComment existingBookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    assertThat(existingBookComment).isNotNull();
    assertThat(existingBookComment.getId()).isEqualTo(EXISTING_BOOK_COMMENT_ID_VALUE);
    BookComment exceptedBookComment = testEntityManager.find(BookComment.class,
        EXISTING_BOOK_COMMENT_ID_VALUE);
    assertThat(exceptedBookComment)
        .isNotNull()
        .isEqualTo(existingBookComment);
    assertThat(exceptedBookComment.getBook())
        .isNotNull()
        .isEqualTo(bookTestDataComponent.getGirlFromEarthBook());
    BookComment exceptedUpdatedBookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    Book newBook = bookTestDataComponent.getChildhoodBoyhoodYouthBook();
    Long newBookId = newBook.getId();
    assertThat(newBookId)
        .isNotNull()
        .isEqualTo(2L);
    Book newUpdatingBook = new Book();
    newUpdatingBook.setId(newBookId);
    exceptedUpdatedBookComment.setBook(newUpdatingBook);
    exceptedUpdatedBookComment = bookCommentRepository.saveOrUpdate(exceptedUpdatedBookComment);
    BookComment actualUpdatedBookComment = testEntityManager.find(BookComment.class,
        EXISTING_BOOK_COMMENT_ID_VALUE);
    assertThat(actualUpdatedBookComment)
        .isNotNull()
        .isEqualTo(exceptedUpdatedBookComment);
    assertThat(actualUpdatedBookComment.getBook())
        .isNotNull()
        .isEqualTo(newBook);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteInsertedBook() {
    BookComment existingBookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    Optional<BookComment> actualBookCommentOptional = bookCommentRepository.findById(EXISTING_BOOK_COMMENT_ID_VALUE);
    assertThat(actualBookCommentOptional)
        .isNotNull()
        .isPresent();
    BookComment actualBookComment = actualBookCommentOptional.get();
    assertThat(actualBookComment).isEqualTo(existingBookComment);
    bookCommentRepository.deleteById(EXISTING_BOOK_COMMENT_ID_VALUE);
    assertThat(bookCommentRepository.findById(EXISTING_BOOK_COMMENT_ID_VALUE))
        .isNotNull()
        .isNotPresent();
  }
  //endregion
}