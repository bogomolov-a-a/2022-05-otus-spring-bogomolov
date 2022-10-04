package ru.otus.group202205.homework.spring05.dao;

import java.util.List;
import ru.otus.group202205.homework.spring05.model.Book;

public interface BookDao {

  List<Book> getAll();

  Book getById(Long id);

  void insert(Book book);

  void update(Book book);

  void deleteById(Long id);

}
