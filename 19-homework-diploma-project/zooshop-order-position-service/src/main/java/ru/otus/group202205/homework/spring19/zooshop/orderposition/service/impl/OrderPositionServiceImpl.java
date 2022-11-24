package ru.otus.group202205.homework.spring19.zooshop.orderposition.service.impl;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring19.zooshop.orderposition.dao.OrderPositionRepository;
import ru.otus.group202205.homework.spring19.zooshop.orderposition.dto.OrderPositionDto;
import ru.otus.group202205.homework.spring19.zooshop.orderposition.feign.ActionServiceFeignProxy;
import ru.otus.group202205.homework.spring19.zooshop.orderposition.feign.GoodServiceFeignProxy;
import ru.otus.group202205.homework.spring19.zooshop.orderposition.mapper.OrderPositionMapper;
import ru.otus.group202205.homework.spring19.zooshop.orderposition.model.OrderPosition;
import ru.otus.group202205.homework.spring19.zooshop.orderposition.service.OrderPositionService;

@Service
@RequiredArgsConstructor
public class OrderPositionServiceImpl implements OrderPositionService {

  private final OrderPositionRepository orderPositionRepository;
  private final OrderPositionMapper actionMapper;
  private final GoodServiceFeignProxy goodServiceFeignProxy;

  private final ActionServiceFeignProxy actionServiceFeignProxy;

  @Override
  public List<OrderPositionDto> findAll(Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return orderPositionRepository
        .findAll(pageable)
        .stream()
        .map(actionMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public OrderPositionDto findById(Long id) {
    return actionMapper.toDto(orderPositionRepository
        .findById(id)
        .orElseThrow());
  }


  @Override
  public List<OrderPositionDto> findAllByOrderId(Long orderId, Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return orderPositionRepository
        .findAllByOrderId(orderId,
            pageable)
        .stream()
        .map(actionMapper::toDto)
        .collect(Collectors.toList());
  }


  @Override
  public OrderPositionDto save(OrderPositionDto orderPosition) {
    goodServiceFeignProxy.existsById(orderPosition.getGoodId());
    actionServiceFeignProxy.existsById(orderPosition.getActionId());
    return actionMapper.toDto(orderPositionRepository.save(actionMapper.toEntity(orderPosition)));
  }

  @Override
  public void deleteById(Long id) {
    orderPositionRepository.deleteById(id);
  }

  @Override
  public void deleteAll() {
    orderPositionRepository
        .findAll()
        .forEach(orderPosition -> this.deleteById(orderPosition.getId()));
  }

  @Override
  public boolean existsById(Long id) {
    return orderPositionRepository.existsById(id);
  }

  @Override
  public void deleteAllByGoodId(Long goodId) {
    orderPositionRepository
        .findAllByGoodId(goodId)
        .stream()
        .map(OrderPosition::getId)
        .forEach(this::deleteById);
  }

  @Override
  public void deleteAllByActionId(Long actionId) {
    orderPositionRepository
        .findAllByActionId(actionId)
        .stream()
        .map(OrderPosition::getId)
        .forEach(this::deleteById);
  }

}
