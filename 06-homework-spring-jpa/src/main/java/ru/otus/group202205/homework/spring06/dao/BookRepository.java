package ru.otus.group202205.homework.spring06.dao;

import java.util.List;
import java.util.Optional;
import ru.otus.group202205.homework.spring06.model.Book;

public interface BookRepository {

  List<Book> findAll();

  Optional<Book> findById(Long id);

  Book saveOrUpdate(Book book);

  void deleteById(Long id);

}
