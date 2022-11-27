package ru.otus.group202205.homework.spring19.zooshop.orderposition.dao;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.otus.group202205.homework.spring19.zooshop.orderposition.model.OrderPosition;

public interface OrderPositionRepository extends PagingAndSortingRepository<OrderPosition, Long> {

  Page<OrderPosition> findAllByOrderId(Long orderId, Pageable pageable);

  List<OrderPosition> findAllByGoodId(Long goodId);

  List<OrderPosition> findAllByActionId(Long actionId);

  @Query("select 1 as result from OrderPosition op where exists(select op.id from OrderPosition op where id is not null)")
  Long exists();

}
