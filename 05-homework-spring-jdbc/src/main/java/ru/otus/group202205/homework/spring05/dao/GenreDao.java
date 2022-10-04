package ru.otus.group202205.homework.spring05.dao;

import java.util.List;
import ru.otus.group202205.homework.spring05.model.Genre;

public interface GenreDao {

  List<Genre> getAll();

  Genre getById(Long id);

  void insert(Genre genre);

  void update(Genre genre);

  void deleteById(Long id);

}
