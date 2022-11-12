package ru.otus.group202205.homework.spring08.shell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

import com.mongodb.MongoException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.shell.Shell;
import ru.otus.group202205.homework.spring08.dto.BookCommentDto;
import ru.otus.group202205.homework.spring08.dto.BookSimpleDto;
import ru.otus.group202205.homework.spring08.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring08.model.Book;
import ru.otus.group202205.homework.spring08.model.BookComment;
import ru.otus.group202205.homework.spring08.service.BookCommentService;
import ru.otus.group202205.homework.spring08.service.converter.BookCommentConverter;
import ru.otus.group202205.homework.spring08.service.mapper.BookCommentMapper;
import ru.otus.group202205.homework.spring08.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring08.testdata.BookCommentTestDataComponent;
import ru.otus.group202205.homework.spring08.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring08.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {BookCommentShellComponent.class, BookCommentTestDataComponent.class, BookTestDataComponent.class, AuthorTestDataComponent.class,
    GenreTestDataComponent.class, TestShellConfig.class})
class BookCommentShellComponentTest {

  private static final String INSERTED_BOOK_COMMENT_ID_VALUE = "4";
  private static final String EXISTING_BOOK_COMMENT_ID_VALUE = "1";
  private static final String EXISTING_BOOK_ID_VALUE = "1";
  private static final String RUSSIAN_COMMENT_TEXT = "Классная_книга!";

  private static final String MISSING_BOOK_COMMENT_ID_VALUE = "5";
  @Autowired
  private Shell shell;
  @Autowired
  private BookCommentTestDataComponent bookCommentTestDataComponent;
  @MockBean
  private BookCommentService bookCommentService;
  @MockBean
  private BookCommentMapper bookCommentMapper;
  @MockBean
  private BookCommentConverter bookCommentConverter;

  @BeforeEach
  void init() {
    Mockito.reset(bookCommentMapper);
    Mockito.reset(bookCommentConverter);
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
            result.setBook(book);
          }
          return result;
        })
        .when(bookCommentMapper)
        .toEntity(any());
    Mockito
        .doAnswer(invocation -> {
          BookCommentDto bookComment = invocation.getArgument(0);
          return String.format("Comment id %s book with id %s at %s.%s with text: '%s'",
              bookComment.getId(),
              bookComment
                  .getBook()
                  .getId(),
              bookComment
                  .getCreated()
                  .format(DateTimeFormatter.ISO_DATE_TIME),
              System.lineSeparator(),
              bookComment.getText());
        })
        .when(bookCommentConverter)
        .convertBookComment(any());
    Mockito
        .doAnswer(invocation -> {
          List<BookCommentDto> bookComments = invocation.getArgument(0);
          StringBuilder result = new StringBuilder("Comment list:").append(System.lineSeparator());
          bookComments.forEach(comment -> result
              .append(bookCommentConverter.convertBookComment(comment))
              .append(System.lineSeparator()));
          return result.toString();
        })
        .when(bookCommentConverter)
        .convertBookComments(anyList());
  }

  //region create
  @Test
  void shouldBeInsertNewBookComment() {
    BookComment exceptedBookComment = bookCommentTestDataComponent.getAliceMielophonJokeCommentForGirlFromTheEarthBook();
    assertThat(exceptedBookComment.getId()).isNull();
    exceptedBookComment.setText("Alice!_I_have_a_mielophone!");
    BookCommentDto exceptedBookCommentDto = bookCommentMapper.toDto(exceptedBookComment);
    Mockito
        .doAnswer(invocation -> {
          BookCommentDto insertableBookComment = invocation.getArgument(0);
          insertableBookComment.setId(INSERTED_BOOK_COMMENT_ID_VALUE);
          return insertableBookComment;
        })
        .when(bookCommentService)
        .saveOrUpdate(exceptedBookCommentDto);
    String successCreatedBookCommentMessage = (String) shell.evaluate(() -> String.format("create-book-comment --text %s --created %s --book-id %s",
        exceptedBookComment.getText(),
        exceptedBookComment
            .getCreated()
            .format(DateTimeFormatter.ISO_DATE_TIME),
        exceptedBookComment
            .getBook()
            .getId()));
    assertThat(successCreatedBookCommentMessage)
        .isNotNull()
        .isEqualTo("Book comment with id " + INSERTED_BOOK_COMMENT_ID_VALUE + " created!");
  }

  @Test
  void shouldBeThrowDuplicateKeyExceptionInInsertNewBookComment() {
    BookComment exceptedBookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    exceptedBookComment.setId(null);
    exceptedBookComment.setText("Nice_book!");
    BookCommentDto exceptedBookCommentDto = bookCommentMapper.toDto(exceptedBookComment);
    Mockito
        .doThrow(new LibraryGeneralException("Can't create book comment",
            new DuplicateKeyException("unique key violation!")))
        .when(bookCommentService)
        .saveOrUpdate(exceptedBookCommentDto);
    String actualCreatedBookMessage = (String) shell.evaluate(() -> String.format("create-book-comment --book-id %s --created %s --text %s",
        exceptedBookComment
            .getBook()
            .getId(),
        exceptedBookComment.getCreated(),
        exceptedBookComment.getText()));
    assertThat(actualCreatedBookMessage)
        .isNotNull()
        .isEqualTo("Can't create book comment Cause unique key violation!");
  }
  //endregion

  //region read
  @Test
  void shouldBeGetBookCommentById() {
    BookComment exceptedBookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    assertThat(exceptedBookComment.getId())
        .isNotNull()
        .isEqualTo(EXISTING_BOOK_COMMENT_ID_VALUE);
    Mockito
        .doReturn(bookCommentMapper.toDto(exceptedBookComment))
        .when(bookCommentService)
        .findById(EXISTING_BOOK_COMMENT_ID_VALUE);
    String successAllBookReadCommandOutput = (String) shell.evaluate(() -> String.format("read-book-comment --id %s",
        EXISTING_BOOK_COMMENT_ID_VALUE));
    assertThat(successAllBookReadCommandOutput)
        .isNotNull()
        .isEqualTo("Comment id 1 book with id 1 at 2022-10-26T22:35:31." + System.lineSeparator() + " with text: 'Nice book!'");
  }

  @Test
  void shouldBeThrowMongoExceptionInGetBookCommentById() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't read book comment",
            new MongoException("Book comment not found with id " + MISSING_BOOK_COMMENT_ID_VALUE)))
        .when(bookCommentService)
        .findById(MISSING_BOOK_COMMENT_ID_VALUE);
    String errorReadBookMessage = (String) shell.evaluate(() -> String.format("read-book-comment --id %s",
        MISSING_BOOK_COMMENT_ID_VALUE));
    assertThat(errorReadBookMessage)
        .isNotNull()
        .isEqualTo("Can't read book comment Cause Book comment not found with id " + MISSING_BOOK_COMMENT_ID_VALUE);
  }

  @Test
  void shouldBeGetAllInsertedBookComments() {
    List<BookCommentDto> bookCommentDtos = bookCommentTestDataComponent
        .getAllBookComments()
        .stream()
        .map(bookCommentMapper::toDto)
        .collect(Collectors.toList());
    Mockito
        .doReturn(bookCommentDtos)
        .when(bookCommentService)
        .findAll();
    String successAllBookReadCommandOutput = (String) shell.evaluate(() -> "read-book-comments");
    assertThat(successAllBookReadCommandOutput)
        .isNotNull()
        .isEqualTo("Comment list:" + System.lineSeparator() + "Comment id 3 book with id 2 at 2022-10-26T22:35:32." + System.lineSeparator()
            + " with text: 'Complicated book!'" + System.lineSeparator() + "Comment id 2 book with id 1 at 2022-10-26T22:35:32." + System.lineSeparator()
            + " with text: 'I like this author!'" + System.lineSeparator() + "Comment id 1 book with id 1 at 2022-10-26T22:35:31." + System.lineSeparator()
            + " with text: 'Nice book!'" + System.lineSeparator());

  }

  @Test
  void shouldBeGetAllInsertedBookCommentsByBookId() {
    List<BookCommentDto> bookCommentDtos = bookCommentTestDataComponent
        .getAllBookCommentsByExistingBookId()
        .stream()
        .map(bookCommentMapper::toDto)
        .collect(Collectors.toList());
    Mockito
        .doReturn(bookCommentDtos)
        .when(bookCommentService)
        .findAllByBookId(EXISTING_BOOK_ID_VALUE);
    String successAllBookReadCommandOutput = (String) shell.evaluate(() -> "read-comments-for-book --book-id " + EXISTING_BOOK_ID_VALUE);
    assertThat(successAllBookReadCommandOutput)
        .isNotNull()
        .isEqualTo("Comment list:" + System.lineSeparator() + "Comment id 2 book with id 1 at 2022-10-26T22:35:32." + System.lineSeparator()
            + " with text: 'I like this author!'" + System.lineSeparator() + "Comment id 1 book with id 1 at 2022-10-26T22:35:31." + System.lineSeparator()
            + " with text: 'Nice book!'" + System.lineSeparator());
  }

  @Test
  void shouldBeMongoExceptionInGetAllInsertedBookCommentsByBookId() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't read book comments",
            new MongoException("Data corrupted")))
        .when(bookCommentService)
        .findAllByBookId(EXISTING_BOOK_ID_VALUE);
    String successAllBookReadCommandOutput = (String) shell.evaluate(() -> "read-comments-for-book --book-id " + EXISTING_BOOK_ID_VALUE);
    assertThat(successAllBookReadCommandOutput)
        .isNotNull()
        .isEqualTo("Can't read book comments Cause Data corrupted");
  }

  @Test
  void shouldBeThrowMongoExceptionInGetAllBookComments() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't read book comments",
            new MongoException("Data corrupted")))
        .when(bookCommentService)
        .findAll();
    String errorReadBooksMessage = (String) shell.evaluate(() -> "read-book-comments");
    assertThat(errorReadBooksMessage)
        .isNotNull()
        .isEqualTo("Can't read book comments Cause Data corrupted");
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateInsertedBookCommentText() {
    BookComment existingBookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    BookCommentDto existingBookCommentDto = bookCommentMapper.toDto(existingBookComment);
    Mockito
        .doAnswer(invocation -> {
          existingBookComment.setText(((BookCommentDto) invocation.getArgument(0)).getText());
          return existingBookComment;
        })
        .when(bookCommentService)
        .saveOrUpdate(existingBookCommentDto);
    String successUpdateBookMessage = (String) shell.evaluate(() -> String.format("update-book-comment --id %s --text %s --created %s",
        EXISTING_BOOK_ID_VALUE,
        RUSSIAN_COMMENT_TEXT,
        existingBookComment.getCreated()));
    assertThat(successUpdateBookMessage)
        .isNotNull()
        .isEqualTo("Book comment with id " + EXISTING_BOOK_ID_VALUE + " updated!");
  }

  @Test
  void shouldBeMongoExceptionInUpdateBookComment() {
    BookComment emptyNotExistingBookComment = new BookComment();
    emptyNotExistingBookComment.setId(MISSING_BOOK_COMMENT_ID_VALUE);
    Book existingBook = new Book();
    existingBook.setId(EXISTING_BOOK_ID_VALUE);
    emptyNotExistingBookComment.setBook(existingBook);
    BookCommentDto emptyNotExistingBookCommentDto = bookCommentMapper.toDto(emptyNotExistingBookComment);
    Mockito
        .doThrow(new LibraryGeneralException("Can't update book comment",
            new MongoException("Book comment not found with id " + MISSING_BOOK_COMMENT_ID_VALUE)))
        .when(bookCommentService)
        .saveOrUpdate(emptyNotExistingBookCommentDto);
    String errorUpdateBookMessage = (String) shell.evaluate(() -> String.format("update-book-comment --id %s --book-id %s --created %s",
        MISSING_BOOK_COMMENT_ID_VALUE,
        EXISTING_BOOK_ID_VALUE,
        emptyNotExistingBookComment
            .getCreated()
            .format(DateTimeFormatter.ISO_DATE_TIME)));
    assertThat(errorUpdateBookMessage)
        .isNotNull()
        .isEqualTo("Can't update book comment Cause Book comment not found with id " + MISSING_BOOK_COMMENT_ID_VALUE);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteInsertedBookComment() {
    Mockito
        .doNothing()
        .when(bookCommentService)
        .deleteById(EXISTING_BOOK_COMMENT_ID_VALUE);
    String successDeletedBookMessage = (String) shell.evaluate(() -> String.format("delete-book-comment --id %s",
        EXISTING_BOOK_COMMENT_ID_VALUE));
    assertThat(successDeletedBookMessage)
        .isNotNull()
        .isEqualTo("Book comment with id " + EXISTING_BOOK_COMMENT_ID_VALUE + " deleted!");
  }

  @Test
  void shouldBeMongoExceptionInDeleteBookCommentById() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't delete book comment",
            new MongoException("Book comment not found with id " + MISSING_BOOK_COMMENT_ID_VALUE)))
        .when(bookCommentService)
        .deleteById(MISSING_BOOK_COMMENT_ID_VALUE);
    String deletedBookErrorMessage = (String) shell.evaluate(() -> String.format("delete-book-comment --id %s",
        MISSING_BOOK_COMMENT_ID_VALUE));
    assertThat(deletedBookErrorMessage)
        .isNotNull()
        .isEqualTo("Can't delete book comment Cause Book comment not found with id " + MISSING_BOOK_COMMENT_ID_VALUE);
  }
  //endregion
}