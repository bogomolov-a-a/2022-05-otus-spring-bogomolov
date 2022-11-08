package ru.otus.group202205.homework.spring19.zooshop.good.dao;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.otus.group202205.homework.spring19.zooshop.good.model.Good;

public interface GoodRepository extends PagingAndSortingRepository<Good, Long> {

  List<Good> findAllByName(String name, Pageable pageable);

  List<Good> findAllByProducerName(String producerName, Pageable pageable);

  List<Good> findAllByCategoryName(String categoryName, Pageable pageable);

  List<Good> findAllByPriceBetween(Float bottomBorder, Float topBorder, Pageable pageable);

}
