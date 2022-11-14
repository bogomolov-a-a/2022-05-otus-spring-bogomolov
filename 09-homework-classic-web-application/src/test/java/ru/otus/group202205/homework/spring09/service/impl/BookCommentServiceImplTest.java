package ru.otus.group202205.homework.spring09.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.RawMethod;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring09.dao.BookCommentRepository;
import ru.otus.group202205.homework.spring09.dao.BookRepository;
import ru.otus.group202205.homework.spring09.dto.BookCommentDto;
import ru.otus.group202205.homework.spring09.dto.BookSimpleDto;
import ru.otus.group202205.homework.spring09.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring09.model.Book;
import ru.otus.group202205.homework.spring09.model.BookComment;
import ru.otus.group202205.homework.spring09.service.BookCommentService;
import ru.otus.group202205.homework.spring09.service.mapper.BookCommentMapper;
import ru.otus.group202205.homework.spring09.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring09.testdata.BookCommentTestDataComponent;
import ru.otus.group202205.homework.spring09.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring09.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {BookCommentServiceImpl.class, BookCommentTestDataComponent.class, BookTestDataComponent.class,
    AuthorTestDataComponent.class, GenreTestDataComponent.class})
class BookCommentServiceImplTest {

  private static final Long INSERTED_BOOK_COMMENT_ID_VALUE = 4L;
  private static final Long EXISTING_BOOK_COMMENT_ID_VALUE = 1L;
  private static final Long EXISTING_BOOK_ID_VALUE = 1L;
  private static final String RUSSIAN_COMMENT_TEXT = "Классная книга!";
  private static final Long MISSING_BOOK_COMMENT_ID_VALUE = 5L;
  @Autowired
  private BookCommentService bookCommentService;
  @Autowired
  private BookCommentTestDataComponent bookCommentTestDataComponent;
  @Autowired
  private BookTestDataComponent bookTestDataComponent;
  @MockBean
  private BookCommentRepository bookCommentRepositoryJpa;
  @MockBean
  private BookRepository bookRepository;
  @MockBean
  private BookCommentMapper bookCommentMapper;

  @BeforeEach
  void init() {
    Mockito.reset(bookCommentMapper);
    Mockito
        .doAnswer(invocation -> {
          BookComment bookComment = invocation.getArgument(0);
          BookCommentDto result = new BookCommentDto();
          result.setId(bookComment.getId());
          result.setText(bookComment.getText());
          result.setCreated(bookComment.getCreated());
          Book book = bookComment.getBook();
          if (book != null) {
            BookSimpleDto bookDto = new BookSimpleDto();
            bookDto.setId(book.getId());
            bookDto.setTitle(book.getTitle());
            result.setBook(bookDto);
          }
          return result;
        })
        .when(bookCommentMapper)
        .toDto(any());
    Mockito
        .doAnswer(invocation -> {
          BookCommentDto bookCommentDto = invocation.getArgument(0);
          BookComment result = new BookComment();
          result.setId(bookCommentDto.getId());
          result.setText(bookCommentDto.getText());
          result.setCreated(bookCommentDto.getCreated());
          BookSimpleDto bookDto = bookCommentDto.getBook();
          if (bookDto != null) {
            Book book = new Book();
            book.setId(bookDto.getId());
            book.setTitle(bookDto.getTitle());
            result.setBook(book);
          }
          return result;
        })
        .when(bookCommentMapper)
        .toEntity(any());
  }

  //region create
  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeInsertNewBookComment() {
    BookComment exceptedBookComment = bookCommentTestDataComponent.getAliceMielophonJokeCommentForGirlFromTheEarthBook();
    Book exceptedBook = exceptedBookComment.getBook();
    assertThat(exceptedBook).isNotNull();
    assertThat(exceptedBook.getId()).isNotNull();
    Mockito
        .doAnswer(invocation -> {
          BookComment insertableBookComment = invocation.getArgument(0);
          insertableBookComment.setId(INSERTED_BOOK_COMMENT_ID_VALUE);
          return insertableBookComment;
        })
        .when(bookCommentRepositoryJpa)
        .save(exceptedBookComment);
    BookCommentDto exceptedBookCommentDto = bookCommentMapper.toDto(exceptedBookComment);
    assertThat(exceptedBookCommentDto.getId()).isNull();
    BookCommentDto actualBookCommentDto = bookCommentService.saveOrUpdate(exceptedBookCommentDto);
    assertThat(exceptedBookCommentDto.getId()).isNull();
    assertThat(actualBookCommentDto.getId())
        .isNotNull()
        .isEqualTo(INSERTED_BOOK_COMMENT_ID_VALUE);
    exceptedBookCommentDto.setId(INSERTED_BOOK_COMMENT_ID_VALUE);
    assertThat(actualBookCommentDto)
        .isNotNull()
        .isEqualTo(exceptedBookCommentDto);
    assertThat(actualBookCommentDto.getBook())
        .isNotNull()
        .isEqualTo(bookTestDataComponent.getGirlFromEarthBookSimpleDto());
  }

  @Test
  void shouldBeThrowDataIntegrityConstraintViolationForEmptyComment() {
    BookComment expectedBookComment = bookCommentTestDataComponent.getConstraintViolationBookComment();
    Mockito
        .doThrow(PersistenceException.class)
        .when(bookCommentRepositoryJpa)
        .save(expectedBookComment);
    assertThatCode(() -> bookCommentService.saveOrUpdate(bookCommentMapper.toDto(expectedBookComment)))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
  }
  //endregion

  //region read
  @Test
  void shouldBeGetAllInsertedBookComments() {
    List<BookComment> allExistingBookComments = bookCommentTestDataComponent.getAllBookComments();
    List<BookCommentDto> allExistingBookCommentDtos = allExistingBookComments
        .stream()
        .map(bookCommentMapper::toDto)
        .collect(Collectors.toList());
    Mockito
        .doReturn(allExistingBookComments)
        .when(bookCommentRepositoryJpa)
        .findAll();
    assertThat(bookCommentService.findAll())
        .isNotNull()
        .isNotEmpty()
        .isEqualTo(allExistingBookCommentDtos);
  }

  @Test
  void shouldBeGetAllInsertedBookCommentsByBookId() {
    List<BookComment> allExistingBookCommentsByBookId = bookCommentTestDataComponent.getAllBookCommentsByExistingBookId();
    List<BookCommentDto> allExistingBookCommentsByBookIds = allExistingBookCommentsByBookId
        .stream()
        .map(bookCommentMapper::toDto)
        .collect(Collectors.toList());
    Mockito
        .doReturn(Optional.of(bookCommentTestDataComponent.getExistingBookWithComments()))
        .when(bookRepository)
        .findById(EXISTING_BOOK_ID_VALUE);
    assertThat(bookCommentService.findAllByBookId(EXISTING_BOOK_ID_VALUE))
        .isNotNull()
        .isNotEmpty()
        .isEqualTo(allExistingBookCommentsByBookIds);
  }

  @Test
  void shouldBeThrowPersistenceExceptionInGetAllBookCommentsByBookId() {
    Mockito
        .doThrow(PersistenceException.class)
        .when(bookCommentRepositoryJpa)
        .findById(EXISTING_BOOK_ID_VALUE);
    assertThatCode(() -> bookCommentService.findAllByBookId(EXISTING_BOOK_ID_VALUE))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(NoSuchElementException.class);
  }

  @Test
  void shouldBeThrowPersistenceExceptionInGetAllBookComments() {
    Mockito
        .doThrow(PersistenceException.class)
        .when(bookCommentRepositoryJpa)
        .findAll();
    assertThatCode(() -> bookCommentService.findAll())
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateInsertedBookComment() {
    BookComment exceptedInsertedBookComment = bookCommentTestDataComponent.getAliceMielophonJokeCommentForGirlFromTheEarthBook();
    Book exceptedBook = exceptedInsertedBookComment.getBook();
    assertThat(exceptedBook).isNotNull();
    assertThat(exceptedBook.getId()).isNotNull();
    Mockito
        .doAnswer(invocation -> {
          BookComment insertableBookComment = invocation.getArgument(0);
          insertableBookComment.setId(INSERTED_BOOK_COMMENT_ID_VALUE);
          return insertableBookComment;
        })
        .when(bookCommentRepositoryJpa)
        .save(exceptedInsertedBookComment);
    BookCommentDto exceptedInsertedBookCommentDto = bookCommentMapper.toDto(exceptedInsertedBookComment);
    assertThat(exceptedInsertedBookCommentDto.getId()).isNull();
    BookCommentDto actualInsertedBookCommentDto = bookCommentService.saveOrUpdate(exceptedInsertedBookCommentDto);
    Long exceptedBookCommentId = actualInsertedBookCommentDto.getId();
    assertThat(exceptedBookCommentId)
        .isNotNull()
        .isEqualTo(INSERTED_BOOK_COMMENT_ID_VALUE);
    BookComment updatingBookComment = new BookComment();
    updatingBookComment.setId(exceptedBookCommentId);
    updatingBookComment.setText(RUSSIAN_COMMENT_TEXT);

    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          exceptedInsertedBookComment.setId(INSERTED_BOOK_COMMENT_ID_VALUE);
          exceptedInsertedBookComment.setText(((BookComment) args[0]).getText());
          return exceptedInsertedBookComment;
        })
        .when(bookCommentRepositoryJpa)
        .save(updatingBookComment);
    BookCommentDto updatingBookCommentDto = bookCommentMapper.toDto(updatingBookComment);
    BookCommentDto actualUpdatedDto = bookCommentService.saveOrUpdate(updatingBookCommentDto);
    assertThat(actualUpdatedDto.getId())
        .isNotNull()
        .isEqualTo(INSERTED_BOOK_COMMENT_ID_VALUE);
    Mockito.reset(bookCommentRepositoryJpa);
    Mockito
        .doReturn(Optional.of(exceptedInsertedBookComment))
        .when(bookCommentRepositoryJpa)
        .findById(INSERTED_BOOK_COMMENT_ID_VALUE);
    BookCommentDto exceptedUpdatedBookCommentDto = bookCommentMapper.toDto(exceptedInsertedBookComment);
    BookCommentDto actualUpdatedBookCommentDto = bookCommentService.findById(INSERTED_BOOK_COMMENT_ID_VALUE);
    assertThat(actualUpdatedBookCommentDto)
        .isNotNull()
        .isEqualTo(exceptedUpdatedBookCommentDto);
    assertThat(actualUpdatedBookCommentDto.getBook())
        .isNotNull()
        .isEqualTo(exceptedUpdatedBookCommentDto.getBook());
  }

  @Test
  void shouldBePersistenceExceptionInUpdateBookComment() {
    BookComment emptyBookComment = new BookComment();
    Mockito
        .doThrow(PersistenceException.class)
        .when(bookCommentRepositoryJpa)
        .save(emptyBookComment);
    assertThatCode(() -> bookCommentService.saveOrUpdate(bookCommentMapper.toDto(emptyBookComment)))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteInsertedBook() {
    BookComment existingBookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    Mockito
        .doReturn(Optional.of(existingBookComment))
        .when(bookCommentRepositoryJpa)
        .findById(EXISTING_BOOK_COMMENT_ID_VALUE);
    BookCommentDto actualBookComment = bookCommentService.findById(EXISTING_BOOK_COMMENT_ID_VALUE);
    BookCommentDto existingBookCommentDto = bookCommentMapper.toDto(existingBookComment);
    assertThat(actualBookComment)
        .isNotNull()
        .isEqualTo(existingBookCommentDto);
    Mockito
        .doNothing()
        .when(bookCommentRepositoryJpa)
        .deleteById(EXISTING_BOOK_COMMENT_ID_VALUE);
    bookCommentService.deleteById(EXISTING_BOOK_COMMENT_ID_VALUE);
    Mockito.reset(bookCommentRepositoryJpa);
    Mockito
        .doReturn(Optional.empty())
        .when(bookCommentRepositoryJpa)
        .findById(EXISTING_BOOK_COMMENT_ID_VALUE);
    assertThatCode(() -> bookCommentService.findById(EXISTING_BOOK_COMMENT_ID_VALUE))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(NoSuchElementException.class);
  }

  @Test
  void shouldBePersistenceExceptionInDeleteBookById() {
    Mockito
        .doThrow(PersistenceException.class)
        .when(bookCommentRepositoryJpa)
        .deleteById(MISSING_BOOK_COMMENT_ID_VALUE);
    assertThatCode(() -> bookCommentService.deleteById(MISSING_BOOK_COMMENT_ID_VALUE))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
  }

  //endregion
  @Test
  void shouldBeAllPublicMethodsMarkedTransactional() {
    TypeResolver resolver = new TypeResolver();
    ResolvedType resolvedType = resolver.resolve(BookCommentServiceImpl.class);
    List<RawMethod> methods = resolvedType.getMemberMethods();
    methods
        .stream()
        .filter(rawMethod -> rawMethod.isPublic() && !rawMethod
            .getName()
            .equals("findAllByBookId"))
        .forEach(method -> assertThat(Arrays
            .stream(method.getAnnotations())
            .map(Annotation::annotationType)
            .collect(Collectors.toList())
            .contains(Transactional.class)).isFalse());
  }

}