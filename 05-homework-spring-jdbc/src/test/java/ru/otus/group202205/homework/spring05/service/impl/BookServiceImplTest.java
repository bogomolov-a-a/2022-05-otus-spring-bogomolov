package ru.otus.group202205.homework.spring05.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.group202205.homework.spring05.dao.BookDao;
import ru.otus.group202205.homework.spring05.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring05.model.Book;
import ru.otus.group202205.homework.spring05.service.BookService;
import ru.otus.group202205.homework.spring05.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring05.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring05.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {BookServiceImpl.class, BookTestDataComponent.class, AuthorTestDataComponent.class,
    GenreTestDataComponent.class})
class BookServiceImplTest {

  private static final Long INSERTED_BOOK_ID_VALUE = 3L;
  private static final Long EXISTING_BOOK_ID_VALUE = 1L;
  private static final Long MISSING_BOOK_ID_VALUE = 4L;
  private static final String GIRL_FROM_EARTH_RUSSIAN_NAME = "Девочка с Земли";
  @Autowired
  private BookService bookService;
  @Autowired
  private BookTestDataComponent bookTestDataComponent;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;

  @MockBean
  private BookDao bookDaoJdbc;

  //region create
  @Test
  void shouldBeInsertNewBook() {
    Book actualBook = bookTestDataComponent.getChildIslandBook();
    Mockito
        .doAnswer(invocation -> {
          Book insertableBook = invocation.getArgument(0);
          insertableBook.setId(INSERTED_BOOK_ID_VALUE);
          return insertableBook;
        })
        .when(bookDaoJdbc)
        .insert(actualBook);
    bookService.insert(actualBook);
    Book exceptedBook = bookTestDataComponent.getChildIslandBook();
    exceptedBook.setId(INSERTED_BOOK_ID_VALUE);
    assertThat(actualBook)
        .isNotNull()
        .isEqualTo(exceptedBook);
    assertThat(actualBook.getAuthor())
        .isNotNull()
        .isEqualTo(authorTestDataComponent.getBulychevKirAuthor());
    assertThat(actualBook.getGenre())
        .isNotNull()
        .isEqualTo(genreTestDataComponent.getScienceFictionGenre());
  }

  @Test
  void shouldBeThrowDataIntegrityViolationExceptionExceptionForWithoutAuthorAndGenre() {
    Book expectedBook = bookTestDataComponent.getBookWithoutAuthorAndGenre();
    Mockito
        .doThrow(DataIntegrityViolationException.class)
        .when(bookDaoJdbc)
        .insert(expectedBook);
    assertThatCode(() -> bookService.insert(expectedBook))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  void shouldBeThrowDataIntegrityViolationExceptionExceptionForNotExistsAuthorAndGenre() {
    Book expectedBook = bookTestDataComponent.getBookWithMissingAuthorOrGenre();
    Mockito
        .doThrow(DataIntegrityViolationException.class)
        .when(bookDaoJdbc)
        .insert(expectedBook);
    assertThatCode(() -> bookService.insert(expectedBook))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(DataIntegrityViolationException.class);
  }
  //endregion

  //region read
  @Test
  void shouldBeGetAllInsertedBook() {
    Mockito
        .doReturn(bookTestDataComponent.getAllExistingBooks())
        .when(bookDaoJdbc)
        .getAll();
    assertThat(bookService.getAll())
        .isNotNull()
        .isNotEmpty()
        .isEqualTo(bookTestDataComponent.getAllExistingBooks());
  }

  @Test
  void shouldBeThrowDataRetrievalFailureExceptionInGetAllBooks() {
    Mockito
        .doThrow(DataRetrievalFailureException.class)
        .when(bookDaoJdbc)
        .getAll();
    assertThatCode(() -> bookService.getAll())
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(DataRetrievalFailureException.class);
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateInsertedBookTitle() {
    Book existingBook = bookTestDataComponent.getGirlFromEarthBook();
    assertThat(existingBook.getId()).isEqualTo(EXISTING_BOOK_ID_VALUE);
    Mockito
        .doReturn(existingBook)
        .when(bookDaoJdbc)
        .getById(EXISTING_BOOK_ID_VALUE);
    Book actualBook = bookService.getById(EXISTING_BOOK_ID_VALUE);
    assertThat(actualBook)
        .isNotNull()
        .isEqualTo(existingBook);
    assertThat(actualBook.getAuthor())
        .isNotNull()
        .isEqualTo(authorTestDataComponent.getBulychevKirAuthor());
    assertThat(actualBook.getGenre())
        .isNotNull()
        .isEqualTo(genreTestDataComponent.getScienceFictionGenre());
    Book actualUpdatedBook = new Book();
    actualUpdatedBook.setId(EXISTING_BOOK_ID_VALUE);
    actualUpdatedBook.setTitle(GIRL_FROM_EARTH_RUSSIAN_NAME);
    Mockito
        .doNothing()
        .when(bookDaoJdbc)
        .update(actualUpdatedBook);
    bookService.update(actualUpdatedBook);
    Book exceptedUpdatedBook = bookTestDataComponent.getGirlFromEarthBook();
    exceptedUpdatedBook.setTitle(GIRL_FROM_EARTH_RUSSIAN_NAME);
    Mockito.reset(bookDaoJdbc);
    Mockito
        .doReturn(exceptedUpdatedBook)
        .when(bookDaoJdbc)
        .getById(EXISTING_BOOK_ID_VALUE);
    assertThat(actualUpdatedBook)
        .isNotNull()
        .isEqualTo(exceptedUpdatedBook);
    assertThat(actualUpdatedBook.getAuthor())
        .isNotNull()
        .isEqualTo(exceptedUpdatedBook.getAuthor());
    assertThat(actualUpdatedBook.getGenre())
        .isNotNull()
        .isEqualTo(exceptedUpdatedBook.getGenre());
  }

  @Test
  void shouldBeDataRetrievalFailureExceptionInUpdateBook() {
    Book emptyBook = new Book();
    Mockito
        .doThrow(DataRetrievalFailureException.class)
        .when(bookDaoJdbc)
        .update(emptyBook);
    assertThatCode(() -> bookService.update(emptyBook))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(NullPointerException.class);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteInsertedBook() {
    Book existingBook = bookTestDataComponent.getGirlFromEarthBook();
    Mockito
        .doReturn(existingBook)
        .when(bookDaoJdbc)
        .getById(EXISTING_BOOK_ID_VALUE);
    Book actualBook = bookService.getById(EXISTING_BOOK_ID_VALUE);
    assertThat(actualBook)
        .isNotNull()
        .isEqualTo(existingBook);
    Mockito
        .doNothing()
        .when(bookDaoJdbc)
        .deleteById(EXISTING_BOOK_ID_VALUE);
    bookService.deleteById(EXISTING_BOOK_ID_VALUE);
    Mockito.reset(bookDaoJdbc);
    Mockito
        .doThrow(EmptyResultDataAccessException.class)
        .when(bookDaoJdbc)
        .getById(EXISTING_BOOK_ID_VALUE);
    assertThatCode(() -> bookService.getById(EXISTING_BOOK_ID_VALUE))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(EmptyResultDataAccessException.class);
  }

  @Test
  void shouldBeDataRetrievalFailureExceptionInDeleteBookById() {
    Mockito
        .doThrow(DataRetrievalFailureException.class)
        .when(bookDaoJdbc)
        .deleteById(MISSING_BOOK_ID_VALUE);
    assertThatCode(() -> bookService.deleteById(MISSING_BOOK_ID_VALUE))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(DataRetrievalFailureException.class);
  }
  //endregion

}