package ru.otus.group202205.homework.spring05.shell;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.group202205.homework.spring05.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring05.model.Genre;
import ru.otus.group202205.homework.spring05.service.GenreConverter;
import ru.otus.group202205.homework.spring05.service.GenreService;

@ShellComponent
@RequiredArgsConstructor
public class GenreShellComponent {

  private final GenreService genreService;
  private final GenreConverter genreConverter;

  @ShellMethod(key = {"create-genre"}, value = "Try to insert genre in db. In success case return created genre id")
  public String createGenre(@ShellOption(value = "--name") String name) {
    Genre genre = new Genre();
    genre.setName(name);
    try {
      genreService.insert(genre);
      return String.format("Genre with id %d created!",
          genre.getId());
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"read-genre"}, value = "Read Genre by id")
  public String readGenre(@ShellOption(value = "--id") Long id) {
    try {
      Genre genre = genreService.getById(id);
      return genreConverter.convertGenre(genre);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"read-genres"}, value = "Read all genres in db")
  public String readGenres() {
    try {
      List<Genre> genres = genreService.getAll();
      return genreConverter.convertGenres(genres);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"update-genre"}, value = "Try to update genre in db. In success case return updated genre id")
  public String updateGenre(@ShellOption(value = "--name") String name, @ShellOption(value = "--id") Long id) {
    Genre genre = new Genre();
    genre.setId(id);
    genre.setName(name);
    try {
      genreService.update(genre);
      return String.format("Genre with id %d updated!",
          id);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"delete-genre"}, value = "Delete genre by id")
  public String deleteGenresById(@ShellOption(value = "--id") Long id) {
    try {
      genreService.deleteById(id);
      return String.format("Genre with id %d deleted!",
          id);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

}
