package ru.otus.group202205.homework.spring19.zooshop.orderposition.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.group202205.homework.spring19.zooshop.orderposition.dto.OrderPositionDto;

public interface OrderPositionService {

  List<OrderPositionDto> findAll(Pageable pageable, Sort sort);

  OrderPositionDto findById(Long id);

  List<OrderPositionDto> findAllByOrderId(Long orderId, Pageable pageable, Sort sort);

  OrderPositionDto save(OrderPositionDto category);

  void deleteById(Long id);

  void deleteAll();

  boolean existsById(Long id);

  void deleteAllByGoodId(Long goodId);

  void deleteAllByActionId(Long actionId);

  void deleteAllByOrderId(Long orderId);
}
