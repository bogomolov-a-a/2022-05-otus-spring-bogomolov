package ru.otus.group202205.homework.spring19.zooshop.good.service;

import java.util.List;
import ru.otus.group202205.homework.spring19.zooshop.good.dto.GoodDto;

public interface GoodService {

  List<GoodDto> findAll(int pageNumber, int pageSize);

  GoodDto findById(Long id);

  List<GoodDto> findAllByName(String name, int pageNumber, int pageSize);

  List<GoodDto> findAllByProducerName(String producerName, int pageNumber, int pageSize);


  List<GoodDto> findAllByCategoryName(String categoryName, int pageNumber, int pageSize);


  List<GoodDto> findAllByPriceBetween(Float bottomBorder, Float topBorder, int pageNumber, int pageSize);

  GoodDto save(GoodDto good);

  void deleteById(Long id);

  void deleteAll();

  void deleteAllByProducerName(String producerName);

  void deleteAllByCategoryName(String categoryName);

}
