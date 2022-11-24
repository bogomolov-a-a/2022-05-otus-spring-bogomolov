package ru.otus.group202205.homework.spring19.zooshop.orderposition.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "zooshop-good-service")
public interface GoodServiceFeignProxy {

  @GetMapping("/existence/{id}")
  void existsById(@PathVariable("id") Long goodId);

}
