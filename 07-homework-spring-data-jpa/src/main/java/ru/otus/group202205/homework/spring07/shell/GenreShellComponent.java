package ru.otus.group202205.homework.spring07.shell;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.group202205.homework.spring07.dto.GenreDto;
import ru.otus.group202205.homework.spring07.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring07.service.GenreService;
import ru.otus.group202205.homework.spring07.service.converter.GenreConverter;

@ShellComponent
@RequiredArgsConstructor
public class GenreShellComponent {

  private final GenreService genreService;
  private final GenreConverter genreConverter;

  @ShellMethod(key = {"create-genre"}, value = "Try to insert genre in db. In success case return created genre id")
  public String createGenre(@ShellOption(value = "--name") String name) {
    GenreDto genre = new GenreDto();
    genre.setName(name);
    try {
      genre = genreService.saveOrUpdate(genre);
      return String.format("Genre with id %d created!",
          genre.getId());
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"read-genre"}, value = "Read genre by id")
  public String readGenre(@ShellOption(value = "--id") Long id) {
    try {
      GenreDto genre = genreService.findById(id);
      return genreConverter.convertGenre(genre);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"read-genres"}, value = "Read all genres in db")
  public String readGenres() {
    try {
      List<GenreDto> genres = genreService.findAll();
      return genreConverter.convertGenres(genres);
    } catch (LibraryGeneralException e) {
      return e.toString();
    }
  }

  @ShellMethod(key = {"update-genre"}, value = "Try to update genre in db. In success case return updated genre id")
  public String updateGenre(@ShellOption(value = "--name") String name, @ShellOption(value = "--id") Long id) {
    GenreDto genre = new GenreDto();
    genre.setId(id);
    genre.setName(name);
    try {
      genreService.saveOrUpdate(genre);
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
