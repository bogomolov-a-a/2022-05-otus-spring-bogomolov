package ru.otus.group202205.homework.spring13.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring13.dao.BookRepository;
import ru.otus.group202205.homework.spring13.dao.DaoConfig;
import ru.otus.group202205.homework.spring13.model.Author;
import ru.otus.group202205.homework.spring13.model.Book;
import ru.otus.group202205.homework.spring13.model.Genre;
import ru.otus.group202205.homework.spring13.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring13.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring13.testdata.GenreTestDataComponent;

@DataJpaTest
@Import({JpaRepositoriesAutoConfiguration.class, BookTestDataComponent.class, AuthorTestDataComponent.class,
    GenreTestDataComponent.class, DaoConfig.class})
class BookRepositoryJpaTest {

  private static final Long INSERTED_BOOK_ID_VALUE = 3L;
  private static final Long MISSING_AUTHOR_ID_VALUE = 4L;
  private static final Long MISSING_GENRE_ID_VALUE = 4L;
  private static final Long EXISTING_BOOK_ID_VALUE = 1L;
  private static final String GIRL_FROM_EARTH_RUSSIAN_NAME = "Девочка с Земли";
  @Autowired
  private BookRepository bookRepository;
  @Autowired
  private TestEntityManager testEntityManager;
  @Autowired
  private BookTestDataComponent bookTestDataComponent;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;

  //region create
  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeInsertNewBook() {
    Assertions
        .assertThat(testEntityManager.find(Book.class,
            INSERTED_BOOK_ID_VALUE))
        .isNull();
    Book exceptedBook = bookTestDataComponent.getChildIslandBook();
    Genre exceptedGenre = exceptedBook.getGenre();
    assertThat(exceptedGenre).isNotNull();
    assertThat(exceptedGenre.getId()).isNotNull();
    assertThat(exceptedGenre.getName())
        .isNotNull()
        .isEqualTo("Science fiction");
    Author exceptedAuthor = exceptedBook.getAuthor();
    assertThat(exceptedAuthor).isNotNull();
    assertThat(exceptedAuthor.getId()).isNotNull();
    assertThat(exceptedAuthor.getSurname())
        .isNotNull()
        .isEqualTo("Bulychev");
    assertThat(exceptedAuthor.getName())
        .isNotNull()
        .isEqualTo("Kir");
    Assertions
        .assertThat(exceptedBook.getComments())
        .isNotNull()
        .isEmpty();
    assertThat(exceptedBook.getId()).isNull();
    exceptedBook = bookRepository.save(exceptedBook);
    assertThat(exceptedBook.getId())
        .isNotNull()
        .isEqualTo(INSERTED_BOOK_ID_VALUE);
    Book actualBook = testEntityManager.find(Book.class,
        INSERTED_BOOK_ID_VALUE);
    assertThat(actualBook)
        .isNotNull()
        .isEqualTo(exceptedBook);
    assertThat(actualBook.getAuthor())
        .isNotNull()
        .isEqualTo(exceptedAuthor);
    assertThat(actualBook.getGenre())
        .isNotNull()
        .isEqualTo(exceptedGenre);
  }

  //fool guard for save method
  @Test
  void shouldBeNotThrowPersistentObjectExceptionForDetachedEntity() {
    Book existingBook = testEntityManager.find(Book.class,
        EXISTING_BOOK_ID_VALUE);
    testEntityManager.detach(existingBook);
    assertThatCode(() -> bookRepository.save(existingBook)).isNull();
  }

  @Test
  void shouldBeThrowDataIntegrityConstraintViolationForNotExistsAuthorAndGenre() {
    Book exceptedBook = bookTestDataComponent.getBookWithMissingAuthorOrGenre();
    assertThat(exceptedBook
        .getAuthor()
        .getId())
        .isNotNull()
        .isEqualTo(MISSING_AUTHOR_ID_VALUE);
    assertThat(exceptedBook
        .getGenre()
        .getId())
        .isNotNull()
        .isEqualTo(MISSING_GENRE_ID_VALUE);
    assertThatCode(() -> bookRepository.save(exceptedBook))
        .isNotNull()
        .isInstanceOf(DataIntegrityViolationException.class)
        .hasCauseInstanceOf(ConstraintViolationException.class);
  }

  @Test
  void shouldBeThrowDataIntegrityConstraintViolationForWithoutAuthorAndGenre() {
    Book expectedBook = bookTestDataComponent.getBookWithoutAuthorAndGenre();
    assertThat(expectedBook.getAuthor()).isNull();
    assertThat(expectedBook.getGenre()).isNull();
    assertThatCode(() -> bookRepository.save(expectedBook))
        .isNotNull()
        .isInstanceOf(DataIntegrityViolationException.class)
        .hasCauseInstanceOf(ConstraintViolationException.class);
  }
  //endregion

  //region read
  @Transactional(readOnly = true)
  @Test
  void shouldBeGetAllInsertedBook() {
    Assertions
        .assertThat(bookRepository.findAll())
        .isNotNull()
        .isNotEmpty()
        .isEqualTo(bookTestDataComponent.getAllExistingBooks());
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateInsertedBook() {
    Book existingBook = bookTestDataComponent.getGirlFromEarthBook();
    assertThat(existingBook.getId()).isEqualTo(EXISTING_BOOK_ID_VALUE);
    Book exceptedBook = testEntityManager.find(Book.class,
        EXISTING_BOOK_ID_VALUE);
    assertThat(exceptedBook)
        .isNotNull()
        .isEqualTo(existingBook);
    assertThat(exceptedBook.getAuthor())
        .isNotNull()
        .isEqualTo(existingBook.getAuthor());
    assertThat(exceptedBook.getGenre())
        .isNotNull()
        .isEqualTo(existingBook.getGenre());
    Book exceptedUpdatedBook = bookTestDataComponent.getGirlFromEarthBook();
    exceptedUpdatedBook.setTitle(GIRL_FROM_EARTH_RUSSIAN_NAME);
    exceptedUpdatedBook = bookRepository.save(exceptedUpdatedBook);
    Book actualUpdatedBook = testEntityManager.find(Book.class,
        EXISTING_BOOK_ID_VALUE);
    assertThat(actualUpdatedBook)
        .isNotNull()
        .isEqualTo(exceptedUpdatedBook);
    assertThat(actualUpdatedBook.getAuthor())
        .isNotNull()
        .isEqualTo(exceptedBook.getAuthor());
    assertThat(actualUpdatedBook.getGenre())
        .isNotNull()
        .isEqualTo(exceptedBook.getGenre());
  }

  @Test
  void shouldBeUpdateAuthorWithOnlyIdInsertedBook() {
    Book existingBook = bookTestDataComponent.getGirlFromEarthBook();
    assertThat(existingBook.getId()).isEqualTo(EXISTING_BOOK_ID_VALUE);
    Book exceptedBook = testEntityManager.find(Book.class,
        EXISTING_BOOK_ID_VALUE);
    assertThat(exceptedBook)
        .isNotNull()
        .isEqualTo(existingBook);
    assertThat(exceptedBook.getAuthor())
        .isNotNull()
        .isEqualTo(existingBook.getAuthor());
    assertThat(exceptedBook.getGenre())
        .isNotNull()
        .isEqualTo(existingBook.getGenre());
    Book exceptedUpdatedBook = bookTestDataComponent.getGirlFromEarthBook();
    Author newBookAuthor = authorTestDataComponent.getTolstoyLevNikAuthor();
    Long newBookAuthorId = newBookAuthor.getId();
    assertThat(newBookAuthorId).isNotNull();
    Author newBookUpdatingAuthor = new Author();
    newBookUpdatingAuthor.setId(newBookAuthorId);
    newBookUpdatingAuthor.setPatronymic(null);
    exceptedUpdatedBook.setAuthor(newBookUpdatingAuthor);
    exceptedUpdatedBook = bookRepository.save(exceptedUpdatedBook);
    Book actualUpdatedBook = testEntityManager.find(Book.class,
        EXISTING_BOOK_ID_VALUE);
    assertThat(actualUpdatedBook)
        .isNotNull()
        .isEqualTo(exceptedUpdatedBook);
    assertThat(actualUpdatedBook.getAuthor())
        .isNotNull()
        .isEqualTo(newBookAuthor);
    assertThat(actualUpdatedBook.getGenre())
        .isNotNull()
        .isEqualTo(exceptedBook.getGenre());
  }

  @Test
  void shouldBeUpdateGenreWithOnlyIdInsertedBook() {
    Book existingBook = bookTestDataComponent.getGirlFromEarthBook();
    assertThat(existingBook.getId()).isEqualTo(EXISTING_BOOK_ID_VALUE);
    Book exceptedBook = testEntityManager.find(Book.class,
        EXISTING_BOOK_ID_VALUE);
    assertThat(exceptedBook)
        .isNotNull()
        .isEqualTo(existingBook);
    assertThat(exceptedBook.getAuthor())
        .isNotNull()
        .isEqualTo(existingBook.getAuthor());
    assertThat(exceptedBook.getGenre())
        .isNotNull()
        .isEqualTo(existingBook.getGenre());
    Book exceptedUpdatedBook = bookTestDataComponent.getGirlFromEarthBook();
    Genre newBookGenre = genreTestDataComponent.getNovellGenre();
    Long newBookGenreId = newBookGenre.getId();
    assertThat(newBookGenreId).isNotNull();
    Genre newBookUpdatingGenre = new Genre();
    newBookUpdatingGenre.setId(newBookGenreId);
    exceptedUpdatedBook.setGenre(newBookUpdatingGenre);
    exceptedUpdatedBook = bookRepository.save(exceptedUpdatedBook);
    Book actualUpdatedBook = testEntityManager.find(Book.class,
        EXISTING_BOOK_ID_VALUE);
    assertThat(actualUpdatedBook)
        .isNotNull()
        .isEqualTo(exceptedUpdatedBook);
    assertThat(actualUpdatedBook.getAuthor())
        .isNotNull()
        .isEqualTo(exceptedBook.getAuthor());
    assertThat(actualUpdatedBook.getGenre())
        .isNotNull()
        .isEqualTo(newBookGenre);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteInsertedBook() {
    Book existingBook = bookTestDataComponent.getGirlFromEarthBook();
    Optional<Book> actualBookOptional = bookRepository.findById(EXISTING_BOOK_ID_VALUE);
    assertThat(actualBookOptional)
        .isNotNull()
        .isPresent();
    Book actualBook = actualBookOptional.get();
    assertThat(actualBook).isEqualTo(existingBook);
    bookRepository.deleteById(EXISTING_BOOK_ID_VALUE);
    Assertions
        .assertThat(bookRepository.findById(EXISTING_BOOK_ID_VALUE))
        .isNotNull()
        .isNotPresent();
  }
  //endregion

}