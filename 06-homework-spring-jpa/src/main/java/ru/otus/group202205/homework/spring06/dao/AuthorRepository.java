package ru.otus.group202205.homework.spring06.dao;

import java.util.List;
import java.util.Optional;
import ru.otus.group202205.homework.spring06.model.Author;

public interface AuthorRepository {

  List<Author> findAll();

  Optional<Author> findById(Long id);

  Author saveOrUpdate(Author author);

  void deleteById(Long id);

}
