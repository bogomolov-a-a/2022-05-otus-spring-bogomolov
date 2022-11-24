package ru.otus.group202205.homework.spring19.zooshop.action.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "zooshop-order-position-service")
public interface OrderPositionServiceFeignProxy {

  @DeleteMapping("/by/action/{actionId}")
  void deleteAllByAction(@PathVariable("actionId") Long actionId);

}
