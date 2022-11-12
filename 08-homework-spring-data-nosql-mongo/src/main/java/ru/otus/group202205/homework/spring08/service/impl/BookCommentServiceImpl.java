package ru.otus.group202205.homework.spring08.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring08.dao.BookCommentRepository;
import ru.otus.group202205.homework.spring08.dao.BookRepository;
import ru.otus.group202205.homework.spring08.dto.BookCommentDto;
import ru.otus.group202205.homework.spring08.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring08.model.Book;
import ru.otus.group202205.homework.spring08.service.BookCommentService;
import ru.otus.group202205.homework.spring08.service.mapper.BookCommentMapper;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {

  private final BookCommentRepository bookCommentRepository;
  private final BookRepository bookRepository;
  private final BookCommentMapper bookCommentMapper;

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

  @Override
  public BookCommentDto findById(String id) {
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
  public List<BookCommentDto> findAllByBookId(String id) {
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

  @Override
  public BookCommentDto saveOrUpdate(BookCommentDto bookComment) {
    try {
      return bookCommentMapper.toDto(bookCommentRepository.save(bookCommentMapper.toEntity(bookComment)));
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't insert or update book",
          e);
    }
  }

  @Override
  public void deleteById(String id) {
    try {
      bookCommentRepository.deleteById(id);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't delete book comment",
          e);
    }
  }

}
