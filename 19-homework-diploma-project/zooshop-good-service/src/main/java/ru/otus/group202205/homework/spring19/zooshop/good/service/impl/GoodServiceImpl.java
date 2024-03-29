package ru.otus.group202205.homework.spring19.zooshop.good.service.impl;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring19.zooshop.good.dao.GoodRepository;
import ru.otus.group202205.homework.spring19.zooshop.good.dto.GoodDto;
import ru.otus.group202205.homework.spring19.zooshop.good.feign.ActionService;
import ru.otus.group202205.homework.spring19.zooshop.good.feign.CategoryService;
import ru.otus.group202205.homework.spring19.zooshop.good.feign.OrderPositionService;
import ru.otus.group202205.homework.spring19.zooshop.good.feign.ProducerService;
import ru.otus.group202205.homework.spring19.zooshop.good.mapper.GoodMapper;
import ru.otus.group202205.homework.spring19.zooshop.good.model.Good;
import ru.otus.group202205.homework.spring19.zooshop.good.service.GoodService;

@Service
@RequiredArgsConstructor
public class GoodServiceImpl implements GoodService {

  private final GoodRepository goodRepository;
  private final GoodMapper goodMapper;
  private final ProducerService producerService;
  private final CategoryService categoryService;
  private final ActionService actionService;
  private final OrderPositionService orderPositionService;

  @Override
  public List<GoodDto> findAll(Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return goodRepository
        .findAll(pageable)
        .stream()
        .map(goodMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public GoodDto findById(Long id) {
    return goodMapper.toDto(goodRepository
        .findById(id)
        .orElseThrow());
  }

  @Override
  public List<GoodDto> findAllByName(String name, Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return goodRepository
        .findAllByName(name,
            pageable)
        .stream()
        .map(goodMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<GoodDto> findAllByProducerId(Long producerId, Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return goodRepository
        .findAllByProducerId(producerId,
            pageable)
        .stream()
        .map(goodMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<GoodDto> findAllByCategoryId(Long categoryId, Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return goodRepository
        .findAllByCategoryId(categoryId,
            pageable)
        .stream()
        .map(goodMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<GoodDto> findAllByPriceBetween(Float bottomBorder, Float topBorder, Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return goodRepository
        .findAllByPriceBetween(bottomBorder,
            topBorder,
            pageable)
        .stream()
        .map(goodMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public GoodDto save(GoodDto good) {
    producerService.existsById(good.getProducerId());
    categoryService.existsById(good.getCategoryId());
    return goodMapper.toDto(goodRepository.save(goodMapper.toEntity(good)));
  }

  @Override
  public void deleteById(Long id) {
    actionService.deleteAllByGood(id);
    orderPositionService.deleteAllByGood(id);
    goodRepository.deleteById(id);
  }

  @Override
  public void deleteAll() {
    goodRepository
        .findAll()
        .forEach(good -> deleteById(good.getId()));
  }

  @Override
  public void deleteAllByProducerId(Long producerId) {
    goodRepository
        .findAllByProducerId(producerId,
            Pageable.unpaged())
        .getContent()
        .stream()
        .map(Good::getId)
        .forEach(this::deleteById);
  }

  @Override
  public void deleteAllByCategoryId(Long categoryId) {
    goodRepository
        .findAllByCategoryId(categoryId,
            Pageable.unpaged())
        .getContent()
        .stream()
        .map(Good::getId)
        .forEach(this::deleteById);
  }

}
