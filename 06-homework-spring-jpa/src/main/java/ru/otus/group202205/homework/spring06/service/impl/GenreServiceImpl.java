package ru.otus.group202205.homework.spring06.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring06.dao.GenreRepository;
import ru.otus.group202205.homework.spring06.dto.GenreDto;
import ru.otus.group202205.homework.spring06.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring06.service.GenreService;
import ru.otus.group202205.homework.spring06.service.mapper.GenreMapper;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

  private final GenreRepository genreRepository;

  private final GenreMapper genreMapper;

  @Transactional(readOnly = true)
  @Override
  public List<GenreDto> findAll() {
    try {
      return genreRepository
          .findAll()
          .stream()
          .map(genreMapper::toDto)
          .collect(Collectors.toList());
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't get all genres",
          e);
    }
  }

  @Transactional(readOnly = true)
  @Override
  public GenreDto findById(Long id) {
    try {
      return genreMapper.toDto(genreRepository
          .findById(id)
          .orElseThrow());
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't get genre by id " + id,
          e);
    }
  }

  @Transactional
  @Override
  public GenreDto saveOrUpdate(GenreDto genre) {
    try {
      return genreMapper.toDto(genreRepository.saveOrUpdate(genreMapper.toEntity(genre)));
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't insert or update genre",
          e);
    }
  }


  @Transactional
  @Override
  public void deleteById(Long id) {
    try {
      genreRepository.deleteById(id);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't update genre ",
          e);
    }
  }

}
