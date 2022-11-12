package ru.otus.group202205.homework.spring08.service.converter;

import java.util.List;
import ru.otus.group202205.homework.spring08.dto.GenreDto;

public interface GenreConverter {

  String convertGenre(GenreDto genre);

  String convertGenres(List<GenreDto> genres);

}
