package ru.otus.group202205.homework.spring05.dao;

import java.util.List;
import ru.otus.group202205.homework.spring05.model.Author;

public interface AuthorDao {

  List<Author> getAll();

  Author getById(Long id);

  void insert(Author author);

  void update(Author author);

  void deleteById(Long id);

}
