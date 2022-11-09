package ru.otus.group202205.homework.spring08.shell;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.group202205.homework.spring08.dto.BookCommentDto;
import ru.otus.group202205.homework.spring08.dto.BookSimpleDto;
import ru.otus.group202205.homework.spring08.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring08.service.BookCommentService;
import ru.otus.group202205.homework.spring08.service.converter.BookCommentConverter;

@ShellComponent
@RequiredArgsConstructor
public class BookCommentShellComponent {

  private final BookCommentService bookCommentService;
  private final BookCommentConverter bookCommentConverter;

  @ShellMethod(key = {"create-book-comment"},
      value = "Try to insert book comment for book id in db. In success case return created book comment id('--created' must be specified in ISO format)")
  public String createBookComment(@ShellOption(value = "--text", defaultValue = ShellOption.NULL) String text,
      @ShellOption(value = "--created", defaultValue = ShellOption.NULL) String created, @ShellOption(value = "--book-id") String bookId) {
    BookCommentDto bookComment = new BookCommentDto();
    bookComment.setText(text);
    if (created != null) {
      bookComment.setCreated(LocalDateTime.parse(created,
          DateTimeFormatter.ISO_DATE_TIME));
    }
    BookSimpleDto book = new BookSimpleDto();
    book.setId(bookId);
    bookComment.setBook(book);
    try {
      bookComment = bookCommentService.saveOrUpdate(bookComment);
      return String.format("Book comment with id %s created!",
          bookComment.getId());
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"read-book-comment"}, value = "Read book comment with book id by id")
  public String readBookComment(@ShellOption(value = "--id") String id) {
    try {
      BookCommentDto bookComment = bookCommentService.findById(id);
      return bookCommentConverter.convertBookComment(bookComment);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"read-book-comments"}, value = "Read all book comments(with book ids).")
  public String readBookComments() {
    try {
      List<BookCommentDto> bookComments = bookCommentService.findAll();
      return bookCommentConverter.convertBookComments(bookComments);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"read-comments-for-book"}, value = "Read all book comments for specified book.")
  public String readBookCommentForSpecifiedBook(@ShellOption(value = "--book-id") String bookId) {
    try {
      List<BookCommentDto> bookComments = bookCommentService.findAllByBookId(bookId);
      return bookCommentConverter.convertBookComments(bookComments);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"update-book-comment"}, value = "Try to update book in db(one of params can't be not null). In success case return updated book id")
  public String updateBook(@ShellOption(value = "--text", defaultValue = ShellOption.NULL) String text,
      @ShellOption(value = "--book-id", defaultValue = ShellOption.NULL) String bookId,
      @ShellOption(value = "--created", defaultValue = ShellOption.NULL) String created, @ShellOption(value = "--id") String id) {
    BookCommentDto bookComment = new BookCommentDto();
    bookComment.setId(id);
    bookComment.setText(text);
    bookComment.setCreated(LocalDateTime.parse(created,
        DateTimeFormatter.ISO_DATE_TIME));
    BookSimpleDto book = new BookSimpleDto();
    book.setId(bookId);
    bookComment.setBook(book);
    try {
      bookCommentService.saveOrUpdate(bookComment);
      return String.format("Book comment with id %s updated!",
          id);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"delete-book-comment"}, value = "Delete book comment by id")
  public String deleteBookById(@ShellOption(value = "--id") String id) {
    try {
      bookCommentService.deleteById(id);
      return String.format("Book comment with id %s deleted!",
          id);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

}
