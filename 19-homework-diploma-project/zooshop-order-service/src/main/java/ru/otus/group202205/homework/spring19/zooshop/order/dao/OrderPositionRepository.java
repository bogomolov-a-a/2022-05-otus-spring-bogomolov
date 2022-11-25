package ru.otus.group202205.homework.spring19.zooshop.order.dao;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.otus.group202205.homework.spring19.zooshop.order.model.Order;

public interface OrderPositionRepository extends PagingAndSortingRepository<Order, Long> {

  Page<Order> findAllByCustomerId(Long customerId, Pageable pageable);

  List<Order> findAllByDeliveredAddressId(Long addressId);


  @Query("select 1 as result from Order o where exists(select o.id from Order o where id is not null)")
  Long exists();

}
