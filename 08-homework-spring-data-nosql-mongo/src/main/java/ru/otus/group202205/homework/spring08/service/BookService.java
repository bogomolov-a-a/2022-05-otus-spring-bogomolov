package ru.otus.group202205.homework.spring08.service;

import java.util.List;
import ru.otus.group202205.homework.spring08.dto.BookFullDto;

public interface BookService {

  List<BookFullDto> findAll();

  BookFullDto findById(String id);

  BookFullDto saveOrUpdate(BookFullDto book);

  void deleteById(String id);

}
