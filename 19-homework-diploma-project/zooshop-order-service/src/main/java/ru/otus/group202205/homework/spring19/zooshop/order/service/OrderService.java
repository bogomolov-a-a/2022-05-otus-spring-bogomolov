package ru.otus.group202205.homework.spring19.zooshop.order.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.group202205.homework.spring19.zooshop.order.dto.OrderDto;

public interface OrderService {

  List<OrderDto> findAll(Pageable pageable, Sort sort);

  OrderDto findById(Long id);

  List<OrderDto> findAllByCustomerId(Long customerId, Pageable withPage, Sort id);

  OrderDto save(OrderDto category);

  void deleteById(Long id);

  void deleteAll();

  boolean existsById(Long id);


  void deleteAllByCustomerId(Long customerId);

  void deleteAllByAddressId(Long addressId);

}
