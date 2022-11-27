package ru.otus.group202205.homework.spring19.zooshop.category.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.group202205.homework.spring19.zooshop.category.dto.CategoryDto;

public interface CategoryService {

  List<CategoryDto> findAll(Pageable pageable, Sort sort);

  CategoryDto findById(Long id);

  List<CategoryDto> findAllByName(String name, Pageable pageable, Sort sort);


  CategoryDto save(CategoryDto category);

  void deleteById(Long id);

  void deleteAll();

  boolean existsById(Long id);

}
