package ru.otus.group202205.homework.spring05.service.impl;

import static ru.otus.group202205.homework.spring05.model.Author.PATRONYMIC_DEFAULT_VALUE;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring05.dao.AuthorDao;
import ru.otus.group202205.homework.spring05.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring05.model.Author;
import ru.otus.group202205.homework.spring05.service.AuthorService;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

  private final AuthorDao authorDao;

  @Transactional(readOnly = true)
  @Override
  public List<Author> getAll() {
    try {
      return authorDao.getAll();
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't get author list",
          e);
    }
  }

  @Transactional(readOnly = true)
  @Override
  public Author getById(Long id) {
    try {
      return authorDao.getById(id);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't get author by id " + id,
          e);
    }
  }

  @Transactional
  @Override
  public void insert(Author author) {
    try {
      authorDao.insert(author);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't insert author",
          e);
    }
  }

  @Transactional
  @Override
  public void update(Author author) {
    try {
      mergeAuthors(author);
      authorDao.update(author);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't update author",
          e);
    }
  }

  @Transactional
  @Override
  public void deleteById(Long id) {
    try {
      authorDao.deleteById(id);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't delete author",
          e);
    }
  }

  private void mergeAuthors(Author author) {
    Author existingAuthor = getById(author.getId());
    if (author.getSurname() == null) {
      author.setSurname(existingAuthor.getSurname());
    }
    if (author.getName() == null) {
      author.setName(existingAuthor.getName());
    }
    if (author
        .getPatronymic()
        .equals(PATRONYMIC_DEFAULT_VALUE)) {
      author.setPatronymic(existingAuthor.getPatronymic());
    }
    if (author.getBirthYear() == null) {
      author.setBirthYear(existingAuthor.getBirthYear());
    }
    if (author.getDeathYear() == null) {
      author.setDeathYear(existingAuthor.getDeathYear());
    }
  }

}
