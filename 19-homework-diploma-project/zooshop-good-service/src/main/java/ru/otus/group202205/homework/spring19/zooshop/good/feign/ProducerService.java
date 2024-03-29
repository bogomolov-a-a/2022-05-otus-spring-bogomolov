package ru.otus.group202205.homework.spring19.zooshop.good.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "zooshop-producer-service")
public interface ProducerService {

  @GetMapping("/existence/{id}")
  void existsById(@PathVariable("id") Long producerId);

}
