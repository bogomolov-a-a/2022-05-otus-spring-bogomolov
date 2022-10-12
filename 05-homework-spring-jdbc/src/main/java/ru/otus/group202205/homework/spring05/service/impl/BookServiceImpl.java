package ru.otus.group202205.homework.spring05.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring05.dao.BookDao;
import ru.otus.group202205.homework.spring05.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring05.model.Book;
import ru.otus.group202205.homework.spring05.service.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookDao bookDao;

  @Transactional(readOnly = true)
  @Override
  public List<Book> getAll() {
    try {
      return bookDao.getAll();
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't get all books",
          e);
    }
  }

  @Transactional(readOnly = true)
  @Override
  public Book getById(Long id) {
    try {
      return bookDao.getById(id);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't get book by id" + id,
          e);
    }
  }

  @Transactional
  @Override
  public void insert(Book book) {
    try {
      bookDao.insert(book);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't insert book",
          e);
    }
  }

  @Transactional
  @Override
  public void update(Book book) {
    try {
      Book existingBook = bookDao.getById(book.getId());
      mergeBooks(book,
          existingBook);
      bookDao.update(book);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't update book",
          e);
    }
  }

  @Transactional
  @Override
  public void deleteById(Long id) {
    try {
      bookDao.deleteById(id);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't delete book",
          e);
    }
  }

  private void mergeBooks(Book book, Book existingBook) {
    if (book.getTitle() == null) {
      book.setTitle(existingBook.getTitle());
    }
    if (book.getIsbn() == null) {
      book.setIsbn(existingBook.getIsbn());
    }
    if (book.getAuthor() == null) {
      book.setAuthor(existingBook.getAuthor());
    }
    if (book.getGenre() == null) {
      book.setGenre(existingBook.getGenre());
    }
  }

}
