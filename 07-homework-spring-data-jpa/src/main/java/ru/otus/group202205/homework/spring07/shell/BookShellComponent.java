package ru.otus.group202205.homework.spring07.shell;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.group202205.homework.spring07.dto.AuthorDto;
import ru.otus.group202205.homework.spring07.dto.BookFullDto;
import ru.otus.group202205.homework.spring07.dto.GenreDto;
import ru.otus.group202205.homework.spring07.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring07.service.BookService;
import ru.otus.group202205.homework.spring07.service.converter.BookConverter;

@ShellComponent
@RequiredArgsConstructor
public class BookShellComponent {

  private final BookService bookService;
  private final BookConverter bookConverter;

  @ShellMethod(key = {"create-book"}, value = "Try to insert book with existing author and genre ids in db. In success case return created book id")
  public String createBook(@ShellOption(value = "--title") String title, @ShellOption(value = "--isbn") String isbn,
      @ShellOption(value = "--author-id") Long authorId, @ShellOption(value = "--genre-id") Long genreId) {
    BookFullDto book = new BookFullDto();
    book.setTitle(title);
    book.setIsbn(isbn);
    AuthorDto author = new AuthorDto();
    author.setId(authorId);
    book.setAuthor(author);
    GenreDto genre = new GenreDto();
    genre.setId(genreId);
    book.setGenre(genre);
    try {
      book = bookService.saveOrUpdate(book);
      return String.format("Book with id %d created!",
          book.getId());
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"read-book"}, value = "Read book with author and genre by id")
  public String readBook(@ShellOption(value = "--id") Long id) {
    try {
      BookFullDto book = bookService.findById(id);
      return bookConverter.convertBook(book);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"read-books"}, value = "Read all books with authors and genres in db")
  public String readBooks() {
    try {
      List<BookFullDto> books = bookService.findAll();
      return bookConverter.convertBooks(books);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"update-book"}, value = "Try to update book in db(one of params can't be not null). In success case return updated book id")
  public String updateBook(@ShellOption(value = "--title", defaultValue = ShellOption.NULL) String title,
      @ShellOption(value = "--isbn", defaultValue = ShellOption.NULL) String isbn,
      @ShellOption(value = "--author-id", defaultValue = ShellOption.NULL) Long authorId,
      @ShellOption(value = "--genre-id", defaultValue = ShellOption.NULL) Long genreId, @ShellOption(value = "--id") Long id) {
    BookFullDto book = new BookFullDto();
    book.setId(id);
    book.setTitle(title);
    book.setIsbn(isbn);
    AuthorDto author = new AuthorDto();
    author.setId(authorId);
    book.setAuthor(author);
    GenreDto genre = new GenreDto();
    genre.setId(genreId);
    book.setGenre(genre);
    try {
      bookService.saveOrUpdate(book);
      return String.format("Book with id %d updated!",
          id);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"delete-book"}, value = "Delete book by id")
  public String deleteBookById(@ShellOption(value = "--id") Long id) {
    try {
      bookService.deleteById(id);
      return String.format("Book with id %d deleted!",
          id);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

}
