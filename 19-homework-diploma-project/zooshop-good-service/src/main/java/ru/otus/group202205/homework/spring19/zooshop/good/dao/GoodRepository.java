package ru.otus.group202205.homework.spring19.zooshop.good.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.otus.group202205.homework.spring19.zooshop.good.model.Good;

public interface GoodRepository extends PagingAndSortingRepository<Good, Long> {

  Page<Good> findAllByName(String name, Pageable pageable);

  Page<Good> findAllByProducerId(Long producerId, Pageable pageable);

  Page<Good> findAllByCategoryId(Long categoryId, Pageable pageable);

  Page<Good> findAllByPriceBetween(Float bottomBorder, Float topBorder, Pageable pageable);

  @Query("select 1 as result from Good g where exists(select g.id from Good g where id is not null)")
  Long exists();

}
