package ru.otus.group202205.homework.spring05.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring05.dao.GenreDao;
import ru.otus.group202205.homework.spring05.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring05.model.Genre;
import ru.otus.group202205.homework.spring05.service.GenreService;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

  private final GenreDao genreDao;

  @Transactional(readOnly = true)
  @Override
  public List<Genre> getAll() {
    try {
      return genreDao.getAll();
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't get all genres",
          e);
    }
  }

  @Transactional(readOnly = true)
  @Override
  public Genre getById(Long id) {
    try {

      return genreDao.getById(id);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't get all genre by id " + id,
          e);
    }
  }

  @Transactional
  @Override
  public void insert(Genre genre) {
    try {
      genreDao.insert(genre);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't insert genre",
          e);
    }
  }

  @Transactional
  @Override
  public void update(Genre genre) {
    try {
      genreDao.update(genre);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't update genre ",
          e);
    }
  }

  @Transactional
  @Override
  public void deleteById(Long id) {
    try {
      genreDao.deleteById(id);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't update genre ",
          e);
    }
  }

}
