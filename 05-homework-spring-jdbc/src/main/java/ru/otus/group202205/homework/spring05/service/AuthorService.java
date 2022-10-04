package ru.otus.group202205.homework.spring05.service;

import java.util.List;
import ru.otus.group202205.homework.spring05.model.Author;

public interface AuthorService {

  List<Author> getAll();

  Author getById(Long id);

  void insert(Author author);

  void update(Author author);

  void deleteById(Long id);

}
