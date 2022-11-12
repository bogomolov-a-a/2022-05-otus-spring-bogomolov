package ru.otus.group202205.homework.spring08.service;

import java.util.List;
import ru.otus.group202205.homework.spring08.dto.BookCommentDto;

public interface BookCommentService {

  List<BookCommentDto> findAll();

  BookCommentDto findById(String id);

  List<BookCommentDto> findAllByBookId(String id);

  BookCommentDto saveOrUpdate(BookCommentDto bookComment);

  void deleteById(String id);

}
