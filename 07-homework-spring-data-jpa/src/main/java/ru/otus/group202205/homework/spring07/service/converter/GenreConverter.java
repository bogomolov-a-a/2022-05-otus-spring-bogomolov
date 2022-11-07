package ru.otus.group202205.homework.spring07.service.converter;

import java.util.List;
import ru.otus.group202205.homework.spring07.dto.GenreDto;

public interface GenreConverter {

  String convertGenre(GenreDto genre);

  String convertGenres(List<GenreDto> genres);

}
