package ru.otus.group202205.homework.spring07.shell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import ru.otus.group202205.homework.spring07.dto.AuthorDto;
import ru.otus.group202205.homework.spring07.dto.BookFullDto;
import ru.otus.group202205.homework.spring07.dto.GenreDto;
import ru.otus.group202205.homework.spring07.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring07.model.Author;
import ru.otus.group202205.homework.spring07.model.Book;
import ru.otus.group202205.homework.spring07.model.Genre;
import ru.otus.group202205.homework.spring07.service.BookService;
import ru.otus.group202205.homework.spring07.service.converter.AuthorConverter;
import ru.otus.group202205.homework.spring07.service.converter.BookConverter;
import ru.otus.group202205.homework.spring07.service.converter.GenreConverter;
import ru.otus.group202205.homework.spring07.service.mapper.BookMapper;
import ru.otus.group202205.homework.spring07.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring07.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring07.testdata.GenreTestDataComponent;

@SpringBootTest(
    classes = {BookShellComponent.class, BookTestDataComponent.class, AuthorTestDataComponent.class, GenreTestDataComponent.class, TestShellConfig.class})
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
  @MockBean
  private BookMapper bookMapper;
  @MockBean
  private BookConverter bookConverter;
  @MockBean
  private AuthorConverter authorConverter;
  @MockBean
  private GenreConverter genreConverter;

  @BeforeEach
  void init() {
    Mockito.reset(bookMapper);
    Mockito.reset(authorConverter);
    Mockito.reset(genreConverter);
    Mockito.reset(bookConverter);
    Mockito
        .doAnswer(invocation -> {
          BookFullDto bookDto = invocation.getArgument(0);
          Book book = new Book();
          book.setId(bookDto.getId());
          book.setTitle(bookDto.getTitle());
          book.setIsbn(bookDto.getIsbn());
          AuthorDto authorDto = bookDto.getAuthor();
          if (authorDto != null) {
            Author author = new Author();
            author.setId(authorDto.getId());
            author.setSurname(authorDto.getSurname());
            author.setName(authorDto.getName());
            author.setPatronymic(authorDto.getPatronymic());
            author.setBirthYear(authorDto.getBirthYear());
            author.setDeathYear(authorDto.getDeathYear());
            book.setAuthor(author);
          }
          GenreDto genreDto = bookDto.getGenre();
          if (genreDto != null) {
            Genre genre = new Genre();
            genre.setId(genreDto.getId());
            genre.setName(genreDto.getName());
            book.setGenre(genre);
          }
          return book;
        })
        .when(bookMapper)
        .toEntityFromFull(any());
    Mockito
        .doAnswer(invocation -> {
          Book book = invocation.getArgument(0);
          BookFullDto bookDto = new BookFullDto();
          bookDto.setId(book.getId());
          bookDto.setTitle(book.getTitle());
          bookDto.setIsbn(book.getIsbn());
          Author author = book.getAuthor();
          if (author != null) {
            AuthorDto authorDto = new AuthorDto();
            authorDto.setId(author.getId());
            authorDto.setSurname(author.getSurname());
            authorDto.setName(author.getName());
            authorDto.setPatronymic(author.getPatronymic());
            authorDto.setBirthYear(author.getBirthYear());
            authorDto.setDeathYear(author.getDeathYear());
            bookDto.setAuthor(authorDto);
          }
          Genre genre = book.getGenre();
          if (genre != null) {
            GenreDto genreDto = new GenreDto();
            genreDto.setId(genre.getId());
            genreDto.setName(genre.getName());
            bookDto.setGenre(genreDto);
          }
          return bookDto;
        })
        .when(bookMapper)
        .toFullDto(any());
    Mockito
        .doAnswer(invocation -> {
          AuthorDto author = invocation.getArgument(0);
          return String.format("Author id: %d%ssurname: %s%sname: %s%spatronymic: %s%sbirth year: %d%sdeath year: %s%s",
              author.getId(),
              System.lineSeparator(),
              author.getSurname(),
              System.lineSeparator(),
              author.getName(),
              System.lineSeparator(),
              author.getPatronymic(),
              System.lineSeparator(),
              author.getBirthYear(),
              System.lineSeparator(),
              author.getDeathYear(),
              System.lineSeparator());
        })
        .when(authorConverter)
        .convertAuthor(any());
    Mockito
        .doAnswer(invocation -> {
          GenreDto genre = invocation.getArgument(0);
          return String.format("Genre id: %d%sname: %s%s",
              genre.getId(),
              System.lineSeparator(),
              genre.getName(),
              System.lineSeparator());
        })
        .when(genreConverter)
        .convertGenre(any());
    Mockito
        .doAnswer(invocation -> {
          BookFullDto book = invocation.getArgument(0);
          return String.format("Book id: %d%stitle: %s%sisbn: %s%swritten by: %s in genre: %s%s",
              book.getId(),
              System.lineSeparator(),
              book.getTitle(),
              System.lineSeparator(),
              book.getIsbn(),
              System.lineSeparator(),
              authorConverter
                  .convertAuthor(book.getAuthor())
                  .trim(),
              genreConverter
                  .convertGenre(book.getGenre())
                  .trim(),
              System.lineSeparator());
        })
        .when(bookConverter)
        .convertBook(any());
    Mockito
        .doAnswer(invocation -> {
          List<BookFullDto> books = invocation.getArgument(0);
          StringBuilder result = new StringBuilder("Book list").append(System.lineSeparator());
          books.forEach(book -> result.append(bookConverter.convertBook(book)));
          return result.toString();
        })
        .when(bookConverter)
        .convertBooks(anyList());
  }

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
    BookFullDto exceptedBookDto = bookMapper.toFullDto(exceptedBook);
    Mockito
        .doAnswer(invocation -> {
          BookFullDto insertableBook = invocation.getArgument(0);
          insertableBook.setId(INSERTED_BOOK_ID_VALUE);
          return insertableBook;
        })
        .when(bookService)
        .saveOrUpdate(exceptedBookDto);
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
    BookFullDto exceptedBookDto = bookMapper.toFullDto(exceptedBook);
    Mockito
        .doThrow(new LibraryGeneralException("Can't create book",
            new PersistenceException("author or genre not found")))
        .when(bookService)
        .saveOrUpdate(exceptedBookDto);
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
    BookFullDto actualUpdatedBookDto = bookMapper.toFullDto(actualUpdatedBook);
    Mockito
        .doAnswer(invocation -> {
          existingBook.setTitle(((BookFullDto) invocation.getArgument(0)).getTitle());
          return existingBook;
        })
        .when(bookService)
        .saveOrUpdate(actualUpdatedBookDto);
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
    BookFullDto emptyNotExistingBookDto = bookMapper.toFullDto(emptyNotExistingBook);
    Mockito
        .doThrow(new LibraryGeneralException("Can't update book",
            new PersistenceException("Book not found with id " + MISSING_BOOK_ID_VALUE)))
        .when(bookService)
        .saveOrUpdate(emptyNotExistingBookDto);
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