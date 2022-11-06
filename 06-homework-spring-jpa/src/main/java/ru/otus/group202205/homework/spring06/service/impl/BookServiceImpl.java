package ru.otus.group202205.homework.spring06.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring06.dao.BookRepository;
import ru.otus.group202205.homework.spring06.dto.BookFullDto;
import ru.otus.group202205.homework.spring06.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring06.service.BookService;
import ru.otus.group202205.homework.spring06.service.mapper.BookMapper;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;
  private final BookMapper bookMapper;

  @Transactional(readOnly = true)
  @Override
  public List<BookFullDto> findAll() {
    try {
      return bookRepository
          .findAll()
          .stream()
          .map(bookMapper::toFullDto)
          .collect(Collectors.toList());
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't get all books",
          e);
    }
  }

  @Transactional(readOnly = true)
  @Override
  public BookFullDto findById(Long id) {
    try {
      return bookMapper.toFullDto(bookRepository
          .findById(id)
          .orElseThrow());
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't get book by id" + id,
          e);
    }
  }

  @Transactional
  @Override
  public BookFullDto saveOrUpdate(BookFullDto book) {
    try {
      return bookMapper.toFullDto(bookRepository.saveOrUpdate(bookMapper.toEntityFromFull(book)));
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't insert or update book",
          e);
    }
  }

  @Transactional
  @Override
  public void deleteById(Long id) {
    try {
      bookRepository.deleteById(id);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't delete book",
          e);
    }
  }

}
