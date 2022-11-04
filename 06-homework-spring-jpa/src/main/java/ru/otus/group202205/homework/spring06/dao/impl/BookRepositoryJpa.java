package ru.otus.group202205.homework.spring06.dao.impl;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring06.dao.BookRepository;
import ru.otus.group202205.homework.spring06.model.Book;

@Component
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {

  @PersistenceContext
  private final EntityManager entityManager;

  @Override
  public List<Book> findAll() {
    return entityManager
        .createQuery("select b from Book b",
            Book.class)
        .getResultList();
  }

  @Override
  public Optional<Book> findById(Long id) {
    return Optional.ofNullable(entityManager.find(Book.class,
        id));
  }

  @Override
  public Book saveOrUpdate(Book book) {
    if (book.getId() == null) {
      entityManager.persist(book);
      return book;
    }
    return entityManager.merge(book);
  }


  @Override
  public void deleteById(Long id) {
    Optional<Book> book = findById(id);
    book.ifPresent(entityManager::remove);
  }

}
