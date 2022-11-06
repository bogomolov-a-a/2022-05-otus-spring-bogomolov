package ru.otus.group202205.homework.spring06.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.RawMember;
import com.fasterxml.classmate.members.RawMethod;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring06.dao.BookRepository;
import ru.otus.group202205.homework.spring06.dto.BookFullDto;
import ru.otus.group202205.homework.spring06.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring06.model.Book;
import ru.otus.group202205.homework.spring06.service.BookService;
import ru.otus.group202205.homework.spring06.service.mapper.BookMapper;
import ru.otus.group202205.homework.spring06.service.mapper.impl.BookMapperImpl;
import ru.otus.group202205.homework.spring06.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring06.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring06.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {BookServiceImpl.class, BookTestDataComponent.class, AuthorTestDataComponent.class,
    GenreTestDataComponent.class, BookMapperImpl.class})
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
  @Autowired
  private BookMapper bookMapper;
  @MockBean
  private BookRepository bookRepositoryJpa;

  //region create
  @Test
  void shouldBeInsertNewBook() {
    Book exceptedBook = bookTestDataComponent.getChildIslandBook();
    assertThat(exceptedBook.getAuthor())
        .isNotNull()
        .isEqualTo(authorTestDataComponent.getBulychevKirAuthor());
    assertThat(exceptedBook.getGenre())
        .isNotNull()
        .isEqualTo(genreTestDataComponent.getScienceFictionGenre());
    Mockito
        .doAnswer(invocation -> {
          Book insertableBook = invocation.getArgument(0);
          insertableBook.setId(INSERTED_BOOK_ID_VALUE);
          return insertableBook;
        })
        .when(bookRepositoryJpa)
        .saveOrUpdate(exceptedBook);
    BookFullDto exceptedBookFullDto = bookMapper.toFullDto(exceptedBook);
    assertThat(exceptedBookFullDto.getId()).isNull();
    BookFullDto actualBookDto = bookService.saveOrUpdate(exceptedBookFullDto);
    assertThat(exceptedBookFullDto.getId()).isNull();
    exceptedBookFullDto.setId(INSERTED_BOOK_ID_VALUE);
    assertThat(actualBookDto)
        .isNotNull()
        .isEqualTo(exceptedBookFullDto);
    assertThat(actualBookDto.getAuthor())
        .isNotNull()
        .isEqualTo(authorTestDataComponent.getBulychevKirAuthorDto());
    assertThat(actualBookDto.getGenre())
        .isNotNull()
        .isEqualTo(genreTestDataComponent.getScienceFictionGenreDto());
  }

  @Test
  void shouldBeThrowPersistenceExceptionForWithoutAuthorAndGenre() {
    Book expectedBook = bookTestDataComponent.getBookWithoutAuthorAndGenre();
    Mockito
        .doThrow(PersistenceException.class)
        .when(bookRepositoryJpa)
        .saveOrUpdate(expectedBook);
    assertThatCode(() -> bookService.saveOrUpdate(bookMapper.toFullDto(expectedBook)))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
  }

  @Test
  void shouldBeThrowPersistenceExceptionForNotExistsAuthorAndGenre() {
    Book expectedBook = bookTestDataComponent.getBookWithMissingAuthorOrGenre();
    Mockito
        .doThrow(PersistenceException.class)
        .when(bookRepositoryJpa)
        .saveOrUpdate(expectedBook);
    assertThatCode(() -> bookService.saveOrUpdate(bookMapper.toFullDto(expectedBook)))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
  }
  //endregion

  //region read
  @Test
  void shouldBeGetAllInsertedBook() {
    List<Book> allExistingBooks = bookTestDataComponent.getAllExistingBooks();
    List<BookFullDto> allExistingBookDtos = allExistingBooks
        .stream()
        .map(bookMapper::toFullDto)
        .collect(Collectors.toList());
    Mockito
        .doReturn(allExistingBooks)
        .when(bookRepositoryJpa)
        .findAll();
    assertThat(bookService.findAll())
        .isNotNull()
        .isNotEmpty()
        .isEqualTo(allExistingBookDtos);
  }

  @Test
  void shouldBeThrowPersistenceExceptionInGetAllBooks() {
    Mockito
        .doThrow(PersistenceException.class)
        .when(bookRepositoryJpa)
        .findAll();
    assertThatCode(() -> bookService.findAll())
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateInsertedBookTitle() {
    Book exceptedInsertedBook = bookTestDataComponent.getChildIslandBook();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          Book result = ((Book) args[0]);
          result.setId(INSERTED_BOOK_ID_VALUE);
          return result;
        })
        .when(bookRepositoryJpa)
        .saveOrUpdate(exceptedInsertedBook);
    BookFullDto exceptedInsertedBookDto = bookMapper.toFullDto(exceptedInsertedBook);
    assertThat(exceptedInsertedBookDto.getId()).isNull();
    BookFullDto actualInsertedBookDto = bookService.saveOrUpdate(exceptedInsertedBookDto);
    assertThat(exceptedInsertedBookDto.getId()).isNull();
    Long exceptedBookId = actualInsertedBookDto.getId();
    assertThat(exceptedBookId)
        .isNotNull()
        .isEqualTo(INSERTED_BOOK_ID_VALUE);
    Book updatingBook = new Book();
    updatingBook.setId(exceptedBookId);
    updatingBook.setTitle(GIRL_FROM_EARTH_RUSSIAN_NAME);

    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          exceptedInsertedBook.setId(INSERTED_BOOK_ID_VALUE);
          exceptedInsertedBook.setTitle(((Book) args[0]).getTitle());
          return exceptedInsertedBook;
        })
        .when(bookRepositoryJpa)
        .saveOrUpdate(updatingBook);
    BookFullDto updatingBookDto = bookMapper.toFullDto(updatingBook);
    BookFullDto actualUpdatedDto = bookService.saveOrUpdate(updatingBookDto);
    assertThat(actualUpdatedDto.getId())
        .isNotNull()
        .isEqualTo(INSERTED_BOOK_ID_VALUE);
    Mockito.reset(bookRepositoryJpa);
    Mockito
        .doReturn(Optional.of(exceptedInsertedBook))
        .when(bookRepositoryJpa)
        .findById(INSERTED_BOOK_ID_VALUE);
    BookFullDto exceptedUpdatedBookDto = bookMapper.toFullDto(exceptedInsertedBook);
    BookFullDto actualUpdatedBookDto = bookService.findById(INSERTED_BOOK_ID_VALUE);
    assertThat(actualUpdatedBookDto)
        .isNotNull()
        .isEqualTo(exceptedUpdatedBookDto);
    assertThat(actualUpdatedBookDto.getAuthor())
        .isNotNull()
        .isEqualTo(exceptedUpdatedBookDto.getAuthor());
    assertThat(actualUpdatedBookDto.getGenre())
        .isNotNull()
        .isEqualTo(exceptedUpdatedBookDto.getGenre());
  }

  @Test
  void shouldBePersistenceExceptionInUpdateBook() {
    Book emptyBook = new Book();
    Mockito
        .doThrow(PersistenceException.class)
        .when(bookRepositoryJpa)
        .saveOrUpdate(emptyBook);
    assertThatCode(() -> bookService.saveOrUpdate(bookMapper.toFullDto(emptyBook)))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteInsertedBook() {
    Book existingBook = bookTestDataComponent.getGirlFromEarthBook();
    Mockito
        .doReturn(Optional.of(existingBook))
        .when(bookRepositoryJpa)
        .findById(EXISTING_BOOK_ID_VALUE);
    BookFullDto actualBookDto = bookService.findById(EXISTING_BOOK_ID_VALUE);
    BookFullDto existingBookDto = bookMapper.toFullDto(existingBook);
    assertThat(actualBookDto)
        .isNotNull()
        .isEqualTo(existingBookDto);
    Mockito
        .doNothing()
        .when(bookRepositoryJpa)
        .deleteById(EXISTING_BOOK_ID_VALUE);
    bookService.deleteById(EXISTING_BOOK_ID_VALUE);
    Mockito.reset(bookRepositoryJpa);
    Mockito
        .doReturn(Optional.empty())
        .when(bookRepositoryJpa)
        .findById(EXISTING_BOOK_ID_VALUE);
    assertThatCode(() -> bookService.findById(EXISTING_BOOK_ID_VALUE))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(NoSuchElementException.class);
  }

  @Test
  void shouldBePersistenceExceptionInDeleteBookById() {
    Mockito
        .doThrow(PersistenceException.class)
        .when(bookRepositoryJpa)
        .deleteById(MISSING_BOOK_ID_VALUE);
    assertThatCode(() -> bookService.deleteById(MISSING_BOOK_ID_VALUE))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
  }

  //endregion
  @Test
  void shouldBeAllPublicMethodsMarkedTransactional() {
    TypeResolver resolver = new TypeResolver();
    ResolvedType resolvedType = resolver.resolve(BookServiceImpl.class);
    List<RawMethod> methods = resolvedType.getMemberMethods();
    methods
        .stream()
        .filter(RawMember::isPublic)
        .forEach(method -> assertThat(Arrays
            .stream(method.getAnnotations())
            .map(Annotation::annotationType)
            .collect(Collectors.toList())
            .contains(Transactional.class)).isFalse());
  }

}