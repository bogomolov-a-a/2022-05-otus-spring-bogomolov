package ru.otus.group202205.homework.spring05.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring05.model.Genre;
import ru.otus.group202205.homework.spring05.service.GenreConverter;

@Service
public class GenreConverterImpl implements GenreConverter {

  @Override
  public String convertGenre(Genre genre) {
    return String.format("Genre id: %d%sname: %s%s",
        genre.getId(),
        System.lineSeparator(),
        genre.getName(),
        System.lineSeparator());
  }

  @Override
  public String convertGenres(List<Genre> genres) {
    StringBuilder result = new StringBuilder("Genre list").append(System.lineSeparator());
    genres.forEach(genre -> result.append(convertGenre(genre)));
    return result.toString();
  }

}
