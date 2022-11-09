package ru.otus.group202205.homework.spring08.shell;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.group202205.homework.spring08.dto.AuthorDto;
import ru.otus.group202205.homework.spring08.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring08.model.Author;
import ru.otus.group202205.homework.spring08.service.AuthorService;
import ru.otus.group202205.homework.spring08.service.converter.AuthorConverter;

@ShellComponent
@RequiredArgsConstructor
public class AuthorShellComponent {

  private final AuthorService authorService;
  private final AuthorConverter authorConverter;

  //region create
  @ShellMethod(key = {"create-author"}, value = "Try to insert author in db. In success case return created author id")
  public String createAuthor(@ShellOption(value = "--surname") String surname, @ShellOption(value = "--name") String name,
      @ShellOption(value = "--patronymic", defaultValue = Author.PATRONYMIC_DEFAULT_VALUE) String patronymic,
      @ShellOption(value = "--birth-year") Long birthYear, @ShellOption(value = "--death-year", defaultValue = ShellOption.NULL) Long deathYear) {
    AuthorDto author = new AuthorDto();
    author.setSurname(surname);
    author.setName(name);
    author.setPatronymic(patronymic);
    author.setBirthYear(birthYear);
    author.setDeathYear(deathYear);
    try {
      author = authorService.saveOrUpdate(author);
      return String.format("Author with id %s created!",
          author.getId());
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }
  //endregion

  //region read
  @ShellMethod(key = {"read-authors"}, value = "Read all authors in db")
  public String readAuthors() {
    try {
      List<AuthorDto> authors = authorService.findAll();
      return authorConverter.convertAuthors(authors);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"read-author"}, value = "Read author by id")
  public String readAuthor(@ShellOption(value = "--id") String id) {
    try {
      AuthorDto author = authorService.findById(id);
      return authorConverter.convertAuthor(author);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }
  //endregion

  //region update
  @ShellMethod(key = {"update-author"}, value = "Try to update author in db. In success case return updated author id")
  public String updateAuthor(@ShellOption(value = "--surname", defaultValue = ShellOption.NULL) String surname,
      @ShellOption(value = "--name", defaultValue = ShellOption.NULL) String name,
      @ShellOption(value = "--patronymic", defaultValue = ShellOption.NULL) String patronymic,
      @ShellOption(value = "--birth-year", defaultValue = ShellOption.NULL) Long birthYear,
      @ShellOption(value = "--death-year", defaultValue = ShellOption.NULL) Long deathYear, @ShellOption(value = "--id") String id) {
    AuthorDto author = new AuthorDto();
    author.setId(id);
    author.setSurname(surname);
    author.setName(name);
    author.setPatronymic(patronymic);
    author.setBirthYear(birthYear);
    author.setDeathYear(deathYear);
    try {
      author = authorService.saveOrUpdate(author);
      return String.format("Author with id %s updated!",
          author.getId());
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }
  //endregion

  //region delete
  @ShellMethod(key = {"delete-author"}, value = "Delete author by id")
  public String deleteAuthorById(@ShellOption(value = "--id") String id) {
    try {
      authorService.deleteById(id);
      return String.format("Author with id %s deleted!",
          id);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }
  //endregion

}
