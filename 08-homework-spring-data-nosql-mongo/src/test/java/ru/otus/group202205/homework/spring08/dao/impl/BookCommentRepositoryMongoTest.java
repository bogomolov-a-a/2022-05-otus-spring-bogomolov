package ru.otus.group202205.homework.spring08.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.mongodb.MongoWriteException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import ru.otus.group202205.homework.spring08.dao.AuthorRepository;
import ru.otus.group202205.homework.spring08.dao.BookCommentRepository;
import ru.otus.group202205.homework.spring08.dao.BookRepository;
import ru.otus.group202205.homework.spring08.dao.DaoConfig;
import ru.otus.group202205.homework.spring08.dao.GenreRepository;
import ru.otus.group202205.homework.spring08.model.Book;
import ru.otus.group202205.homework.spring08.model.BookComment;
import ru.otus.group202205.homework.spring08.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring08.testdata.BookCommentTestDataComponent;
import ru.otus.group202205.homework.spring08.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring08.testdata.GenreTestDataComponent;

@DataMongoTest
@Import({TransactionAutoConfiguration.class, DaoConfig.class, MongoDbRepositoryUnitTestConfig.class, BookCommentTestDataComponent.class,
    BookTestDataComponent.class, AuthorTestDataComponent.class, GenreTestDataComponent.class})
@DirtiesContext
class BookCommentRepositoryMongoTest {

  private static final String EXISTING_BOOK_COMMENT_ID_VALUE = "1";
  private static final String RUSSIAN_COMMENT_TEXT = "Классная книга!";
  @Autowired
  private BookCommentRepository bookCommentRepository;
  @Autowired
  private BookRepository bookRepository;
  @Autowired
  private GenreRepository genreRepository;
  @Autowired
  private AuthorRepository authorRepository;
  @Autowired
  private BookCommentTestDataComponent bookCommentTestDataComponent;
  @Autowired
  private BookTestDataComponent bookTestDataComponent;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;

  /*Mongock can't work(correctly) with @Transactional and mongo replicas. TransactionManager only work with replicas. */
  @BeforeEach
  void beforeEach() {
    bookCommentRepository.deleteAll();
    bookRepository.deleteAll();
    authorRepository.deleteAll();
    genreRepository.deleteAll();
    genreRepository.saveAll(genreTestDataComponent.getAllExistingGenres());
    authorRepository.saveAll(authorTestDataComponent.getAllExistingAuthors());
    bookRepository.saveAll(bookTestDataComponent.getAllExistingBooks());
    bookCommentRepository.saveAll(bookCommentTestDataComponent.getAllBookComments());
  }

  //region create
  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeInsertNewBookComment() {
    BookComment exceptedBookComment = bookCommentTestDataComponent.getAliceMielophonJokeCommentForGirlFromTheEarthBook();
    Book exceptedBook = exceptedBookComment.getBook();
    assertThat(exceptedBook).isNotNull();
    assertThat(exceptedBook.getId()).isNotNull();
    exceptedBookComment = bookCommentRepository.save(exceptedBookComment);
    String savedBookCommentId = exceptedBookComment.getId();
    assertThat(exceptedBookComment.getId()).isNotNull();
    BookComment actualBookComment = bookCommentRepository
        .findById(savedBookCommentId)
        .orElseThrow();
    assertThat(actualBookComment)
        .isNotNull()
        .isEqualTo(exceptedBookComment);
    assertThat(exceptedBookComment.getBook())
        .isNotNull()
        .isEqualTo(bookTestDataComponent.getGirlFromEarthBook());
  }
  //endregion

  //region read
  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeGetAllInsertedBookComments() {
    assertThat(bookCommentRepository.findAll())
        .isNotNull()
        .isNotEmpty()
        .isEqualTo(bookCommentTestDataComponent.getAllBookComments());
  }

  @Test
  void shouldBeThrowDuplicateKeyExceptionByNaturalKey() {
    BookComment expectedBookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    assertThat(expectedBookComment.getBook()).isNotNull();
    assertThat(expectedBookComment
        .getBook()
        .getId()).isNotNull();
    expectedBookComment.setId(null);
    assertThatCode(() -> bookCommentRepository.save(expectedBookComment))
        .isNotNull()
        .isInstanceOf(DuplicateKeyException.class)
        .hasCauseInstanceOf(MongoWriteException.class);
  }
  //endregion

  //region update
  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeUpdateInsertedBookComment() {
    BookComment existingBookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    assertThat(existingBookComment).isNotNull();
    assertThat(existingBookComment.getId()).isEqualTo(EXISTING_BOOK_COMMENT_ID_VALUE);
    BookComment exceptedBookComment = bookCommentRepository
        .findById(EXISTING_BOOK_COMMENT_ID_VALUE)
        .orElseThrow();
    assertThat(exceptedBookComment)
        .isNotNull()
        .isEqualTo(existingBookComment);
    assertThat(exceptedBookComment.getBook())
        .isNotNull()
        .isEqualTo(bookTestDataComponent.getGirlFromEarthBook());
    BookComment exceptedUpdatedBookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    exceptedUpdatedBookComment.setText(RUSSIAN_COMMENT_TEXT);
    exceptedUpdatedBookComment = bookCommentRepository.save(exceptedUpdatedBookComment);
    String savedBookCommentId = exceptedUpdatedBookComment.getId();
    BookComment actualUpdatedBookComment = bookCommentRepository
        .findById(savedBookCommentId)
        .orElseThrow();
    assertThat(actualUpdatedBookComment)
        .isNotNull()
        .isEqualTo(exceptedUpdatedBookComment);
    assertThat(exceptedBookComment.getBook())
        .isNotNull()
        .isEqualTo(existingBookComment.getBook());
  }

  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeUpdateBookWithOnlyIdInsertedBookComment() {
    BookComment existingBookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    assertThat(existingBookComment).isNotNull();
    assertThat(existingBookComment.getId()).isEqualTo(EXISTING_BOOK_COMMENT_ID_VALUE);
    BookComment exceptedBookComment = bookCommentRepository
        .findById(EXISTING_BOOK_COMMENT_ID_VALUE)
        .orElseThrow();
    assertThat(exceptedBookComment)
        .isNotNull()
        .isEqualTo(existingBookComment);
    assertThat(exceptedBookComment.getBook())
        .isNotNull()
        .isEqualTo(bookTestDataComponent.getGirlFromEarthBook());
    BookComment exceptedUpdatedBookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    Book newBook = bookTestDataComponent.getChildhoodBoyhoodYouthBook();
    String newBookId = newBook.getId();
    assertThat(newBookId)
        .isNotNull()
        .isEqualTo("2");
    Book newUpdatingBook = new Book();
    newUpdatingBook.setId(newBookId);
    exceptedUpdatedBookComment.setBook(newUpdatingBook);
    exceptedUpdatedBookComment = bookCommentRepository.save(exceptedUpdatedBookComment);
    String updateBookCommentId = exceptedUpdatedBookComment.getId();
    BookComment actualUpdatedBookComment = bookCommentRepository
        .findById(updateBookCommentId)
        .orElseThrow();
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
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
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