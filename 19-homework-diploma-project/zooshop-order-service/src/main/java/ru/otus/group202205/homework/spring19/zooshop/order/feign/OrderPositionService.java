package ru.otus.group202205.homework.spring19.zooshop.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("zooshop-order-position-service")
public interface OrderPositionService {

  @DeleteMapping("/by/order/{actionId}")
  void deleteAllByOrderId(@PathVariable("orderId") Long orderId);

}
