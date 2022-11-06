package ru.otus.group202205.homework.spring06.dao.impl;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring06.dao.GenreRepository;
import ru.otus.group202205.homework.spring06.model.Genre;

@Component
@RequiredArgsConstructor
public class GenreRepositoryJpa implements GenreRepository {

  @PersistenceContext
  private final EntityManager entityManager;

  @Override
  public List<Genre> findAll() {
    return entityManager
        .createQuery("select g from Genre g",
            Genre.class)
        .getResultList();
  }

  @Override
  public Optional<Genre> findById(Long id) {
    return Optional.ofNullable(entityManager.find(Genre.class,
        id));
  }

  @Override
  public Genre saveOrUpdate(Genre genre) {
    if (genre.getId() == null) {
      entityManager.persist(genre);
      return genre;
    }
    return entityManager.merge(genre);
  }


  @Override
  public void deleteById(Long id) {
    Optional<Genre> genre = findById(id);
    genre.ifPresent(entityManager::remove);
  }

}
