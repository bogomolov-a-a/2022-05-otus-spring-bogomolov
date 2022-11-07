package ru.otus.group202205.homework.spring07.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring07.dao.AuthorRepository;
import ru.otus.group202205.homework.spring07.dto.AuthorDto;
import ru.otus.group202205.homework.spring07.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring07.service.AuthorService;
import ru.otus.group202205.homework.spring07.service.mapper.AuthorMapper;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

  private final AuthorRepository authorRepository;

  private final AuthorMapper authorMapper;

  @Override
  public List<AuthorDto> findAll() {
    try {
      return authorRepository
          .findAll()
          .stream()
          .map(authorMapper::toDto)
          .collect(Collectors.toList());
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't get author list",
          e);
    }
  }

  @Override
  public AuthorDto findById(Long id) {
    try {
      return authorMapper.toDto(authorRepository
          .findById(id)
          .orElseThrow());
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't get author by id " + id,
          e);
    }
  }

  @Override
  public AuthorDto saveOrUpdate(AuthorDto author) {
    try {
      return authorMapper.toDto(authorRepository.save(authorMapper.toEntity(author)));
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't insert or update author",
          e);
    }
  }

  @Override
  public void deleteById(Long id) {
    try {
      authorRepository.deleteById(id);
    } catch (RuntimeException e) {
      throw new LibraryGeneralException("Can't delete author",
          e);
    }
  }

}
