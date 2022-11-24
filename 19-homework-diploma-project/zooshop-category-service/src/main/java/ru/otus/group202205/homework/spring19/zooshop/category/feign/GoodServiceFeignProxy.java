package ru.otus.group202205.homework.spring19.zooshop.category.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "zooshop-good-service")
public interface GoodServiceFeignProxy {

  @DeleteMapping("/by/category/{categoryId}")
  void deleteAllByCategory(@PathVariable("categoryId") Long categoryId);

}
