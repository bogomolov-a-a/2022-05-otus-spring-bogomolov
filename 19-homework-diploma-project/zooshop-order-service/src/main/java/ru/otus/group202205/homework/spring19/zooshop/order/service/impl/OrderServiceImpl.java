package ru.otus.group202205.homework.spring19.zooshop.order.service.impl;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring19.zooshop.order.dao.OrderPositionRepository;
import ru.otus.group202205.homework.spring19.zooshop.order.dto.OrderDto;
import ru.otus.group202205.homework.spring19.zooshop.order.feign.CustomerService;
import ru.otus.group202205.homework.spring19.zooshop.order.feign.OrderPositionService;
import ru.otus.group202205.homework.spring19.zooshop.order.mapper.OrderMapper;
import ru.otus.group202205.homework.spring19.zooshop.order.model.Order;
import ru.otus.group202205.homework.spring19.zooshop.order.service.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderPositionRepository orderPositionRepository;
  private final OrderMapper orderMapper;
  private final CustomerService customerService;
  private final OrderPositionService orderPositionService;

  @Override
  public List<OrderDto> findAll(Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return orderPositionRepository
        .findAll(pageable)
        .stream()
        .map(orderMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public OrderDto findById(Long id) {
    return orderMapper.toDto(orderPositionRepository
        .findById(id)
        .orElseThrow());
  }


  @Override
  public List<OrderDto> findAllByCustomerId(Long customerId, Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return orderPositionRepository
        .findAllByCustomerId(customerId,
            pageable)
        .stream()
        .map(orderMapper::toDto)
        .collect(Collectors.toList());
  }


  @Override
  public OrderDto save(OrderDto order) {
    customerService.existsById(order.getCustomerId());
    return orderMapper.toDto(orderPositionRepository.save(orderMapper.toEntity(order)));
  }

  @Override
  public void deleteById(Long id) {
    orderPositionService.deleteAllByOrderId(id);
    orderPositionRepository.deleteById(id);
  }

  @Override
  public void deleteAll() {
    orderPositionRepository
        .findAll()
        .forEach(order -> this.deleteById(order.getId()));
  }

  @Override
  public boolean existsById(Long id) {
    return orderPositionRepository.existsById(id);
  }

  @Override
  public void deleteAllByCustomerId(Long customerId) {
    orderPositionRepository
        .findAllByCustomerId(customerId,
            Pageable.unpaged())
        .stream()
        .map(Order::getId)
        .forEach(this::deleteById);
  }

  @Override
  public void deleteAllByAddressId(Long addressId) {
    orderPositionRepository
        .findAllByDeliveredAddressId(addressId)
        .stream()
        .map(Order::getId)
        .forEach(this::deleteById);
  }

}
