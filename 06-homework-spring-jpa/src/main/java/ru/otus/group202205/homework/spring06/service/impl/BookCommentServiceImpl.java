package ru.otus.group202205.homework.spring06.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring06.dao.BookCommentRepository;
import ru.otus.group202205.homework.spring06.dao.BookRepository;
import ru.otus.group202205.homework.spring06.dto.BookCommentDto;
import ru.otus.group202205.homework.spring06.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring06.model.Book;
import ru.otus.group202205.homework.spring06.service.BookCommentService;
import ru.otus.group202205.homework.spring06.service.mapper.BookCommentMapper;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {

  private final BookCommentRepository bookCommentRepository;
  private final BookRepository bookRepository;
  private final BookCommentMapper bookCommentMapper;

  @Transactional(readOnly = true)
  @Override
  public List<BookCommentDto> findAll() {
    try {
      return bookCommentRepository
          .findAll()
          .stream()
          .map(bookCommentMapper::toDto)
          .collect(Collectors.toList());
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't get all book comments",
          e);
    }
  }

  @Transactional(readOnly = true)
  @Override
  public BookCommentDto findById(Long id) {
    try {
      return bookCommentMapper.toDto(bookCommentRepository
          .findById(id)
          .orElseThrow());
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't get book comment by id " + id,
          e);
    }
  }

  @Transactional(readOnly = true)
  @Override
  public List<BookCommentDto> findAllByBookId(Long id) {
    try {
      Book book = bookRepository
          .findById(id)
          .orElseThrow();
      return book
          .getComments()
          .stream()
          .map(bookCommentMapper::toDto)
          .collect(Collectors.toList());
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't get all book comments by book id " + id,
          e);
    }
  }

  @Transactional
  @Override
  public BookCommentDto saveOrUpdate(BookCommentDto bookComment) {
    try {
      return bookCommentMapper.toDto(bookCommentRepository.saveOrUpdate(bookCommentMapper.toEntity(bookComment)));
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't insert or update book",
          e);
    }
  }

  @Transactional
  @Override
  public void deleteById(Long id) {
    try {
      bookCommentRepository.deleteById(id);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't delete book comment",
          e);
    }
  }

}
