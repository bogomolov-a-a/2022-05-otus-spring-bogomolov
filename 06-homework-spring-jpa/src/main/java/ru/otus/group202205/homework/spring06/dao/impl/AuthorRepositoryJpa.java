package ru.otus.group202205.homework.spring06.dao.impl;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring06.dao.AuthorRepository;
import ru.otus.group202205.homework.spring06.model.Author;

@Component
@RequiredArgsConstructor
public class AuthorRepositoryJpa implements AuthorRepository {

  @PersistenceContext
  private final EntityManager entityManager;

  @Override
  public List<Author> findAll() {
    return entityManager
        .createQuery("select a from Author a ",
            Author.class)
        .getResultList();
  }

  @Override
  public Optional<Author> findById(Long id) {
    return Optional.ofNullable(entityManager.find(Author.class,
        id));
  }

  @Override
  public Author saveOrUpdate(Author author) {
    if (author.getId() == null) {
      entityManager.persist(author);
      return author;
    }
    return entityManager.merge(author);
  }

  @Override
  public void deleteById(Long id) {
    Optional<Author> author = findById(id);
    author.ifPresent(entityManager::remove);
  }

}
