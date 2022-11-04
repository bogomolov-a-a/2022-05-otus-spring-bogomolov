package ru.otus.group202205.homework.spring06.shell;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import ru.otus.group202205.homework.spring06.dto.BookFullDto;
import ru.otus.group202205.homework.spring06.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring06.model.Author;
import ru.otus.group202205.homework.spring06.model.Book;
import ru.otus.group202205.homework.spring06.model.Genre;
import ru.otus.group202205.homework.spring06.service.BookService;
import ru.otus.group202205.homework.spring06.service.converter.impl.AuthorConverterImpl;
import ru.otus.group202205.homework.spring06.service.converter.impl.BookConverterImpl;
import ru.otus.group202205.homework.spring06.service.converter.impl.GenreConverterImpl;
import ru.otus.group202205.homework.spring06.service.mapper.BookMapper;
import ru.otus.group202205.homework.spring06.service.mapper.impl.BookMapperImpl;
import ru.otus.group202205.homework.spring06.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring06.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring06.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {BookShellComponent.class, BookConverterImpl.class, AuthorConverterImpl.class, GenreConverterImpl.class, BookTestDataComponent.class,
    AuthorTestDataComponent.class, GenreTestDataComponent.class, TestShellConfig.class, BookMapperImpl.class})
class BookShellComponentTest {

  private static final Long INSERTED_BOOK_ID_VALUE = 3L;
  private static final Long EXISTING_BOOK_ID_VALUE = 1L;
  private static final Long MISSING_BOOK_ID_VALUE = 4L;
  private static final Long MISSING_AUTHOR_ID_VALUE = 4L;
  private static final Long MISSING_GENRE_ID_VALUE = 4L;
  private static final String GIRL_FROM_EARTH_RUSSIAN_NAME = "Девочка_с_Земли";
  @Autowired
  private Shell shell;
  @Autowired
  private BookTestDataComponent bookTestDataComponent;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;
  @MockBean
  private BookService bookService;
  @Autowired
  private BookMapper bookMapper;

  //region create
  @Test
  void shouldBeInsertNewBook() {
    Book exceptedBook = bookTestDataComponent.getChildIslandBook();
    exceptedBook.setId(null);
    assertThat(exceptedBook.getAuthor())
        .isNotNull()
        .isEqualTo(authorTestDataComponent.getBulychevKirAuthor());
    assertThat(exceptedBook.getGenre())
        .isNotNull()
        .isEqualTo(genreTestDataComponent.getScienceFictionGenre());
    Author exceptedAuthor = new Author();
    exceptedAuthor.setId(exceptedBook
        .getAuthor()
        .getId());
    exceptedBook.setAuthor(exceptedAuthor);
    Genre exceptedGenre = new Genre();
    exceptedGenre.setId(exceptedBook
        .getGenre()
        .getId());
    exceptedBook.setGenre(exceptedGenre);
    Mockito
        .doAnswer(invocation -> {
          BookFullDto insertableBook = invocation.getArgument(0);
          insertableBook.setId(INSERTED_BOOK_ID_VALUE);
          return insertableBook;
        })
        .when(bookService)
        .saveOrUpdate(bookMapper.toFullDto(exceptedBook));
    String successCreatedBookMessage = (String) shell.evaluate(() -> String.format("create-book --title %s --isbn %s --author-id %d --genre-id %d",
        exceptedBook.getTitle(),
        exceptedBook.getIsbn(),
        exceptedBook
            .getAuthor()
            .getId(),
        exceptedBook
            .getGenre()
            .getId()));
    assertThat(successCreatedBookMessage)
        .isNotNull()
        .isEqualTo("Book with id " + INSERTED_BOOK_ID_VALUE + " created!");
  }


  @Test
  void shouldBeThrowPersistenceExceptionForNotExistsAuthorAndGenre() {
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
    Author exceptedAuthor = new Author();
    exceptedAuthor.setId(exceptedBook
        .getAuthor()
        .getId());
    exceptedBook.setAuthor(exceptedAuthor);
    Genre exceptedGenre = new Genre();
    exceptedGenre.setId(exceptedBook
        .getGenre()
        .getId());
    exceptedBook.setGenre(exceptedGenre);
    Mockito
        .doThrow(new LibraryGeneralException("Can't create book",
            new PersistenceException("author or genre not found")))
        .when(bookService)
        .saveOrUpdate(bookMapper.toFullDto(exceptedBook));
    String errorCreatedBookMessage = (String) shell.evaluate(() -> String.format("create-book --title %s --isbn %s --author-id %d --genre-id %d",
        exceptedBook.getTitle(),
        exceptedBook.getIsbn(),
        exceptedBook
            .getAuthor()
            .getId(),
        exceptedBook
            .getGenre()
            .getId()));
    assertThat(errorCreatedBookMessage)
        .isNotNull()
        .isEqualTo("Can't create book Cause author or genre not found");
  }
  //endregion

  //region read
  @Test
  void shouldBeGetBookById() {
    Book exceptedBook = bookTestDataComponent.getGirlFromEarthBook();
    assertThat(exceptedBook.getId())
        .isNotNull()
        .isEqualTo(EXISTING_BOOK_ID_VALUE);
    assertThat(exceptedBook.getAuthor())
        .isNotNull()
        .isEqualTo(authorTestDataComponent.getBulychevKirAuthor());
    assertThat(exceptedBook.getGenre())
        .isNotNull()
        .isEqualTo(genreTestDataComponent.getScienceFictionGenre());
    Mockito
        .doReturn(bookMapper.toFullDto(exceptedBook))
        .when(bookService)
        .findById(EXISTING_BOOK_ID_VALUE);
    String successAllBookReadCommandOutput = (String) shell.evaluate(() -> String.format("read-book --id %d",
        EXISTING_BOOK_ID_VALUE));
    assertThat(successAllBookReadCommandOutput)
        .isNotNull()
        .isEqualTo(
            "Book id: 1" + System.lineSeparator() + "title: Girl from the Earth" + System.lineSeparator() + "isbn: 978-5-699-11438-2" + System.lineSeparator()
                + "written by: Author id: 1" + System.lineSeparator() + "surname: Bulychev" + System.lineSeparator() + "name: Kir" + System.lineSeparator()
                + "patronymic: not set" + System.lineSeparator() + "birth year: 1934" + System.lineSeparator() + "death year: 2003 in genre: Genre id: 2"
                + System.lineSeparator() + "name: Science fiction" + System.lineSeparator());
  }

  @Test
  void shouldBeThrowPersistenceExceptionInGetAllBook() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't read book",
            new PersistenceException("Book not found with id " + MISSING_BOOK_ID_VALUE)))
        .when(bookService)
        .findById(MISSING_BOOK_ID_VALUE);
    String errorReadBookMessage = (String) shell.evaluate(() -> String.format("read-book --id %d",
        MISSING_BOOK_ID_VALUE));
    assertThat(errorReadBookMessage)
        .isNotNull()
        .isEqualTo("Can't read book Cause Book not found with id " + MISSING_BOOK_ID_VALUE);
  }

  @Test
  void shouldBeGetAllInsertedBooks() {
    List<BookFullDto> bookDtos = bookTestDataComponent
        .getAllExistingBooks()
        .stream()
        .map(bookMapper::toFullDto)
        .collect(Collectors.toList());
    Mockito
        .doReturn(bookDtos)
        .when(bookService)
        .findAll();
    String successAllBookReadCommandOutput = (String) shell.evaluate(() -> "read-books");
    assertThat(successAllBookReadCommandOutput)
        .isNotNull()
        .isEqualTo("Book list" + System.lineSeparator() + "Book id: 1" + System.lineSeparator() + "title: Girl from the Earth" + System.lineSeparator()
            + "isbn: 978-5-699-11438-2" + System.lineSeparator() + "written by: Author id: 1" + System.lineSeparator() + "surname: Bulychev"
            + System.lineSeparator() + "name: Kir" + System.lineSeparator() + "patronymic: not set" + System.lineSeparator() + "birth year: 1934"
            + System.lineSeparator() + "death year: 2003 in genre: Genre id: 2" + System.lineSeparator() + "name: Science fiction" + System.lineSeparator()
            + "Book id: 2" + System.lineSeparator() + "title: Childhood. Boyhood. Youth" + System.lineSeparator() + "isbn: 978-5-04-116640-3"
            + System.lineSeparator() + "written by: Author id: 2" + System.lineSeparator() + "surname: Tolstoy" + System.lineSeparator() + "name: Lev"
            + System.lineSeparator() + "patronymic: Nikolayevich" + System.lineSeparator() + "birth year: 1828" + System.lineSeparator()
            + "death year: 1910 in genre: Genre id: 1" + System.lineSeparator() + "name: Novell" + System.lineSeparator());

  }

  @Test
  void shouldBeThrowPersistenceExceptionInGetAllBooks() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't read books",
            new PersistenceException("Data corrupted")))
        .when(bookService)
        .findAll();
    String errorReadBooksMessage = (String) shell.evaluate(() -> "read-books");
    assertThat(errorReadBooksMessage)
        .isNotNull()
        .isEqualTo("Can't read books Cause Data corrupted");
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateInsertedBookTitle() {
    Book existingBook = bookTestDataComponent.getGirlFromEarthBook();
    assertThat(existingBook.getId()).isEqualTo(EXISTING_BOOK_ID_VALUE);
    assertThat(existingBook.getAuthor())
        .isNotNull()
        .isEqualTo(authorTestDataComponent.getBulychevKirAuthor());
    assertThat(existingBook.getGenre())
        .isNotNull()
        .isEqualTo(genreTestDataComponent.getScienceFictionGenre());
    Book actualUpdatedBook = new Book();
    actualUpdatedBook.setId(EXISTING_BOOK_ID_VALUE);
    actualUpdatedBook.setTitle(GIRL_FROM_EARTH_RUSSIAN_NAME);
    Mockito
        .doAnswer(invocation -> {
          existingBook.setTitle(((BookFullDto) invocation.getArgument(0)).getTitle());
          return existingBook;
        })
        .when(bookService)
        .saveOrUpdate(bookMapper.toFullDto(actualUpdatedBook));
    String successUpdateBookMessage = (String) shell.evaluate(() -> String.format("update-book --id %d --title %s",
        EXISTING_BOOK_ID_VALUE,
        GIRL_FROM_EARTH_RUSSIAN_NAME));
    assertThat(successUpdateBookMessage)
        .isNotNull()
        .isEqualTo("Book with id " + EXISTING_BOOK_ID_VALUE + " updated!");
  }

  @Test
  void shouldBePersistenceExceptionInUpdateBook() {
    Book emptyNotExistingBook = new Book();
    emptyNotExistingBook.setId(MISSING_BOOK_ID_VALUE);
    emptyNotExistingBook.setAuthor(new Author());
    emptyNotExistingBook.setGenre(new Genre());
    Mockito
        .doThrow(new LibraryGeneralException("Can't update book",
            new PersistenceException("Book not found with id " + MISSING_BOOK_ID_VALUE)))
        .when(bookService)
        .saveOrUpdate(bookMapper.toFullDto(emptyNotExistingBook));
    String errorUpdateBookMessage = (String) shell.evaluate(() -> String.format("update-book --id %d",
        MISSING_BOOK_ID_VALUE));
    assertThat(errorUpdateBookMessage)
        .isNotNull()
        .isEqualTo("Can't update book Cause Book not found with id " + MISSING_BOOK_ID_VALUE);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteInsertedBook() {
    Mockito
        .doNothing()
        .when(bookService)
        .deleteById(EXISTING_BOOK_ID_VALUE);
    String successDeletedBookMessage = (String) shell.evaluate(() -> String.format("delete-book --id %d",
        EXISTING_BOOK_ID_VALUE));
    assertThat(successDeletedBookMessage)
        .isNotNull()
        .isEqualTo("Book with id " + EXISTING_BOOK_ID_VALUE + " deleted!");
  }

  @Test
  void shouldBePersistenceExceptionInDeleteBookById() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't delete book",
            new PersistenceException("Book not found with id " + MISSING_BOOK_ID_VALUE)))
        .when(bookService)
        .deleteById(MISSING_BOOK_ID_VALUE);
    String deletedBookErrorMessage = (String) shell.evaluate(() -> String.format("delete-book --id %d",
        MISSING_BOOK_ID_VALUE));
    assertThat(deletedBookErrorMessage)
        .isNotNull()
        .isEqualTo("Can't delete book Cause Book not found with id " + MISSING_BOOK_ID_VALUE);
  }
  //endregion
}