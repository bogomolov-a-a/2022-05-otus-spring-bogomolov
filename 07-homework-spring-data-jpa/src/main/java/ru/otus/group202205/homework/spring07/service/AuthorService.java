package ru.otus.group202205.homework.spring07.service;

import java.util.List;
import ru.otus.group202205.homework.spring07.dto.AuthorDto;

public interface AuthorService {

  List<AuthorDto> findAll();

  AuthorDto findById(Long id);

  AuthorDto saveOrUpdate(AuthorDto author);

  void deleteById(Long id);

}
