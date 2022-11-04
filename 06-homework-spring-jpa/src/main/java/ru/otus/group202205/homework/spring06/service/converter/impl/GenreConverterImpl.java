package ru.otus.group202205.homework.spring06.service.converter.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring06.dto.GenreDto;
import ru.otus.group202205.homework.spring06.service.converter.GenreConverter;

@Service
public class GenreConverterImpl implements GenreConverter {

  @Override
  public String convertGenre(GenreDto genre) {
    return String.format("Genre id: %d%sname: %s%s",
        genre.getId(),
        System.lineSeparator(),
        genre.getName(),
        System.lineSeparator());
  }

  @Override
  public String convertGenres(List<GenreDto> genres) {
    StringBuilder result = new StringBuilder("Genre list").append(System.lineSeparator());
    genres.forEach(genre -> result.append(convertGenre(genre)));
    return result.toString();
  }

}
