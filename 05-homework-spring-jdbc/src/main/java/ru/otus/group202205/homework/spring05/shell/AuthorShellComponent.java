
package ru.otus.group202205.homework.spring05.shell;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.group202205.homework.spring05.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring05.model.Author;
import ru.otus.group202205.homework.spring05.service.AuthorConverter;
import ru.otus.group202205.homework.spring05.service.AuthorService;

@ShellComponent
@RequiredArgsConstructor
public class AuthorShellComponent {

  private final AuthorService authorService;
  private final AuthorConverter authorConverter;

  @ShellMethod(key = {"create-author"}, value = "Try to insert author in db. In success case return created author id")
  public String createAuthor(@ShellOption(value = "--surname") String surname, @ShellOption(value = "--name") String name,
      @ShellOption(value = "--patronymic", defaultValue = Author.PATRONYMIC_DEFAULT_VALUE) String patronymic,
      @ShellOption(value = "--birth-year") Long birthYear, @ShellOption(value = "--death-year", defaultValue = ShellOption.NULL) Long deathYear) {
    Author author = new Author();
    author.setSurname(surname);
    author.setName(name);
    author.setPatronymic(patronymic);
    author.setBirthYear(birthYear);
    author.setDeathYear(deathYear);
    try {
      authorService.insert(author);
      return String.format("Author with id %d created!",
          author.getId());
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"read-author"}, value = "Read author by id")
  public String readAuthor(@ShellOption(value = "--id") Long id) {
    try {
      Author author = authorService.getById(id);
      return authorConverter.convertAuthor(author);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"read-authors"}, value = "Read all authors in db")
  public String readAuthors() {
    try {
      List<Author> authors = authorService.getAll();
      return authorConverter.convertAuthors(authors);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"update-author"}, value = "Try to update author in db. In success case return updated author id")
  public String updateAuthor(@ShellOption(value = "--surname", defaultValue = ShellOption.NULL) String surname,
      @ShellOption(value = "--name", defaultValue = ShellOption.NULL) String name,
      @ShellOption(value = "--patronymic", defaultValue = ShellOption.NULL) String patronymic,
      @ShellOption(value = "--birth-year", defaultValue = ShellOption.NULL) Long birthYear,
      @ShellOption(value = "--death-year", defaultValue = ShellOption.NULL) Long deathYear, @ShellOption(value = "--id") Long id) {
    Author author = new Author();
    author.setId(id);
    author.setSurname(surname);
    author.setName(name);
    author.setPatronymic(patronymic);
    author.setBirthYear(birthYear);
    author.setDeathYear(deathYear);
    try {
      authorService.update(author);
      return String.format("Author with id %d updated!",
          id);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"delete-author"}, value = "Delete author by id")
  public String deleteAuthorById(@ShellOption(value = "--id") Long id) {
    try {
      authorService.deleteById(id);
      return String.format("Author with id %d deleted!",
          id);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

}
