package ru.otus.group202205.homework.spring06.service;

import java.util.List;
import ru.otus.group202205.homework.spring06.dto.BookCommentDto;

public interface BookCommentService {

  List<BookCommentDto> findAll();

  BookCommentDto findById(Long id);

  List<BookCommentDto> findAllByBookId(Long id);

  BookCommentDto saveOrUpdate(BookCommentDto bookComment);

  void deleteById(Long id);

}
