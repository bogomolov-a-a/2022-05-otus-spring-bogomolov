package ru.otus.group202205.homework.spring06.dao;

import java.util.List;
import java.util.Optional;
import ru.otus.group202205.homework.spring06.model.BookComment;

public interface BookCommentRepository {

  List<BookComment> findAll();

  Optional<BookComment> findById(Long id);

  BookComment saveOrUpdate(BookComment bookComment);

  void deleteById(Long id);

}
