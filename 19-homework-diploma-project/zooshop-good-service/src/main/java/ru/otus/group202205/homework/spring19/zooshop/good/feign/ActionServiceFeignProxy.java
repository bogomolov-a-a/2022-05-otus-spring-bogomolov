package ru.otus.group202205.homework.spring19.zooshop.good.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "zooshop-action-service")
public interface ActionServiceFeignProxy {

  @DeleteMapping("/by/good/{goodId}")
  void deleteAllByGood(@PathVariable("goodId") Long goodId);

}
