package ru.otus.group202205.homework.spring06.service;

import java.util.List;
import ru.otus.group202205.homework.spring06.dto.GenreDto;

public interface GenreService {

  List<GenreDto> findAll();

  GenreDto findById(Long id);

  GenreDto saveOrUpdate(GenreDto genre);

  void deleteById(Long id);

}
