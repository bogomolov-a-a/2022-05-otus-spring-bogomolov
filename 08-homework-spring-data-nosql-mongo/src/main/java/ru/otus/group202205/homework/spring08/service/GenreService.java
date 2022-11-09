package ru.otus.group202205.homework.spring08.service;

import java.util.List;
import ru.otus.group202205.homework.spring08.dto.GenreDto;

public interface GenreService {

  List<GenreDto> findAll();

  GenreDto findById(String id);

  GenreDto saveOrUpdate(GenreDto genre);

  void deleteById(String id);

}
