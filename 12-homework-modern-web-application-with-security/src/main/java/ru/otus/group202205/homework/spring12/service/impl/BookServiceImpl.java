package ru.otus.group202205.homework.spring12.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring12.dao.BookRepository;
import ru.otus.group202205.homework.spring12.dto.BookFullDto;
import ru.otus.group202205.homework.spring12.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring12.service.BookService;
import ru.otus.group202205.homework.spring12.service.mapper.BookMapper;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;
  private final BookMapper bookMapper;

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


  @Override
  public BookFullDto saveOrUpdate(BookFullDto book) {
    try {
      return bookMapper.toFullDto(bookRepository.save(bookMapper.toEntityFromFull(book)));
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't insert or update book",
          e);
    }
  }

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
