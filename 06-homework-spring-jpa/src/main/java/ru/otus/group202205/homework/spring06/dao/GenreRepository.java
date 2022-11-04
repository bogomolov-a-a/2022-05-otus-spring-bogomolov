package ru.otus.group202205.homework.spring06.dao;

import java.util.List;
import java.util.Optional;
import ru.otus.group202205.homework.spring06.model.Genre;

public interface GenreRepository {

  List<Genre> findAll();

  Optional<Genre> findById(Long id);

  Genre saveOrUpdate(Genre genre);

  void deleteById(Long id);

}
