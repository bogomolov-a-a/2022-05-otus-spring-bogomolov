package ru.otus.group202205.homework.spring06.service;

import java.util.List;
import ru.otus.group202205.homework.spring06.dto.BookFullDto;

public interface BookService {

  List<BookFullDto> findAll();

  BookFullDto findById(Long id);

  BookFullDto saveOrUpdate(BookFullDto book);

  void deleteById(Long id);

}
