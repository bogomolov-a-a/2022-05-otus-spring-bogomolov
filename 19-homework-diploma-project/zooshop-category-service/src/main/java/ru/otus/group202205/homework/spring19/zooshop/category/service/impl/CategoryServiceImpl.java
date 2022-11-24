package ru.otus.group202205.homework.spring19.zooshop.category.service.impl;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring19.zooshop.category.dao.CategoryRepository;
import ru.otus.group202205.homework.spring19.zooshop.category.dto.CategoryDto;
import ru.otus.group202205.homework.spring19.zooshop.category.feign.GoodServiceFeignProxy;
import ru.otus.group202205.homework.spring19.zooshop.category.mapper.CategoryMapper;
import ru.otus.group202205.homework.spring19.zooshop.category.service.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;
  private final GoodServiceFeignProxy goodServiceFeignProxy;

  @Override
  public List<CategoryDto> findAll(Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return categoryRepository
        .findAll(pageable)
        .stream()
        .map(categoryMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public CategoryDto findById(Long id) {
    return categoryMapper.toDto(categoryRepository
        .findById(id)
        .orElseThrow());
  }

  @Override
  public List<CategoryDto> findAllByName(String name, Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return categoryRepository
        .findAllByName(name,
            pageable)
        .stream()
        .map(categoryMapper::toDto)
        .collect(Collectors.toList());
  }


  @Override
  public CategoryDto save(CategoryDto category) {
    Long parentId = category.getParentId();
    if (parentId == null) {
      return categoryMapper.toDto(categoryRepository.save(categoryMapper.toEntity(category)));
    }
    if (categoryRepository.existsById(parentId)) {
      return categoryMapper.toDto(categoryRepository.save(categoryMapper.toEntity(category)));
    }
    throw new IllegalArgumentException("parent category with id " + parentId + " not found!");
  }

  @Override
  public void deleteById(Long id) {
    goodServiceFeignProxy.deleteAllByCategory(id);
    categoryRepository.deleteById(id);
  }

  @Override
  public void deleteAll() {
    categoryRepository
        .findAll()
        .forEach(address -> this.deleteById(address.getId()));
  }

  @Override
  public boolean existsById(Long id) {
    return categoryRepository.existsById(id);
  }

}
