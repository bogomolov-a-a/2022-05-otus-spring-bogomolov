package ru.otus.group202205.homework.spring19.zooshop.producer.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.otus.group202205.homework.spring19.zooshop.producer.model.Producer;

public interface ProducerRepository extends PagingAndSortingRepository<Producer, Long> {

  Page<Producer> findAllByAddressId(Long addressId, Pageable pageable);

  Page<Producer> findAllByName(String name, Pageable pageable);

  @Query("select 1 as result from Producer p where exists(select p.id from Producer p where id is not null)")
  Long exists();

}
