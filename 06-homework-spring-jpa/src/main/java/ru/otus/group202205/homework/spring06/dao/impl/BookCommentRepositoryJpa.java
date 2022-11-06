package ru.otus.group202205.homework.spring06.dao.impl;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring06.dao.BookCommentRepository;
import ru.otus.group202205.homework.spring06.model.BookComment;

@Component
@RequiredArgsConstructor
public class BookCommentRepositoryJpa implements BookCommentRepository {

  @PersistenceContext
  private final EntityManager entityManager;

  @Override
  public List<BookComment> findAll() {
    return entityManager
        .createQuery("select bc from BookComment bc",
            BookComment.class)
        .getResultList();
  }

  @Override
  public Optional<BookComment> findById(Long id) {
    return Optional.ofNullable(entityManager.find(BookComment.class,
        id));
  }

  @Override
  public BookComment saveOrUpdate(BookComment bookComment) {
    if (bookComment.getId() == null) {
      entityManager.persist(bookComment);
      return bookComment;
    }
    return entityManager.merge(bookComment);
  }

  @Override
  public void deleteById(Long id) {
    Optional<BookComment> bookComment = findById(id);
    bookComment.ifPresent(entityManager::remove);
  }

}
