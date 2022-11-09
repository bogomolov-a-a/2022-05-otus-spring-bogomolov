package ru.otus.group202205.homework.spring08.service;

import java.util.List;
import ru.otus.group202205.homework.spring08.dto.AuthorDto;

public interface AuthorService {

  List<AuthorDto> findAll();

  AuthorDto findById(String id);

  AuthorDto saveOrUpdate(AuthorDto author);

  void deleteById(String id);

}
