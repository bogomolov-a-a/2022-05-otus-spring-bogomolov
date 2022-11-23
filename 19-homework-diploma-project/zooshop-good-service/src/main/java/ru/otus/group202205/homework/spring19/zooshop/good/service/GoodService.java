package ru.otus.group202205.homework.spring19.zooshop.good.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.group202205.homework.spring19.zooshop.good.dto.GoodDto;

public interface GoodService {

  List<GoodDto> findAll(Pageable pageable, Sort sort);

  GoodDto findById(Long id);

  List<GoodDto> findAllByName(String name, Pageable pageable, Sort sort);

  List<GoodDto> findAllByProducerId(Long producerId, Pageable pageable, Sort sort);


  List<GoodDto> findAllByCategoryId(Long categoryId, Pageable pageable, Sort sort);


  List<GoodDto> findAllByPriceBetween(Float bottomBorder, Float topBorder,Pageable pageable, Sort sort);

  GoodDto save(GoodDto good);

  void deleteById(Long id);

  void deleteAll();

  void deleteAllByProducerId(Long producerId);

  void deleteAllByCategoryId(Long categoryId);

}
