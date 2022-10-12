package ru.otus.group202205.homework.spring05.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.group202205.homework.spring05.dao.AuthorDao;
import ru.otus.group202205.homework.spring05.dao.BookDao;
import ru.otus.group202205.homework.spring05.dao.GenreDao;
import ru.otus.group202205.homework.spring05.model.Author;
import ru.otus.group202205.homework.spring05.model.Book;
import ru.otus.group202205.homework.spring05.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring05.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring05.testdata.GenreTestDataComponent;

@JdbcTest
@Import({BookDaoJdbc.class, GenreDaoJdbc.class, AuthorDaoJdbc.class, BookTestDataComponent.class, AuthorTestDataComponent.class, GenreTestDataComponent.class})
class BookDaoJdbcTest {

  private static final Long INSERTED_BOOK_ID_VALUE = 3L;
  private static final Long MISSING_AUTHOR_ID_VALUE = 4L;
  private static final Long MISSING_GENRE_ID_VALUE = 4L;
  private static final Long EXISTING_BOOK_ID_VALUE = 1L;
  private static final String GIRL_FROM_EARTH_RUSSIAN_NAME = "Девочка с Земли";
  @Autowired
  private BookDao bookDao;
  @Autowired
  private GenreDao genreDao;
  @Autowired
  private AuthorDao authorDao;
  @Autowired
  private BookTestDataComponent bookTestDataComponent;

  //region create
  @Test
  void shouldBeInsertNewBook() {
    Book exceptedBook = bookTestDataComponent.getChildIslandBook();
    assertThat(exceptedBook
        .getGenre()
        .getName())
        .isNotNull()
        .isEqualTo("Science fiction");
    Author exceptedAuthor = exceptedBook.getAuthor();
    assertThat(exceptedAuthor.getSurname())
        .isNotNull()
        .isEqualTo("Bulychev");
    assertThat(exceptedAuthor.getName())
        .isNotNull()
        .isEqualTo("Kir");
    bookDao.insert(exceptedBook);
    assertThat(exceptedBook.getId())
        .isNotNull()
        .isEqualTo(INSERTED_BOOK_ID_VALUE);
  }

  @Test
  void shouldBeThrowDataIntegrityViolationExceptionExceptionForNotExistsAuthorAndGenre() {
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
    assertThatCode(() -> bookDao.insert(exceptedBook))
        .isNotNull()
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  void shouldBeThrowDataIntegrityViolationExceptionExceptionForWithoutAuthorAndGenre() {
    Book expectedBook = bookTestDataComponent.getBookWithoutAuthorAndGenre();
    assertThat(expectedBook.getAuthor()).isNull();
    assertThat(expectedBook.getGenre()).isNull();
    assertThatCode(() -> bookDao.insert(expectedBook))
        .isNotNull()
        .isInstanceOf(DataIntegrityViolationException.class);
  }
  //endregion

  //region read
  @Test
  void shouldBeGetAllInsertedBook() {
    assertThat(bookDao.getAll())
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
    Book actualBook = bookDao.getById(EXISTING_BOOK_ID_VALUE);
    assertThat(actualBook)
        .isNotNull()
        .isEqualTo(existingBook);
    assertThat(actualBook.getAuthor())
        .isNotNull()
        .isEqualTo(existingBook.getAuthor());
    assertThat(actualBook.getGenre())
        .isNotNull()
        .isEqualTo(existingBook.getGenre());
    Book exceptedUpdatedBook = bookTestDataComponent.getGirlFromEarthBook();
    exceptedUpdatedBook.setTitle(GIRL_FROM_EARTH_RUSSIAN_NAME);
    bookDao.update(exceptedUpdatedBook);
    Book actualUpdatedBook = bookDao.getById(EXISTING_BOOK_ID_VALUE);
    assertThat(actualUpdatedBook)
        .isNotNull()
        .isEqualTo(exceptedUpdatedBook);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteInsertedBook() {
    Book existingBook = bookTestDataComponent.getGirlFromEarthBook();
    Book actualBook = bookDao.getById(EXISTING_BOOK_ID_VALUE);
    assertThat(actualBook)
        .isNotNull()
        .isEqualTo(existingBook);
    bookDao.deleteById(EXISTING_BOOK_ID_VALUE);
    assertThatCode(() -> bookDao.getById(EXISTING_BOOK_ID_VALUE))
        .isNotNull()
        .isInstanceOf(EmptyResultDataAccessException.class);
  }
  //endregion

}