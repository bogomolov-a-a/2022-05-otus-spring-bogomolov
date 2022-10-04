package ru.otus.group202205.homework.spring05.service;

import java.util.List;
import ru.otus.group202205.homework.spring05.model.Genre;

public interface GenreConverter {

  String convertGenre(Genre genre);

  String convertGenres(List<Genre> genres);

}
