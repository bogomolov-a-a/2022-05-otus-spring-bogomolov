package ru.otus.group202205.homework.spring07.shell;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import ru.otus.group202205.homework.spring07.dto.BookCommentDto;
import ru.otus.group202205.homework.spring07.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring07.model.Book;
import ru.otus.group202205.homework.spring07.model.BookComment;
import ru.otus.group202205.homework.spring07.service.BookCommentService;
import ru.otus.group202205.homework.spring07.service.converter.impl.BookCommentConverterImpl;
import ru.otus.group202205.homework.spring07.service.mapper.BookCommentMapper;
import ru.otus.group202205.homework.spring07.service.mapper.impl.BookCommentMapperImpl;
import ru.otus.group202205.homework.spring07.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring07.testdata.BookCommentTestDataComponent;
import ru.otus.group202205.homework.spring07.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring07.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {BookCommentShellComponent.class, BookCommentConverterImpl.class, BookCommentTestDataComponent.class, BookTestDataComponent.class,
    AuthorTestDataComponent.class, GenreTestDataComponent.class, TestShellConfig.class, BookCommentMapperImpl.class})
class BookCommentShellComponentTest {

  private static final Long INSERTED_BOOK_COMMENT_ID_VALUE = 4L;
  private static final Long EXISTING_BOOK_COMMENT_ID_VALUE = 1L;
  private static final Long EXISTING_BOOK_ID_VALUE = 1L;
  private static final String RUSSIAN_COMMENT_TEXT = "Классная_книга!";
  private static final Long MISSING_BOOK_COMMENT_ID_VALUE = 5L;
  private static final Long MISSING_BOOK_ID_VALUE = 6L;
  @Autowired
  private Shell shell;
  @Autowired
  private BookCommentTestDataComponent bookCommentTestDataComponent;
  @MockBean
  private BookCommentService bookCommentService;
  @Autowired
  private BookCommentMapper bookCommentMapper;

  //region create
  @Test
  void shouldBeInsertNewBookComment() {
    BookComment exceptedBookComment = bookCommentTestDataComponent.getAliceMielophonJokeCommentForGirlFromTheEarthBook();
    assertThat(exceptedBookComment.getId()).isNull();
    exceptedBookComment.setText("Nice_book!");
    Mockito
        .doAnswer(invocation -> {
          BookCommentDto insertableBookComment = invocation.getArgument(0);
          insertableBookComment.setId(INSERTED_BOOK_COMMENT_ID_VALUE);
          return insertableBookComment;
        })
        .when(bookCommentService)
        .saveOrUpdate(bookCommentMapper.toDto(exceptedBookComment));
    String successCreatedBookCommentMessage = (String) shell.evaluate(() -> String.format("create-book-comment --text %s --created %s --book-id %d",
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
  void shouldBeThrowPersistenceExceptionInInsertNewBookComment() {
    BookComment exceptedBookComment = bookCommentTestDataComponent.getConstraintViolationBookComment();
    assertThat(exceptedBookComment.getId()).isNull();
    Book missingBook = new Book();
    missingBook.setId(MISSING_BOOK_ID_VALUE);
    exceptedBookComment.setBook(missingBook);
    Mockito
        .doThrow(new LibraryGeneralException("Can't create book comment",
            new PersistenceException("Book not specified!")))
        .when(bookCommentService)
        .saveOrUpdate(bookCommentMapper.toDto(exceptedBookComment));
    String actualCreatedBookMessage = (String) shell.evaluate(() -> String.format("create-book-comment --book-id %d --created %s",
        exceptedBookComment
            .getBook()
            .getId(),
        exceptedBookComment.getCreated()));
    assertThat(actualCreatedBookMessage)
        .isNotNull()
        .isEqualTo("Can't create book comment Cause Book not specified!");
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
    String successAllBookReadCommandOutput = (String) shell.evaluate(() -> String.format("read-book-comment --id %d",
        EXISTING_BOOK_COMMENT_ID_VALUE));
    assertThat(successAllBookReadCommandOutput)
        .isNotNull()
        .isEqualTo("Comment id 1 book with id 1 at 2022-10-26T22:35:31." + System.lineSeparator() + " with text: 'Nice book!'");
  }

  @Test
  void shouldBeThrowPersistenceExceptionInGetBookCommentById() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't read book comment",
            new PersistenceException("Book comment not found with id " + MISSING_BOOK_COMMENT_ID_VALUE)))
        .when(bookCommentService)
        .findById(MISSING_BOOK_COMMENT_ID_VALUE);
    String errorReadBookMessage = (String) shell.evaluate(() -> String.format("read-book-comment --id %d",
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
  void shouldBePersistenceExceptionInGetAllInsertedBookCommentsByBookId() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't read book comments",
            new PersistenceException("Data corrupted")))
        .when(bookCommentService)
        .findAllByBookId(EXISTING_BOOK_ID_VALUE);
    String successAllBookReadCommandOutput = (String) shell.evaluate(() -> "read-comments-for-book --book-id " + EXISTING_BOOK_ID_VALUE);
    assertThat(successAllBookReadCommandOutput)
        .isNotNull()
        .isEqualTo("Can't read book comments Cause Data corrupted");
  }

  @Test
  void shouldBeThrowPersistenceExceptionInGetAllBookComments() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't read book comments",
            new PersistenceException("Data corrupted")))
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
    Mockito
        .doAnswer(invocation -> {
          existingBookComment.setText(((BookCommentDto) invocation.getArgument(0)).getText());
          return existingBookComment;
        })
        .when(bookCommentService)
        .saveOrUpdate(bookCommentMapper.toDto(existingBookComment));
    String successUpdateBookMessage = (String) shell.evaluate(() -> String.format("update-book-comment --id %d --text %s --created %s",
        EXISTING_BOOK_ID_VALUE,
        RUSSIAN_COMMENT_TEXT,
        existingBookComment.getCreated()));
    assertThat(successUpdateBookMessage)
        .isNotNull()
        .isEqualTo("Book comment with id " + EXISTING_BOOK_ID_VALUE + " updated!");
  }

  @Test
  void shouldBePersistenceExceptionInUpdateBookComment() {
    BookComment emptyNotExistingBookComment = new BookComment();
    emptyNotExistingBookComment.setId(MISSING_BOOK_COMMENT_ID_VALUE);
    Book existingBook = new Book();
    existingBook.setId(EXISTING_BOOK_ID_VALUE);
    emptyNotExistingBookComment.setBook(existingBook);
    BookCommentDto emptyNotExistingBookCommentDto = bookCommentMapper.toDto(emptyNotExistingBookComment);
    Mockito
        .doThrow(new LibraryGeneralException("Can't update book comment",
            new PersistenceException("Book comment not found with id " + MISSING_BOOK_COMMENT_ID_VALUE)))
        .when(bookCommentService)
        .saveOrUpdate(emptyNotExistingBookCommentDto);
    String errorUpdateBookMessage = (String) shell.evaluate(() -> String.format("update-book-comment --id %d --book-id %d --created %s",
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
    String successDeletedBookMessage = (String) shell.evaluate(() -> String.format("delete-book-comment --id %d",
        EXISTING_BOOK_COMMENT_ID_VALUE));
    assertThat(successDeletedBookMessage)
        .isNotNull()
        .isEqualTo("Book comment with id " + EXISTING_BOOK_COMMENT_ID_VALUE + " deleted!");
  }

  @Test
  void shouldBePersistenceExceptionInDeleteBookCommentById() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't delete book comment",
            new PersistenceException("Book comment not found with id " + MISSING_BOOK_COMMENT_ID_VALUE)))
        .when(bookCommentService)
        .deleteById(MISSING_BOOK_COMMENT_ID_VALUE);
    String deletedBookErrorMessage = (String) shell.evaluate(() -> String.format("delete-book-comment --id %d",
        MISSING_BOOK_COMMENT_ID_VALUE));
    assertThat(deletedBookErrorMessage)
        .isNotNull()
        .isEqualTo("Can't delete book comment Cause Book comment not found with id " + MISSING_BOOK_COMMENT_ID_VALUE);
  }
  //endregion
}