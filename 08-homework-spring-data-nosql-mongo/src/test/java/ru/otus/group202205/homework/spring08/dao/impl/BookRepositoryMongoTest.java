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
import ru.otus.group202205.homework.spring08.dao.BookRepository;
import ru.otus.group202205.homework.spring08.dao.DaoConfig;
import ru.otus.group202205.homework.spring08.dao.GenreRepository;
import ru.otus.group202205.homework.spring08.model.Author;
import ru.otus.group202205.homework.spring08.model.Book;
import ru.otus.group202205.homework.spring08.model.Genre;
import ru.otus.group202205.homework.spring08.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring08.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring08.testdata.GenreTestDataComponent;

@DataMongoTest
@Import({TransactionAutoConfiguration.class, DaoConfig.class, MongoDbRepositoryUnitTestConfig.class, BookTestDataComponent.class, AuthorTestDataComponent.class,
    GenreTestDataComponent.class})
@DirtiesContext
class BookRepositoryMongoTest {

  private static final String INSERTED_BOOK_ID_VALUE = "3";
  private static final String EXISTING_BOOK_ID_VALUE = "1";
  private static final String GIRL_FROM_EARTH_RUSSIAN_NAME = "Девочка с Земли";
  @Autowired
  private BookRepository bookRepository;
  @Autowired
  private GenreRepository genreRepository;
  @Autowired
  private AuthorRepository authorRepository;
  @Autowired
  private BookTestDataComponent bookTestDataComponent;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;

  /*Mongock can't work(correctly) with @Transactional and mongo replicas. TransactionManager only work with replicas. */
  @BeforeEach
  void beforeEach() {
    bookRepository.deleteAll();
    authorRepository.deleteAll();
    genreRepository.deleteAll();
    genreRepository.saveAll(genreTestDataComponent.getAllExistingGenres());
    authorRepository.saveAll(authorTestDataComponent.getAllExistingAuthors());
    bookRepository.saveAll(bookTestDataComponent.getAllExistingBooks());
  }

  //region create
  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeInsertNewBook() {
    assertThat(bookRepository.existsById(INSERTED_BOOK_ID_VALUE)).isFalse();
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
    assertThat(exceptedBook.getComments())
        .isNotNull()
        .isEmpty();
    assertThat(exceptedBook.getId()).isNull();
    exceptedBook = bookRepository.save(exceptedBook);
    String savedBookId = exceptedBook.getId();
    assertThat(savedBookId).isNotNull();
    Book actualBook = bookRepository
        .findById(savedBookId)
        .orElseThrow();
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

  @Test
  void shouldBeThrowDuplicateKeyExceptionByNaturalKey() {
    Book exceptedBook = bookTestDataComponent.getGirlFromEarthBook();
    assertThat(exceptedBook.getAuthor()).isNotNull();
    assertThat(exceptedBook
        .getAuthor()
        .getId()).isNotNull();
    assertThat(exceptedBook.getGenre()).isNotNull();
    assertThat(exceptedBook
        .getGenre()
        .getId()).isNotNull();
    exceptedBook.setId(null);
    assertThatCode(() -> bookRepository.save(exceptedBook))
        .isNotNull()
        .isInstanceOf(DuplicateKeyException.class)
        .hasCauseInstanceOf(MongoWriteException.class);
  }

  //endregion

  //region read
  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeGetAllInsertedBook() {
    assertThat(bookRepository.findAll())
        .isNotNull()
        .isNotEmpty()
        .isEqualTo(bookTestDataComponent.getAllExistingBooks());
  }
  //endregion

  //region update
  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeUpdateInsertedBook() {
    Book existingBook = bookTestDataComponent.getGirlFromEarthBook();
    assertThat(existingBook.getId()).isEqualTo(EXISTING_BOOK_ID_VALUE);
    Book exceptedBook = bookRepository
        .findById(EXISTING_BOOK_ID_VALUE)
        .orElseThrow();
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
    Book actualUpdatedBook = bookRepository
        .findById(EXISTING_BOOK_ID_VALUE)
        .orElseThrow();
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
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeUpdateAuthorWithOnlyIdInsertedBook() {
    Book existingBook = bookTestDataComponent.getGirlFromEarthBook();
    assertThat(existingBook.getId()).isEqualTo(EXISTING_BOOK_ID_VALUE);
    Book exceptedBook = bookRepository
        .findById(EXISTING_BOOK_ID_VALUE)
        .orElseThrow();
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
    String newBookAuthorId = newBookAuthor.getId();
    assertThat(newBookAuthorId).isNotNull();
    Author newBookUpdatingAuthor = new Author();
    newBookUpdatingAuthor.setId(newBookAuthorId);
    newBookUpdatingAuthor.setPatronymic(null);
    exceptedUpdatedBook.setAuthor(newBookUpdatingAuthor);
    exceptedUpdatedBook = bookRepository.save(exceptedUpdatedBook);
    Book actualUpdatedBook = bookRepository
        .findById(EXISTING_BOOK_ID_VALUE)
        .orElseThrow();
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
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeUpdateGenreWithOnlyIdInsertedBook() {
    Book existingBook = bookTestDataComponent.getGirlFromEarthBook();
    assertThat(existingBook.getId()).isEqualTo(EXISTING_BOOK_ID_VALUE);
    Book exceptedBook = bookRepository
        .findById(EXISTING_BOOK_ID_VALUE)
        .orElseThrow();
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
    String newBookGenreId = newBookGenre.getId();
    assertThat(newBookGenreId).isNotNull();
    Genre newBookUpdatingGenre = new Genre();
    newBookUpdatingGenre.setId(newBookGenreId);
    exceptedUpdatedBook.setGenre(newBookUpdatingGenre);
    exceptedUpdatedBook = bookRepository.save(exceptedUpdatedBook);
    Book actualUpdatedBook = bookRepository
        .findById(EXISTING_BOOK_ID_VALUE)
        .orElseThrow();
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
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeDeleteInsertedBook() {
    Book existingBook = bookTestDataComponent.getGirlFromEarthBook();
    Optional<Book> actualBookOptional = bookRepository.findById(EXISTING_BOOK_ID_VALUE);
    assertThat(actualBookOptional)
        .isNotNull()
        .isPresent();
    Book actualBook = actualBookOptional.get();
    assertThat(actualBook).isEqualTo(existingBook);
    bookRepository.deleteById(EXISTING_BOOK_ID_VALUE);
    assertThat(bookRepository.findById(EXISTING_BOOK_ID_VALUE))
        .isNotNull()
        .isNotPresent();
  }
  //endregion

}