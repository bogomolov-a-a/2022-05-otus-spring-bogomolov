package ru.otus.group202205.homework.spring19.zooshop.good.service.impl;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring19.zooshop.good.dao.GoodRepository;
import ru.otus.group202205.homework.spring19.zooshop.good.dto.GoodDto;
import ru.otus.group202205.homework.spring19.zooshop.good.mapper.GoodMapper;
import ru.otus.group202205.homework.spring19.zooshop.good.model.Good;
import ru.otus.group202205.homework.spring19.zooshop.good.service.GoodService;

@Service
@RequiredArgsConstructor
public class GoodServiceImpl implements GoodService {

  private final GoodRepository goodRepository;
  private final GoodMapper goodMapper;

  @Override
  public List<GoodDto> findAll(int pageNumber, int pageSize) {
    return goodRepository
        .findAll(Pageable
            .ofSize(pageSize)
            .withPage(pageNumber))
        .get()
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
  public List<GoodDto> findAllByName(String name, int pageNumber, int pageSize) {
    return goodRepository
        .findAllByName(name,
            Pageable
                .ofSize(pageSize)
                .withPage(pageNumber))
        .stream()
        .map(goodMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<GoodDto> findAllByProducerName(String producerName, int pageNumber, int pageSize) {
    return goodRepository
        .findAllByProducerName(producerName,
            Pageable
                .ofSize(pageSize)
                .withPage(pageNumber))
        .stream()
        .map(goodMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<GoodDto> findAllByCategoryName(String categoryName, int pageNumber, int pageSize) {
    return goodRepository
        .findAllByCategoryName(categoryName,
            Pageable
                .ofSize(pageSize)
                .withPage(pageNumber))
        .stream()
        .map(goodMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<GoodDto> findAllByPriceBetween(Float bottomBorder, Float topBorder, int pageNumber, int pageSize) {
    return goodRepository
        .findAllByPriceBetween(bottomBorder,
            topBorder,
            Pageable
                .ofSize(pageSize)
                .withPage(pageNumber))
        .stream()
        .map(goodMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public GoodDto save(GoodDto good) {
    return goodMapper.toDto(goodRepository.save(goodMapper.toEntity(good)));
  }

  @Override
  public void deleteById(Long id) {
    goodRepository.deleteById(id);
  }

  @Override
  public void deleteAll() {
    goodRepository.deleteAll();
  }

  @Override
  public void deleteAllByProducerName(String producerName) {
    List<Good> goods = goodRepository.findAllByProducerName(producerName,
        Pageable.unpaged());
    goodRepository.deleteAll(goods);
  }

  @Override
  public void deleteAllByCategoryName(String categoryName) {
    List<Good> goods = goodRepository.findAllByCategoryName(categoryName,
        Pageable.unpaged());
    goodRepository.deleteAll(goods);
  }

}
